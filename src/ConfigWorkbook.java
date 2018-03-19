import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.*;
import jxl.read.biff.BiffException;
public class ConfigWorkbook {
	private Sheet masterSchedule;
	private Sheet supplyList;
	private Sheet skills;
	private Sheet absenceSchedule;
	private Sheet tallySheet;
	private File configFile;
	
	public ConfigWorkbook(File configFileIn) throws BiffException, IOException {
		configFile = configFileIn;
		Workbook wb = Workbook.getWorkbook(configFileIn);
		masterSchedule = wb.getSheet("Master Schedule");
		supplyList = wb.getSheet("Supply List");
		skills = wb.getSheet("Skills");
		tallySheet = wb.getSheet("Tally");
		absenceSchedule = wb.getSheet("Week X");
	}
	
	public void getTeachers() {
		//Locate teachers in the spreadsheet
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		int numberOfPeriods = 5;
		int column;
		int row;
		int next;
		
			column = masterSchedule.findCell("Teacher's Name").getColumn();
			row = masterSchedule.findCell("Teacher's Name").getRow();
			//Create Teacher Objects from the spreadsheet
			next = 1;
			while (! masterSchedule.getCell(column ,row + next).getContents().equals("")){
				teachers.add(new Teacher(masterSchedule.getCell(column ,row + next).getContents()));
				next = next + 1;
			}
		
			//Add courses to teachers starting with the first teacher
			column = masterSchedule.findCell(teachers.get(0).NAME).getColumn();
			row = masterSchedule.findCell(teachers.get(0).NAME).getRow();
			for(Teacher teacher : teachers){
				ArrayList<Course> courses = new ArrayList<Course>();
				for(int i = 1; i <= numberOfPeriods; i++){
					Period period = Period.Period1;
					switch(i) { 
					case 1:
						period = Period.Period1;
						break;
					case 2: 
						period = Period.Period2;
						break;
					case 3:
						period = Period.Period3A;
						break;
					case 4:
						period = Period.Period3B;
						break;
					case 5: 
						period = Period.Period4;
						break;	
					}
					courses.add(new Course(masterSchedule.getCell(column + i ,row).getContents(), period ,teacher));
				}
				
				for(Course course : courses){
					teacher.addCourse(course);
				}
			}
			
			//Add skills to teacher 
			column = masterSchedule.findCell("Teachable Skill").getColumn();
			row = masterSchedule.findCell("Teachable Skill").getRow() + 1;
			
			for(Teacher teacher : teachers){
				ArrayList<String> skills = new ArrayList<String>();
				String teacherSkill = masterSchedule.getCell(column ,row + teachers.indexOf(teacher)).getContents() ;
				skills.add(teacherSkill);
				
				for(String skill : skills){
					teacher.addSkill(skill);
				}
			}
			
			//Print out teachers , courses, and Period they teach
			for(Teacher teacher : teachers){
				System.out.println(teacher.NAME + " Skill:" + teacher.skills);
				for(Course course : teacher.courses){
					System.out.println(course.courseTitle + " " + course.period);
				}
				System.out.println();
			}
	}

	public void resetMonthlyTally() {
		
	}
	public void resetWeeklyTally() {
		
	}
			
	
}
