/**
 * This class represents a Waypoint that tells a 
 * snake segment where to go next.
 * @author Jim
 */
public class Waypoint {
  
  // The direction to move in
  private double angle;
  
  // How far to go
  private double distance;
  
  // The next waypoint 
  private Waypoint next;
  
  /**
   * Constructor, pretty basic
   * @param a
   */
  public Waypoint(double a) {
    angle = a;
    distance = 0;
  }
  
  /**
   * Add distance on to this Waypoint's distance
   * @param d
   */
  public void addDistance(double d) {
    distance += d;
  }
  
  /**
   * Getter for angle
   * @return angle
   */
  public double getAngle() {
    return angle;
  }
  
  /**
   * Getter for distance
   * @return distance
   */
  public double getDistance() {
    return distance;
  }
  
  /**
   * Sets the next Waypoint
   * @param wp
   */
  public void setNext(Waypoint wp) {
    next = wp;
  }
  
  /**
   * Gets the next Waypoint
   * @return the next Waypoint
   */
  public Waypoint next() {
    return next;
  }
}
