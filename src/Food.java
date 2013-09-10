/**
 * This class represents a circular piece of food in the 
 * game.
 * @author Jim
 */
public class Food extends Circle {
  
  // Coordinates
  private Coordinates coordinates;
  
  // Radius 
  final static double RADIUS = 5;
  
  /** 
   * Constructor, pretty basic
   * @param coordinates
   */
  public Food(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  double getRadius() {
    return RADIUS;
  }

  @Override
  public Coordinates getCoordinates() {
    return coordinates;
  }
}
