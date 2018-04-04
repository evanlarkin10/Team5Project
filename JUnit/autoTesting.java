import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jxl.*;

class autoTesting {

	
	@Test
	@DisplayName("Varifies that the teachers avalabilty method")
	void checkTeachersAvailabilityByPeriod() {
		Teacher teacher = new Teacher("Test", "101");
		teacher.isAvailableP2 = false;
		assertFalse(teacher.getAvailability(Period.Period2));
	}	
	
	@Test
	@DisplayName("Checks the teachers name comes back correct")
	void teachersNameComesBackCorrect(){
		Teacher teacher = new Teacher("Test", "101");
		assertEquals(teacher.NAME,"Test");
	}	
	

}
