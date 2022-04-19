package breakout;

/**
 * Each instance of this class represents a rectangle.
 * 
 * @immutable
 * 
 * @invar The top left point should be situated up and left from the bottom right point.
 * 	| getTopLeft().isUpAndLeftFrom(getBottomRight())
 */
public class Rect {
	
	// Fields
	/**
	 * @invar | TL.isUpAndLeftFrom(BR)
	 */
	private final Point TL;
	private final Point BR;
	
	// Constructor
	/**
	 * Returns a Rectangle object representing a rectangle as defined by its top left and bottom right point.
	 * @creates | result
	 * @pre | TL != null
	 * @pre | BR != null
	 * @post | getBottomRight().equals(BR)
	 * @post | getTopLeft().equals(TL)
	 */
	public Rect(Point TL, Point BR) {
		this.BR=BR;
		this.TL=TL;
	}
	
	// Getters
	/**
	 * Returns the top left Point object that defines this Rectangle object.
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottom right Point object that defines this Rectangle object.
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Detects whether two Rectangle objects are colliding and returns the normal vector of the plane of collision using a Vector object.
	 * In case no collision is detected, null is returned.
	 * @inspects | this, other
	 * @creates | result
	 * @pre | other != null
	 * @post | result == null || result.getSquareLength() == 1
	 */
	public Vector overlap(Rect other) {
		int thisLeftX = this.TL.getX();
		int thisRightX = this.BR.getX();
		int thisCenterX = this.TL.getX() + (this.BR.getX() - this.TL.getX())/2;
		int thisTopY = this.TL.getY();
		int thisBottomY = this.BR.getY();
		int thisCenterY = this.TL.getY() + (this.BR.getY() - this.TL.getY())/2;
		int otherLeftX = other.getTopLeft().getX();
		int otherRightX = other.getBottomRight().getX();
		int otherTopY = other.getTopLeft().getY();
		int otherBottomY = other.getBottomRight().getY();
		
		// Collision at the right side of this
		if (thisRightX >= otherLeftX && thisLeftX < otherLeftX && thisCenterY >= otherTopY && thisCenterY <= otherBottomY) {
			return Vector.RIGHT;
		}
		// Collision at the left side of this
		if (thisLeftX <= otherRightX && thisRightX > otherRightX && thisCenterY >= otherTopY && thisCenterY <= otherBottomY) {
			return Vector.LEFT;
		}
		// Collision at the top of this
		if (thisBottomY >= otherTopY && thisTopY < otherTopY && thisCenterX >= otherLeftX && thisCenterX <= otherRightX) {
			return Vector.DOWN;
		}
		// Collision at the bottom of this
		if (thisTopY <= otherBottomY && thisBottomY > otherBottomY && thisCenterX >= otherLeftX && thisCenterX <= otherRightX) {
			return Vector.UP;
		}
		// No collision
		return null;
		}
}
