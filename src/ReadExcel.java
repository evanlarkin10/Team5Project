
import java.io.File;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Workbook;

public class ReadExcel {

	
	public static void main(String []args) throws Exception{
		
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		int numberOfPeriods = 5;
		int column;
		int row;
		int next;
		
		//Sets up workbook
		File src = new File("WorkBook.xls");
		Workbook wb = Workbook.getWorkbook(src);
		
		//Locate teachers in the spreadsheet
		column = wb.getSheet("Master Schedule").findCell("Teacher's Name").getColumn();
		row = wb.getSheet("Master Schedule").findCell("Teacher's Name").getRow();
		
		//Create Teacher Objects from the spreadsheet
		next = 1;
		while (! wb.getSheet("Master Schedule").getCell(column ,row + next).getContents().equals("")){
			teachers.add(new Teacher(wb.getSheet("Master Schedule").getCell(column ,row + next).getContents()));
			next = next + 1;
		}
	
		//Add courses to teachers starting with the first teacher
		column = wb.getSheet("Master Schedule").findCell(teachers.get(0).NAME).getColumn();
		row = wb.getSheet("Master Schedule").findCell(teachers.get(0).NAME).getRow();
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
				courses.add(new Course(wb.getSheet("Master Schedule").getCell(column + i ,row).getContents(), period ,teacher));
			}
			
			for(Course course : courses){
				teacher.addCourse(course);
			}
		}
		
		//Add skills to teacher 
		column = wb.getSheet("Master Schedule").findCell("Teachable Skill").getColumn();
		row = wb.getSheet("Master Schedule").findCell("Teachable Skill").getRow() + 1;
		
		for(Teacher teacher : teachers){
			ArrayList<String> skills = new ArrayList<String>();
			String teacherSkill = wb.getSheet("Master Schedule").getCell(column ,row + teachers.indexOf(teacher)).getContents() ;
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
	
}
