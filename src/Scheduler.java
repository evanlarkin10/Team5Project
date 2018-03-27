import java.util.Calendar;
import java.io.File;

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
	Config Workbook configWB;
	
	//Constructor
	public Scheduler(ConfigWorkbook wbIn){
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
	
	public void assignOnCalls() {
		
	}
	
	
}

