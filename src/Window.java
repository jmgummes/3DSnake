import javax.swing.JFrame;

/**
 * This class represents the window that the game is displayed
 * in
 * @author Jim
 */
public final class Window extends JFrame {
  
  // Dimensions
  final int WIDTH = 600;
  final int HEIGHT = 600;
  
  // There's one window -- this is it
  private static final Window SINGLETON_INSTANCE = new Window();
  
  /**
   * Constructor, pretty basic
   */
  private Window() {
    super("3D Snake -- Jim Gummeson");   
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  /**
   * Getter for the window
   * @return return the window
   */
  public static Window getWindow() {
    return SINGLETON_INSTANCE;
  }
}
