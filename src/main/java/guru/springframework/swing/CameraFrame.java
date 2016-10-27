package guru.springframework.swing;

import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;


public class CameraFrame extends javax.swing.JFrame{

	/**
	 * 
	 */
	
	public static void main(String[] args) {
		CameraFrame  cameraFrame = new CameraFrame();
		cameraFrame.setVisible(true);
	}
	
	Webcam webcam = Webcam.getDefault();
	WebcamPanel panel = new WebcamPanel(webcam);
	
    @Autowired(required=true)
    ProductService productService;
	
	
	private static final long serialVersionUID = 1L;

	public CameraFrame() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
		//webcam.setViewSize(WebcamResolution.VGA.getSize());

//		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		
		add(panel);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		
		
		
		JButton showDialogButton = new JButton("Text Button");
		add(showDialogButton);
		showDialogButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    // display/center the jdialog when the button is pressed
			  Product shirt = new Product();
		        shirt.setDescription("ramazan");
		        shirt.setPrice(new BigDecimal("18.95"));
		        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
		        shirt.setProductId("235268845711068308");
				productService.saveProduct(shirt);
				System.out.println("kaydedildi");
		  }
		});
	}

	
}
