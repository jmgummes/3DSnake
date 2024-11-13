package front;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.GLAutoDrawable;

import javax.vecmath.Color3f;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * This class represents a strategy for rendering 
 * text onto the screen.
 * @author Jim
 */
public class TextRenderingStrategy {
  
  // Spacing between lines of text
  private final static double LINE_SPACING = 35;
  
  // The lines that this strategy renders
  private List<Line> lines = new LinkedList<Line>();
  
  private GLUT glut = new GLUT();
  
  /**
   * Constructor, pretty basic
   * @param line
   */
  public TextRenderingStrategy(Line line) {
    lines.add(line);
  }
  
  /**
   * Constructor, pretty basic
   * @param lines
   */
  public TextRenderingStrategy(List<Line> lines) {
    this.lines.addAll(lines);
  }
  
  /**
   * The width of the orthographic projection that
   * the text gets rendered onto
   * @return width of projection
   */
  public double orthoWidth() {
    double max = 0;
    for(Line l : lines) {
      if(l.scaledWidth() > max) max = l.scaledWidth();
    }
    return max;
  }
  
  /**
   * The height of the orthographic projection that
   * the text gets rendered onto
   * @return height of projection
   */
  public double orthoHeight() {
    double sum = 0;
    for(int i = 0; i < lines.size(); i++)
      sum += lines.get(i).scaledHeightIncludingSpacing();
    return sum;
  }
  
  /**
   * Renders the text
   * @param drawable
   */
  public void render(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();

    gl.glDisable(GL.GL_DEPTH_TEST);
    
    gl.glMatrixMode (GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrtho(0, orthoWidth(), orthoHeight(), 0, 0, 1);
    
    gl.glMatrixMode (GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadIdentity();
    
    for(Line l : lines) {
      gl.glColor3f(l.getColor().x, l.getColor().y, l.getColor().z); 
      gl.glPushMatrix();
      gl.glRotated(180, 1, 0, 0);
      gl.glTranslated(0, -l.scaledHeight(), .00001);
      gl.glScaled(l.getScale(), l.getScale(), l.getScale());
      glut.glutStrokeString(GLUT.STROKE_ROMAN, l.getText());
      gl.glPopMatrix();
      gl.glTranslated(0, l.scaledHeightIncludingSpacing(), 0);
    }
  } 
  
  /**
   * This class represents one line of text to be rendered
   * @author Jim
   */
  public static class Line {
  
    // The height of the tallest letter?
    private final double LETTER_HEIGHT = 113;
    
    // The text to render
    private String text;
    
    // The scaling to apply to the text
    private double scale;
    
    // The color of the text
    private Color3f color;
    
    /**
     * Constructor, pretty basic
     * @param string
     * @param scale
     * @param color
     */
    public Line(String string, double scale, Color3f color) {
      this.text = string;
      this.scale = scale;
      this.color = color;
    }
    
    /**
     * @return the length of the text
     */
    public double textLength() {
      return new GLUT().glutStrokeLength(GLUT.STROKE_ROMAN, text); 
    }
    
    /**
     * @return the scaled height of the text
     */
    public double scaledHeight() {
      return LETTER_HEIGHT * scale; 
    }
    
    /**
     * @return the scaled height of the text including line spacing
     */
    public double scaledHeightIncludingSpacing() {
      return scaledHeight() + LINE_SPACING * scale; 
    }
    
    /**
     * @return the scaled width of the text
     */
    public double scaledWidth() {
      return textLength() * scale;
    }
    
    /**
     * @return the color of the text
     */
    public Color3f getColor() {
      return color;
    }
    
    /**
     * @return the scaling to apply to the text
     */
    public double getScale() {
      return scale;
    }
    
    /**
     * @return the text
     */
    public String getText() {
      return text;
    }
  }
}
