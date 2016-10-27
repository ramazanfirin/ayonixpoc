package guru.springframework.swing.components.enrollment;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import guru.springframework.swing.components.WebcamViewerExample2;
@Component
public class EnrollmentDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	ApplicationContext context;

	EnrollmentPanel enrollmentPanel;
	
	public EnrollmentDialog(JFrame parent, String title, String message) {
		super(parent, title);
		context = ((WebcamViewerExample2)getParent()).getContext();
		enrollmentPanel = new EnrollmentPanel(context,this);
		add(enrollmentPanel);
		//setPreferredSize(new Dimension(300, 500));
		setSize(300, 500);
	}

	

	public EnrollmentDialog() {
		super();
		// TODO Auto-generated constructor stub
	}



	public static void main(String[] a) {
//		EnrollmentDialog dialog = new EnrollmentDialog(new JFrame(), "hello JCGs", "This is a JDialog example");
//		// set the size of the window
//		dialog.setSize(300, 150);
//		dialog.setVisible(true);
	}



	public EnrollmentPanel getEnrollmentPanel() {
		return enrollmentPanel;
	}



	public void setEnrollmentPanel(EnrollmentPanel enrollmentPanel) {
		this.enrollmentPanel = enrollmentPanel;
	}
}