package breakout;

import java.awt.Color;

/**
 * Each instance of this class represents a paddle of the breakout game.
 */
public abstract class PaddleState {

	protected Point center;
	protected Vector size;
	
	protected static final int MAX_REPLICATOR_LIFETIME = 3;
	
	public abstract Point getCenter();
	public abstract Vector getSize();
	public abstract Rect rectangleOf();
	public abstract PaddleState changeCenter(Point center);
	public abstract Color getColor();
	public abstract ballPaddleHitResults hitBall(Ball ball, int paddleDir);
	public abstract ReplicatorPaddleState powerup();
}

/**
 * Each instance of this class represents a normal paddle in the breakout game.
 * 
 * @immutable
 * 
 * @invar The size of a paddle is given by a vector with both x and y being positive
 * 	| getSize().getX() >= 0 && getSize().getY() >= 0
 * @invar | getCenter() != null
 * @invar | getSize() != null
 */
final class NormalPaddleState extends PaddleState {
	/**
	 * @invar | center != null
	 * @invar | size != null
	 */
	private final Point center;
	private final Vector size;
	
	// Constructor
	/**
	 * Returns an object representing a normal rectangular paddle defined by a center point and a size vector
	 * that is positive for both coordinates.
	 * 
	 * @pre | center != null
	 * @pre | size != null
	 * @pre The given size vector must be positive for both coordinates.
	 * 	| size.getX() >= 0 && size.getY() >= 0
	 * @post | getCenter().equals(center)
	 * @post | getSize().equals(size)
	 */
	public NormalPaddleState(Point center, Vector size) {
		this.center=center;
		this.size=size;
	}
	
	// Getters
	/**
	 * Returns the center Point object contained within this NormalPaddleState object.
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the size Vector object contained within this NormalPaddleState object.
	 */
	public Vector getSize() {
		return size;
	}
	
	// Setters
	/**
	 * Sets the center Point object contained within this NormalPaddleState object to the one supplied.
	 * @creates | result
	 * @pre | center != null
	 * @post | result.getCenter().equals(center)
	 */
	public NormalPaddleState changeCenter(Point center) {
		return new NormalPaddleState(center, size);
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the paddle represented by this NormalPaddleState object.
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getCenter().minus(getSize()))
	 * @post | result.getBottomRight().equals(getCenter().plus(getSize()))
	 */
	public Rect rectangleOf() {
		return new Rect(center.minus(size), center.plus(size));
	}
	
	/**
	 * Returns the default colour used to display normal paddles in the breakout game, i.e. green.
	 */
	public Color getColor() {
		return Color.green;
	}
	
	/**
	 * Returns a copy of this NormalPaddleState object converted to a ReplicatorPaddleState object with the default lifetime.
	 * @creates | result
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getSize().equals(old(getSize()))
	 * @post | result.getLifetime() == MAX_REPLICATOR_LIFETIME
	 */
	public ReplicatorPaddleState convertToReplicator() {
		return new ReplicatorPaddleState(center, size, MAX_REPLICATOR_LIFETIME);
	}
	
	/**
	 * Returns a ballPaddleHitResults object containing the ball and paddle states and the required number of replicates to be made,
	 * resulting from a possible ball-paddle hit. A normal paddle cannot request replicates.
	 * @creates | result
	 * @inspects | ball
	 * @pre | ball != null
	 * @pre | paddleDir == 0 || paddleDir == 1 || paddleDir == -1
	 * @post | result.reps == 0
	 * @post | result.reps >= 0 && result.reps <= 3
	 * @post | result.ball instanceof Ball
	 * @post | result.paddle instanceof NormalPaddleState
	 */
	public ballPaddleHitResults hitBall(Ball ball, int paddleDir) {
		Vector normVecPaddle = ball.rectangleOf().overlap(this.rectangleOf());
		if (normVecPaddle != null &&
			normVecPaddle.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			ball.bounce(normVecPaddle);
			ball.changeVelocity(ball.getVelocity().plus(Vector.RIGHT.scaled(2*paddleDir)));
		}
		return new ballPaddleHitResults(ball, this, 0);
	}
	
	/**
	 * Returns a copy of this NormalPaddleState object representing a normal paddle that has been powered up
	 * to a replicator paddle state with default lifetime.
	 * @creates | result
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getSize().equals(old(getSize()))
	 * @post | result.getLifetime() == MAX_REPLICATOR_LIFETIME
	 */
	public ReplicatorPaddleState powerup() {
		return this.convertToReplicator();
	}
}

/**
 * Each instance of this class represents a replicator paddle of the breakout game.
 * 
 * @immutable
 * 
 * @invar The size of a paddle is given by a vector with both x and y being positive
 * 	| getSize().getX() >= 0 && getSize().getY() >= 0
 * @invar | getCenter() != null
 * @invar | getSize() != null
 * @invar | getLifetime() >= 1 && getLifetime() <= 3
 */
