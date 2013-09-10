import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for loading levels from files.
 * @author Jim
 */
public class LevelLoader {

  // The levels directory
  final static String LEVEL_DIR = "levels" + File.separator;
  
  // The level file extension
  final static String LEVEL_EXTENSION = ".level";
	
  /**
   * Loads up a level with the given name
   * @param levelName -- filename without extension
   * @return the level
   * @throws FileNotFoundException
   * @throws BadFileException
   */
  public static Level load(String levelName) throws FileNotFoundException, BadFileException {
	
    // The file we're loading up
	  File levelFile = new File(LEVEL_DIR + levelName + LEVEL_EXTENSION);
	  
	  // How far to look ahead in the file while parsing through it
  	final int HORIZON = 999999999;
  	
  	// We'll stuff errors in here if we find problems
	  BadFileException errors = new BadFileException();
	
	  // Setup level height
	  double height = 0;
    Scanner scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("height", HORIZON) == null)
      errors.addError("Level height is missing.");
    else {
      try {
        height = scanner.nextDouble();
      } 
      catch(Exception e) {
    	errors.addError("Level height is invalid.");  
      }
    }
    scanner.close();
    
    // Setup level width
    double width = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("width", HORIZON) == null)
      errors.addError("Level width is missing.");
    else {
      try {
        width = scanner.nextDouble();	  
      }
      catch(Exception e) {
        errors.addError("Level width is invalid.");	  
      }
    }
    scanner.close();
    
    // Setup snake's x coordinates
    double snakeX = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("snakeX", HORIZON) == null)
      errors.addError("snakeX is missing.");
    else {
      try {
        snakeX = scanner.nextDouble();
      }
      catch(Exception e) {
        errors.addError("snakeX is invalid.");
      }
    }
    scanner.close();
    
    // Setup snake's y coordinate
    double snakeY = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("snakeY", HORIZON) == null)
      errors.addError("snakeY is missing.");
    else {
      try {
        snakeY = scanner.nextDouble();
      }
      catch(Exception e) {
        errors.addError("snakeY is invalid.");
      }
    }
    scanner.close();
    
    // Setup snake's length
    int snakeLength = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("snakeLength", HORIZON) == null)
      errors.addError("snakeLength is missing.");
    else {
      try {
        snakeLength = scanner.nextInt(); 
      }
      catch(Exception e) {
        errors.addError("snakeLength is invalid."); 
      }
    }
    scanner.close();
    
    // Setup snake's angle
    double snakeAngle = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("snakeAngle", HORIZON) == null)
      errors.addError("snakeAngle is missing");
    else {
      try {
        snakeAngle = scanner.nextDouble();
      }
      catch(Exception e) {
        errors.addError("snakeAngle is invalid");
      }
    }
    scanner.close();
    
    // Setup snake's speed
    double snakeSpeed = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("snakeSpeed", HORIZON) == null)
      errors.addError("snakeSpeed is missing");
    else {
      try {
        snakeSpeed = scanner.nextDouble();
      }
      catch(Exception e) {
        errors.addError("snakeSpeed is invalid");
      }
    }
    scanner.close();
    
    // Setup foodNumber
    int foodNumber = 0;
    scanner = new Scanner(levelFile);
    if(scanner.findWithinHorizon("foodNumber", HORIZON) == null)
      errors.addError("foodNumber is missing");
    else {
      try {
        foodNumber = scanner.nextInt();
      }
      catch(Exception e) {
       errors.addError("snakeSpeed is invalid"); 
      }
    }
    scanner.close();
    
    // Setup obstacles
    List<Obstacle> obstacles = new LinkedList<Obstacle>();
    scanner = new Scanner(levelFile);
    while(scanner.findWithinHorizon("obstacle", HORIZON) != null) {
      try {
        obstacles.add(new Obstacle(
          new Coordinates(null, scanner.nextDouble(), scanner.nextDouble()), 
          scanner.nextDouble(), scanner.nextDouble()
        ));
      } 
      catch(Exception e) {
        errors.addError("Obstacle parameters are wrong");
      }
    }
    scanner.close();
    
    // If there were any problems just throw the errors
    if(errors.getErrors().size() > 0) throw errors;
    
    // Build the level
    Snake snake = new Snake(new Coordinates(null, snakeX, snakeY), snakeAngle, snakeSpeed, snakeLength);
    Level level = new Level(width, height, snake, foodNumber, obstacles);
    return level;
  }
	
  /**
   * This class represents the errors encountered while
   * loading up a level from a file (except file-not-found)
   * @author Jim
   */
  public static class BadFileException extends Exception {
    
    // List of problems
    private List<String> errors = new LinkedList<String>();
    
    /**
     * Adds an error
     * @param error
     */
    public void addError(String error) {
      errors.add(error);	
    }
    
    /**
     * Gets all of the errors
     * @return the errors
     */
    public List<String> getErrors() {
      return errors;	
    }
  }
}
