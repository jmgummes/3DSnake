import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.Timer;
import javax.vecmath.Color3f;

/**
 * This class represents the titlescreen display
 * @author Jim
 */
public class TitleScreen extends Display implements KeyListener, ActionListener {

  // List of level descriptions found in levels directory
  private List<LevelDescription> levelDescriptions = new LinkedList<LevelDescription>(); 
  
  /**
   * The states that this display can be in
   * @author Jim
   */
  enum State {
    NORMAL,
    NO_LEVEL_SELECTED,
    MISSING_LEVEL_SELECTED,
    BAD_LEVEL_SELECTED
  }
  
  // The current state of this display
  private State state = null;
  
  // The level that is selected (if any)
  private Level selectedLevel = null;
  
  // Code for level index when no level is selected
  private final int NO_SELECTED_LEVEL_INDEX = -1;
  
  // The index of the selected level (if any)
  private int selectedLevelIndex = NO_SELECTED_LEVEL_INDEX;
  
  // Animation for the rotating preview
  Timer animation = new Timer(10, this);
  
  // Angle for the rotating preview
  double previewAngle = 0;
  
  @Override
  protected List<SubDisplay> getSubDisplays() {
    List<SubDisplay> subDisplays = new LinkedList<SubDisplay>();
    subDisplays.add(new TitleDisplay());
    subDisplays.add(new AuthorDisplay());
    subDisplays.add(new LevelList());
    subDisplays.add(new SelectedLevelDisplay());
    return subDisplays;
  }
  
