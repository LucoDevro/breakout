package breakout;

/**
 * Each instance of this class represents a ball of the breakout game.
 *
 * @immutable
 * 
 * @invar A ball's diameter is bigger or equal to 0.
 * 	| getDiameter() >= 0
 * @invar A ball's initial velocity is not equal to a zero vector.
 * 	| !(getVelocity().equals(new Vector(0,0)))
 */

public final class BallState {
	
	// Fields
	/**
	 * @invar | diameter >= 0
	 * @invar | !(velocity.equals(new Vector(0,0)))
	 */
	private final Point center;
	private final int diameter;
	private final Vector velocity;
	
	// Constructor
	/**
	 * Returns an object representing a ball in the breakout game defined by a center point,
	 * a positive diameter and a non-zero initial velocity
	 * @pre The diameter must be positive.
	 * 	| diameter >= 0
	 * @pre The initial velocity vector must be different from a zero vector.
	 * 	| !(initVelocity.equals(new Vector(0,0)))
	 * @post | getCenter().equals(center)
	 * @post | getDiameter() == diameter
	 * @post | getVelocity().equals(initVelocity)
	 */
	public BallState(Point center, int diameter, Vector initVelocity) {
		this.center=center;
		this.diameter=diameter;
		this.velocity=initVelocity;
	}
	
	// Getters
	/**
	 * Returns the center Point object contained within this BallState object.
	 * @inspects | this
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the velocity Vector object contained within this BallState object.
	 * @inspects | this
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Returns the diameter of the ball represented by this BallState object.
	 * @inspects | this
	 */
	public int getDiameter() {
		return diameter;
	}
	
	// Setters
	/**
	 * Returns a new object representing a ball in the breakout game with the same diameter
	 * and velocity as the old one, but situated at the given center point.
	 * @creates | result
	 * @post | result != null
	 * @post | result.getCenter().equals(center)
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 */
	public BallState setCenter(Point center) {
		return new BallState(center,this.diameter,this.velocity);
	}
	
	/**
	 * Returns a new object representing a ball in the breakout game with the same diameter
	 * and center point as the old one, but with the given velocity
	 * @creates | result
	 * @throws IllegalArgumentException if the given velocity is a zero vector
	 * 	| velocity.equals(new Vector(0,0))
	 * @post | result != null
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(velocity)
	 */
	public BallState setVelocity(Vector velocity) {
		if (velocity.equals(new Vector(0,0))) {
			throw new IllegalArgumentException("Given velocity is a zero vector!");
		}
		return new BallState(this.center,this.diameter,velocity);
	}
	
	// Additional methods
	/**
	 * Returns a new object representing a ball in the breakout game that has bounced on
	 * a surface with unit normal vector direction and consequently changed its velocity
	 * in comparison with its old state.
	 * 
	 * @creates | result
	 * @throws IllegalArgumentException if the given direction is not a unit vector
	 * 	| direction.getSquareLength() != 1
	 * @post | result != null
	 * @post | result.getCenter().equals(old(getCenter()))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().mirrorOver(direction).equals(old(getVelocity()))
	 */
	public BallState bounce(Vector direction) {
		if (direction.getSquareLength() != 1) {
			throw new IllegalArgumentException("Bounce direction is not a unit vector!");
		}
		return new BallState(center,diameter,velocity.mirrorOver(direction));
	}
	
	/**
	 * Returns a new object representing a ball in the breakout game that has rolled in
	 * its current direction velocity and consequently changed its center point
	 * in comparison with its old state.
	 * 
	 * @creates | result
	 * @post | result != null
	 * @post | result.getCenter().equals(old(getCenter().plus(velocity)))
	 * @post | result.getDiameter() == old(getDiameter())
	 * @post | result.getVelocity().equals(old(getVelocity()))
	 */
	public BallState roll() {
		return new BallState(center.plus(velocity),diameter,velocity);
	}
	
	public Rectangle rectangleOf() {
		return new Rectangle(center.minus(new Vector(diameter, diameter)), center.plus(new Vector(diameter, diameter)));
	}
}
