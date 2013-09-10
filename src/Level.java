import java.util.List;

/**
 * This class represents a level of the game.
 * @author Jim
 */
public class Level {
  
  // Spacing between lines on the surface of any level
  final static double LINE_SPACING = 6.0;
  
  // The dimensions of this level
  private double width;
  private double height;
  
  // The snake that lives on this level
  private Snake snake;
  
  // The food that lives on this level
  private Food food; 
  
  // The number of food items left
  private int foodLeft;
  
  // The obstacles that live on this level
  private List<Obstacle> obstacles;
  
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
  public Level(double width, double height, Snake snake, int foodNumber, List<Obstacle> obstacles) {
    this.width = width;
    this.height = height;
    this.obstacles = obstacles;
    this.snake = snake;
    snake.setLevel(this);
    for(Obstacle o : obstacles)
      o.setLevel(this);
    this.foodLeft = foodNumber;
    placeFood();
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
    for(Obstacle o : obstacles) 
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
   * Getter for obstacles
   * @return obstacles
   */
  public List<Obstacle> getObstacles() {
    return obstacles;
  }
  
  /**
   * Randomly places food onto the level
   */
  public void placeFood() {
    do {
      food = new Food(new Coordinates(this, Math.random() * width, Math.random() * height));
    } 
    while(foodOverlapsWithObstacle());
  }
  
  /**
   * @return whether food overlaps with any obstacle
   */
  private boolean foodOverlapsWithObstacle() {
    for(Obstacle o : obstacles)
      if(o.overlapsWithCircle(food))
        return true;
    return false;
  }
  
  /**
   * @return the innerRadius of the torus that this level maps onto
   */
  public double innerRadius() {
    return getHeight() / (Math.PI * 2);
  }
  
  /**
   * @return the outerRadius of the torus that this level maps onto
   */
  public double outerRadius() {
    return getWidth() / (Math.PI * 2);
  }
}
