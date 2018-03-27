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
	
	
	
	public ArrayList<Course> getCourses(){
		return courses;
	}
	
	public boolean getAvailability() {
		return isAvailable;
	}
	public boolean getAvailability(Period period) {
		if(period == Period.Period1) 
			return isAvailableP1;
		
		else if(period == Period.Period2) 
			return isAvailableP2;
		
		else if(period == Period.Period3A) 
			return isAvailableP3A;
		
		else if(period == Period.Period3B) 
			return isAvailableP3B;
		
		else
			return isAvailableP4;
		
	}
	
	public void isAvailable() {
		isAvailable  = true;
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
