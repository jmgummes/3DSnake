package front;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;

import back.Level;
import back.LevelSchema;

import javax.swing.Timer;
import javax.vecmath.Color3f;

/**
 * This class represents the titlescreen display
 * 
 * @author Jim
 */
public class TitleScreen extends Display implements KeyListener, ActionListener {

	// The window that this TitleScreen is inside
	Window window;

	// The available level schemas
	private List<LevelSchema> levelSchemas;
	
	// The level that is selected (if any)
	private Level selectedLevel;

	// Special code for the level index when no level is selected
	private final int NO_LEVEL_INDEX = -1;
	
	// The index of the selected level (if any)
	private int selectedLevelIndex;

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
	public TitleScreen(Window window, List<LevelSchema> levelSchemas) {
		super();

		this.levelSchemas = levelSchemas;
		if(!levelSchemas.isEmpty())
		  selectLevel(0);
		
		this.window = window;
		window.add(this);

		// Request focus
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		this.display();
	}

	/**
	 * Selects a level
	 * 
	 * @param levelNumber
	 */
	public void selectLevel(int levelNumber) {
	  selectedLevelIndex = levelNumber;
	  selectedLevel = new Level(this.levelSchemas.get(levelNumber));
	  previewAngle = 0;
	  if (!animation.isRunning())
	    animation.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	  if(levelSchemas.isEmpty()) 
	    return;
	  
	  switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		  selectLevel((selectedLevelIndex + levelSchemas.size() - 1) % levelSchemas.size());
		  display();
		  break;
		case KeyEvent.VK_DOWN:
		  selectLevel((selectedLevelIndex + 1) % levelSchemas.size());
		  display();
		  break;
		case KeyEvent.VK_ENTER:
		  animation.stop();
		  animation.removeActionListener(this);
		  this.window.remove(this);
		  GameDisplay gameDisplay = new GameDisplay(window, levelSchemas, selectedLevel);
		  this.window.add(gameDisplay);
		  this.window.setVisible(true);
		  gameDisplay.requestFocus();
		  break;
      }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		previewAngle += .5;
		display();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	/**
	 * This class represents the SubDisplay that shows the title
	 * 
	 * @author Jim
	 */
	public class TitleDisplay extends SubDisplay {

		// Delegate to this strategy to render
		private TextRenderingStrategy textRenderingStrategy = new TextRenderingStrategy(
				new TextRenderingStrategy.Line("3D Snake", 1, new Color3f(1, 1, 1)));

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
	 * This class represents the SubDislpay that shows the author
	 * 
	 * @author Jim
	 */
	public class AuthorDisplay extends SubDisplay {

		// Delegate to this strategy to render
		private TextRenderingStrategy textRenderingStrategy = new TextRenderingStrategy(
				new TextRenderingStrategy.Line("By Jimmy Fox", .85, new Color3f(1, 1, 1)));

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
	 * This class represents the SubDisplay that shows the list of levels on disk.
	 * 
	 * @author Jim
	 */
	public class LevelList extends SubDisplay {

		// Delegate to this strategy to render
		TextRenderingStrategy textRenderingStrategy;

		/**
		 * Constructor, does some work to setup the text rendering strategy properly.
		 */
		public LevelList() {
			List<TextRenderingStrategy.Line> lines = new LinkedList<TextRenderingStrategy.Line>();
			lines.add(new TextRenderingStrategy.Line("Levels", 1, new Color3f(1, 1, 1)));
			for (int i = 0; i < levelSchemas.size(); i++) {
				Color3f color;
				if (i == selectedLevelIndex)
					color = new Color3f(0, 0, 1);
				else
					color = new Color3f(1, 1, 1);
				lines.add(new TextRenderingStrategy.Line(levelSchemas.get(i).getName(), .5, color));
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
	 * This class represents the SubDisplay that shows the selected level (or errors
	 * if there are problems with it)
	 * 
	 * @author Jim
	 */
	public class SelectedLevelDisplay extends SubDisplay {

		@Override
		protected double getAspectRatio() {
		  return 1;
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
			GL gl = drawable.getGL();
			gl.glDisable(GL.GL_DEPTH_TEST);
			new ThreeDDrawingStrategyTitleScreen(previewAngle).render(drawable, selectedLevel);
		}
	}
}
