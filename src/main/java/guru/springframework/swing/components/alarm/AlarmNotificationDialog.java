package guru.springframework.swing.components.alarm;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import net.miginfocom.swing.MigLayout;

@Component
public class AlarmNotificationDialog extends JDialog implements ActionListener{

	JLabel nameLabel;
	JLabel surnameLabel;
	JLabel sourcaImageLabel;
	JLabel currentImageLabel;
	JLabel scoreLabel;
	JLabel dateLabel;
	
	
	AlarmNotificationPanel alarmNotificationPanel;
	
	public AlarmNotificationDialog() {
		super();
		JPanel messagePane = new JPanel();
		alarmNotificationPanel = new AlarmNotificationPanel();
		Point p = new Point(100, 100);
	    setLocation(p.x, p.y);

	    setPreferredSize(new Dimension(400, 700));
	    setSize(new Dimension(400, 600));
		getContentPane().add(messagePane);
		
		JLabel lblNewLabel = new JLabel("New label");
		messagePane.setLayout(new MigLayout("", "[396px]", "[145px][]"));
		messagePane.add(alarmNotificationPanel, "cell 0 0,alignx left,aligny top");
		
		JButton btnKapat = new JButton("Kapat");
		btnKapat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		messagePane.add(btnKapat, "cell 0 1");
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public AlarmNotificationPanel getAlarmNotificationPanel() {
		return alarmNotificationPanel;
	}

	public void setAlarmNotificationPanel(AlarmNotificationPanel alarmNotificationPanel) {
		this.alarmNotificationPanel = alarmNotificationPanel;
	}

}
