package breakout;

import java.awt.Color;

// TODO: Check whether the encapsulation still holds!
public abstract class BlockState {
	protected Point TL;
	protected Point BR;
	
	public abstract Point getTopLeft();
	public abstract Point getBottomRight();
	public abstract Rect rectangleOf();
	public abstract Color getColor();
	public abstract ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle);
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
	
	public Color getColor() {
		return Color.blue;
	}
	
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			destroyed = true;
			
			// Make ball bounce
			ball.hitBlock(blockRect, destroyed);
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
	}
}

final class SturdyBlockState extends BlockState {
	private int lifetime;
	
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
	
	public int getLifetime() {
		return lifetime;
	}
	
	public SturdyBlockState decreaseLifetime() {
		return new SturdyBlockState(TL,BR,lifetime-1);
	}
	
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
	
	public Color getColor() {
		switch (lifetime) {
		case 3 -> {
			return Color.gray;}
		case 2 -> {
			return Color.lightGray;}
		case 1 -> {
			return Color.white;}
		}
		return Color.blue;
	}
	
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			destroyed = true;
			
			// Block is always destroyed unless block is a sturdy one with a lifetime bigger than 1.
			if (((SturdyBlockState) block).getLifetime() > 1) {
				destroyed=false;
				block = ((SturdyBlockState) block).decreaseLifetime();
			}
			
			// Make ball bounce when required 
			ball.hitBlock(blockRect, destroyed);
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
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
	
	public Color getColor() {
		return Color.orange;
	}
	
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			destroyed = true;
						
			// Make ball bounce 
			ball.hitBlock(blockRect, destroyed);
			
			// Execute block effects
			ball = ball.powerup();
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
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
	
	public Color getColor() {
		return Color.cyan;
	}
	
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			// Make ball bounce
			destroyed = true;
			ball.hitBlock(blockRect, destroyed);
			
			// Execute block effects
			paddle = paddle.powerup();
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
	}
}