import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a level of the game.
 * @author Jim
 */
public class Level {
	
  // Spacing between lines on the surface of any level
  final static double LINE_SPACING = 6.0;
	
  abstract static class Description {
	// Name
    protected abstract String getName();
	  
	// The dimensions of this level
	protected abstract double getWidth();
	protected abstract double getHeight();
	
	// The starting food number
	protected abstract int getStartingFoodNumber();

	// The obstacles that live on this level
	protected List<Obstacle.Description> getObstacleDescriptions() {
	  return Collections.emptyList();		
	}
	  
	// The snake's starting x coordinate
	protected abstract double getSnakeStartingX();
	
	// The snake's starting y coordinate
	protected abstract double getSnakeStartingY();
	
	// The snake's starting angle
	protected abstract double getSnakeStartingAngle();
	
	// The snake's starting speed
	protected abstract double getSnakeStartingSpeed();
	
	// The snake's starting length
	protected abstract int getSnakeStartingLength();
  }
  
  // Level description
  private Description description;
  
  // The snake that lives on this level
  private Snake snake;
  
  // The food that lives on this level
  private Food food; 
  
  // The number of food items left
  private int foodLeft;
  
  /**
   * The possible states that a level can be in
   * @author Jim
   */
  enum State {
    NORMAL {
      public boolean gameOver() {return false;}   
    },
    WON {
      public boolean gameOver() {return true;}
    }, 
    LOST {
      public boolean gameOver() {return true;}
    };
    
    abstract boolean gameOver();
  }
  
  // The state of this level
  private State state = State.NORMAL;
  
  /**
   * Constructor
   * @param width
   * @param height
   * @param snake
   * @param foodNumber
   * @param obstacles
   */
  public Level(Description description) {
	this.description = description;
    this.snake = new Snake(
      this, description.getSnakeStartingX(), description.getSnakeStartingY(),
      description.getSnakeStartingAngle(), description.getSnakeStartingSpeed(),
      description.getSnakeStartingLength()
    );
    for(Obstacle o : this.getObstacles())
      o.setLevel(this);
    this.foodLeft = description.getStartingFoodNumber();
    placeFood();
  }
  
  /**
   * Get the description of this level
   * @return description
   */
  public Description getDescription() {
    return this.description;
  }
  
  /**
   * Get the obstacles
   * @return obstacles
   */
  public List<Obstacle> getObstacles() {
    List<Obstacle> obstacles = new LinkedList<Obstacle>();
    for(Obstacle.Description od : this.description.getObstacleDescriptions())
    	obstacles.add(new Obstacle(this, od));
    return obstacles;
  }
  
  /**
   * Updates this level for one frame of the game
   * @param angle -- the amount that the head of the snake has rotated
   */
  public void update(double angle) {
    
    // Rotate the head
    if(angle != 0) 
      snake.getHead().rotate(angle);
    
    // Move the snake
    snake.move();
    
    // Check for collisions with food
    if(snake.getHead().overlapsWithCircle(food)) {
      foodLeft--;
      snake.new BodySegment();
      if(foodLeft > 0)
        placeFood();
      else
        state = State.WON;
    }
    
    // Check for collisions with snake
    for(Snake.Segment s : snake) 
      if(s != snake.getHead() 
      && snake.getHead().overlapsWithCircle(s))
        state = State.LOST;
    
    // Check for collisions with obstacle
    for(Obstacle o : getObstacles()) 
      if(o.overlapsWithCircle(snake.getHead()))
        state = State.LOST;
  }
  
  /**
   * Getter for state
   * @return state
   */
  public State getState() {
    return state;
  }
  
  /**
   * Getter for foodLeft
   * @return foodLeft
   */
  public int getFoodLeft() {
    return foodLeft;
  }
  
  /**
   * Getter for snake
   * @return snake
   */
  public Snake getSnake() {
    return snake;
  }
  
  /**
   * Getter for food
   * @return food
   */
  public Food getFood() {
    return food;
  }
  
  /**
   * Randomly places food onto the level
   */
  public void placeFood() {
    do {
      food = new Food(new Coordinates(this, Math.random() * description.getWidth(), Math.random() * description.getHeight()));
    } 
    while(foodOverlapsWithObstacle());
  }
  
  /**
   * @return whether food overlaps with any obstacle
   */
  private boolean foodOverlapsWithObstacle() {
    for(Obstacle o : getObstacles())
      if(o.overlapsWithCircle(food))
        return true;
    return false;
  }
  
  /**
   * @return the innerRadius of the torus that this level maps onto
   */
  public double innerRadius() {
    return description.getHeight() / (Math.PI * 2);
  }
  
  /**
   * @return the outerRadius of the torus that this level maps onto
   */
  public double outerRadius() {
    return description.getWidth() / (Math.PI * 2);
  }
}
