package breakout;

import java.awt.Color;

/**
 * Each instance of this class represents a block in the breakout game.
 * 
 * @immutable
 * 
 * @invar | getTopLeft() != null
 * @invar | getBottomRight() != null
 * @invar This object's upper left point is situated up and left from its lower right point.
 * 	| getTopLeft().isUpAndLeftFrom(getBottomRight())
 */
public abstract class BlockState {
	/**
	 * @invar | TL != null
	 * @invar | BR != null 
	 */
	protected Point TL;
	protected Point BR;
	
	protected static final int MAX_STURDY_LIFETIME = 3;
	
	public abstract Point getTopLeft();
	public abstract Point getBottomRight();
	public abstract Rect rectangleOf();
	public abstract Color getColor();
	public abstract ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle);
}

/**
 * Each instance of this class represents a normal block in the breakout game.
 */
final class NormalBlockState extends BlockState {
	private final Point TL;
	private final Point BR;
	
	// Constructor
	/**
	 * Returns an object representing a normal rectangular block defined by the given upper left and lower right points.
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
	 * Returns the topleft Point object contained within this NormalBlockState object
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottomright Point object contained within this NormalBlockState object
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the block represented by this NormalBlockState object
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getTopLeft())
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
	
	/**
	 * Returns the default colour used to display a normal block in the breakout game, i.e. blue.
	 */
	public Color getColor() {
		return Color.blue;
	}
	
	/**
	 * Returns ballBlockHitResults object containing the balls, the blocks and the paddle states resulting 
	 * from a possible ball-block hit, and a boolean indicating whether the block was destroyed by this hit
	 * and consequently should be removed from the breakout game.
	 * @creates | result
	 * @inspects | ball
	 * @pre | block != null
	 * @pre | ball != null
	 * @pre | paddle != null
	 * @post | result.ball instanceof Ball
	 * @post | result.block instanceof NormalBlockState
	 * @post | result.paddle instanceof PaddleState
	 */
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			// Normal blocks are always destroyed when hit
			destroyed = true;
			
			// Make ball bounce
			ball.hitBlock(blockRect, destroyed);
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
	}
}

/**
 * Each instance of this class represents a sturdy block in the breakout game
 * @invar | getLifetime() > 0 && getLifetime() <= 3
 */
final class SturdyBlockState extends BlockState {
	/**
	 * @invar | lifetime > 0 && lifetime <= 3
	 */
	private final Point TL;
	private final Point BR;
	private final int lifetime;
	
	/**
	 * Returns an object representing a sturdy rectangular block defined by the given upper left and lower right points.
	 * @pre | TL != null
	 * @pre | BR != null
	 * @pre The upper left point mist be situated up and left from the lower right point
	 * 	| TL.isUpAndLeftFrom(BR)
	 * @pre | lifetime > 0 && lifetime <= MAX_STURDY_LIFETIME
	 * @post | getTopLeft()==TL
	 * @post | getBottomRight()==BR
	 */
	public SturdyBlockState(Point TL, Point BR, int lifetime) {
		this.TL=TL;
		this.BR=BR;
		this.lifetime=lifetime;
	}
	
	/**
	 * Returns the topleft Point object contained within this SturdyBlockState object
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottomright Point object contained within this SturdyBlockState object
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Returns the remaining lifetime in number of hits of this SturdyBlockState object
	 */
	public int getLifetime() {
		return lifetime;
	}
	
	/**
	 * Returns a copy of this SturdyBlockState object with a lifetime decreased by 1 hit.
	 * @creates | result
	 * @post | result.getLifetime() == old(getLifetime())-1
	 */
	public SturdyBlockState decreaseLifetime() {
		return new SturdyBlockState(TL,BR,lifetime-1);
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the block represented by this SturdyBlockState object
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getTopLeft())
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
	
	/**
	 * Returns the default colour used to display a sturdy block in the breakout game, i.e. grey up to white,
	 * depending on the number of hits.
	 */
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
	
	/**
	 * Returns ballBlockHitResults object containing the balls, the blocks and the paddle states resulting 
	 * from a possible ball-block hit, and a boolean indicating whether the block was destroyed by this hit
	 * and consequently should be removed from the breakout game.
	 * @creates | result
	 * @inspects | ball
	 * @pre | block != null
	 * @pre | ball != null
	 * @pre | paddle != null
	 * @post | result.ball instanceof Ball
	 * @post | result.block instanceof SturdyBlockState
	 * @post | result.paddle instanceof PaddleState
	 */
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			destroyed = true;
			
			// Sturdy blocks are destroyed when hit only if its lifetime is 1.
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

/**
 * Each instance of this class represents a normal block in the breakout game.
 */
final class PowerupBallBlockState extends BlockState {
	private final Point TL;
	private final Point BR;
	
	/**
	 * Returns an object representing a power-up-ball rectangular block defined by the given upper left and lower right points.
	 * @pre | TL != null
	 * @pre | BR != null
	 * @pre The upper left point mist be situated up and left from the lower right point
	 * 	| TL.isUpAndLeftFrom(BR)
	 * @post | getTopLeft()==TL
	 * @post | getBottomRight()==BR
	 */
	public PowerupBallBlockState(Point TL, Point BR) {
		this.TL=TL;
		this.BR=BR;
	}
	
