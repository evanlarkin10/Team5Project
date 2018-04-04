import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;

import jxl.*;
import jxl.write.*;
import jxl.format.CellFormat;
import jxl.format.Colour;
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
	private WritableSheet skillSheetWritable;
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
		int nameColumn;
		int roomColumn;
		int row;
		int next;
		
			nameColumn = masterSchedule.findCell("Teacher's Name").getColumn();
			roomColumn = masterSchedule.findCell("Room Number").getColumn();
			row = masterSchedule.findCell("Teacher's Name").getRow();
			
			//Create Teacher Objects from the spreadsheet
			next = 1;
			while (! masterSchedule.getCell(nameColumn ,row + next).getContents().equals("")){
				teachers.add(new Teacher(masterSchedule.getCell(nameColumn ,row + next).getContents(),
						masterSchedule.getCell(roomColumn, row + next).getContents()));
				
				int currentWeeklyTally = Integer.parseInt(tallySheet.getCell(1, row + next).getContents());
				int currentMonthlyTally = Integer.parseInt(tallySheet.getCell(2, row + next).getContents());

				int currentWeeklyMax = Integer.parseInt(tallySheet.getCell(3, row + next).getContents());
				int currentMonthlyMax = Integer.parseInt(tallySheet.getCell(4, row + next).getContents());
				System.out.print(currentWeeklyTally + " " + currentWeeklyMax + " " + currentMonthlyTally + " " + currentMonthlyMax);
				if ((currentWeeklyTally < currentWeeklyMax) && (currentMonthlyTally < currentMonthlyMax)) {
					teachers.get(next - 1).setIsUnderLimit();
				}
				
				next = next + 1;
			}
		
			//Add courses to teachers starting with the first teacher
			nameColumn = masterSchedule.findCell(teachers.get(0).NAME).getColumn();
			row = masterSchedule.findCell(teachers.get(0).NAME).getRow();
			next=2;
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

					courses.add(new Course(masterSchedule.getCell(nameColumn + i +2 ,next).getContents(), period ,teacher));

				}
				
				for(Course course : courses){
					teacher.addCourse(course);
				}
				next = next + 1;
			}
			
		//getAbsence for teacher
		getAbsences(teachers);
		
		//Add skills to teacher 
			ArrayList<ArrayList<String>> skillList = getSkillList();
			int column = masterSchedule.findCell("Teachable Skill").getColumn();
			row = masterSchedule.findCell("Teachable Skill").getRow() + 1;
			
			for(Teacher teacher : teachers){
				String teacherSkill = masterSchedule.getCell(column ,row + teachers.indexOf(teacher)).getContents() ;
				if(teacherSkill == "") {
					teacher.assignSkill(skillList);
					try {
						this.writeSkills(teacher.skills, 2, row + teachers.indexOf(teacher));
					}catch(Exception e) {
						System.out.println("Error writing in skills");
					}
				}
				else {
					teacher.addSkill(teacherSkill);
				}

			}
			//Print out teachers , courses, and Period they teach
			

			
			//Print out teachers , courses, and Period they teach

			for(Teacher teacher : teachers){
				System.out.println(teacher.NAME + " Skill:" + teacher.skills);
				for(Course course : teacher.courses){
					System.out.println(course.courseTitle + " " + course.period);
				}
				System.out.println();
			}
			
			return teachers;
	}
	

	public void resetMonthlyTally()  throws BiffException, IOException, RowsExceededException, WriteException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		tallySheetWritable = wbWritable.getSheet("Tally");
		
		int column = 2;
		int startingRow = 2;
		int next = 0;
		
		while (! tallySheetWritable.getCell(column , startingRow + next).getContents().equals("")) {
			
			WritableCell cell;
			CellFormat cf = (tallySheet.getCell(column, (startingRow + next))).getCellFormat();
			Number resetTally = new Number(column, (startingRow + next), 0);
			cell = (WritableCell) resetTally;
			cell.setCellFormat(cf);
			tallySheetWritable.addCell(cell);
			next++;
		}
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	}

	public void resetWeeklyTally()  throws BiffException, IOException, RowsExceededException, WriteException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		tallySheetWritable = wbWritable.getSheet("Tally");	
		
		int column = 1;
		int startingRow = 2;
		int next = 0;
		
		while (!(tallySheetWritable.getCell(column, (startingRow + next)).getContents()).equals("")) {			
			WritableCell cell;
			CellFormat cf = (tallySheet.getCell(column, (startingRow + next))).getCellFormat();
			Number resetTally = new Number(column, (startingRow + next), 0);
			cell = (WritableCell) resetTally;
			cell.setCellFormat(cf);
			tallySheetWritable.addCell(cell);	
			next++;
		}
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	}
	
	public void incrementTally(String teacherName)  throws BiffException, IOException, RowsExceededException, WriteException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		tallySheetWritable = wbWritable.getSheet("Tally");	

		int row = tallySheetWritable.findCell(teacherName).getRow();
		int currentWeeklyTally = Integer.parseInt(tallySheetWritable.getCell(1, row).getContents());
		int currentMonthlyTally = Integer.parseInt(tallySheetWritable.getCell(2, row).getContents());
				
		WritableCell weeklyCell;
		WritableCell monthlyCell;
		
		CellFormat cf = (tallySheet.getCell(1, row)).getCellFormat();
		
		Number incrementedWeeklyTally = new Number(1, row, currentWeeklyTally + 1);
		Number incrementedMonthlyTally = new Number(2, row, currentMonthlyTally + 1);
		
		weeklyCell = (WritableCell) incrementedWeeklyTally;
		monthlyCell = (WritableCell) incrementedMonthlyTally;
		
		weeklyCell.setCellFormat(cf);
		monthlyCell.setCellFormat(cf);
		tallySheetWritable.addCell(weeklyCell);
		tallySheetWritable.addCell(monthlyCell);
		
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	}
	
	public void writeSkills(String skills, int col, int row)  throws BiffException, IOException, RowsExceededException, WriteException{
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		skillSheetWritable = wbWritable.getSheet("Master Schedule");	
		skillSheetWritable.addCell(new Label(col, row, skills));		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	
	}
	private ArrayList<ArrayList<String>> getSkillList(){
		Sheet schedule = masterSchedule;
		Sheet skillList=skills;
		ArrayList<ArrayList<String>> skillNames = new ArrayList<ArrayList<String>>();
		int row = 2;
		int next = 0;
		int skillNameCol = skillList.findCell("Skill Name").getColumn();
		int skillIDCol = skillList.findCell("Skill Identifiers").getColumn();
		while (! skillList.getCell(skillNameCol, row+next).getContents().equals("")){
			skillNames.add(new ArrayList<String>());
			skillNames.get(next).add(skillList.getCell(skillNameCol ,row + next).getContents());
			//Add each ID to the array list following the skill name
			String skillIDs = skillList.getCell(skillIDCol ,row + next).getContents();
			ArrayList<String> IDList = new ArrayList<String>(Arrays.asList(skillIDs.split(",")));
			for(String id: IDList) {
				skillNames.get(next).add(id);
			}
			next = next + 1;
		}
		return skillNames;
	}
	public ArrayList<Teacher> getSpareList(Period period, ArrayList<Teacher> teachers) {
		
		ArrayList<Teacher> spareTeachers = new ArrayList<Teacher>();
		
		for(Teacher teacher : teachers ){
			for(Course course : teacher.courses){
				if(course.period.equals(period) && course.courseTitle.equals("Spare")
						&& (teacher.getAvailability(period))){
					//System.out.println("Period: " + course.courseTitle + " Teacher:" + teacher.NAME);
					spareTeachers.add(teacher);
				}
			}
		}	
		return spareTeachers;
	}
	
	//creates a list of Absent teachers by period
	public ArrayList<Teacher> getAbsencesByPeriod(Period period , ArrayList<Teacher> teachers){
		ArrayList<Teacher> absentTeachers = new ArrayList<Teacher>();
		
		if(period.equals(Period.Period1)){
			 for(Teacher teacher : teachers){
				 if((!teacher.isAvailableP1)){
					 absentTeachers.add(teacher);
				 }				 
			 }
		}else if(period.equals(Period.Period2)){
			for(Teacher teacher : teachers){
				 if((!teacher.isAvailableP2)){
					 absentTeachers.add(teacher);
				 }
			 }
		}else if(period.equals(Period.Period3A)){
			for(Teacher teacher : teachers){
				 if((!teacher.isAvailableP3A)){
					 absentTeachers.add(teacher);
				 }
			 }
		}else if(period.equals(Period.Period3B)){
			for(Teacher teacher : teachers){
				 if((!teacher.isAvailableP3B)){
					 absentTeachers.add(teacher);
				 }
			 }
		}else{
			for(Teacher teacher : teachers){
				 if((!teacher.isAvailableP4)){
					 absentTeachers.add(teacher);
				 }
			 }
		}
		
		return absentTeachers;
	}
	
	//sets the available variables in teacher for use in the getAbsence
	private void getAbsences(ArrayList<Teacher> teachers){
		
		int column;
		int row;
		
		if(! getDayOfWeek().equals("week end")){
			
			for(Teacher teacher : teachers){
				column = absenceSchedule.findCell(getDayOfWeek()).getColumn();
				row = absenceSchedule.findCell(teacher.NAME).getRow();
				Period period;
				
				for(int i = 0; i < Period.values().length; i++ ){
					if (i == 0){
						period = Period.Period1;
					}
					else if (i == 1){
						period = Period.Period2;
					}
					else if (i == 2){
						period = Period.Period3A;
					}
					else if (i == 3){
						period = Period.Period3B;
					}
					else {
						period = Period.Period4;
					}
					String contents = absenceSchedule.getCell(column + i, row).getContents();
					if(contents.equalsIgnoreCase("x") && !(getCourseName(teacher.NAME, period).equalsIgnoreCase("Spare"))){
						switch(i) { 
						case 0:
							teacher.isAvailableP1 = false;
							break;
						case 1: 
							teacher.isAvailableP2 = false;
							break;
						case 2:
							teacher.isAvailableP3A = false;
							break;
						case 3:
							teacher.isAvailableP3B = false;
							break;
						case 4: 
							teacher.isAvailableP4 = false;
							break;	
						}
					}
				}
			}
			
		}

	}

	//returns the numeric day of the week - to be used inside assignOnCalls
	private String getDayOfWeek() {
			Calendar calendar = Calendar.getInstance();
			int dayOfWeekint = calendar.get(Calendar.DAY_OF_WEEK);
			String dayOfWeek = "week end";
			
			switch(dayOfWeekint) { 
			case 2:
				dayOfWeek = "Monday";
				break;
			case 3: 
				dayOfWeek = "Tuesday";
				break;
			case 4:
				dayOfWeek = "Wednesday";
				break;
			case 5:
				dayOfWeek = "Thursday";
				break;
			case 6: 
				dayOfWeek = "Friday";
				break;	
			case 7: 
				dayOfWeek = "week end";
				break;
			case 1: 
				dayOfWeek = "week end";
				break;
			}
			
			return dayOfWeek;
	}
	
	public void turnCellRed(Period period, String teacherName) throws WriteException, IOException, BiffException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		WritableSheet absenceScheduleWritable = wbWritable.getSheet("Week X");	
		WritableCell cell = absenceScheduleWritable.getWritableCell(0, 0);
		
		int column = absenceSchedule.findCell(getDayOfWeek()).getColumn();
		int row = absenceSchedule.findCell(teacherName).getRow();
		
		
		if (period == Period.Period1) {
			cell = absenceScheduleWritable.getWritableCell(column, row);
		}
		else if (period == Period.Period2) {
			cell = absenceScheduleWritable.getWritableCell(column + 1, row);
		}
		else if (period == Period.Period3A) {
			cell = absenceScheduleWritable.getWritableCell(column + 2, row);
		}
		else if (period == Period.Period3B) {
			cell = absenceScheduleWritable.getWritableCell(column + 3, row);
		}
		else if (period == Period.Period4) {
			cell = absenceScheduleWritable.getWritableCell(column + 4, row);
		}

		WritableCellFormat newFormat = new WritableCellFormat();
		newFormat.setBackground(Colour.RED);
		cell.setCellFormat(newFormat);
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	}
	public void turnCellGreen(Period period, String teacherName) throws WriteException, IOException, BiffException {
		wbWritable = Workbook.createWorkbook(new File("ConfigFile.xls"), wb);
		WritableSheet absenceScheduleWritable = wbWritable.getSheet("Week X");	
		WritableCell cell = absenceScheduleWritable.getWritableCell(0, 0);
		
		int column = absenceSchedule.findCell(getDayOfWeek()).getColumn();
		int row = absenceSchedule.findCell(teacherName).getRow();
		
		
		if (period == Period.Period1) {
			cell = absenceScheduleWritable.getWritableCell(column, row);
		}
		else if (period == Period.Period2) {
			cell = absenceScheduleWritable.getWritableCell(column + 1, row);
		}
		else if (period == Period.Period3A) {
			cell = absenceScheduleWritable.getWritableCell(column + 2, row);
		}
		else if (period == Period.Period3B) {
			cell = absenceScheduleWritable.getWritableCell(column + 3, row);
		}
		else if (period == Period.Period4) {
			cell = absenceScheduleWritable.getWritableCell(column + 4, row);
		}

		WritableCellFormat newFormat = new WritableCellFormat();
		newFormat.setBackground(Colour.GREEN);
		cell.setCellFormat(newFormat);
		
		wbWritable.write();
		wbWritable.close();
		
		wb = Workbook.getWorkbook(new File("ConfigFile.xls"));
	}
	
	public String getCourseName(String Teacher, Period period) {
		String course = "";
		
		int column = masterSchedule.findCell(period.name()).getColumn();
		int row = masterSchedule.findCell(Teacher).getRow();
		
		course = masterSchedule.getCell(column, row).getContents();
		return course;
	}
	
	public String getRoomNumber(String Teacher) {
		String roomNumber = "";
		
		int column = 1;
		int row = masterSchedule.findCell(Teacher).getRow();
		
		roomNumber = masterSchedule.getCell(column, row).getContents();
		return roomNumber;
	}
	
	
}