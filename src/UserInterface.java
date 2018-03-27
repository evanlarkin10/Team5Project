import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import jxl.Workbook;
import java.io.IOException;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class UserInterface extends JFrame implements ActionListener{
	private JButton chooseFileButton;
	private JButton assignOnCallsButton;
	private JButton resetWeeklyTally;
	private JButton resetMonthlyTally;
	private JButton printFormButton;
	private JPanel buttonPanel;
	private JPanel outputPanel;
	private JLabel label;
	private File fileSelected;
	private Container contentPane;
	private ConfigWorkbook configWorkbook;
	private ConfigWorkbook workbook;
	
	public UserInterface() {
		setSize(600,350);
		resetWeeklyTally = new JButton("Reset Weekly");
		resetMonthlyTally = new JButton("Reset Monthly");
		chooseFileButton = new JButton("Select Configuration File");
		resetWeeklyTally.setSize(200, 80);
		resetMonthlyTally.setSize(200, 80);
		chooseFileButton.setSize(200, 160);
		resetMonthlyTally.addActionListener(this);
		resetWeeklyTally.addActionListener(this);
		chooseFileButton.addActionListener(this);
		buttonPanel = new JPanel();
		GridLayout layout = new GridLayout(2,0);
		buttonPanel.add(chooseFileButton);
		label = new JLabel("Welcome to the on-call assigner, select the configuration file to begin.");
		outputPanel = new JPanel();
		outputPanel.add(label);
		contentPane = getContentPane();
		contentPane.setLayout(layout);
		contentPane.add(buttonPanel);
		contentPane.add(outputPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==chooseFileButton) {
			final JFileChooser fc = new JFileChooser();
			
			FileNameExtensionFilter excel = new FileNameExtensionFilter(
				     "Excel only", "xlsx", "xls");
			fc.setFileFilter(excel);
			
			int returnVal = fc.showOpenDialog(UserInterface.this);

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
		
		if(e.getSource()==resetMonthlyTally){
				try {
					configWorkbook.resetMonthlyTally();
					resetMonthlyTally.setVisible(false);
					
				} catch (BiffException | WriteException | IOException e1) {
					System.out.println("There's been an issue resetting the Monthly Tally");
					e1.printStackTrace();
				}		
		}

		if(e.getSource()==resetWeeklyTally) {
			try {
				configWorkbook.resetWeeklyTally();
				resetWeeklyTally.setVisible(false);
				
			} catch (BiffException | WriteException | IOException e1) {
				System.out.println("There's been an issue resetting the Weekly Tally");
				e1.printStackTrace();
			}
		}
		
		if(e.getSource()==assignOnCallsButton) {
			try {
				assignOnCalls();
			} catch (BiffException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String []args) throws Exception{
		new UserInterface().setVisible(true);
		
		
	}
	
	public void initialize(File configFile) throws IOException, BiffException {
		
		GridLayout layout = new GridLayout(2,0);
		JPanel resetPanel = new JPanel();
		assignOnCallsButton = new JButton("Assign On Calls");
		assignOnCallsButton.addActionListener(this);
		resetPanel.setLayout(layout);
		resetPanel.add(resetWeeklyTally);
		resetPanel.add(resetMonthlyTally);
		buttonPanel.add(resetPanel);
		printFormButton=new JButton("Print On-Call Forms");
		buttonPanel.add(printFormButton);
		buttonPanel.remove(chooseFileButton);
		buttonPanel.add(assignOnCallsButton,0);
		contentPane.remove(buttonPanel);
		contentPane.add(buttonPanel,0);
		label.setText("Workbook Found");
		fileSelected = configFile;
		
		//Sets up workbook
		workbook = new ConfigWorkbook(fileSelected);
		configWorkbook = workbook;
		
	}
	public void assignOnCalls() throws IOException, BiffException{
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();

		teachers = workbook.getTeachers();
		//Available On-Callers by period {Note it doesnt check for absents yet}
		ArrayList<Teacher> onCallersP1 = workbook.getSpareList(Period.Period1, teachers);
		ArrayList<Teacher> onCallersP2 = workbook.getSpareList(Period.Period2, teachers);
		ArrayList<Teacher> onCallersP3A = workbook.getSpareList(Period.Period3A, teachers);
		ArrayList<Teacher> onCallersP3B = workbook.getSpareList(Period.Period3B, teachers);
		ArrayList<Teacher> onCallersP4 = workbook.getSpareList(Period.Period4, teachers);
		 
		//get a list of teachers by absent period
		ArrayList<Teacher> absentP1 = workbook.getAbsencesByPeriod(Period.Period1, teachers);
		ArrayList<Teacher> absentP2 = workbook.getAbsencesByPeriod(Period.Period2, teachers);
		ArrayList<Teacher> absentP3A = workbook.getAbsencesByPeriod(Period.Period3A, teachers);
		ArrayList<Teacher> absentP3B = workbook.getAbsencesByPeriod(Period.Period3B, teachers);
		ArrayList<Teacher> absentP4 = workbook.getAbsencesByPeriod(Period.Period4, teachers); 
		 
		
		
	}
	
	
}

