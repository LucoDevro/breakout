package breakout;

import java.util.stream.IntStream;
import java.awt.Color;

/**
 * Each instance of this class represents a ball in the breakout game.
 * 
 * @invar A ball's diameter is bigger than zero.
 * 	| getDiameter() >= 0
 * @invar A ball's velocity is not equal to the zero vector.
 * 	| getVelocity().equals(new Vector(0,0))
 * @invar | getCenter() != null
 * @invar | getVelocity() != null
 */

public abstract class Ball {
	/**
	 * @invar | diameter >= 0
	 * @invar | velocity.equals(new Vector(0,0))
	 * @invar | center != null
	 * @invar | velocity != null
	 */
	protected Point center;
	protected int diameter;
	protected Vector velocity;
	
	protected static final Vector[] replicateBallsSpeedDiff = {new Vector(2,-2), new Vector(-2,2), new Vector(2,2)};
	protected static final int MAX_LIFETIME = 10000;
	
	public abstract Point getCenter();
	public abstract int getDiameter();
	public abstract Vector getVelocity();
	public abstract void changeCenter(Point center);
	public abstract void changeVelocity(Vector velocity);
	public abstract void hitBlock(Rect rect, boolean destroyed);
	public abstract void roll(int elapsedTime);
	public abstract void bounce(Vector direction);
	public abstract Rect rectangleOf();
	public abstract Color getColor();
	public abstract Ball[] replicate(int reps);
	public abstract SuperBall powerup();
	public abstract Ball age(int elapsedTime);
}
/**
 * Each instance of this class represents a normal ball in a breakout game.
 * @invar A ball's diameter is bigger than zero.
 * 	| getDiameter() >= 0
 * @invar A ball's velocity is not equal to the zero vector.
 * 	| !(getVelocity().equals(new Vector(0,0)))
 */
class NormalBall extends Ball {
	/**
	 * Returns an object representing a normal ball in the breakout game, defined by a center Point object,
	 * a positive diameter and a non-zero velocity Vector object.
	 * @pre | velocity != null
	 * @pre | center != null
	 * @pre | diameter > 0
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @post | getCenter().equals(center)
	 * @post | getDiameter() == diameter
	 * @post | getVelocity().equals(velocity)
	 */
	public NormalBall(Point center, int diameter, Vector velocity) {
		this.center=center;
		this.diameter=diameter;
		this.velocity=velocity;
	}
	
	/**
	 * Returns the center Point object contained within this NormalBall object.
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the diameter contained within this NormalBall object.
	 */
	public int getDiameter() {
		return diameter;
	}
	
	/**
	 * Returns the velocity Vector object contained within this NormalBall object.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Sets the center Point object of this NormalBall object to the supplied Point object.
	 * @mutates | this
	 * @pre | center != null
	 * @post | getCenter().equals(center)
	 */
	public void changeCenter(Point center) {
		this.center=center;
	}
	
	/**
	 * Sets the velocity Vector object of this NormalBall object to the supplied Velocity object.
	 * @mutates | this
	 * @pre | velocity != null
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @post | getVelocity().equals(velocity)
	 */
	public void changeVelocity(Vector velocity) {
		this.velocity=velocity;
	}
	
