import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
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

	// The level that is selected (if any)
	private Level selectedLevel;

	// The index of the selected level (if any)
	private int selectedLevelIndex;

	// Animation for the rotating preview
	Timer animation = new Timer(10, this);

	// Angle for the rotating preview
	double previewAngle = 0;

	private Level.Description[] levelDescriptions = {
      new Level.Description() {
		@Override
		protected String getName() {
		  return "Boxed In";
		}
    	
		@Override
		protected double getWidth() {
		  return 400;
		}
		
		@Override
		protected double getHeight() {
		  return 200;
		}
		
		@Override
		protected double getSnakeStartingX() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingY() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingAngle() {
          return 0;
		}
	
		@Override
		protected double getSnakeStartingSpeed() {
		  return 2;
		}
		
		@Override
		protected int getSnakeStartingLength() {
		  return 3;
		}
		
		@Override
		protected int getStartingFoodNumber() {
		  return 20;
		}
		
		@Override
		protected List<Obstacle.Description> getObstacleDescriptions() {
		  return List.of(
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 0;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 0;
			  }
			
	  		  @Override
			  double getWidth() {
				return 400;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 0;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 0;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 200;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 0;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 190;
			  }
			
	  		  @Override
			  double getWidth() {
				return 400;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 390;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 0;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 200;
			  }
		    }
		  );
		}
      },
      new Level.Description() {
		@Override
		protected String getName() {
		  return "For Beginners";
		}
    	
		@Override
		protected double getWidth() {
		  return 400;
		}
		
		@Override
		protected double getHeight() {
		  return 200;
		}
		
		@Override
		protected double getSnakeStartingX() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingY() {
		  return 75;
		}
		
		@Override
		protected double getSnakeStartingAngle() {
          return 0;
		}
	
		@Override
		protected double getSnakeStartingSpeed() {
		  return 2;
		}
		
		@Override
		protected int getSnakeStartingLength() {
		  return 3;
		}
		
		@Override
		protected int getStartingFoodNumber() {
		  return 20;
		}
      },
      new Level.Description() {
		@Override
		protected String getName() {
		  return "Horizontal Obstacles";
		}
    	
		@Override
		protected double getWidth() {
		  return 400;
		}
		
		@Override
		protected double getHeight() {
		  return 200;
		}
		
		@Override
		protected double getSnakeStartingX() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingY() {
		  return 75;
		}
		
		@Override
		protected double getSnakeStartingAngle() {
          return 0;
		}
	
		@Override
		protected double getSnakeStartingSpeed() {
		  return 2;
		}
		
		@Override
		protected int getSnakeStartingLength() {
		  return 3;
		}
		
		@Override
		protected int getStartingFoodNumber() {
		  return 20;
		}
		
		@Override
		protected List<Obstacle.Description> getObstacleDescriptions() {
		  return List.of(
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 100;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 100;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 300;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 100;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 100;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 150;
			  }
			
	  		  @Override
			  double getWidth() {
				return 100;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 300;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 150;
			  }
			
	  		  @Override
			  double getWidth() {
				return 100;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    }
		  );
		}
      },
      new Level.Description() {
		@Override
		protected String getName() {
		  return "Jim's First Level";
		}
    	
		@Override
		protected double getWidth() {
		  return 400;
		}
		
		@Override
		protected double getHeight() {
		  return 200;
		}
		
		@Override
		protected double getSnakeStartingX() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingY() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingAngle() {
          return 0;
		}
	
		@Override
		protected double getSnakeStartingSpeed() {
		  return 2;
		}
		
		@Override
		protected int getSnakeStartingLength() {
		  return 3;
		}
		
		@Override
		protected int getStartingFoodNumber() {
		  return 20;
		}
		
		@Override
		protected List<Obstacle.Description> getObstacleDescriptions() {
		  return List.of(
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 30;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 150;
			  }
			
	  		  @Override
			  double getWidth() {
				return 80;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    }
		  );
		}
      },
      new Level.Description() {
		@Override
		protected String getName() {
		  return "Little Squares";
		}
    	
		@Override
		protected double getWidth() {
		  return 400;
		}
		
		@Override
		protected double getHeight() {
		  return 200;
		}
		
		@Override
		protected double getSnakeStartingX() {
		  return 100;
		}
		
		@Override
		protected double getSnakeStartingY() {
		  return 75;
		}
		
		@Override
		protected double getSnakeStartingAngle() {
          return 0;
		}
	
		@Override
		protected double getSnakeStartingSpeed() {
		  return 2;
		}
		
		@Override
		protected int getSnakeStartingLength() {
		  return 3;
		}
		
		@Override
		protected int getStartingFoodNumber() {
		  return 20;
		}
		
		@Override
		protected List<Obstacle.Description> getObstacleDescriptions() {
		  return List.of(
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 0;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 100;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 200;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 300;
			  }	
		    	
		  	  @Override
			  double getY() {
			    return 50;
			  }
			
	  		  @Override
			  double getWidth() {
				return 10;
			  }
			
			  @Override
			  double getHeight() {
				return 10;
			  }
		    },
		    new Obstacle.Description() {
			  @Override
			  double getX() {	
			    return 0;
			  }	
			    	
			  @Override
			  double getY() {
			    return 150;
			  }
	
			  @Override
			  double getWidth() {
				return 10;
			  }
	
			  @Override
			  double getHeight() {
				return 10;
			  }
			},
		    new Obstacle.Description() {
			  @Override
			  double getX() {
				return 100;
			  }
	
			  @Override
			  double getY() {
				return 150;
			  }
	
			  @Override
			  double getWidth() {
				return 10;
			  }
	
			  @Override
			  double getHeight() {
				return 10;
			  }
			},
		    new Obstacle.Description() {
			  @Override
			  double getX() {
			    return 200;
			  }
	
			  @Override
			  double getY() {
				return 150;
			  }
	
			  @Override
			  double getWidth() {
				return 10;
			  }
	
			  @Override
			  double getHeight() {
				return 10;
			  }
			},
		    new Obstacle.Description() {
			  @Override
			  double getX() {
				return 300;
			  }
	
			  @Override
			  double getY() {
				return 150;
			  }
	
			  @Override
			  double getWidth() {
				return 10;
			  }
	
			  @Override
			  double getHeight() {
				return 10;
			  }
			}
		  );
		}
      },
      new Level.Description() {
  		@Override
  		protected String getName() {
  		  return "Test";
  		}
      	
  		@Override
  		protected double getWidth() {
  		  return 400;
  		}
  		
  		@Override
  		protected double getHeight() {
  		  return 200;
  		}
  		
  		@Override
  		protected double getSnakeStartingX() {
  		  return 100;
  		}
  		
  		@Override
  		protected double getSnakeStartingY() {
  		  return 100;
  		}
  		
  		@Override
  		protected double getSnakeStartingAngle() {
            return 0;
  		}
  	
  		@Override
  		protected double getSnakeStartingSpeed() {
  		  return 2;
  		}
  		
  		@Override
  		protected int getSnakeStartingLength() {
  		  return 3;
  		}
  		
  		@Override
  		protected int getStartingFoodNumber() {
  		  return 20;
  		}
  		
  		@Override
  		protected List<Obstacle.Description> getObstacleDescriptions() {
  		  return List.of(
  		    new Obstacle.Description() {
  			  @Override
  			  double getX() {	
  			    return 395;
  			  }	
  		    	
  		  	  @Override
  			  double getY() {
  			    return 150;
  			  }
  			
  	  		  @Override
  			  double getWidth() {
  				return 10;
  			  }
  			
  			  @Override
  			  double getHeight() {
  				return 100;
  			  }
  		    }
  		  );
  		}
      },
      new Level.Description() {
  		@Override
  		protected String getName() {
  		  return "Tunnels";
  		}
      	
  		@Override
  		protected double getWidth() {
  		  return 400;
  		}
  		
  		@Override
  		protected double getHeight() {
  		  return 200;
  		}
  		
  		@Override
  		protected double getSnakeStartingX() {
  		  return 100;
  		}
  		
  		@Override
  		protected double getSnakeStartingY() {
  		  return 100;
  		}
  		
  		@Override
  		protected double getSnakeStartingAngle() {
            return 0;
  		}
  	
  		@Override
  		protected double getSnakeStartingSpeed() {
  		  return 2;
  		}
  		
  		@Override
  		protected int getSnakeStartingLength() {
  		  return 3;
  		}
  		
  		@Override
  		protected int getStartingFoodNumber() {
  		  return 20;
  		}
  		
  		@Override
  		protected List<Obstacle.Description> getObstacleDescriptions() {
  		  return List.of(
  		    new Obstacle.Description() {
  			  @Override
  			  double getX() {	
  			    return 200;
  			  }	
  		    	
  		  	  @Override
  			  double getY() {
  			    return 25;
  			  }
  			
  	  		  @Override
  			  double getWidth() {
  				return 10;
  			  }
  			
  			  @Override
  			  double getHeight() {
  				return 150;
  			  }
  		    },
  		    new Obstacle.Description() {
  			  @Override
  			  double getX() {	
  			    return 0;
  			  }	
  		    	
  		  	  @Override
  			  double getY() {
  			    return 25;
  			  }
  			
  	  		  @Override
  			  double getWidth() {
  				return 10;
  			  }
  			
  			  @Override
  			  double getHeight() {
  				return 150;
  			  }
  		    }
  		  );
  		}
      }
	};	  

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
	public TitleScreen(Window window) {
		super();

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
	  selectedLevel = new Level(this.levelDescriptions[selectedLevelIndex]);
	  previewAngle = 0;
	  if (!animation.isRunning())
	    animation.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	  switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		  selectLevel((selectedLevelIndex + levelDescriptions.length - 1) % levelDescriptions.length);
		  display();
		  break;
		case KeyEvent.VK_DOWN:
		  selectLevel((selectedLevelIndex + 1) % levelDescriptions.length);
		  display();
		  break;
		case KeyEvent.VK_ENTER:
		  animation.stop();
		  animation.removeActionListener(this);
		  this.window.remove(this);
		  GameDisplay gameDisplay = new GameDisplay(window, selectedLevel);
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
			for (int i = 0; i < levelDescriptions.length; i++) {
				Color3f color;
				if (i == selectedLevelIndex)
					color = new Color3f(0, 0, 1);
				else
					color = new Color3f(1, 1, 1);
				lines.add(new TextRenderingStrategy.Line(levelDescriptions[i].getName(), .5, color));
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
			missingLevelStrategy = new TextRenderingStrategy(
					new TextRenderingStrategy.Line("File not found!", 1, new Color3f(1, 0, 0)));
		}

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
