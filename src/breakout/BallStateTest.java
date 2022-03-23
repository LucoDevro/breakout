package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BallStateTest {

	@Test
	void test() {
		// Testing constructor
		Point center = new Point(5,5);
		int diameter = 2;
		Vector initVelocity = new Vector(2,-1);
		BallState ball = new BallState(center, diameter, initVelocity);
		
		assertEquals(ball.getCenter(),center);
		assertEquals(ball.getDiameter(),diameter);
		assertEquals(ball.getVelocity(),initVelocity);
		
		// Testing encapsulation of the constructor
		center = new Point(4,4);
		assertNotEquals(ball.getCenter(),center);
		diameter = 3;
		assertNotEquals(ball.getDiameter(),diameter);
		initVelocity = new Vector(1,-2);
		assertNotEquals(ball.getVelocity(),initVelocity);
		
		// Testing setters and their encapsulation
		ball=ball.setCenter(center);
		assertEquals(ball.getCenter(),center);
		center=center.reflectHorizontal(5);
		assertNotEquals(ball.getCenter(),center);
		ball=ball.setCenter(new Point(5,5));
		assertNotEquals(ball.getCenter(),center);
		
		ball=ball.setVelocity(initVelocity);
		assertEquals(ball.getVelocity(),initVelocity);
		initVelocity=initVelocity.mirrorOver(Vector.DOWN);
		assertNotEquals(ball.getVelocity(),initVelocity);
		ball=ball.setVelocity(new Vector(2,-1));
		assertNotEquals(ball.getVelocity(),initVelocity);
		
		// Testing additional methods
		center=ball.getCenter();
		ball=ball.roll();
		initVelocity=ball.getVelocity();
		assertEquals(ball.getCenter().minus(initVelocity),center);
		ball=ball.bounce(Vector.UP);
		assertEquals(ball.getVelocity().plus(initVelocity),new Vector(2*initVelocity.getX(),0));
	}
}
