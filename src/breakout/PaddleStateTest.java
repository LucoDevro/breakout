package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaddleStateTest {

	@Test
	void test() {
		// Testing constructor
		Point center = new Point(5,5);
		Vector size = new Vector(4,1);
		PaddleState paddle = new PaddleState(center,size);
		
		assertEquals(paddle.getCenter(),center);
		assertEquals(paddle.getSize(),size);
		
		// Testing constructor encapsulation
		center = center.reflectVertical(4);
		assertNotEquals(paddle.getCenter(),center);
		size = size.mirrorOver(Vector.UP);
		assertNotEquals(paddle.getSize(),size);
		
		// Testing setters and their encapsulation
		paddle=paddle.setCenter(center);
		assertEquals(paddle.getCenter(),center);
		center = center.reflectHorizontal(4);
		assertNotEquals(paddle.getCenter(),center);
		paddle=paddle.setCenter(new Point(5,5));
		assertNotEquals(paddle.getCenter(),center);
	}
}