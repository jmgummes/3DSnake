import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

/**
 * This class represents a top-level display that
 * is put inside of a JFrame window.  Each display
 * has many SubDisplays which display the important
 * content.
 * @author Jim
 */
public abstract class Display extends GLCanvas implements GLEventListener { 
  // The dimensions of this display
  private int width = getWidth();
  private int height = getHeight();
  
  /**
   * Constructor, pretty basic
   */
  public Display() {
    addGLEventListener(this);
    requestFocus();
  }
  
  /**
   * Draws this display.
   */
  @Override
  public final void display(GLAutoDrawable drawable) {
	GLUT glut = new GLUT();
	
    GL gl = drawable.getGL();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glClearColor(0f, 0f, 0f, 0f);
    
    for(SubDisplay sD : getSubDisplays()) {
      sD.viewPort(drawable);
      sD.render(drawable);
    }  
  }
  
  /**
   * Returns a list of SubDisplays that are part 
   * of this display.  Subclass implements this.
   * @return list of SubDisplays
   */
  protected abstract List<SubDisplay> getSubDisplays();

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    width = w;
    height = h;
    display(drawable);
  }
  
  @Override
  public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

  @Override
  public void init(GLAutoDrawable arg0) {}
  

  /**
   * This class represents a SubDisplay that
   * shows some graphics in a rectangular 
   * region of a display.
   * @author Jim
   */
  public abstract class SubDisplay {
    
    /**
     * x coordinate of the top left corner.  0 is the left
     * edge; 1 is the right edge.
     * @return x coordinate 
     */
    abstract protected double getX();
    
    /**
     * y coordinate of the top left corner. 0 is the top
     * edge; 1 is the bottom edge.
     * @return y coordinate of the top left corner
     */
    abstract protected double getY();
    
    /**
     * width of this SubDisplay.  1 is all the way across.
     * @return width
     */
    abstract protected double getW();
    
    /**
     * height of this SubDisplay. 1 is all the way down.
     * @return height
     */
    abstract protected double getH();
    
    /**
     * number of pixels of padding.  padding is
     * the same at each side.
     * @return padding
     */
    abstract protected double getPadding();
    
    /**
     * @return aspect ratio
     */
    abstract protected double getAspectRatio();  
    
    /**
     * Draws this SubDisplay.
     * @param drawable
     */
    abstract void render(GLAutoDrawable drawable);
    
    /**
     * Does an OpenGL viewport call to make this SubDisplay
     * get drawn in the correct region without being distorted.
     * @param drawable
     */
    private final void viewPort(GLAutoDrawable drawable) {
      int viewPortX = (int)(getX() * getWidth() + getPadding());
      int viewPortY = (int)( height - getY() * height - getH() * height + getPadding() );
      int viewPortWidth = (int)(getW() * getWidth() - 2 * getPadding());
      int viewPortHeight = (int)(getH() * height - 2 * getPadding());
      
      double enclosingAspectRatio =   
        (getW() * width - 2 * getPadding()) / ((double)(getH() * height - 2 * getPadding()));
      
      if(getAspectRatio() < enclosingAspectRatio) {
        // Too tall / narrow
        double WidthScaleDown = 
          enclosingAspectRatio / (double) getAspectRatio();
        viewPortWidth = (int)(viewPortWidth / WidthScaleDown);
        int extraSpace = (int)(getW() * width - viewPortWidth);
        viewPortX = (int)(getX() * width + extraSpace / 2);
      }
      else if(getAspectRatio() > enclosingAspectRatio) {
        // Too wide / short
        double heightScaleDown =
          getAspectRatio() / (double) enclosingAspectRatio;
        viewPortHeight = (int)(viewPortHeight / heightScaleDown);
        int extraSpace = (int)(getH() * height - viewPortHeight);
        viewPortY = (int)(height - getY() * height - getH() * height + extraSpace / 2);
      }
      
      drawable.getGL().glViewport(viewPortX, viewPortY, viewPortWidth, viewPortHeight);
    }
  }
}
