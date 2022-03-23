package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BlockStateTest {

	@Test
	void test() {
		// Testing constructor
		Point TL = new Point(0,0);
		Point BR = new Point(10,10);
		BlockState block = new BlockState(TL,BR);
		
		assertEquals(block.getBottomRight(),BR);
		assertEquals(block.getTopLeft(),TL);
		
		// Testing encapsulation
		TL = TL.reflectVertical(1);
		assertNotEquals(block.getTopLeft(),TL);
		TL = block.getTopLeft();
		TL = TL.reflectVertical(1);
		assertNotEquals(block.getTopLeft(),TL);
		BR = BR.reflectHorizontal(9);
		assertNotEquals(block.getBottomRight(),BR);
		BR = block.getBottomRight();
		BR = BR.reflectHorizontal(9);
		assertNotEquals(block.getBottomRight(),BR);
	}

}
