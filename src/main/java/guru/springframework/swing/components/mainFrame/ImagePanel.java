package guru.springframework.swing.components.mainFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ImagePanel extends JPanel {
	/**
	 *
	 * 
	 */
	
	 Dimension dimension = new Dimension(300, 300);
	
	private static final long serialVersionUID = 1L;
	private Image image;
	JButton fullScreen;
	FullScreenDialog dialog = new FullScreenDialog();;
	
	 public ImagePanel(String titleString) {
		super();
		setBackground(Color.red);
		//setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(dimension);
		TitledBorder title;
		title = BorderFactory.createTitledBorder(titleString);
		setBorder(title);
		setLayout(new BorderLayout());
		fullScreen = new JButton("FullScreen");
		add(fullScreen, BorderLayout.SOUTH);
		
		fullScreen.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    // display/center the jdialog when the button is pressed
			  e.getSource();
		  
		   // d.setLocationRelativeTo(this.);
		    dialog.setVisible(true);
		  }
		});
	}

	@Override
	    protected void paintComponent(Graphics grphcs) {
	        super.paintComponent(grphcs);
	        Graphics2D g2d = (Graphics2D) grphcs;
	        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	        
	       
	    }

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public FullScreenDialog getDialog() {
		return dialog;
	}

	public void setDialog(FullScreenDialog dialog) {
		this.dialog = dialog;
	}
}
