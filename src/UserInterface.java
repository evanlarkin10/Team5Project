import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import jxl.Workbook;
import java.io.IOException;
import jxl.read.biff.BiffException;

public class UserInterface extends JFrame implements ActionListener{
	private JButton assignButton;
	private JButton resetWeeklyTally;
	private JButton resetMonthlyTally;
	private JButton printFormButton;
	private JPanel buttonPanel;
	private JPanel outputPanel;
	private JLabel label;
	private Container contentPane;
	private ConfigWorkbook configWorkbook;
	
	public UserInterface() {
		setSize(600,350);
		resetWeeklyTally = new JButton("Reset Weekly");
		resetMonthlyTally = new JButton("Reset Monthly");
		assignButton = new JButton("Assign On-Calls");
		resetWeeklyTally.setSize(200, 80);
		resetMonthlyTally.setSize(200, 80);
		assignButton.setSize(200, 160);
		resetMonthlyTally.addActionListener(this);
		resetWeeklyTally.addActionListener(this);
		assignButton.addActionListener(this);
		buttonPanel = new JPanel();
		GridLayout layout = new GridLayout(2,0);
		buttonPanel.add(assignButton);
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
		if(e.getSource()==assignButton) {
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
		
		if(e.getSource()==resetMonthlyTally) {
			configWorkbook.resetMonthlyTally();
		}

		if(e.getSource()==resetWeeklyTally) {
			configWorkbook.resetWeeklyTally();
		}
	       
		}
	
	public static void main(String []args) throws Exception{
		new UserInterface().setVisible(true);
		
		
	}
	
	public void initialize(File configFile) throws IOException, BiffException {
		
		GridLayout layout = new GridLayout(2,0);
		JPanel resetPanel = new JPanel();
		resetPanel.setLayout(layout);
		resetPanel.add(resetWeeklyTally);
		resetPanel.add(resetMonthlyTally);
		buttonPanel.add(resetPanel);
		printFormButton=new JButton("Print On-Call Forms");
		buttonPanel.add(printFormButton);
		contentPane.remove(buttonPanel);
		contentPane.add(buttonPanel,0);
		label.setText("Workbook Found");
		
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		int numberOfPeriods = 5;
		int column;
		int row;
		int next;
		
		//Sets up workbook
		File src = configFile;
		ConfigWorkbook workbook = new ConfigWorkbook(configFile);
		configWorkbook = workbook;
		workbook.getTeachers();
		
	}
	
}
