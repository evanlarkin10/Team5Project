import java.util.ArrayList;


public class Teacher {
	
	final String NAME;
	ArrayList<Course> courses = new ArrayList<Course>();
	String skills = "";
	String roomNumber;
	
	public Teacher(String NAME, String roomIn){
		this.NAME = NAME;
		this.roomNumber = roomIn;
	}
	
	
	public void addCourse(Course course){ courses.add(course); }
	
	public void addSkill(String skill){  skills = skills + " " + skill;     }
	
}
