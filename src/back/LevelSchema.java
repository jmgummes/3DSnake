package back;

import java.util.LinkedList;
import java.util.List;

public class LevelSchema {
	private String name;
	private double width;
	private double height;
	private double snakeStartingX;
	private double snakeStartingY;
	private double snakeStartingAngle;
	private double snakeStartingSpeed;
	private int snakeStartingLength;
	private int startingFoodNumber;
	private List<Obstacle> obstacles;

	public LevelSchema(String name, double width, double height, double snakeStartingX,
			double snakeStartingY, double snakeStartingAngle, double snakeStartingSpeed,
			int snakeStartingLength, int startingFoodNumber) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.snakeStartingX = snakeStartingX;
		this.snakeStartingY = snakeStartingY;
		this.snakeStartingAngle = snakeStartingAngle;
		this.snakeStartingSpeed = snakeStartingSpeed;
		this.snakeStartingLength = snakeStartingLength;
		this.startingFoodNumber = startingFoodNumber;
		this.obstacles = new LinkedList<Obstacle>();
		
	}
	
	/**
	 * Getter for name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for width
	 * 
	 * @return width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Getter for height
	 * 
	 * @return height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Getter for snakeStartingX
	 * 
	 * @return snakeStartingX
	 */
	public double getSnakeStartingX() {
		return snakeStartingX;
	}

	/**
	 * Getter for snakeStartingY
	 * 
	 * @return snakeStartingY
	 */
	public double getSnakeStartingY() {
		return snakeStartingY;
	}

	/**
	 * Getter for snakeStargingAngle
	 * 
	 * @return snakeStartingAngle
	 */
	public double getSnakeStartingAngle() {
		return snakeStartingAngle;
	}

	/**
	 * Getter for snakeStartingSpeed
	 * 
	 * @return snakeStartingSpeed
	 */
	public double getSnakeStartingSpeed() {
		return snakeStartingSpeed;
	}

	/**
	 * Getter for snakeStartingLength
	 * 
	 * @return snakeStartingLength
	 */
	public int getSnakeStartingLength() {
		return snakeStartingLength;
	}

	/**
	 * Getter for startingFoodNumber
	 * 
	 * @return startingFoodNumber
	 */
	public int getStartingFoodNumber() {
		return startingFoodNumber;
	}

	/**
	 * Getter for obstacles
	 * 
	 * @return obstacles
	 */
	public List<Obstacle> getObstacles() {
		return obstacles;
	}

	/**
	 * @return the innerRadius of the torus that this level schema maps onto
	 */
	public double innerRadius() {
		return getHeight() / (Math.PI * 2);
	}

	/**
	 * @return the outerRadius of the torus that this level schema maps onto
	 */
	public double outerRadius() {
		return getWidth() / (Math.PI * 2);
	}

	public class Obstacle {
		private double x;
		private double y;
		private double width;
		private double height;

		public Obstacle(double x, double y, double width, double height) {
			LevelSchema.this.obstacles.add(this);
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		/**
		 * Getter for x
		 * @return x
		 */
		public double getX() {
			return x;
		}

		/**
		 * Getter for y
		 * @return y
		 */
		public double getY() {
			return y;
		}

		/**
		 * Getter for width
		 * @return width
		 */
		public double getWidth() {
			return width;
		}

		/**
		 * Getter for height
		 * @return height
		 */
		public double getHeight() {
			return height;
		}
		
		public final Coordinates getCoordinates() {
			return new Coordinates(LevelSchema.this, getX(), getY());
		}

		/**
		 * Finds whether this obstacle overlaps with a given circle
		 * 
		 * @param c
		 * @return whether this obstacle overlaps with c
		 */
		public boolean overlapsWithCircle(Circle c) {
			for (Obstacle o : split())
				if (overlap(o.getCoordinates().getX(), o.getCoordinates().getX() + o.getWidth(),
					c.getCoordinates().getX() - c.getRadius(), c.getCoordinates().getX() + c.getRadius())
						&& overlap(o.getCoordinates().getY(), o.getCoordinates().getY() + o.getHeight(),
								c.getCoordinates().getY() - c.getRadius(), c.getCoordinates().getY() + c.getRadius()))
					return true;
			return false;
		}

		/**
		 * Returns whether two ranges of numbers overlap.
		 * 
		 * @param start1
		 * @param end1
		 * @param start2
		 * @param end2
		 * @return whether there is overlap between the two ranges of numbers
		 */
		private boolean overlap(double start1, double end1, double start2, double end2) {
			return (start1 < end2 && end1 > start2);
		}

		/**
		 * @return x coordinate of the left side of this Obstacle
		 */
		public double getLeftSideX() {
			return getX();
		}

		/**
		 * @return x coordinate of the right side of this Obstacle
		 */
		public double getRightSideX() {
			double toReturn = getLeftSideX() + getWidth();
			while (toReturn > LevelSchema.this.getWidth())
				toReturn -= LevelSchema.this.getWidth();
			return toReturn;
		}

		/**
		 * @return y coordinate of the top side of this Obstacle
		 */
		public double getTopSideY() {
			return getCoordinates().getY();
		}

		/**
		 * @return y coordinate of the bottom side of this Obstacle
		 */
		public double getBottomSideY() {
			double toReturn = getTopSideY() + getHeight();
			while (toReturn > LevelSchema.this.getHeight())
				toReturn -= LevelSchema.this.getHeight();
			return toReturn;
		}

		/**
		 * Get the height of this obstacle in the 3D world. The height varies depending
		 * on where this Obstacle is due to distortions involved in mapping the flat
		 * world onto a torus. Basically, it looks smaller when it is on the inside of
		 * the torus, but bigger when it is on the outside.
		 * 
		 * @return the height
		 */
		public double threeDHeight() {
			return Math.tan(getCoordinates().xToAngle(Snake.SEGMENT_RADIUS) / 2.0) * 2
					* (LevelSchema.this.outerRadius()
							+ Math.cos(getCoordinates().yToAngle(getTopSideY() + getHeight() / 2.0))
									* LevelSchema.this.innerRadius());
		}

		/**
		 * Splits up the obstacle so that the pieces form the same obstacle, but none of
		 * the pieces hang over the edges of the level.
		 * 
		 * @return list of obstacles that add up to this one, but don't hang over any
		 *         level boundaries
		 */
		public List<Obstacle> split() {
			List<Obstacle> obstacles = new LinkedList<Obstacle>();

			if (getRightSideX() <= getLeftSideX()) {
				Obstacle left = LevelSchema.this.new Obstacle(getLeftSideX(), getTopSideY(),
						LevelSchema.this.getWidth() - getLeftSideX(), getHeight());

				Obstacle right = LevelSchema.this.new Obstacle(0, getTopSideY(), getRightSideX(), getHeight());

				obstacles.addAll(left.split());
				obstacles.addAll(right.split());
			} else if (getBottomSideY() <= getTopSideY()) {
				Obstacle bottom = LevelSchema.this.new Obstacle(getLeftSideX(), getTopSideY(), getWidth(),
						LevelSchema.this.getHeight() - getTopSideY());

				Obstacle top = new Obstacle(getLeftSideX(), 0, getWidth(), getBottomSideY());

				obstacles.addAll(top.split());
				obstacles.addAll(bottom.split());
			} else {
				obstacles.add(this);
			}

			return obstacles;
		}
	}
}
