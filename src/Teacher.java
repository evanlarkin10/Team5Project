import java.util.ArrayList;


public class Teacher {
	
	final String NAME;
	ArrayList<Course> courses = new ArrayList<Course>();
	String skills;
	
	public Teacher(String NAME){
		this.NAME = NAME;
	}
	
	
	public void addCourse(Course course){ courses.add(course); }
	
	public void addSkill(String skill){  skills = skills + "," + skill;     }
	
}
