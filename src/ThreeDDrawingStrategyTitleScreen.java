import javax.media.opengl.GL2;

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
  protected void setupCamera(GL2 gl, Level level) {
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    gl.glOrtho(-level.outerRadius() - level.innerRadius() - 30, level.outerRadius() + level.innerRadius() + 30, 
               -level.outerRadius() - level.innerRadius() - 30, level.outerRadius() + level.innerRadius() + 30, 
               -100000, 100000);
    
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  @Override
  protected void transformScene(GL2 gl, Level level) {
    gl.glRotated(angle, 1, 1, 0);
  }

}
