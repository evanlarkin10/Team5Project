import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.read.biff.BiffException;

public class ConfigWorkbook {
	private Sheet masterSchedule;
	private Sheet supplyList;
	private Sheet skills;
	private Sheet absenceSchedule;
	private Sheet tallySheet;
	private File configFile;
	private Workbook wb;
	private WritableWorkbook wbWritable;
	private WritableSheet tallySheetWritable;
	private WritableWorkbook copyDocument;
	
	public ConfigWorkbook(File configFileIn) throws BiffException, IOException {
		configFile = configFileIn;
		wb = Workbook.getWorkbook(configFileIn);
		masterSchedule = wb.getSheet("Master Schedule");
		supplyList = wb.getSheet("Supply List");
		skills = wb.getSheet("Skills");
		tallySheet = wb.getSheet("Tally");
		absenceSchedule = wb.getSheet("Week X");
	}
	
	public ArrayList<Teacher> getTeachers() {
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
			next = 0;
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
					courses.add(new Course(masterSchedule.getCell(column + i ,row + next).getContents(), period ,teacher));
				}
				
				for(Course course : courses){
					teacher.addCourse(course);
				}
				next = next + 1;
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
			/*
			for(Teacher teacher : teachers){
				System.out.println(teacher.NAME + " Skill:" + teacher.skills);
				for(Course course : teacher.courses){
					System.out.println(course.courseTitle + " " + course.period);
				}
				System.out.println();
			}
			*/
			return teachers;
	}

	public void resetMonthlyTally()  throws BiffException, IOException, RowsExceededException, WriteException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		tallySheetWritable = wbWritable.getSheet("Tally");	
		
		int column = 2;
		int startingRow = 2;
		int next = 0;
		
		while (! tallySheetWritable.getCell(column , startingRow + next).getContents().equals("")) {
			System.out.println("Entered the while loop");
			
			WritableCell cell;
			Number resetTally = new Number(column, (startingRow + next), 0);
			cell = (WritableCell) resetTally;
			
			tallySheetWritable.addCell(cell);	
			next++;
		}
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
		wb.close();
	}
	
	public void copySheet() {
		
	}

	public void resetWeeklyTally() {
		
	}
	
	public ArrayList<Teacher> getSpareList(Period period, ArrayList<Teacher> teachers) {
		
		ArrayList<Teacher> spareTeachers = new ArrayList<Teacher>();
		
		for(Teacher teacher : teachers ){
			for(Course course : teacher.courses){
				if(course.period.equals(period) && course.courseTitle.equals("Spare")){
					//System.out.println("Period: " + course.courseTitle + " Teacher:" + teacher.NAME);
					spareTeachers.add(teacher);
				}
			}
		}	
		return spareTeachers;
	}
			
	
}
