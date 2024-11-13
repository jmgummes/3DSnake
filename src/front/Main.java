package front;
/**
 * This is the main class for running the game.  Pretty basic.
 * @author Jim
 */
public class Main {
  
  /**
   * Runs the game.
   */
  public static void main(String[] args) {
    System.setProperty("sun.java2d.uiScale", "1.0");
    Window window = new Window();
    new TitleScreen(window);
    window.setVisible(true);
  }
  
}