  /**
   * Constructor
   */
  public TitleScreen() {
    super();
    
    // Request focus
    addKeyListener(this);
    this.setFocusable(true);
    this.requestFocus();
    
    CodeSource src = Window.class.getProtectionDomain().getCodeSource();
    if (src != null) {
      URL jar = src.getLocation();
      try {
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        ZipEntry entry = null;
        while((entry = zip.getNextEntry()) != null) {
          if(entry.getName().endsWith(".level"))
          this.levelDescriptions.add(new LevelDescription(new File(entry.getName()).getName()));
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }  
    } 
    else {
      System.out.println("Error: getCodeSource() failed");
    }
    
    // Select the first level
    if(levelDescriptions.size() > 0) 
      selectLevel(0); 
    else
      state = State.NO_LEVEL_SELECTED;
    
    this.display();
  }
  
  /**
   * Selects a level
   * @param levelNumber
   */
  public void selectLevel(int levelNumber) {
    selectedLevelIndex = levelNumber;
    state = State.NORMAL;
    try {
      selectedLevel = LevelLoader.load(levelDescriptions.get(selectedLevelIndex).getLevelName());
    }
    catch(FileNotFoundException e) {
      state = State.MISSING_LEVEL_SELECTED; 
    }
    catch(LevelLoader.BadFileException e) {
      System.out.println("Problems with " + levelDescriptions.get(selectedLevelIndex).getLevelName() + ":");
      for(String error : e.getErrors()) {
        System.out.println("-" + error);
      }
      state = State.BAD_LEVEL_SELECTED;
    }
    if(state == State.NORMAL) {
      previewAngle = 0;
      if(!animation.isRunning()) animation.start();
    }
    else {
      if(animation.isRunning())  animation.stop();
      display();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_UP:
        if(state != State.NO_LEVEL_SELECTED) {
          selectLevel((selectedLevelIndex + levelDescriptions.size() - 1) % levelDescriptions.size());
          display();
        }
        break;
      case KeyEvent.VK_DOWN:
        if(state != State.NO_LEVEL_SELECTED) {
          selectLevel((selectedLevelIndex + 1) % levelDescriptions.size());
          display();
        }
        break;
      case KeyEvent.VK_ENTER:  
        if(state == State.NORMAL) {  
          animation.stop();
          animation.removeActionListener(this);
          Applet window = (Applet) this.getParent();
          window.remove(this);
          GameDisplay gameDisplay = new GameDisplay(selectedLevel);
          window.add(gameDisplay, BorderLayout.CENTER);
          window.doLayout();
          gameDisplay.display();
          window.setVisible(true);
          gameDisplay.requestFocus();
        }
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void actionPerformed(ActionEvent arg0) {
    previewAngle += .5;
    display();
  }
  
  /**
   * This class is a basic description of a level file 
   * found on disk.
   * @author Jim
   */
  public class LevelDescription {
    
    // level filename
    private String fileName;
    
    /**
     * Constructor, pretty basic
     * @param fileName
     */
    public LevelDescription(String fileName) {
      this.fileName = fileName;
    }
    
    /**
     * Getter for filename
     * @return filename
     */
    public String getFileName() {
      return fileName;
    }
    
    /**
     * @return the filename without any extension
     */
    public String getLevelName() {
      return fileName.substring(0, fileName.length() - LevelLoader.LEVEL_EXTENSION.length());
    }
    
    /**
     * @return a nice looking version of the levelname that
     *         can be displayed on-screen
     */
    public String getPrettyLevelName() {
      return getLevelName().replace("_", " ");
    }
  }

  /**
   * This class represents the SubDisplay that shows the title
   * @author Jim
   */
  public class TitleDisplay extends SubDisplay {

    // Delegate to this strategy to render
    private TextRenderingStrategy textRenderingStrategy = 
      new TextRenderingStrategy(new TextRenderingStrategy.Line("3D Snake", 1, new Color3f(1,1,1)));
    
    @Override
    protected double getAspectRatio() {
      return textRenderingStrategy.orthoWidth() / textRenderingStrategy.orthoHeight();
    }

    @Override
    protected double getH() {
      return .1;
    }

    @Override
    protected double getPadding() {
      return 10;
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
    void render(GLAutoDrawable drawable) {
      textRenderingStrategy.render(drawable);
    }   
  }
  
  /**
   * This class represents the SubDislpay that shows
   * the author
   * @author Jim
   */
  public class AuthorDisplay extends SubDisplay {
    
    // Delegate to this strategy to render
    private TextRenderingStrategy textRenderingStrategy =
      new TextRenderingStrategy(new TextRenderingStrategy.Line(
        "By Jim Gummeson", 1, new Color3f(1, 1, 1)    
      ));
    
    @Override
    protected double getAspectRatio() {
      return textRenderingStrategy.orthoWidth() / textRenderingStrategy.orthoHeight();
    }

    @Override
    protected double getH() {
      return .05;
    }

    @Override
    protected double getPadding() {
      return 0;
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
      return .1;
    }

    @Override
    void render(GLAutoDrawable drawable) {
      textRenderingStrategy.render(drawable);    
    }   
  }
  
  /**
   * This class represents the SubDisplay that shows the list
   * of levels on disk.
   * @author Jim
   */
  public class LevelList extends SubDisplay {

    // Delegate to this strategy to render
    TextRenderingStrategy textRenderingStrategy;
    
    /**
     * Constructor, does some work to setup the 
     * text rendering strategy properly.
     */
    public LevelList() {
      List<TextRenderingStrategy.Line> lines = 
        new LinkedList<TextRenderingStrategy.Line>();
      lines.add(new TextRenderingStrategy.Line("Levels", 1, new Color3f(1, 1, 1)));
      for(int i = 0; i < levelDescriptions.size(); i++) {
        Color3f color;
        if(i == selectedLevelIndex) color = new Color3f(0,0,1);
        else color = new Color3f(1, 1, 1);
        lines.add(new TextRenderingStrategy.Line(
          levelDescriptions.get(i).getPrettyLevelName(),
          .5, color   
        ));
      }
      textRenderingStrategy = new TextRenderingStrategy(lines);
    }
    
    @Override
    protected double getAspectRatio() {
      return textRenderingStrategy.orthoWidth() / textRenderingStrategy.orthoHeight();
    }

    @Override
    protected double getH() {
      return .85;
    }

    @Override
    protected double getPadding() {
      return 10;
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
      return .15;
    }
    
    @Override
    void render(GLAutoDrawable drawable) {
      textRenderingStrategy.render(drawable);
    } 
  }
  
  /**
   * This class represents the SubDisplay that shows the
   * selected level (or errors if there are problems with it)
   * @author Jim
   */
  public class SelectedLevelDisplay extends SubDisplay {
  
    // Text rendering strategies to delegate to
    private TextRenderingStrategy badLevelStrategy;
    private TextRenderingStrategy missingLevelStrategy;

    /**
     * Constructor, does some work to setup the text rendering strategies
     */
    public SelectedLevelDisplay() {
      List<TextRenderingStrategy.Line> badFileLines = new LinkedList<TextRenderingStrategy.Line>();
      badFileLines.add(new TextRenderingStrategy.Line("Level file has errors!", 1, new Color3f(1, 0, 0)));
      badFileLines.add(new TextRenderingStrategy.Line("(See console)", 1, new Color3f(1, 0, 0)));
      badLevelStrategy = new TextRenderingStrategy(badFileLines);
      missingLevelStrategy = new TextRenderingStrategy(new TextRenderingStrategy.Line(
        "File not found!", 1, new Color3f(1, 0, 0) 
      ));
    }
    
    @Override
    protected double getAspectRatio() {
      switch(state) {
        case BAD_LEVEL_SELECTED:
          return badLevelStrategy.orthoWidth() / badLevelStrategy.orthoHeight();
        case MISSING_LEVEL_SELECTED:
          return missingLevelStrategy.orthoWidth() / missingLevelStrategy.orthoHeight();
        default:
          return 1;
      }
    }

    @Override
    protected double getH() {
      return .85;
    }

    @Override
    protected double getPadding() {
      return 10;
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
      return .15;
    }

    @Override
    void render(GLAutoDrawable drawable) {
      GL2 gl = (GL2) drawable.getGL();
      gl.glDisable(GL2.GL_DEPTH_TEST);
      
      switch(state) {       
        case NORMAL:
          new ThreeDDrawingStrategyTitleScreen(previewAngle).render(drawable, selectedLevel);
          break;
          
        case BAD_LEVEL_SELECTED: 
          badLevelStrategy.render(drawable);     
          break;
          
        case MISSING_LEVEL_SELECTED:
          missingLevelStrategy.render(drawable);       
          break;
      }
    }
  }

@Override
public void dispose(GLAutoDrawable arg0) {
	// TODO Auto-generated method stub
	
}
}
