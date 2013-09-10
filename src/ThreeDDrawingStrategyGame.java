import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector4d;

/**
 * This class represents the algorithm for drawing the level
 * in 3D for the main game.
 * @author Jim
 */
public class ThreeDDrawingStrategyGame extends ThreeDDrawingStrategy {
  
  /**
   * Puts the camera inside of the snake's head and aims it
   * in the direction that the snake is looking.
   */
  @Override
  protected void setupCamera(GL gl, Level level) {
    
    // setup the frustum
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    double nearRadius = level.getSnake().getHead().threeDRadius()*2.5;
    gl.glFrustum(-nearRadius, nearRadius, -nearRadius, nearRadius, level.getSnake().getHead().threeDRadius()*1.1, 1000);
    
    // go back to modelview
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
    
    // Setup the camera...
    //
    
    // Transformation for the camera location
    Matrix4d cameraTransformation =
      level.getSnake().getHead().getCoordinates().getCoordinatesTransformation(level.getSnake().getHead().threeDRadius());
    
    // Transformation for moving to a point above the camera
    Matrix4d aboveCameraTransformation = new Matrix4d(cameraTransformation);
    aboveCameraTransformation.mul( new Matrix4d(
      1, 0, 0, 0,
      0, 1, 0, level.getSnake().getHead().threeDRadius(),
      0, 0, 1, 0,
      0, 0, 0, 1
    ));
 
    // Transformation for moving to a point ahead of the snake
    // in the direction that the snake is looking.
    Matrix4d lookingAtTransformation = new Matrix4d(cameraTransformation);
    Matrix4d headFacingTransformation = new Matrix4d();
    headFacingTransformation.rotY(level.getSnake().getHead().getWaypoint().getAngle());
    lookingAtTransformation.mul(headFacingTransformation);
    lookingAtTransformation.mul(new Matrix4d(
      1, 0, 0, -level.getSnake().getHead().threeDRadius(),
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    ));
    
    // Now compute the positions that we need to setup the camera
    Vector4d cameraPos = new Vector4d(0, 0, 0, 1);
    Vector4d lookingAtPos = new Vector4d(0, 0, 0, 1);
    Vector4d aboveCameraPos = new Vector4d(0, 0, 0, 1);
    
    cameraTransformation.transform(cameraPos);
    aboveCameraTransformation.transform(aboveCameraPos);
    lookingAtTransformation.transform(lookingAtPos);
   
    // Compute the up vector for the camera
    Vector4d upVector = new Vector4d(aboveCameraPos);
    upVector.sub(cameraPos);
      
    // Finally, setup the camera
    new GLU().gluLookAt(
      cameraPos.x, cameraPos.y, cameraPos.z, 
      lookingAtPos.x, lookingAtPos.y, lookingAtPos.z, 
      upVector.x, upVector.y, upVector.z
    );  
  }

  @Override
  protected void transformScene(GL gl, Level level) {}

}
