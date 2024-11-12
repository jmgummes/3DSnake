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
  
  /**
   * Constructor, pretty basic
   */
  public Window() {
    super("3D Snake");   
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
}
