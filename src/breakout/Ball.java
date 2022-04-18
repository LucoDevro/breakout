package breakout;

import java.awt.Color;

public abstract class Ball {
	protected Point center;
	protected int diameter;
	protected Vector velocity;
	
	public abstract Point getCenter();
	public abstract int getDiameter();
	public abstract Vector getVelocity();
	public abstract void setCenter(Point center);
	public abstract void setVelocity(Vector velocity);
	public abstract void hitBlock(Rect rect, boolean destroyed);
	public abstract void roll();
	public abstract void bounce(Vector direction);
	public abstract Rect rectangleOf();
	public abstract Color getColor();
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
	public void roll() {
		center=center.plus(velocity);
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
		Vector direction = this.rectangleOf().collide(rect);
		this.bounce(direction);
	}
}

class SuperBall extends Ball {
	int lifetime;
	
	public SuperBall(Point center, int diameter, Vector velocity, int lifetime) {
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
	public int getLifeTime() {
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
	public void roll() {
		center=center.plus(velocity);
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
			Vector direction = this.rectangleOf().collide(rect);
			this.bounce(direction);
		}
	}
}