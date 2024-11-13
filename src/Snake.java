import java.util.Iterator;

/**
 * This class represents the snake.  Basically, it's a glorified
 * linked list data structure.
 * @author Jim
 */
public class Snake implements Iterable<Snake.Segment> {
  
  // Radius of each snake segment
  final static double SEGMENT_RADIUS = 10;
  
  // Spacing between snake segments
  final static double SEGMENT_SPACING = SEGMENT_RADIUS * 2 + 2;
  
  // The level that this snake lives on
  private Level level;
  
  // The head of this snake
  private Head head;
  
  // The tail of this snake
  private Segment tail;
  
  // The speed of this snake
  private double speed;
  
  /**
   * Constructor
   * @param coordinates (of the head)
   * @param angle (of the head)
   * @param speed 
   * @param length
   */
  public Snake(Level level, double x, double y, double angle, double speed, int length) {
	this.level = level;
    this.speed = speed;
    head = new Head(new Coordinates(level, x, y), angle);
    tail = head;
    for(; length - 1 > 0; length--)
      new BodySegment();
  }
  
  /**
   * Getter for head
   * @return the head of this snake
   */
  public Head getHead() {
    return head;
  }
  
  /**
   * Get an iterator that iterates over the segments
   * of this snake
   * @return iterator
   */
  @Override
  public Iterator<Segment> iterator() {
    return new SegmentIterator(head);
  }
  
  /**
   * Move the snake by moving all of its segments
   */
  public void move() {
    for(Segment s : this)
      s.move();
  }
  
  /**
   * This class is for iterating over the segments
   * of a snake
   * @author Jim
   */
  private class SegmentIterator implements Iterator<Segment> {
    
    
    // The segment that this iterator is currently on
    Segment current;
    
    /**
     * Constructor, pretty basic
     * @param start
     */
    public SegmentIterator(Segment start) {
      current = start;  
    }
    
    /**
     * Advances to the next segment
     */
    public Segment next() {
      Segment toReturn = current;
      current = current.next();
      return toReturn;
    }
    
    /**
     * Get whether there is a next segment
     * @return whether there is a next segment
     */
    public boolean hasNext() {
      return current != null;
    }
    
    /**
     * Unsupported!
     */
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    } 
  }
  
  /**
   * A simple abstact class to represent a snake 
   * segment
   * @author Jim
   */
  abstract class Segment extends Circle {
    
    // Coordinates
    protected Coordinates coordinates;
    
    // The last Waypoint that the segment left from
    protected Waypoint waypoint;
   
    // The next and previous segments of the snake
    protected BodySegment next;
    protected Segment prev;
    
    @Override
    public Coordinates getCoordinates() {
      return coordinates;
    }
    
    @Override
    public double getRadius() {
      return SEGMENT_RADIUS;
    }
    
    /**
     * Get the next segment
     * @return the next segment
     */
    public BodySegment next() {
      return next;
    }
    
    /**
     * Get the prev segment
     * @return the prev segment
     */
    public Segment prev() {
      return prev;
    }
    
    /**
     * Getter for waypoint
     * @return waypoint
     */
    public Waypoint getWaypoint() {
      return waypoint;
    }
    
    /**
     * Move this segment.  Assumes that all previous segments
     * have been moved already.
     */
    abstract void move();
    
    /**
     * Get the distance from the last waypoint that this segment
     * left
     */
    abstract double getDistanceFromWaypoint();
  }
  
  
  /**
   * Simple class to represent the head of a snake.
   * @author Jim
   */
  class Head extends Segment {
    
    /**
     * Constructor, pretty basic
     * @param coordinates
     * @param angle
     */
    private Head(Coordinates coordinates, double angle) {
      this.coordinates = coordinates;
      this.waypoint = new Waypoint(angle);
    }
    
    /**
     * Rotate
     * @param angle
     */
    public void rotate(double angle) {
      double newAngle = waypoint.getAngle() + angle;
      Waypoint newWaypoint = new Waypoint(newAngle);
      waypoint.setNext(newWaypoint);
      waypoint = newWaypoint;
    }

    @Override
    public void move() {
      coordinates.setX((coordinates.getX() + (Math.cos(waypoint.getAngle()) * speed)));
      coordinates.setY((coordinates.getY() - (Math.sin(waypoint.getAngle()) * speed)));
      waypoint.addDistance(speed);
    }
    
    @Override
    public double getDistanceFromWaypoint() {
      return waypoint.getDistance(); 
    }
  }
  
  /**
   * Simple class to represent a body segment
   * @author Jim
   */
  class BodySegment extends Segment {    
    
    // Distance from the last waypoint that this body 
    // segment left from
    private double distanceFromWaypoint;
    
    /**
     * Constructor.  Builds a body segment right behind the tail
     * of the snake.
     */
    public BodySegment() {
      double angleFromTail = tail.waypoint.getAngle() + Math.PI;
      double x = tail.coordinates.getX() + Math.cos(angleFromTail) * SEGMENT_SPACING;
      double y = tail.coordinates.getY() - Math.sin(angleFromTail) * SEGMENT_SPACING;
      this.coordinates = new Coordinates(level, x, y);
      waypoint = tail.waypoint;
      distanceFromWaypoint = tail.getDistanceFromWaypoint() - SEGMENT_SPACING;
      tail.next = this;
      this.prev = tail;
      tail = this;
    }
    
    /**
     * Move
     */
    @Override
    void move() {
      moveRecursive(speed);
    }
    
    /**
     * Move recursively by distance
     * @param distance
     */
    private void moveRecursive(double distance) { 
      double usedDistance;
      Waypoint newWaypoint;
      if(distanceFromWaypoint + distance < waypoint.getDistance()) {
        usedDistance = distance;
        newWaypoint = waypoint;
        distanceFromWaypoint += distance; 
      }
      else {
        usedDistance = waypoint.getDistance() - distanceFromWaypoint;
        newWaypoint = waypoint.next();
        distanceFromWaypoint = 0;
      }  

      coordinates.setX((coordinates.getX() + Math.cos(waypoint.getAngle()) * distance));
      coordinates.setY((coordinates.getY() - Math.sin(waypoint.getAngle()) * distance));
      waypoint = newWaypoint;
      
      double distanceLeft = distance - usedDistance;
      if(distanceLeft > 0)moveRecursive(distanceLeft);
    }

    @Override
    double getDistanceFromWaypoint() {
      return distanceFromWaypoint;
    }
  }
}
