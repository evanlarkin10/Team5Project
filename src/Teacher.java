import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.read.biff.BiffException;

public class Teacher {
	
	final String NAME;
	ArrayList<Course> courses = new ArrayList<Course>();
	String skills = "";
	String roomNumber;
	boolean isAvailable = false;
	
	public Teacher(String NAME, String roomIn){
		this.NAME = NAME;
		this.roomNumber = roomIn;
	}
	
	
	public void addCourse(Course course){ courses.add(course); }
	
	public ArrayList<Course> getCourses(){
		return courses;
	}
	
	
	public boolean getAvailability() {
		return isAvailable;
	}
	
	public void isAvailable() {
		isAvailable = true;
	}
	
	public void addSkill(String skill){  skills = skills + " " + skill;     }
	
	//This method searches the teachers courses and assigns skills based on the skill spreadsheet
	public void assignSkill(ArrayList<ArrayList<String>> skillList) {
		int i;
		for( Course course: getCourses()) {
			i=0;
			for(ArrayList<String> list : skillList) {
				for( String skill : skillList.get(i)) {
					if(skill.equals(course.getCourseName())) {
						
						//Add if not already listed
						if(!skills.contains(skillList.get(i).get(0)))
							skills = skills + " " + skillList.get(i).get(0);
					}
					
				}
				i=i+1;
			}
			
		}
	}
	
	
}
