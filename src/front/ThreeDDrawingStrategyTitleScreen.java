package front;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import back.Level;

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
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();

    gl.glOrtho(-level.getSchema().outerRadius() - level.getSchema().innerRadius() - 30, level.getSchema().outerRadius() + level.getSchema().innerRadius() + 30, 
               -level.getSchema().outerRadius() - level.getSchema().innerRadius() - 30, level.getSchema().outerRadius() + level.getSchema().innerRadius() + 30, 
               -100000, 100000);
    
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  @Override
  protected void transformScene(GL2 gl, Level level) {
    gl.glRotated(angle, 1, 1, 0);
  }

}
