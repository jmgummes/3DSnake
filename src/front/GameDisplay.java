package front;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import back.Circle;
import back.Level;
import back.LevelSchema;
import back.LevelSchemaLoader;
import back.Snake;

import com.jogamp.opengl.GLAutoDrawable;

import javax.swing.Timer;
import javax.vecmath.Color3f;

/**
 * This class represents the display for the main game
 * 
 * @author Jim
 */
public class GameDisplay extends Display implements KeyListener, ActionListener {

  final static double LINE_SPACING = 6.0;

  // The window that this GameDisplay is inside
  private Window window;

  // The available level schemas that were loaded at the beginning of the program
  private List<LevelSchema> levelSchemas;

  // The level being played
  private Level level;

  // Animates the game
  Timer animation = new Timer(20, this);;

  // Number of frames before returning to the title screen
  // when you get a game over
  private int END_GAME_TIMER_MAX = 100;

  // Counts down time until returning to the title screen
  private int endGameTimer = END_GAME_TIMER_MAX;

  // Keep track of which left/right keys are down
  private boolean leftKeyDown = false;
  private boolean rightKeyDown = false;

  // The padding for SubDisplays in this Display
  private final int PADDING = 10;

  /**
   * Constructor, pretty basic
   * 
   * @param level
   */
  public GameDisplay(Window window, List<LevelSchema> levelSchemas, Level level) {
    super();
    this.window = window;
    this.levelSchemas = levelSchemas;
    this.level = level;
    addKeyListener(this);
    animation.start();
  }

