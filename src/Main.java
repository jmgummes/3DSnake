/**
 * This is the main class for running the game.  Pretty basic.
 * @author Jim
 */
public class Main {
  
  /**
   * Runs the game.
   */
  public static void main(String[] args) {
    TitleScreen titleScreen = new TitleScreen();
    Window.getWindow().add(titleScreen);
    Window.getWindow().setVisible(true);
    titleScreen.display();
  }
  
}
