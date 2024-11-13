import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * This class represents the algorithm for drawing
 * the level onto a torus.  Subclasses hook into
 * the algorithm by providing their own setupCamera()
 * and transformScene() subroutines.
 * @author Jim
 */
public abstract class ThreeDDrawingStrategy {
  
  /**
   * Sets up the camera for viewing the level in 3D.
   * @param gl
   * @param level
   */
  protected abstract void setupCamera(GL2 gl, Level level);
  
  /**
   * Transforms the whole level
   * @param gl
   * @param level
   */
  protected abstract void transformScene(GL2 gl, Level level);

  
  /**
   * The basic algorithm for drawing the 3D scene
   * @param drawable
   * @param level
   */
  public final void render(GLAutoDrawable drawable, Level level) {
    GL2 gl = drawable.getGL().getGL2();
    GLUT glut = new GLUT();
    
    gl.glEnable(GL.GL_DEPTH_TEST);
    
    // Setup the camera, the subclass provides the implementation
    setupCamera(gl, level);
    
    // Remember where we were before transforming the whole scene
    gl.glPushMatrix();
    
    // Transform the scene, the subclass provides the implementation
    transformScene(gl, level);
    
    // Now we just draw everything..
    //
    
    // Draw the torus
    gl.glColor3f(0,1f,0);
    glut.glutWireTorus(
      level.innerRadius(), level.outerRadius(), 
      (int)(level.getDescription().getHeight() / Level.LINE_SPACING), 
      (int)(level.getDescription().getWidth() / Level.LINE_SPACING)
    );
    
    // Draw the snake
    gl.glColor3d(1, 0, 0);
    for(Snake.Segment s: level.getSnake()) {
      gl.glPushMatrix();     
      Vector3d coords = s.getCoordinates().get3DCoordinates(s.threeDRadius());
      gl.glTranslated(coords.x, coords.y, coords.z);
      glut.glutWireSphere(s.threeDRadius(), 20, 20);
      gl.glPopMatrix();
    }
    
    // Draw the food
    if(level.getFood() != null) {
      gl.glColor3d(1, 0, 1);   
      gl.glPushMatrix();   
      Vector3d coords = level.getFood().getCoordinates().get3DCoordinates(level.getFood().threeDRadius());
      gl.glTranslated(coords.x, coords.y, coords.z); 
      glut.glutWireSphere(level.getFood().threeDRadius(), 10, 10);
      gl.glPopMatrix(); 
    }
    
    // Draw the Obstacles..
    //
    gl.glColor3d(.3,.3,1);
  
    // This is the ideal spacing between adjacent 
    // lines on the obstacle
    final double IDEAL_CHUNK_SIZE = 3;
    for(Obstacle o : level.getObstacles()) {
      
      // Figure out the number of / size of chunks along the x
      // direction
      int numXChunks = (int) (o.getWidth() / IDEAL_CHUNK_SIZE); 
      if(numXChunks == 0) numXChunks++;
      double xChunkSize = o.getWidth() / numXChunks; 
      
      // Figure out the number of / size of chunks along the y
      // direction
      int numYChunks = (int)(o.getHeight() / IDEAL_CHUNK_SIZE);
      if(numYChunks == 0) numYChunks++;
      double yChunkSize = o.getHeight() / numYChunks;
      
      // The obstacle has two layers of vertices. Set them up.
      Vector3d[][] bottomLayer = new Vector3d[numXChunks+1][numYChunks+1];
      Vector3d[][] topLayer = new Vector3d[numXChunks+1][numYChunks+1];
      for(int x = 0; x <= numXChunks; x++) {
        for(int y = 0; y <= numYChunks; y++) {
          
          double xCoord = o.getLeftSideX() + x * xChunkSize;
          double yCoord = o.getTopSideY() + y * yChunkSize;
                   
          bottomLayer[x][y] = o.getCoordinates().get3DCoordinates(xCoord, yCoord, 0);     
          topLayer[x][y] = o.getCoordinates().get3DCoordinates(xCoord, yCoord, o.threeDHeight());
        }
      }
      
      // Draw the lines that go along the y direction on the bottom layer
      for(int x = 0; x <= numXChunks; x++) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for(int y = 0; y <= numYChunks; y++) {
          gl.glVertex3d(bottomLayer[x][y].x, bottomLayer[x][y].y, bottomLayer[x][y].z);
        }
        gl.glEnd();
      }
      
      // Draw the lines that go along the x direction on the bottom layer
      for(int y = 0; y <= numYChunks; y++) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for(int x = 0; x <= numXChunks; x++) {
          gl.glVertex3d(bottomLayer[x][y].x, bottomLayer[x][y].y, bottomLayer[x][y].z);
        }
        gl.glEnd();
      }
      
      // Draw the lines that go along the y direction on the top layer
      for(int x = 0; x <= numXChunks; x++) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for(int y = 0; y <= numYChunks; y++) {
          gl.glVertex3d(topLayer[x][y].x, topLayer[x][y].y, topLayer[x][y].z);
        }
        gl.glEnd();
      }
      
      // Draw the lines that go along the x direction on the top layer
      for(int y = 0; y <= numYChunks; y++) {
        gl.glBegin(GL.GL_LINE_STRIP);
        for(int x = 0; x <= numXChunks; x++) {
          gl.glVertex3d(topLayer[x][y].x, topLayer[x][y].y, topLayer[x][y].z);
        }
        gl.glEnd();
      }
      
      // Draw the lines that go from the bottom layer to the top layer
      gl.glBegin(GL.GL_LINES);
      for(int x = 0; x <= numXChunks; x++) {
        for(int y = 0; y <= numYChunks; y++) {
          gl.glVertex3d(bottomLayer[x][y].x, bottomLayer[x][y].y, bottomLayer[x][y].z);
          gl.glVertex3d(topLayer[x][y].x, topLayer[x][y].y, topLayer[x][y].z);
        }
      }
      gl.glEnd();
    } 
   
    // Go back to where we were before transforming the whole scene
    gl.glPopMatrix();
  }
  
}
