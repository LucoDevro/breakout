package breakout;

/**
 * Each instance of this class represents a paddle of the breakout game.
 * 
 * @immutable
 * 
 * @invar The size of a paddle is given by a vector with both x and y being positive
 * 	| getSize().getX() >= 0 && getSize().getY() >= 0
 */

public final class PaddleState {
	
	// Fields
	/**
	 * @invar | size.getX() >= 0 && size.getY() >= 0
	 */
	private final Point center;
	private final Vector size;
	
	// Constructor
	/**
	 * Returns an object representing a rectangular paddle defined by a center point and a size vector
	 * that is positive for both coordinates.
	 * 
	 * @pre The given size vector must be positive for both coordinates.
	 * 	| size.getX() >= 0 && size.getY() >= 0
	 * @post | getCenter().equals(center)
	 * @post | getSize().equals(size)
	 */
	public PaddleState(Point center, Vector size) {
		this.center=center;
		this.size=size;
	}
	
	// Getters
	/**
	 * Returns the center Point object contained within this PaddleState object.
	 * @inspects | this
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the size Vector object contained within this PaddleState object.
	 * @inspects | this
	 */
	public Vector getSize() {
		return size;
	}
	
	// Setters
	/**
	 * Returns a new object representing a paddle in the breakout game with the same size 
	 * as the old one, but situated at the given center point.
	 * @creates | result
	 * @post | result != null
	 * @post | result.getCenter().equals(center)
	 * @post | result.getSize().equals(old(getSize()))
	 */
	public PaddleState setCenter(Point center) {
		return new PaddleState(center, this.size);
	}
	
	public Rectangle rectangleOf() {
		return new Rectangle(center.minus(size), center.plus(size));
	}
}
