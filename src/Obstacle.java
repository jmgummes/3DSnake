import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a rectangular obstacle that the snake
 * is supposed to avoid.
 * @author Jim
 */
public class Obstacle {
	
  abstract static class Description {
	abstract double getX();
	abstract double getY();
	
	abstract double getWidth();
	abstract double getHeight();
  }	
  
  // Coordinates of the top-left corner of this Obstacle
  private Coordinates coordinates;
  
  // Dimensions of this Obstacle 
  private double width; 
  private double height;
  
  /**
   * Constructor, pretty basic
   * @param coordinates
   * @param width
   * @param height
   */
  public Obstacle(Level level, Description description) {
    this. coordinates = new Coordinates(level, description.getX(), description.getY()); 
    this.width = description.getWidth(); 
    this.height = description.getHeight();
  }
  
  /**
   * Constructor
   * @param coordinates
   * @param width
   * @param height
   */
  public Obstacle(Coordinates coordinates, double width, double height) {
	  this.coordinates = coordinates;
	  this.width = width;
	  this.height = height;
  }
  
  /**
   * Finds whether this obstacle overlaps with a given circle
   * @param c
   * @return whether this obstacle overlaps with c
   */
  public boolean overlapsWithCircle(Circle c) {
    for(Obstacle o : split())
      if(overlap(o.coordinates.getX(), o.coordinates.getX() + o.width, c.getCoordinates().getX() - c.getRadius(), c.getCoordinates().getX() + c.getRadius())
      && overlap(o.coordinates.getY(), o.coordinates.getY() + o.height, c.getCoordinates().getY() - c.getRadius(), c.getCoordinates().getY() + c.getRadius()))
        return true;
    return false;
  }
  
  /**
   * Returns whether two ranges of numbers overlap.
   * @param start1
   * @param end1
   * @param start2
   * @param end2
   * @return whether there is overlap between the two ranges of numbers
   */
  private boolean overlap(double start1, double end1, double start2, double end2) {
    return(start1 < end2 && end1 > start2);
  } 
  
  /**
   * Getter for coordinates
   * @return coordinates
   */
  public Coordinates getCoordinates() {
    return coordinates;
  }
  
  /**
   * @return x coordinate of the left side of this Obstacle
   */
  public double getLeftSideX() {
    return coordinates.getX();
  }
  
  /**
   * @return x coordinate of the right side of this Obstacle
   */
  public double getRightSideX() {
    double toReturn = getLeftSideX() + width;
    while(toReturn > coordinates.getLevel().getDescription().getWidth())
      toReturn -= coordinates.getLevel().getDescription().getWidth();
    return toReturn;
  }
  
  /**
   * @return y coordinate of the top side of this Obstacle
   */
  public double getTopSideY() {
    return coordinates.getY();
  }
  
  /**
   * @return y coordinate of the bottom side of this Obstacle
   */
  public double getBottomSideY() {
    double toReturn = getTopSideY() + height;
    while(toReturn > coordinates.getLevel().getDescription().getHeight())
      toReturn -= coordinates.getLevel().getDescription().getHeight();
    return toReturn;
  }
  
  /**
   * Getter for width
   * @return width
   */
  public double getWidth() {
    return width;   
  }
  
  /**
   * Getter for height
   * @return height
   */
  public double getHeight() {
    return height;
  }
  
  /**
   * Setter for level
   * @param level
   */
  public void setLevel(Level level) {
    coordinates.setLevel(level);
  }
  
  /**
   * Get the height of this obstacle in the 3D world. The 
   * height varies depending on where this Obstacle is due 
   * to distortions involved in mapping the flat world onto 
   * a torus.  Basically, it looks smaller when it is on the 
   * inside of the torus, but bigger when it is on the outside.
   * @return the height
   */
  public double threeDHeight() {
    return Math.tan(getCoordinates().xToAngle(Snake.SEGMENT_RADIUS) / 2.0) * 2 *
    (
      coordinates.getLevel().outerRadius() + 
      Math.cos(getCoordinates().yToAngle(getTopSideY() + getHeight() / 2.0)) * coordinates.getLevel().innerRadius()
    );
  }
  
  /**
   * Splits up the obstacle so that the pieces
   * form the same obstacle, but none of the pieces
   * hang over the edges of the level.
   * @return list of obstacles that add up to this one, but
   *         don't hang over any level boundaries
   */
  public List<Obstacle> split() {  
    List<Obstacle> obstacles = new LinkedList<Obstacle>();
    
    if(getRightSideX() <= getLeftSideX()) {
      Obstacle left = new Obstacle(new Coordinates(coordinates.getLevel(), getLeftSideX(), getTopSideY()),
                                   coordinates.getLevel().getDescription().getWidth() - getLeftSideX(),
                                   getHeight());
      
      Obstacle right = new Obstacle(new Coordinates(coordinates.getLevel(), 0, getTopSideY()),
                                    getRightSideX(), getHeight());
      
      obstacles.addAll(left.split());
      obstacles.addAll(right.split());
    }
    else if(getBottomSideY() <= getTopSideY()) {
      Obstacle bottom = new Obstacle(new Coordinates(coordinates.getLevel(), getLeftSideX(), getTopSideY()),
                                     getWidth(), coordinates.getLevel().getDescription().getHeight() - getTopSideY());
      
      Obstacle top = new Obstacle(new Coordinates(coordinates.getLevel(), getLeftSideX(), 0),
                                  getWidth(), getBottomSideY());
      
      obstacles.addAll(top.split());
      obstacles.addAll(bottom.split());
    }
    else {
      obstacles.add(this);
    }
    
    return obstacles;
  }
}
