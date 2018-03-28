import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;

import jxl.*;
import jxl.write.*;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import jxl.read.biff.BiffException;

public class OnCallWorkbook {
	WritableWorkbook onCallWritable;
	WritableSheet overviewSheet;
	int sheetIndex = 0;
	int rowIndex = 2;
	Workbook onCallWB;
	
	public OnCallWorkbook() throws BiffException, IOException, WriteException {
		
		onCallWritable = Workbook.createWorkbook(new File("onCallWorkbook.xls"));
		overviewSheet = onCallWritable.createSheet("Coverage Overview", sheetIndex);
		
		Label sheetTitle = new Label(0, 0, "Coverage Overview");
		
		Label nameColumn = new Label(0, 1, "Absentee");
		Label onCallColumn = new Label(1, 1, "On-Call");
		Label courseColumn = new Label(2, 1, "Course");
		Label periodColumn = new Label(3, 1, "Period");
		Label roomColumn = new Label(4, 1, "Room");
		
		
		overviewSheet.addCell(sheetTitle);
		overviewSheet.addCell(nameColumn);
		overviewSheet.addCell(courseColumn);
		overviewSheet.addCell(roomColumn);
		overviewSheet.addCell(periodColumn);
		overviewSheet.addCell(onCallColumn);
		
		for(int x = 0; x > 5; x++) {
			CellView cell= overviewSheet.getColumnView(x);
		    cell.setAutosize(true);
		    overviewSheet.setColumnView(x, cell);
		}
		
		onCallWritable.write();
		onCallWritable.close();
		
		onCallWB = Workbook.getWorkbook(new File("onCallWorkbook.xls"));
	}
	
	public void addCoverageToOverview(String absentee, String onCall, String course, String period, String room) throws RowsExceededException, WriteException, IOException, BiffException{

		onCallWritable = Workbook.createWorkbook(new File("onCallWorkbook.xls"), onCallWB);
		WritableSheet overviewSheetW = onCallWritable.getSheet("Coverage Overview");
		System.out.println(absentee);
		
		Label nameL = new Label(0, rowIndex, absentee);
		Label onCallL = new Label(1, rowIndex, onCall);
		Label courseL = new Label(2, rowIndex, course);
		Label periodL = new Label(3, rowIndex, period);
		Label roomL = new Label(4, rowIndex, room);	
		
		WritableCell nameC = (WritableCell) nameL;
		WritableCell onCallC = (WritableCell) onCallL;
		WritableCell courseC = (WritableCell) courseL;
		WritableCell periodC = (WritableCell) periodL;
		WritableCell roomC = (WritableCell) roomL;
		
		overviewSheetW.addCell(nameC);
		overviewSheetW.addCell(onCallC);
		overviewSheetW.addCell(courseC);
		overviewSheetW.addCell(periodC);
		overviewSheetW.addCell(roomC);
		
		rowIndex++;
		
		for(int x = 0; x > 5; x++) {
			CellView cell= overviewSheet.getColumnView(x);
		    cell.setAutosize(true);
		    overviewSheet.setColumnView(x, cell);
		}

		onCallWritable.write();
		onCallWritable.close();
		
		onCallWB = Workbook.getWorkbook(new File ("onCallWorkbook.xls"));
	}
}
