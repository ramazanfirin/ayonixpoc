package guru.springframework.swing.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class AlarmPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int IMG_WIDTH = 100; 
	int IMG_HEIGHT = 100;
	
	//JScrollPane scrollFrame = null;
	JPanel jPanel;
	 GridBagConstraints gbc = new GridBagConstraints();
	
	public AlarmPanel() {
		super();
		// TODO Auto-generated constructor stub
		//setSize(300, 300);
		//setBo
		//setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		//setBounds(400,200,380,310);
		//setLayout(new BorderLayout());
		setMinimumSize(new Dimension(300,300));
		setPreferredSize(new Dimension(600,300));
		
		//setBackground(Color.GREEN);
		jPanel = new JPanel();
		//add(jPanel);
		GridBagLayout layout = new GridBagLayout();
		jPanel.setLayout(new FlowLayout());
		jPanel.setPreferredSize(new Dimension(600,600));
		JScrollPane scrollPane=new JScrollPane(jPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,  
		   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		
		
//		scrollFrame = new JScrollPane(this,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollFrame.setVisible(true);
		//scrollFrame.setPreferredSize(new Dimension( 600,150));
		//add(scrollFrame);
		setAutoscrolls(true);
	}
	
	
	public void addImage(BufferedImage image,String name,float score,String cameraName){
		//add(new ImageIcon(image));
        PersonPanel panel = new PersonPanel(image, name,score,cameraName);
        //scrollFrame.add(panel);
        jPanel.add(panel,0);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        jPanel.add(panel,gbc);
        
        
        //scrollFrame.revalidate();
        jPanel.revalidate();
        jPanel.repaint();
        revalidate();
        repaint();
	}

	public AlarmPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public AlarmPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public AlarmPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}
	
	
}
