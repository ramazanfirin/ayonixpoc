package guru.springframework.swing.components.mainFrame;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.springframework.context.ApplicationContext;

import guru.springframework.domain.Alarm;
import guru.springframework.swing.components.alarm.AlarmNotificationDialog;
import guru.springframework.swing.components.alarm.AlarmTableModel;
import net.miginfocom.swing.MigLayout;

public class AlarmLivePanel extends JPanel implements MouseListener{
	private JTable table;
	public List<Alarm> alarmList = new ArrayList<Alarm>();
	AlarmTableModel modelTable;
	ApplicationContext context;
	
	public AlarmLivePanel(ApplicationContext context) {
		//alarmService = context.getBean(AlarmService.class);
		this.context=context;
		setLayout(new MigLayout("", "[][][grow]", "[][][][grow]"));
		AlarmTableModel modelTable = new AlarmTableModel(alarmList);
		
		table = new JTable(modelTable);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setRowHeight(100);
		add(scrollPane, "cell 0 3 3 1,grow");
		
		
		table.addMouseListener
	      (
	        new MouseAdapter()
	        {
	          public void mouseClicked(MouseEvent e)
	          {
	            if (e.getClickCount() == 2)
	            {
	            	JTable table =(JTable) e.getSource();
	            	int row = table.getSelectedRow();
	            	Alarm alarm = alarmList.get(row);
	            	System.out.println(alarm.getId()+" selected");
	            	AlarmNotificationDialog alarmNotificationDialog =context.getBean(AlarmNotificationDialog.class);
	            	alarmNotificationDialog.getAlarmNotificationPanel().prepareAlarmValue(alarm);
	            	alarmNotificationDialog.setVisible(true);
	                
	            }
	            if (e.getClickCount() == 1)
	            {
	            	System.out.println("one click");
	            }
	          }
	        }
	      );
	    }
		


	public void addAlarm(Alarm alarm){
		alarmList.add(0,alarm);	
//		table.revalidate();
//		table.repaint();
		
		AlarmTableModel model = new AlarmTableModel(alarmList);
		table.setModel(model);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount()==2){
			JTable table = (JTable)arg0.getSource();
			int row = table.getSelectedRow();
			System.out.println(row+" clicked");
					
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
