import javax.media.opengl.GL;

/**
 * This class represents the algorithm for dawing the level
 * in 3D onto the title screen.
 * @author Jim
 */
public class ThreeDDrawingStrategyTitleScreen extends ThreeDDrawingStrategy {

  // The angle to rotate the torus by
  private double angle;
  
  /**
   * Constructor, pretty basic
   * @param angle
   */
  public ThreeDDrawingStrategyTitleScreen(double angle) {
    this.angle = angle;  
  }
  
  @Override
  protected void setupCamera(GL gl, Level level) {
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();

    gl.glOrtho(-level.outerRadius() - level.innerRadius() - 30, level.outerRadius() + level.innerRadius() + 30, 
               -level.outerRadius() - level.innerRadius() - 30, level.outerRadius() + level.innerRadius() + 30, 
               -100000, 100000);
    
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  @Override
  protected void transformScene(GL gl, Level level) {
    gl.glRotated(angle, 1, 1, 0);
  }

}
