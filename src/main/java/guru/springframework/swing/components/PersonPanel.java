package guru.springframework.swing.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class PersonPanel extends JInternalFrame {
	
	final static Logger logger = Logger.getLogger(PersonPanel.class);

	int IMG_WIDTH = 100; 
	int IMG_HEIGHT = 100;

	JPanel imagePanel = new JPanel();
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PersonPanel(BufferedImage image, String name,Float score,String cameraName) {
		super();
		this.image = image;
		this.name = name;
		this.score = score;
		this.cameraName = cameraName;
		addPerson();
		//setVisible(true);
//		setMaximizable(true);
//		setClosable(true);
		setVisible(true);
		BoxLayout boxlayout = new BoxLayout(imagePanel, BoxLayout.Y_AXIS);

		imagePanel.setLayout(boxlayout);
		add(imagePanel);
	   
	}
	BufferedImage image;
	String name;
	Float score;
	String cameraName;
	
	
	public void addPerson(){
		imagePanel.setBackground(Color.RED);
		BufferedImage image2=null;
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		try {
			image2 = resizeImage(image,type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel l = new JLabel();
		l.setIcon(new javax.swing.ImageIcon(image2)); // NOI18N
		imagePanel.add(l,BorderLayout.NORTH);
		
		JLabel l2 = new JLabel("Name:"+name);
		imagePanel.add(l2,BorderLayout.SOUTH);
		
		JLabel l3 = new JLabel("Date:"+dateFormat.format(new Date()));
		imagePanel.add(l3,BorderLayout.SOUTH);
		
		JLabel scoreText = new JLabel("Score:"+score.toString());
		imagePanel.add(scoreText,BorderLayout.SOUTH);
		
//		JLabel cameraNameText = new JLabel("Camera:"+cameraName);
//		imagePanel.add(cameraNameText,BorderLayout.SOUTH);
		
		logger.info("Person added. name:"+name);
		
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	try {
							//setClosed(true);
		            		//Color c = Color.
		            		imagePanel.setBackground(new Color(238,238,238));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        }, 
		        7000 
		);
		
		revalidate();
	}
	
	private  BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	    }

	
	
	
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