	/**
	 * Returns the topleft Point object contained within this PowerupBallBlockState object
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottomright Point object contained within this PowerupBallBlockState object
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the block represented by this PowerupBallBlockState object
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getTopLeft())
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
	
	/**
	 * Returns the default colour used to display a sturdy block in the breakout game, i.e. orange
	 */
	public Color getColor() {
		return Color.orange;
	}
	
	/**
	 * Returns ballBlockHitResults object containing the ball, the block and the paddle states resulting 
	 * from a possible ball-block hit, and a boolean indicating whether the block was destroyed by this hit
	 * and consequently should be removed from the breakout game.
	 * @creates | result
	 * @inspects | ball
	 * @pre | block != null
	 * @pre | ball != null
	 * @pre | paddle != null
	 * @post | result.ball instanceof Ball
	 * @post | result.block instanceof PowerupBallBlockState
	 * @post | result.paddle instanceof PaddleState
	 */
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			// Powerup blocks are always destroyed when hit
			destroyed = true;
						
			// Make ball bounce 
			ball.hitBlock(blockRect, destroyed);
			
			// Execute block effects
			ball = ball.powerup();
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
	} 
}

/**
 * Each instance of this class represents a normal block in the breakout game.
 */
final class ReplicatorBlockState extends BlockState {
	private final Point TL;
	private final Point BR;
	
	/**
	 * Returns an object representing a replicator rectangular block defined by the given upper left and lower right points.
	 * @pre | TL != null
	 * @pre | BR != null
	 * @pre The upper left point mist be situated up and left from the lower right point
	 * 	| TL.isUpAndLeftFrom(BR)
	 * @post | getTopLeft()==TL
	 * @post | getBottomRight()==BR
	 */
	public ReplicatorBlockState(Point TL, Point BR) {
		this.TL=TL;
		this.BR=BR;
	}
	
	/**
	 * Returns the topleft Point object contained within this ReplicatorBlockState object
	 */
	public Point getTopLeft() {
		return TL;
	}
	
	/**
	 * Returns the bottomright Point object contained within this ReplicatorBlockState object
	 */
	public Point getBottomRight() {
		return BR;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the block represented by this ReplicatorBlockState object
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getTopLeft())
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rect rectangleOf() {
		return new Rect(TL, BR);
	}
	
	/**
	 * Returns the default colour used to display a sturdy block in the breakout game, i.e. cyan
	 */
	public Color getColor() {
		return Color.cyan;
	}
	
	/**
	 * Returns ballBlockHitResults object containing the ball, the block and the paddle states resulting 
	 * from a possible ball-block hit, and a boolean indicating whether the block was destroyed by this hit
	 * and consequently should be removed from the breakout game.
	 * @creates | result
	 * @inspects | ball
	 * @pre | block != null
	 * @pre | ball != null
	 * @pre | paddle != null
	 * @post | result.ball instanceof Ball
	 * @post | result.block instanceof ReplicatorBlockState
	 * @post | result.paddle instanceof PaddleState
	 */
	public ballBlockHitResults hitBlock(BlockState block, Ball ball, PaddleState paddle) {
		Rect blockRect = block.rectangleOf();
		Vector normVecBlock = ball.rectangleOf().overlap(blockRect);
		boolean destroyed = false;
		if (normVecBlock != null && 
			normVecBlock.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			
			// Replicator blocks are always destroyed when hit.
			destroyed = true;
			
			// Make ball bounce
			ball.hitBlock(blockRect, destroyed);
			
			// Execute block effects
			paddle = paddle.powerup();
		}
		return new ballBlockHitResults(block, ball, paddle, destroyed);
	}
}

// Some classes simulating structs
/**
 * Each instance of this class simply collects the ball and the paddle states resulting from a possible ball-paddle hit,
 * and an integer indicating how much replicates are required in case of a hit with a replicator paddle.
 */
class ballPaddleHitResults {
	/**
	 * @invar | reps >= 0 && reps <= 3
	 */
	Ball ball;
	PaddleState paddle;
	int reps;
	
	/**
	 * Returns a struct-like ballPaddleHitResults object containing the ball and paddle states and required number of replicates
	 * after a possible ball-paddle hit.
	 * @pre | ball != null
	 * @pre | paddle != null
	 */
	ballPaddleHitResults(Ball ball, PaddleState paddle, int reps) {
		this.ball=ball;
		this.paddle=paddle;
		this.reps=reps;
	}
}

/**
 * Each instance of this class simply collects the ball, the paddle and the block states resulting from a possible ball-block hit,
 * and an integer indicating whether the block was destroyed and consequently must be removed.
 */
class ballBlockHitResults {
	BlockState block;
	Ball ball;
	PaddleState paddle;
	boolean destroyed;
	
	/**
	 * Returns a struct-like ballBlockHitResults object containing the ball, paddle and block states, and the boolean indicating
	 * the block was destroyed.
	 * @pre | ball != null
	 * @pre | paddle != null
	 * @pre | block != null
	 */
	ballBlockHitResults(BlockState block, Ball ball, PaddleState paddle, boolean destroyed) {
		this.ball=ball;
		this.block=block;
		this.paddle=paddle;
		this.destroyed=destroyed;
	}
}