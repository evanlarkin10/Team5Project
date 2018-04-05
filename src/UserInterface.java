import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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
		setSize(600,600);
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
		outputPanel.setLayout(new BorderLayout());
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(buttonPanel);
		contentPane.add(outputPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==chooseFileButton) {
			final JFileChooser fc = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			/*If want to open file chooser to directory before /src
			String[] directory = workingDirectory.toString().split("/");
			String directoryToFile = "";
			for(int i=0;i<directory.length-1;i++) {
				directoryToFile += directory[i] + "/";
			}
			
			workingDirectory = new File(directoryToFile);
			*/
			fc.setCurrentDirectory(workingDirectory);
			FileNameExtensionFilter excel = new FileNameExtensionFilter(
				     "Excel only", "xlsx", "xls");
			fc.setFileFilter(excel);
			
			int returnVal = fc.showOpenDialog(UserInterface.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
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
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==printFormButton) {
			JOptionPane.showMessageDialog(null, "On Call forms were generated and placed in the OnCallWorkbook excel file");
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
		
		workbook = new ConfigWorkbook(fileSelected);
		configWorkbook = workbook;
		
	}
	public void assignOnCalls() throws IOException, BiffException, WriteException{
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();

		teachers = workbook.getTeachers();
		Scheduler scheduleOnCalls = new Scheduler(workbook);
		
		String p1Report = scheduleOnCalls.assignOnCallsP1();
		String p2Report = scheduleOnCalls.assignOnCallsP2();
		String p3AReport = scheduleOnCalls.assignOnCallsP3A();
		String p3BReport = scheduleOnCalls.assignOnCallsP3B();
		String p4Report = scheduleOnCalls.assignOnCallsP4();
		
		String report = p1Report + "\n" + p2Report+ "\n" +p3AReport+ "\n" +p3BReport+ "\n" +p4Report;
		JTextArea reportArea = new JTextArea(report);
		JPanel reportPanel = new JPanel();
		reportPanel.setLayout(new BorderLayout());
		reportPanel.add(reportArea);
		reportPanel.setBorder(new EmptyBorder(10,10,10,10));
		reportPanel.setSize(100, 100);
		JScrollPane scrollPane = new JScrollPane(reportPanel);
		outputPanel.removeAll();
		outputPanel.setLayout(new GridLayout(2,2));
		outputPanel.add(scrollPane,0,0);
		contentPane.remove(outputPanel);
		contentPane.add(outputPanel);
		setVisible(true);
		
	}
	
	
}

