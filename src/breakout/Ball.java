package breakout;

import java.awt.Color;

public abstract class Ball {
	protected Point center;
	protected int diameter;
	protected Vector velocity;
	
	protected static Vector[] replicateBallsSpeedDiff = {new Vector(2,-2), new Vector(-2,2), new Vector(2,2)};
	protected static int LIFETIME = 10000;
	
	public abstract Point getCenter();
	public abstract int getDiameter();
	public abstract Vector getVelocity();
	public abstract void setCenter(Point center);
	public abstract void setVelocity(Vector velocity);
	public abstract void hitBlock(Rect rect, boolean destroyed);
	public abstract void roll(int elapsedTime);
	public abstract void bounce(Vector direction);
	public abstract Rect rectangleOf();
	public abstract Color getColor();
	public abstract Ball[] replicate(int reps);
	public abstract SuperBall powerup();
	public abstract Ball age(int elapsedTime);
}

class NormalBall extends Ball {
	public NormalBall(Point center, int diameter, Vector velocity) {
		this.center=center;
		this.diameter=diameter;
		this.velocity=velocity;
	}
	public Point getCenter() {
		return center;
	}
	public int getDiameter() {
		return diameter;
	}
	public Vector getVelocity() {
		return velocity;
	}
	public void setCenter(Point center) {
		this.center=center;
	}
	public void setVelocity(Vector velocity) {
		this.velocity=velocity;
	}
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}
	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter, diameter)), center.plus(new Vector(diameter, diameter)));
	}
	public Color getColor() {
		return Color.red;
	}
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector direction = this.rectangleOf().overlap(rect);
		this.bounce(direction);
	}
	public SuperBall convertToSuper() {
		return new SuperBall(center, diameter, velocity, LIFETIME);
	}
	public NormalBall[] replicate(int reps) {
		NormalBall[] replicated = new NormalBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new NormalBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]));
		}
		return replicated;
	}
	public SuperBall powerup() {
		return this.convertToSuper();
	}
	public Ball age(int elapsedTime) {
		return this;
	}
}

class SuperBall extends Ball {
	long lifetime;
	
	public SuperBall(Point center, int diameter, Vector velocity, long lifetime) {
		this.center=center;
		this.diameter=diameter;
		this.velocity=velocity;
		this.lifetime=lifetime;
	}
	public Point getCenter() {
		return center;
	}
	public int getDiameter() {
		return diameter;
	}
	public Vector getVelocity() {
		return velocity;
	}
	public long getLifeTime() {
		return lifetime;
	}
	public void setCenter(Point center) {
		this.center=center;
	}
	public void setVelocity(Vector velocity) {
		this.velocity=velocity;
	}
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	public void resetLifetime() {
		this.lifetime = LIFETIME;
	}
	public void roll(int elapsedTime) {
		center=center.plus(velocity.scaled(elapsedTime));
	}
	public void bounce(Vector direction) {
		velocity=velocity.mirrorOver(direction);
	}
	public Rect rectangleOf() {
		return new Rect(center.minus(new Vector(diameter, diameter)), center.plus(new Vector(diameter, diameter)));
	}
	public Color getColor() {
		return Color.pink;
	}
	public void hitBlock(Rect rect, boolean destroyed) {
		if (!(destroyed)) {
			Vector direction = this.rectangleOf().overlap(rect);
			this.bounce(direction);
		}
	}
	public Ball age(int elapsedTime) {
		lifetime-=elapsedTime;
		if (lifetime <= 0) {
			return this.convertToNormal();
		}
		return this;
	}
	public NormalBall convertToNormal() {
		return new NormalBall(center, diameter, velocity); 
	}
	public SuperBall[] replicate(int reps) {
		SuperBall[] replicated = new SuperBall[reps];
		for (int i=0; i<reps; i++) {
			replicated[i] = new SuperBall(center, diameter, velocity.plus(replicateBallsSpeedDiff[i]),lifetime);
		}
		return replicated;
	}
	public SuperBall powerup() {
		this.resetLifetime();
		return this;
	}
}