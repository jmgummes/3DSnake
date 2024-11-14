package back;

/**
 * This class represents a level of the game.
 * @author Jim
 */
public class Level {
  
  // Level description
  private back.LevelSchema schema;
  
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
  public enum State {
    NORMAL {
      public boolean gameOver() {return false;}   
    },
    WON {
      public boolean gameOver() {return true;}
    }, 
    LOST {
      public boolean gameOver() {return true;}
    };
    
    public abstract boolean gameOver();
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
  public Level(LevelSchema schema) {
	this.schema = schema;
    this.snake = new Snake(
      this, schema.getSnakeStartingX(), schema.getSnakeStartingY(),
      schema.getSnakeStartingAngle(), schema.getSnakeStartingSpeed(),
      schema.getSnakeStartingLength()
    );
    this.foodLeft = schema.getStartingFoodNumber();
    placeFood();
  }
  
  /**
   * Get the schema of this level
   * @return schema
   */
  public LevelSchema getSchema() {
    return this.schema;
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
    for(LevelSchema.Obstacle o : schema.getObstacles()) 
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
      food = new Food(new Coordinates(schema, Math.random() * schema.getWidth(), Math.random() * schema.getHeight()));
    } 
    while(foodOverlapsWithObstacle());
  }
  
  /**
   * @return whether food overlaps with any obstacle
   */
  private boolean foodOverlapsWithObstacle() {
    for(LevelSchema.Obstacle o : schema.getObstacles())
      if(o.overlapsWithCircle(food))
        return true;
    return false;
  }
  
}
