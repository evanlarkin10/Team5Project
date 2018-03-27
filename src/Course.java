


public class Course {

	String courseTitle;
	Period period;
	Teacher regularTeacher;
	boolean isAbsent = false;
	
	public Course (String courseTitle, Period period, Teacher regularTeacher){
		this.courseTitle = courseTitle;
		this.period = period;
		this.regularTeacher = regularTeacher;
	}
	
	public String getCourseName() {
		return courseTitle;
	}
	
	public void setIsAbsent(boolean in) {
		isAbsent = in;
	}
	
}