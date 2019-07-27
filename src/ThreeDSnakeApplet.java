import java.applet.Applet;
import java.awt.BorderLayout;


/**
 * This class represents the window that the game is displayed
 * in
 * @author Jim
 */
public final class ThreeDSnakeApplet extends Applet {
  @Override
  public void init() {
    TitleScreen titleScreen = new TitleScreen();
    this.setLayout(new BorderLayout());
    this.add(titleScreen, BorderLayout.CENTER);
    titleScreen.display();
  }
}
