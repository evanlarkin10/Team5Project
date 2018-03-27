import java.util.ArrayList;


public class Teacher {
	
	final String NAME;
	ArrayList<Course> courses = new ArrayList<Course>();
	String skills = "";
	String roomNumber;
	boolean isAvailableP1 = true ,isAvailableP2 = true, isAvailableP3A = true, isAvailableP3B = true, isAvailableP4  = true;
	boolean isAvailable = false;
	
	
	public Teacher(String NAME, String roomIn){
		this.NAME = NAME;
		this.roomNumber = roomIn;
	}
	
	
	public void addCourse(Course course){ courses.add(course); }
	
	public void addSkill(String skill){  skills = skills + " " + skill;     }
	
	public boolean getAvailability() {
		return isAvailable;
	}
	
	public void isAvailable() {
		isAvailable  = true;
	}
	
}
