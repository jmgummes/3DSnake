package back;
/**
 * This is the parent class of the circular
 * objects (food, snake segments) seen in the game. 
 * @author Jim
 */
public abstract class Circle {
  
  // Position in the scene
  public abstract Coordinates getCoordinates();
  
  // Radius
  public abstract double getRadius();
  
  /**
   * Determines whether this circle overlaps with
   * a given circle
   * @param c
   * @return whether this circle overlaps with c
   */
  public boolean overlapsWithCircle(Circle c) {
    return 
    Math.pow(c.getCoordinates().getX() - getCoordinates().getX(), 2) + 
    Math.pow(c.getCoordinates().getY() - getCoordinates().getY(), 2) < 
    Math.pow(c.getRadius() + getRadius(), 2);
  }
  
  /**
   * Get the radius of the sphere in the 3D world that 
   * corresponds to this circle.  The radius varies 
   * depending on where this circle is due to distortions
   * involved in mapping the flat world onto a torus.
   * Basically, it looks smaller when it is on the inside
   * of the torus, but bigger when it is on the outside.
   * @return the radius
   */
  public double threeDRadius() {
    return
    Math.tan(getCoordinates().xToAngle(getRadius()) / 2.0) * 2 * 
    (
        getCoordinates().getLevelSchema().outerRadius() + 
        Math.cos(getCoordinates().yToAngle(getCoordinates().getY())) * getCoordinates().getLevelSchema().innerRadius()
    );
  }
}
