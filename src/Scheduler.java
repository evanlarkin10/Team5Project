import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;

public class Scheduler {
	ArrayList<Teacher> teacherList;
	ArrayList<Teacher> absenceList1;
	ArrayList<Teacher> absenceList2;
	ArrayList<Teacher> absenceList3A;
	ArrayList<Teacher> absenceList3B;
	ArrayList<Teacher> absenceList4;
	ArrayList<Teacher> spareList1;
	ArrayList<Teacher> spareList2;
	ArrayList<Teacher> spareList3A;
	ArrayList<Teacher> spareList3B;
	ArrayList<Teacher> spareList4;
	ConfigWorkbook configWB;
	OnCallWorkbook onCallBook;
	
	//Constructor
	public Scheduler(ConfigWorkbook wbIn) throws BiffException, WriteException, IOException{
		onCallBook = new OnCallWorkbook();
		configWB = wbIn;
		teacherList = configWB.getTeachers();
		absenceList1 = configWB.getAbsencesByPeriod(Period.Period1, teacherList);
		absenceList2 = configWB.getAbsencesByPeriod(Period.Period2, teacherList);
		absenceList3A = configWB.getAbsencesByPeriod(Period.Period3A, teacherList);
		absenceList3B = configWB.getAbsencesByPeriod(Period.Period3B, teacherList);
		absenceList4 = configWB.getAbsencesByPeriod(Period.Period4, teacherList);
		spareList1 = configWB.getSpareList(Period.Period1, teacherList);
		spareList2 = configWB.getSpareList(Period.Period2, teacherList);
		spareList3A = configWB.getSpareList(Period.Period3A, teacherList);
		spareList3B = configWB.getSpareList(Period.Period3B, teacherList);
		spareList4 = configWB.getSpareList(Period.Period4, teacherList);
	}
	
	public String assignOnCallsP1() throws WriteException, BiffException, IOException {
		String report = "";
		Iterator<Teacher> itr = spareList1.iterator();
		for(Teacher absentee: absenceList1) {
			boolean found = false;
			Teacher potentialSub;
			if(itr.hasNext()) {
				//Loop until one is found that is under the board limit
				while(!found) {
					if(itr.hasNext()) {
						potentialSub = itr.next();
						if(potentialSub.getIsUnderLimit()) {
							report += getOnCallRecord(Period.Period1, absentee, potentialSub) + "\n";
							configWB.turnCellGreen(Period.Period1, absentee.NAME);
							//configWB.incrementTally(potentialSub.NAME);
							found=true;
						}
					}
				}
				
			}
			else {
				report += "No on caller found for: " + Period.Period1 + " " + absentee.NAME+ "\n";
				configWB.turnCellRed(Period.Period1, absentee.NAME);
			}
		}
		return report;
	}
	public String assignOnCallsP2() throws WriteException, BiffException, IOException {
		String report = "";
		Iterator<Teacher> itr = spareList2.iterator();
		for(Teacher absentee: absenceList2) {
			boolean found = false;
			Teacher potentialSub;
			if(itr.hasNext()) {
				//Loop until one is found that is under the board limit
				while(!found) {
					if(itr.hasNext()) {
						potentialSub = itr.next();
						if(potentialSub.getIsUnderLimit()) {
							report += getOnCallRecord(Period.Period2, absentee, potentialSub) + "\n";
							configWB.turnCellGreen(Period.Period2, absentee.NAME);
							//configWB.incrementTally(potentialSub.NAME);
							found=true;
						}
					}
				}
				
			}
			else {
				report += "No on caller found for: " + Period.Period2 + " " + absentee.NAME+ "\n";
				configWB.turnCellRed(Period.Period2, absentee.NAME);
			}
		}
		return report;
	}
	public String assignOnCallsP3A() throws WriteException, BiffException, IOException {
		String report = "";
		Iterator<Teacher> itr = spareList3A.iterator();
		for(Teacher absentee: absenceList3A) {
			boolean found = false;
			Teacher potentialSub;
			if(itr.hasNext()) {
				//Loop until one is found that is under the board limit
				while(!found) {
					if(itr.hasNext()) {
						potentialSub = itr.next();
						if(potentialSub.getIsUnderLimit()) {
							report += getOnCallRecord(Period.Period3A, absentee, potentialSub) + "\n";
							configWB.turnCellGreen(Period.Period3A, absentee.NAME);
							//configWB.incrementTally(potentialSub.NAME);
							found=true;
						}
					}
				}
				
			}
			else {
				report += "No on caller found for: " + Period.Period3A + " " + absentee.NAME+ "\n";
				configWB.turnCellRed(Period.Period3A, absentee.NAME);
			}
		}
		return report;
	}
	public String assignOnCallsP3B() throws WriteException, BiffException, IOException {
		String report = "";
		Iterator<Teacher> itr = spareList3B.iterator();
		for(Teacher absentee: absenceList3B) {
			boolean found = false;
			Teacher potentialSub;
			if(itr.hasNext()) {
				//Loop until one is found that is under the board limit
				while(!found) {
					if(itr.hasNext()) {
						potentialSub = itr.next();
						if(potentialSub.getIsUnderLimit()) {
							report += getOnCallRecord(Period.Period3B, absentee, potentialSub) + "\n";
							configWB.turnCellGreen(Period.Period3B, absentee.NAME);
							//configWB.incrementTally(potentialSub.NAME);
							found=true;
						}
					}
				}
				
			}
			else {
				report += "No on caller found for: " + Period.Period3B + " " + absentee.NAME+ "\n";
				configWB.turnCellRed(Period.Period3B, absentee.NAME);
			}
		}
		return report;
	}
	public String assignOnCallsP4() throws WriteException, BiffException, IOException {
		String report = "";
		Iterator<Teacher> itr = spareList4.iterator();
		for(Teacher absentee: absenceList4) {
			boolean found = false;
			Teacher potentialSub;
			if(itr.hasNext()) {
				//Loop until one is found that is under the board limit
				while(!found) {
					if(itr.hasNext()) {
						potentialSub = itr.next();
						if(potentialSub.getIsUnderLimit()) {
							report += getOnCallRecord(Period.Period4, absentee, potentialSub) + "\n";
							configWB.turnCellGreen(Period.Period4, absentee.NAME);
							//configWB.incrementTally(potentialSub.NAME);
							found=true;
						}
					}
				}
				
			}
			else {
				report += "No on caller found for: " + Period.Period4 + " " + absentee.NAME+ "\n";
				configWB.turnCellRed(Period.Period4, absentee.NAME);
			}
		}
		return report;
	}
	
	public String getOnCallRecord(Period period, Teacher teacher, Teacher sub) throws RowsExceededException, WriteException, IOException, BiffException {
		String record = "";
		record = period + "\t\tAbsentee:" + teacher.NAME + "\t\t Sub:" + sub.NAME+ "\n";
		String course = configWB.getCourseName(teacher.NAME, period);
		String room = configWB.getRoomNumber(teacher.NAME);
		
		configWB.incrementTally(sub.NAME);
		onCallBook.addCoverageToOverview(teacher.NAME, sub.NAME, course, period.toString(), room);
		onCallBook.newOnCallSheet(teacher.NAME, sub.NAME, course, period.toString(), room);
		return record;
	}
	
}