final class ReplicatorPaddleState extends PaddleState {
	/**
	 * @invar | center != null
	 * @invar | size != null
	 * @invar | lifetime >= 1 && lifetime <= 3
	 */
	private final Point center;
	private final Vector size;
	private final int lifetime;
	
	/**
	 * /**
	 * Returns an object representing a rectangular replicator paddle defined by a center point and a size vector
	 * that is positive for both coordinates.
	 * 
	 * @pre | center != null
	 * @pre | size != null
	 * @pre The given size vector must be positive for both coordinates.
	 * 	| size.getX() >= 0 && size.getY() >= 0
	 * @post | getCenter().equals(center)
	 * @post | getSize().equals(size)
	 * @post | getLifetime() == i
	 */
	public ReplicatorPaddleState(Point center, Vector size, int i) {
		this.center=center;
		this.size=size;
		this.lifetime=i;
	}
	
	/**
	 * Returns the center Point object contained within this ReplicatorPaddleState object.
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the size Vector object contained within this ReplicatorPaddleState object.
	 */
	public Vector getSize() {
		return size;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the paddle represented by this ReplicatorPaddleState object.
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getCenter().minus(getSize()))
	 * @post | result.getBottomRight().equals(getCenter().plus(getSize()))
	 */
	public Rect rectangleOf() {
		return new Rect(center.minus(size), center.plus(size));
	}
	
	/**
	 * Sets the center Point object contained within this ReplicatorPaddleState object to the one supplied.
	 * @creates | result
	 * @pre | center != null
	 * @post | result.getCenter().equals(center)
	 */
	public ReplicatorPaddleState changeCenter(Point center) {
		return new ReplicatorPaddleState(center, size, lifetime);
	}
	
	/**
	 * Returns the remaining lifetime in number of hits of this ReplicatorPaddleState object.
	 */
	public int getLifetime() {
		return lifetime;
	}
	
	/**
	 * Returns a copy of this ReplicatorPaddleState with the lifetime decreased by one, or converts it to a 
	 * NormalPaddleState in case the lifetime decreased to zero.
	 * @creates | result
	 * @post | result instanceof NormalPaddleState || ((ReplicatorPaddleState) result).getLifetime() == old(getLifetime())-1
	 */
	public PaddleState decreaseLifetime() {
		int newage=lifetime-1;
		if (newage <= 0) {
			return this.convertToNormal();
		}
		return new ReplicatorPaddleState(center,size,newage);
	}
	
	/**
	 * Creates a copy this ReplicatorPaddleState in which the lifetime was reset.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getLifetime() == MAX_REPLICATOR_LIFETIME
	 */
	public ReplicatorPaddleState resetLifetime() {
		return new ReplicatorPaddleState(center,size,MAX_REPLICATOR_LIFETIME);
	}
	
	/**
	 * Returns the default colour used to display replicator paddles in the breakout game, i.e. yellow.
	 */
	public Color getColor() {
		return Color.yellow;
	}
	
	/**
	 * Returns a copy of this ReplicatorPaddleState object that was converted to a NormalPaddleState object
	 * @creates | result
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getSize().equals(old(getSize()))
	 */
	public NormalPaddleState convertToNormal() {
		return new NormalPaddleState(center, size);
	}
	
	/**
	 * Returns a ballPaddleHitResults object containing the ball and paddle states and the required number of replicates to be made,
	 * resulting from a possible ball-paddle hit. A replicator paddle requests 1, 2 or 3 replicates, or 0 in case there was no hit.
	 * @creates | result
	 * @pre | ball != null
	 * @pre | paddleDir == 0 || paddleDir == 1 || paddleDir == -1
	 * @post | result.reps >= 0 && result.reps <= 3
	 * @post | result.ball instanceof Ball
	 * @post | result.paddle instanceof PaddleState
	 */
	public ballPaddleHitResults hitBall(Ball ball, int paddleDir) {
		int reps = 0;
		PaddleState newState = this;
		Vector normVecPaddle = ball.rectangleOf().overlap(this.rectangleOf());
		if (normVecPaddle != null &&
			normVecPaddle.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			ball.bounce(normVecPaddle);
			ball.changeVelocity(ball.getVelocity().plus(Vector.RIGHT.scaled(2*paddleDir)));
			newState = this.decreaseLifetime();
			reps=this.getLifetime();
			}
		return new ballPaddleHitResults(ball, newState, reps);
	}
	
	/**
	 * Returns a copy of this ReplicatorPaddleState object representing a replicator paddle that has been powered up.
	 * This basically resets its lifetime.
	 * @creates | result
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getSize().equals(old(getSize()))
	 * @post | result.getLifetime() == MAX_REPLICATOR_LIFETIME
	 */
	public ReplicatorPaddleState powerup() {
		return this.resetLifetime();
	}
}