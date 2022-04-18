package breakout;

// TODO: Check whether the encapsulation still holds!
public abstract class BlockState {
	protected Point TL;
	protected Point BR;
	
	public abstract Point getTopLeft();
	public abstract Point getBottomRight();
	public abstract Rect rectangleOf();	
}

/**
 * Each instance of this class represents a block in the breakout game.
 * 
 * @immutable
 * 
 * @invar This object's upper left point is situated up and left from its lower right point.
 * 	| getTopLeft().isUpAndLeftFrom(getBottomRight())
 */

final class NormalBlockState extends BlockState {
	
	// Constructor
	/**
	 * Returns an object representing a rectangular block defined by the given upper left and lower right points.
	 * @pre | TL != null
	 * @pre | BR != null
	 * @pre The upper left point mist be situated up and left from the lower right point
	 * 	| TL.isUpAndLeftFrom(BR)
	 * @post | getTopLeft()==TL
	 * @post | getBottomRight()==BR
	 */
	public NormalBlockState(Point TL, Point BR) {
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
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
}

final class SturdyBlockState extends BlockState {
	int lifetime;
	
	public SturdyBlockState(Point TL, Point BR, int lifetime) {
		this.TL=TL;
		this.BR=BR;
		this.lifetime=lifetime;
	}
	
	public Point getTopLeft() {
		return TL;
	}
	
	public Point getBottomRight() {
		return BR;
	}
	
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
}
	
final class PowerupBallBlockState extends BlockState {
	
	public PowerupBallBlockState(Point TL, Point BR) {
		this.TL=TL;
		this.BR=BR;
	}
	
	public Point getTopLeft() {
		return TL;
	}
	
	public Point getBottomRight() {
		return BR;
	}
	
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
}

final class ReplicatorBlockState extends BlockState {
	
	public ReplicatorBlockState(Point TL, Point BR) {
		this.TL=TL;
		this.BR=BR;
	}
	
	public Point getTopLeft() {
		return TL;
	}
	
	public Point getBottomRight() {
		return BR;
	}
	
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
}