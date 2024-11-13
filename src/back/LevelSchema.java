package back;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class LevelSchema {
	// Name
    public abstract String getName();
	  
	// The dimensions of this level
	public abstract double getWidth();
	public abstract double getHeight();
	
	// The starting food number
	public abstract int getStartingFoodNumber();

	// The obstacles that live on this level schema
	public List<Obstacle> getObstacles() {
	  return Collections.emptyList();		
	}
	  
	// The snake's starting x coordinate
	public abstract double getSnakeStartingX();
	
	// The snake's starting y coordinate
	public abstract double getSnakeStartingY();
	
	// The snake's starting angle
	public abstract double getSnakeStartingAngle();
	
	// The snake's starting speed
	public abstract double getSnakeStartingSpeed();
	
	// The snake's starting length
	public abstract int getSnakeStartingLength();
	
	/**
	 * @return the innerRadius of the torus that this level schema maps onto
	 */
	public double innerRadius() {
	  return getHeight() / (Math.PI * 2);
	}
	  
	/**
	 * @return the outerRadius of the torus that this level schema maps onto
	 */
	public double outerRadius() {
	  return getWidth() / (Math.PI * 2);
	}
	
	public abstract class Obstacle {
	  public abstract double getX();
	  public abstract double getY();
			
	  public abstract double getWidth();
	  public abstract double getHeight();
	  
	  public final Coordinates getCoordinates() {
	    return new Coordinates(LevelSchema.this, getX(), getY());
	  }
	  
	  /**
	   * Finds whether this obstacle overlaps with a given circle
	   * @param c
	   * @return whether this obstacle overlaps with c
	   */
	  public boolean overlapsWithCircle(Circle c) {
	    for(Obstacle o : split())
	      if(overlap(o.getCoordinates().getX(), o.getCoordinates().getX() + o.getWidth(), c.getCoordinates().getX() - c.getRadius(), c.getCoordinates().getX() + c.getRadius())
	      && overlap(o.getCoordinates().getY(), o.getCoordinates().getY() + o.getHeight(), c.getCoordinates().getY() - c.getRadius(), c.getCoordinates().getY() + c.getRadius()))
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
	   * @return x coordinate of the left side of this Obstacle
	   */
	  public double getLeftSideX() {
	    return getX();
	  }
	  
	  /**
	   * @return x coordinate of the right side of this Obstacle
	   */
	  public double getRightSideX() {
	    double toReturn = getLeftSideX() + getWidth();
	    while(toReturn > LevelSchema.this.getWidth())
	      toReturn -= LevelSchema.this.getWidth();
	    return toReturn;
	  }
	  
	  /**
	   * @return y coordinate of the top side of this Obstacle
	   */
	  public double getTopSideY() {
	    return getCoordinates().getY();
	  }
	  
	  /**
	   * @return y coordinate of the bottom side of this Obstacle
	   */
	  public double getBottomSideY() {
	    double toReturn = getTopSideY() + getHeight();
	    while(toReturn > LevelSchema.this.getHeight())
	      toReturn -= LevelSchema.this.getHeight();
	    return toReturn;
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
	      LevelSchema.this.outerRadius() + 
	      Math.cos(getCoordinates().yToAngle(getTopSideY() + getHeight() / 2.0)) * LevelSchema.this.innerRadius()
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
	      Obstacle left = LevelSchema.this.new ObstacleWithData(getLeftSideX(), getTopSideY(),
	                                   LevelSchema.this.getWidth() - getLeftSideX(),
	                                   getHeight());
	      
	      Obstacle right = LevelSchema.this.new ObstacleWithData(0, getTopSideY(),
	                                    getRightSideX(), getHeight());
	      
	      obstacles.addAll(left.split());
	      obstacles.addAll(right.split());
	    }
	    else if(getBottomSideY() <= getTopSideY()) {
	      Obstacle bottom = LevelSchema.this.new ObstacleWithData(getLeftSideX(), getTopSideY(),
	                                     getWidth(), LevelSchema.this.getHeight() - getTopSideY());
	      
	      Obstacle top = new ObstacleWithData(getLeftSideX(), 0,
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
	
	public class ObstacleWithData extends Obstacle {
       private double x;
       private double y;
	   private double width;
	   private double height;
		
      public ObstacleWithData(double x, double y, double width, double height) {
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
      }

	  @Override
 	  public double getX() {
	    return x;
	  }

	  @Override
  	  public double getY() {
		return y;
	  }

	@Override
	public double getWidth() {
	  return width;
	}

	@Override
	public double getHeight() {
	  return height;
	}
  }
}
