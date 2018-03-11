import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import jxl.Cell;
import jxl.Workbook;
import java.io.IOException;
import jxl.read.biff.BiffException;

public class ReadExcel extends JFrame implements ActionListener{
	private JButton assignButton;
	private JPanel buttonPanel;
	private JPanel outputPanel;
	private JLabel label;
	
	public ReadExcel() {
		setSize(600,350);
		assignButton = new JButton("Assign On-Calls");
		assignButton.setSize(200, 80);
		assignButton.addActionListener(this);
		buttonPanel = new JPanel();
		buttonPanel.add(assignButton);
		label = new JLabel("Welcome to the on-call assigner, select the configuration file to begin.");
		outputPanel = new JPanel();
		outputPanel.add(label);
		GridLayout layout = new GridLayout(2,0);
		Container contentPane = getContentPane();
		contentPane.setLayout(layout);
		contentPane.add(buttonPanel);
		contentPane.add(outputPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==assignButton) {
			final JFileChooser fc = new JFileChooser();
			
			FileNameExtensionFilter excel = new FileNameExtensionFilter(
				     "Excel only", "xlsx", "xls");
			fc.setFileFilter(excel);
			
			int returnVal = fc.showOpenDialog(ReadExcel.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //Path to file displayed in an alert
	        		//JOptionPane.showMessageDialog(null, fc.getSelectedFile());
	            try {
					initialize(file);
				} 
	            catch (BiffException e1){
	            		label.setText("There was an issue reading the configuration file");
	            }
	            catch (Exception e2) {
					e2.printStackTrace();
				}
	        }
		}
	        
	        
		}
	public static void main(String []args) throws Exception{
		new ReadExcel().setVisible(true);
		
		
	}
	
	public void initialize(File configFile) throws IOException, BiffException {

		
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		int numberOfPeriods = 5;
		int column;
		int row;
		int next;
		
		//Sets up workbook
		File src = configFile;
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
