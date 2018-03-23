import java.util.Calendar;

public class Scheduler {

	
	//Constructor
	public Scheduler(){
		
	}
	
	public void assignOnCalls() {
		
	}
	
	//returns the numeric day of the week - to be used inside assignOnCalls
	private int getDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}
}

