package guru.springframework.swing.components.mainFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class FullScreenDialog extends JDialog{
	//private Image image;
	Dimension dimension = new Dimension(640, 480);
	public InnerPanel panel;
	
	public FullScreenDialog() {
		super();
		panel = new InnerPanel();
		add(panel);
		setPreferredSize(dimension);
		setSize(dimension);
	}
	
	
	public void setImage(Image image){
		panel.setImage(image);
		panel.revalidate();
		panel.repaint();
	}


	public InnerPanel getPanel() {
		return panel;
	}


	public void setPanel(InnerPanel panel) {
		this.panel = panel;
	}
}

class InnerPanel extends JPanel{
	private Image image;
	Dimension dimension = new Dimension(400, 600);
	
	public void setImage(Image image){
		this.image = image;
	}
	
	@Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}