	/**
	 * Changes the state of this NormalBall object so that it reflects a ball that has rolled for a
	 * non-zero amount of time into its current direction and at a speed, both contained in its velocity Vector.
	 * @mutates | this
	 * @pre | elapsedTime != 0
	 * @post | getCenter().equals(old(getCenter().plus(getVelocity().scaled(elapsedTime))))
	 */
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	
	/**
	 * Changes the state of this NormalBall object so that is reflects a ball that has bounced
	 * on a surface represented by the supplied normal unit vector
	 * @mutates | this
	 * @pre | direction != null && direction.getSquareLength() == 1
	 * @pre | !(direction.equals(new Vector(0,0)))
	 * @post | getVelocity().equals(old(getVelocity()).mirrorOver(direction))
	 */
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}
	
	/**
	 * Returns a Rectangle object that represents the rectangle surrounding the ball represented by
	 * this NormalBall object.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getTopLeft().equals(getCenter().minus(new Vector(getDiameter()/2,getDiameter()/2)))
	 * @post | result.getBottomRight().equals(getCenter().plus(new Vector(getDiameter()/2,getDiameter()/2)))
	 */
	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter/2, diameter/2)), center.plus(new Vector(diameter/2, diameter/2)));
	}
	
	/**
	 * Returns the default colour used to display a normal ball in the breakout game, i.e. red.
	 */
	public Color getColor() {
		return Color.red;
	}
	
	/**
	 * Changes the motion of the ball depending on whether the block it hit, was destroyed.
	 * For normal balls, this means it should bounce anyway.
	 * @mutates | this
	 * @pre | rect != null
	 * @post | getVelocity().equals(old(getVelocity()).mirrorOver(rectangleOf().overlap(rect)))
	 */
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector direction = this.rectangleOf().overlap(rect);
		this.bounce(direction);
	}
	
	/**
	 * Returns a copy of this NormallBall object converted to a SuperBall object with the default lifetime
	 * @creates | result
	 * @inspects | this
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 * @post | result.getLifetime() == MAX_LIFETIME
	 */
	public SuperBall convertToSuper() {
		return new SuperBall(center, diameter, velocity, MAX_LIFETIME);
	}
	
	/**
	 * Returns an array with a predefined number (1 up to 3) of replicate normal balls, differing only
	 * in velocity by the preset replication differences 'replicateBallsSpeedDiff'.
	 * @creates | result
	 * @inspects | this
	 * @pre | reps >= 1 && reps <= 3
	 * @post | result.length == reps
	 * @post | IntStream.range(0,result.length).allMatch(i -> result[i].getVelocity().equals(old(getVelocity()).plus(replicateBallsSpeedDiff[i])))
	 */
	public NormalBall[] replicate(int reps) {
		NormalBall[] replicated = new NormalBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new NormalBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]));
		}
		return replicated;
	}
	
	/**
	 * Returns a copy of this NormalBall object representing a normal ball that has been powered up.
	 * Powering up a normal ball always results in converting it to a supercharged ball.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 * @post | result.getLifetime() == MAX_LIFETIME
	 */
	public SuperBall powerup() {
		return this.convertToSuper();
	}
	
	/**
	 * Ages the ball so that its lifetime decreases. Lifetime is not applicable to a normal ball,
	 * so the current normal ball state is returned.
	 */
	public Ball age(int elapsedTime) {
		return this;
	}
}

/**
 * Each instance of this class represents a supercharged ball in a breakout game.
 * @invar A ball's diameter is bigger than zero.
 * 	| getDiameter() >= 0
 * @invar A ball's velocity is not equal to the zero vector.
 * 	| !(getVelocity().equals(new Vector(0,0)))
 * @invar A supercharged ball's lifetime is between 0 and the preset lifetime of 10000 ms.
 * 	| getLifetime() > 0 && getLifetime() <= MAX_LIFETIME
 */
class SuperBall extends Ball {
	/**
	 * @invar | lifetime > 0 && lifetime <= MAX_LIFETIME
	 */
	private long lifetime;
	
	/**
	 * Returns an object representing a supercharged ball in the breakout game, defined by a center Point object,
	 * a positive diameter, a non-zero velocity Vector object and a positive lifetime smaller than or equal to the preset.
	 * @pre | velocity != null
	 * @pre | center != null
	 * @pre | diameter > 0
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @pre | lifetime > 0 && lifetime <= MAX_LIFETIME
	 * @post | getCenter().equals(center)
	 * @post | getDiameter() == diameter
	 * @post | getVelocity().equals(velocity)
	 * @post | getLifetime() == lifetime
	 */
	public SuperBall(Point center, int diameter, Vector velocity, long lifetime) {
		this.center=center;
		this.diameter=diameter;
		this.velocity=velocity;
		this.lifetime=lifetime;
	}
	
	/**
	 * Returns the center Point object contained within this SuperBall object.
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the diameter contained within this SuperBall object.
	 */
	public int getDiameter() {
		return diameter;
	}
	
	/**
	 * Returns the velocity Vector object contained within this SuperBall object.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Returns the remaining lifetime in milliseconds of this SuperBall object.
	 */
	public long getLifetime() {
		return lifetime;
	}
	
	/**
	 * Sets the center Point object of this SuperBall object to the supplied Point object.
	 * @mutates | this
	 * @pre | center != null
	 * @post | getCenter().equals(center)
	 */
	public void changeCenter(Point center) {
		this.center=center;
	}
	
	/**
	 * Sets the velocity Vector object of this SuperBall object to the supplied Velocity object.
	 * @mutates | this
	 * @pre | velocity != null
	 * @pre | !(velocity.equals(new Vector(0,0)))
	 * @post | getVelocity().equals(velocity)
	 */
	public void changeVelocity(Vector velocity) {
		this.velocity=velocity;
	}
	
