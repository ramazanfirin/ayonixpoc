package guru.springframework.swing.components.alarm;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import guru.springframework.domain.Alarm;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.ActionEvent;

public class AlarmNotificationPanel extends JPanel{
	
	JLabel nameLabel;
	JLabel surnameLabel;
	JLabel sourcaImageLabel;
	JLabel currentImageLabel;
	JLabel scoreLabel;
	JLabel dateLabel;
	
	public AlarmNotificationPanel() {
		setLayout(new MigLayout("", "[][289.00]", "[][][][][][][]"));
		
		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel, "cell 0 0");
		
		nameLabel = new JLabel("New label");
		nameLabel.setLabelFor(lblNewLabel);
		add(nameLabel, "cell 1 0");
		
		JLabel lblNewLabel_1 = new JLabel("Surname");
		add(lblNewLabel_1, "cell 0 1");
		
		surnameLabel = new JLabel("New label");
		add(surnameLabel, "cell 1 1");
		
		JLabel lblNewLabel_2 = new JLabel("Source Image");
		add(lblNewLabel_2, "cell 0 2");
		
		sourcaImageLabel = new JLabel("");
		add(sourcaImageLabel, "cell 1 2");
		
		JLabel lblNewLabel_3 = new JLabel("Current Image");
		add(lblNewLabel_3, "cell 0 3");
		
		currentImageLabel = new JLabel("");
		add(currentImageLabel, "cell 1 3");
		
		JLabel lblScore = new JLabel("Score");
		add(lblScore, "cell 0 4");
		
		scoreLabel = new JLabel("score");
		add(scoreLabel, "cell 1 4");
		
		JLabel lblDate = new JLabel("Date");
		add(lblDate, "cell 0 5");
		
		dateLabel = new JLabel("New label");
		add(dateLabel, "cell 1 5");
		

	}
	
	public void prepareAlarmValue(Alarm alarm){
		nameLabel.setText(alarm.getPerson().getName());
		surnameLabel.setText(alarm.getPerson().getSurname());
		
		//ImageIcon image = new ImageIcon(alarm.getPerson().getMugshot());
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(alarm.getPerson().getMugshot()).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
		sourcaImageLabel.setIcon(imageIcon);
		
		//ImageIcon image2 = new ImageIcon(alarm.getAlarmImage());
		ImageIcon imageIcon2 = new ImageIcon(new ImageIcon(alarm.getAlarmImage()).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
		currentImageLabel.setIcon(imageIcon2);

		scoreLabel.setText(alarm.getScore().toString());
		dateLabel.setText(alarm.getInsertDate().toString());
	}

}
