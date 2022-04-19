package breakout;

import java.awt.Color;

// TODO: Check whether the encapsulation still holds!
public abstract class PaddleState {
	protected Point center;
	protected Vector size;
	
	protected static final int LIFETIME = 3;
	
	public abstract Point getCenter();
	public abstract Vector getSize();
	public abstract Rect rectangleOf();
	public abstract void setCenter(Point center);
	public abstract Color getColor();
	public abstract ballPaddleHitResults hitBall(Ball ball, int paddleDir);
	public abstract ReplicatorPaddleState powerup();
}

/**
 * Each instance of this class represents a paddle of the breakout game.
 * 
 * @immutable
 * 
 * @invar The size of a paddle is given by a vector with both x and y being positive
 * 	| getSize().getX() >= 0 && getSize().getY() >= 0
 */

final class NormalPaddleState extends PaddleState {
	
	// Constructor
	/**
	 * Returns an object representing a rectangular paddle defined by a center point and a size vector
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
	 * Returns the center Point object contained within this PaddleState object.
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the size Vector object contained within this PaddleState object.
	 */
	public Vector getSize() {
		return size;
	}
	
	// Setters
	public void setCenter(Point center) {
		this.center = center;
	}
	
	/**
	 * Returns a Rectangle object representing the rectangle surrounding the paddle represented by this PaddleState object.
	 * 
	 * @creates | result
	 * @post | result.getTopLeft().equals(getCenter().minus(getSize()))
	 * @post | result.getBottomRight().equals(getCenter().plus(getSize()))
	 */
	public Rect rectangleOf() {
		return new Rect(center.minus(size), center.plus(size));
	}
	
	public Color getColor() {
		return Color.green;
	}
	
	public ReplicatorPaddleState convertToReplicator() {
		return new ReplicatorPaddleState(center, size, LIFETIME);
	}
	
	public ballPaddleHitResults hitBall(Ball ball, int paddleDir) {
		Vector normVecPaddle = ball.rectangleOf().overlap(this.rectangleOf());
		if (normVecPaddle != null &&
			normVecPaddle.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			ball.bounce(normVecPaddle);
			ball.setVelocity(ball.getVelocity().plus(Vector.RIGHT.scaled(2*paddleDir)));
		}
		return new ballPaddleHitResults(ball, this, 0);
	}
	
	public ReplicatorPaddleState powerup() {
		return this.convertToReplicator();
	}
}

final class ReplicatorPaddleState extends PaddleState {
	int lifetime;
		
	public ReplicatorPaddleState(Point center, Vector size, int i) {
		this.center=center;
		this.size=size;
		this.lifetime=i;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public Vector getSize() {
		return size;
	}
	
	public Rect rectangleOf() {
		return new Rect(center.minus(size), center.plus(size));
	}
	
	public void setCenter(Point center) {
		this.center=center;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public PaddleState decreaseLifetime() {
		int newage=lifetime-1;
		if (newage <= 0) {
			return this.convertToNormal();
		}
		return new ReplicatorPaddleState(center,size,newage);
	}
	
	public ReplicatorPaddleState resetLifetime() {
		return new ReplicatorPaddleState(center,size,LIFETIME);
	}
	
	public Color getColor() {
		return Color.yellow;
	}
	
	public NormalPaddleState convertToNormal() {
		return new NormalPaddleState(center, size);
	}
	
	public ballPaddleHitResults hitBall(Ball ball, int paddleDir) {
		int reps = 0;
		PaddleState newState = this;
		Vector normVecPaddle = ball.rectangleOf().overlap(this.rectangleOf());
		if (normVecPaddle != null &&
			normVecPaddle.product(ball.getVelocity()) > 0) { // Bounce only when the ball is at the outside
			ball.bounce(normVecPaddle);
			ball.setVelocity(ball.getVelocity().plus(Vector.RIGHT.scaled(2*paddleDir)));
			newState = this.decreaseLifetime();
			reps=this.getLifetime();
			}
		return new ballPaddleHitResults(ball, newState, reps);
	}
	
	public ReplicatorPaddleState powerup() {
		this.resetLifetime();
		return this;
	}
}