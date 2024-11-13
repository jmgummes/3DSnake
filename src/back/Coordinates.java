package back;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

/**
 * This class represents a position in the game world.
 * @author Jim
 */
public class Coordinates {

  // The level that these coordinates apply to
  private LevelSchema level;

  // x, y coordinates on the plane
  private double x;
  private double y;

  /**
   * Constructor, pretty basic
   * @param level
   * @param x
   * @param y
   */
  public Coordinates(LevelSchema level, double x, double y) {
    this.level = level;
    this.x = x;
    this.y = y;
  }

  /**
   * Getter for level
   * @return level
   */
  public LevelSchema getLevel() {
    return level;
  }

  /**
   * Setter for level
   * @param level
   */
  public void setLevel(LevelSchema level) {
    this.level = level;
  }

  /**
   * Getter for x
   * @return x
   */
  public double getX() {
    return x;
  }

  /**
   * Getter for y
   * @return y
   */
  public double getY() {
    return y;
  }

  /**
   * Setter for x
   * @param x
   */
  public void setX(double x) {
    this.x = x;
    handleXWrapAround();
  }

  /**
   * Setter for y
   * @param y
   */
  public void setY(double y) {
    this.y = y;
    handleYWrapAround();
  }

  /**
   * Makes sure that x is at least 0 but less than the width of the level while
   * maintaining congruence mod the level width.
   */
  private void handleXWrapAround() { 
    while (x >= level.getWidth())
      x -= level.getWidth();  
    while (x < 0)
      x += level.getWidth();
  }

  /**
   * Makes sure that y is at least 0 but less than the height of the level while
   * maintaining congruence mod the level height.
   */
  private void handleYWrapAround() {
    if(y >= level.getHeight())
      while (y >= level.getHeight())
        y -= level.getHeight();
    else if(y < 0)
      while (y < 0)
        y += level.getHeight();
  }

  /**
   * Converts x to an angle around the torus (the long way)
   * @param x
   * @return angle around the torus
   */
  public double xToAngle(double x) {
    return (x / level.getWidth()) * 2 * Math.PI;
  }

  /**
   * Converts y to an angle around the torus (the short way)
   * @param y
   * @return angle around the torus
   */
  public double yToAngle(double y) {
    return (y / level.getHeight()) * 2 * Math.PI;
  }

  /**
   * Converts x coordinate to an angle around the torus (the long way)
   * @return angle around the torus
   */
  public double xToAngle() {
    return xToAngle(x);
  }

  /**
   * Converts y coordinate to an angle around the torus (the short way)
   * @return
   */
  public double yToAngle() {
    return yToAngle(y);
  }

  /**
   * Get the 3D coordinates of this position
   * @param heightFromSurface -- height above the surface of the torus
   * @return 3D coordinates
   */
  public Vector3d get3DCoordinates(double heightFromSurface) {
    return get3DCoordinates(this.x, this.y, heightFromSurface);
  }

  /**
   * Get the the 3D coordinates of a given position
   * @param x
   * @param y
   * @param heightFromSurface -- height above the surface of the torus
   * @return 3D coordinates
   */
  public Vector3d get3DCoordinates(double x, double y, double heightFromSurface) {
    Vector4d position = new Vector4d(0, 0, 0, 1);
    getCoordinatesTransformation(x, y, heightFromSurface).transform(position);
    return new Vector3d(position.x, position.y, position.z);
  }

  /**
   * Get a 4 x 4 matrix such that when you multiply the vector (0, 0, 0, 1) by
   * it, you get a vector that points to the position at these coordinates.
   * @param heightFromSurface
   * @return 4 x 4 matrix
   */
  public Matrix4d getCoordinatesTransformation(double heightFromSurface) {
    return getCoordinatesTransformation(this.x, this.y, heightFromSurface);
  }

  /**
   * Get a 4 x 4 matrix such that when you multiply the vector (0, 0, 0, 1) by
   * it, you get a vector that points to the position at the given coordinates.
   * @param heightFromSurface
   * @return 4 x 4 matrix
   */
  public Matrix4d getCoordinatesTransformation(double x, double y,
      double heightFromSurface) {
    
    Matrix4d transformation = new Matrix4d(
      1, 0, 0, 0, 
      0, 1, 0, 0, 
      0, 0, 1, 0,
      0, 0, 0, 1
    );

    Matrix4d firstRotation = new Matrix4d();
    firstRotation.rotZ(xToAngle(x));

    Matrix4d firstTranslation = new Matrix4d(
      1, 0, 0, 0, 
      0, 1, 0, level.outerRadius(), 
      0, 0, 1, 0, 
      0, 0, 0, 1
    );

    Matrix4d secondRotation = new Matrix4d();

    secondRotation.rotX(-yToAngle(y));

    Matrix4d secondTranslation = new Matrix4d(
      1, 0, 0, 0, 
      0, 1, 0, level.innerRadius() + heightFromSurface, 
      0, 0, 1, 0, 
      0, 0, 0, 1
    );

    transformation.mul(firstRotation);
    transformation.mul(firstTranslation);
    transformation.mul(secondRotation);
    transformation.mul(secondTranslation);
    return transformation;
  }

}