  @Override
  protected List<SubDisplay> getSubDisplays() {
    List<SubDisplay> subDisplays = new LinkedList<SubDisplay>();
    subDisplays.add(new TwoDView());
    subDisplays.add(new ThreeDView());
    subDisplays.add(new StateDisplay());
    subDisplays.add(new FoodLeftDisplay());
    return subDisplays;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_LEFT:
      leftKeyDown = true;
      break;
    case KeyEvent.VK_RIGHT:
      rightKeyDown = true;
      break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
    case KeyEvent.VK_LEFT:
      leftKeyDown = false;
      break;
    case KeyEvent.VK_RIGHT:
      rightKeyDown = false;
      break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  /**
   * Updates the game for each frame of animation
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (level.getState().gameOver()) {
      endGameTimer--;
      if (endGameTimer <= 0)
        returnToTitleScreen();
    } else {
      double angle = 0;
      if (rightKeyDown)
        angle -= (Math.PI * 2 * 5) / 360.0;
      if (leftKeyDown)
        angle += (Math.PI * 2 * 5) / 360.0;
      level.update(angle);
      display();
    }
  }

  /**
   * Switches the game back to the title screen after a game-over
   */
  private void returnToTitleScreen() {
    animation.stop();
    animation.removeActionListener(this);
    this.window.remove(this);
    TitleScreen titleScreen = new TitleScreen(window, levelSchemas);
    this.window.setVisible(true);
    titleScreen.requestFocus();
    titleScreen.display();
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
  }

  /**
   * SubDisplay for the 2-Dimensional version of the game
   * 
   * @author Jim
   */
  public class TwoDView extends Display.SubDisplay {

    @Override
    protected double getAspectRatio() {
      return level.getSchema().getWidth() / level.getSchema().getHeight();
    }

    @Override
    protected double getH() {
      return .7;
    }

    @Override
    protected double getPadding() {
      return PADDING;
    }

    @Override
    protected double getW() {
      return .5;
    }

    @Override
    protected double getX() {
      return 0;
    }

    @Override
    protected double getY() {
      return .2;
    }

    @Override
    void render(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();

      gl.glDisable(GL.GL_DEPTH_TEST);

      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glOrtho(0, level.getSchema().getWidth(), level.getSchema().getHeight(), 0, 0, 1);

      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glLoadIdentity();

      // Draw a grid of green lines to represent the plane
      gl.glColor3d(0, 1, 0);
      gl.glBegin(GL.GL_LINES);
      for (int i = 0; i < level.getSchema().getWidth(); i += LINE_SPACING) {
        gl.glVertex2d(i, 0);
        gl.glVertex2d(i, level.getSchema().getHeight());
      }
      for (int i = 0; i < level.getSchema().getHeight(); i += LINE_SPACING) {
        gl.glVertex2d(0, i);
        gl.glVertex2d(level.getSchema().getWidth(), i);
      }
      gl.glEnd();

      // Draw each snake segment
      gl.glColor3d(1, 0, 0);
      for (Snake.Segment s : level.getSnake())
        drawCircle(gl, s);

      // Draw the food
      if (level.getFood() != null) {
        gl.glColor3d(1, 0, 1);
        drawCircle(gl, level.getFood());
      }

      // Draw the obstacles
      gl.glColor3d(.3, .3, 1);
      for (LevelSchema.Obstacle o : level.getSchema().getObstacles()) {
        for (LevelSchema.Obstacle _o : o.split()) {
          gl.glBegin(GL2.GL_QUADS);
          gl.glVertex2d(_o.getLeftSideX(), _o.getTopSideY());
          gl.glVertex2d(_o.getLeftSideX(), _o.getBottomSideY());
          gl.glVertex2d(_o.getRightSideX(), _o.getBottomSideY());
          gl.glVertex2d(_o.getRightSideX(), _o.getTopSideY());
          gl.glEnd();
        }
      }
    }

    /**
     * Draws a circle
     * 
     * @param gl
     * @param c
     */
    private void drawCircle(GL2 gl, Circle c) {
      gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glVertex2d(c.getCoordinates().getX(), c.getCoordinates().getY());
      for (int angle = 0; angle <= 360; angle += 5)
        gl.glVertex2d(c.getCoordinates().getX() + Math.sin((angle / 360.0) * 2 * Math.PI) * c.getRadius(),
            c.getCoordinates().getY() + Math.cos((angle / 360.0) * 2 * Math.PI) * c.getRadius());
      gl.glEnd();
    }
  }

  /**
   * SubDisplay for the 3D version of the game.
   * 
   * @author Jim
   */
  public class ThreeDView extends Display.SubDisplay {

    @Override
    protected double getAspectRatio() {
      return 1;
    }

    @Override
    protected double getH() {
      return .7;
    }

    @Override
    protected double getPadding() {
      return PADDING;
    }

    @Override
    protected double getW() {
      return .5;
    }

    @Override
    protected double getX() {
      return .5;
    }

    @Override
    protected double getY() {
      return .2;
    }

    @Override
    void render(GLAutoDrawable drawable) {
      new ThreeDDrawingStrategyGame().render(drawable, level);
    }
  }

  /**
   * SubDisplay for the state of the game (normal, won, or lost).
   * 
   * @author Jim
   */
  public class StateDisplay extends SubDisplay {

    // Text rendering strategy for the "you lose" message
    private TextRenderingStrategy lostStrategy = new TextRenderingStrategy(
        new TextRenderingStrategy.Line("Sorry, you lose.", .78, new Color3f(1, 0, 0)));

    // Text rendering strategy for the "you win" message
    private TextRenderingStrategy wonStrategy = new TextRenderingStrategy(
        new TextRenderingStrategy.Line("Congratulations, you win!", .78, new Color3f(0, 1, 0)));

    @Override
    protected double getAspectRatio() {
      switch (level.getState()) {
      case WON:
        return wonStrategy.orthoWidth() / wonStrategy.orthoHeight();
      case LOST:
        return lostStrategy.orthoWidth() / lostStrategy.orthoHeight();
      default:
        return 0;
      }
    }

    @Override
    protected double getH() {
      return .2;
    }

    @Override
    protected double getPadding() {
      return PADDING;
    }

    @Override
    protected double getW() {
      return 1;
    }

    @Override
    protected double getX() {
      return 0;
    }

    @Override
    protected double getY() {
      return 0;
    }

    @Override
    public void render(GLAutoDrawable drawable) {
      switch (level.getState()) {
      case WON:
        wonStrategy.render(drawable);
        break;

      case LOST:
        lostStrategy.render(drawable);
        break;
      }
    }
  }

  /**
   * SubDisplay for the amount of food left.
   * 
   * @author Jim
   */
  public class FoodLeftDisplay extends SubDisplay {
    // Text rendering strategy to delegate to
    private TextRenderingStrategy getTextRenderingStrategy() {
      return new TextRenderingStrategy(
          new TextRenderingStrategy.Line("Food left: " + level.getFoodLeft(), 1, new Color3f(1, 1, 1)));
    }

    @Override
    protected double getAspectRatio() {
      return getTextRenderingStrategy().orthoWidth() / getTextRenderingStrategy().orthoHeight();
    }

    @Override
    protected double getH() {
      return .1;
    }

    @Override
    protected double getPadding() {
      return PADDING;
    }

    @Override
    protected double getW() {
      return 1;
    }

    @Override
    protected double getX() {
      return 0;
    }

    @Override
    protected double getY() {
      return .9;
    }

    @Override
    public void render(GLAutoDrawable drawable) {
      getTextRenderingStrategy().render(drawable);
    }
  }
}
