package breakout;

/**
 * Each instance of this class represents a block in the breakout game.
 * 
 * @immutable
 * 
 * @invar This object's upper left point is situated up and left from its lower right point.
 * 	| getTopLeft().isUpAndLeftFrom(getBottomRight())
 */

public final class BlockState {
	
	// Fields
	/**
	 * @invar | TL.isUpAndLeftFrom(BR)
	 */
	private final Point TL;
	private final Point BR;;
	
	// Constructor
	/**
	 * Returns an object representing a rectangular block defined by the given upper left and lower right points.
	 * 
	 * @pre The upper left point mist be situated up and left from the lower right point
	 * 	| TL.isUpAndLeftFrom(BR)
	 * @pre | TL != null
	 * @pre | BR != null
	 * @post | getTopLeft()==TL
	 * @post | getBottomRight()==BR
	 */
	public BlockState(Point TL, Point BR) {
		this.TL=TL;
		this.BR=BR;
	}
	
	// Getters
	/**
	 * Returns the topleft Point object contained within this BlockState object
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottomright Point object contained within this BlockState object
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the block represented by this BlockState object
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getTopLeft())
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rectangle rectangleOf() {
		return new Rectangle(TL, BR);
	}
}