	/**
	 * Sets the remaining lifetime in milliseconds of this SuperBall object to the supplied value.
	 * @mutates | this
	 * @pre | lifetime > 0 && lifetime <= MAX_LIFETIME
	 * @post | getLifetime() == lifetime
	 */
	public void changeLifetime(long lifetime) {
		this.lifetime = lifetime;
	}
	
	/**
	 * Resets the remaining lifetime of this SuperBall object to the default, LIFETIME.
	 * @mutates | this
	 * @post | getLifetime() == MAX_LIFETIME
	 */
	public void resetLifetime() {
		this.lifetime = MAX_LIFETIME;
	}
	
	/**
	 * Changes the state of this SuperBall object so that it reflects a ball that has rolled for a
	 * non-zero amount of time into its current direction and at a speed, both contained in its velocity Vector.
	 * @mutates | this
	 * @pre | elapsedTime != 0
	 * @post | getCenter().equals(old(getCenter()).plus(getVelocity().scaled(elapsedTime)))
	 */
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	
	/**
	 * Changes the state of this SuperBall object so that is reflects a ball that has bounced
	 * on a surface represented by the supplied normal unit vector
	 * @mutates | this
	 * @pre | direction != null && direction.getSquareLength() == 1
	 * @post | getVelocity().equals(old(getVelocity()).mirrorOver(direction))
	 */
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}
	
	/**
	 * Returns a Rectangle object that represents the rectangle surrounding the ball represented by
	 * this SuperBall object.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getTopLeft().equals(getCenter().minus(new Vector(getDiameter()/2,getDiameter()/2)))
	 * @post | result.getBottomRight().equals(getCenter().plus(new Vector(getDiameter()/2,getDiameter()/2)))
	 */
	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter/2, diameter/2)), center.plus(new Vector(diameter/2, diameter/2)));
	}
	
	/**
	 * Returns the default colour used to display a supercharged ball in the breakout game, i.e. pink.
	 */
	public Color getColor() {
		return Color.pink;
	}
	
	/**
	 * Changes the motion of the ball depending on whether the block it hit, was destroyed.
	 * For supercharged balls, this means it bounces only on sturdy blocks with a lifetime bigger than 1.
	 * @mutates | this
	 * @pre | rect != null
	 * @post | getCenter().equals(old(getCenter()))
	 */
	public void hitBlock(Rect rect, boolean destroyed) {
		if (!(destroyed)) {
			Vector direction = this.rectangleOf().overlap(rect);
			this.bounce(direction);
		}
	}
	
	/**
	 * Ages the ball so that its lifetime decreases with a positive amount of time elapsed.
	 * @pre | elapsedTime >= 0
	 * @inspects | this
	 * @post | result instanceof NormalBall || ((SuperBall) result).getLifetime() == old(getLifetime())-elapsedTime
	 */
	public Ball age(int elapsedTime) {
		long newlifetime = lifetime - elapsedTime;
		if (newlifetime <= 0) {
			return this.convertToNormal();
		}
		lifetime = newlifetime;
		return this;
	}
	
	/**
	 * Returns a copy of this SuperBall object converted to a NormalBall object.
	 * @creates | result
	 * @inspects | this
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 */
	public NormalBall convertToNormal() {
		return new NormalBall(center, diameter, velocity); 
	}
	
	/**
	 * Returns an array with a predefined number (1 up to 3) of replicate supercharged balls, differing only
	 * in velocity by the preset replication differences 'replicateBallsSpeedDiff'.
	 * @creates | result
	 * @inspects | this
	 * @pre | reps >= 1 && reps <= 3
	 * @post | result.length == reps
	 * @post | IntStream.range(0,result.length).allMatch(i -> result[i].getVelocity().equals(old(getVelocity()).plus(replicateBallsSpeedDiff[i])))
	 */
	public SuperBall[] replicate(int reps) {
		SuperBall[] replicated = new SuperBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new SuperBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]),lifetime);
		}
		return replicated;
	}
	
	/**
	 * Returns a copy of this SuperBall object representing a supercharged ball that has been powered up.
	 * Powering up a supercharged ball results in resetting its lifetime.
	 * @mutates | this
	 */
	public SuperBall powerup() {
		this.resetLifetime();
		return this;
	}
}