package guru.springframework.swing.components.alarm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import guru.springframework.domain.Alarm;
import guru.springframework.services.AlarmService;
import guru.springframework.swing.util.ConverterUtil;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class AlarmReportPanel extends JPanel{
	private JTable table;
	public List<Alarm> alarmList = new ArrayList<Alarm>();
	
	@Autowired(required=true)
	public AlarmService alarmService;
	
	public AlarmReportPanel(ApplicationContext context) {
		alarmService = context.getBean(AlarmService.class);
		setLayout(new MigLayout("", "[][][grow]", "[][][][grow]"));
		
		JLabel lblDate = new JLabel("Start Date");
		add(lblDate, "cell 0 0");
		
		JLabel lblNewLabel = new JLabel("End Date");
		add(lblNewLabel, "cell 0 1");
		
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePickerStart = new JDatePickerImpl(datePanel);
		add(datePickerStart,"cell 2 0");
		
		UtilDateModel model2 = new UtilDateModel();
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
		JDatePickerImpl datePickerStart2 = new JDatePickerImpl(datePanel2);
		add(datePickerStart2,"cell 2 1");
		
		AlarmTableModel modelTable = new AlarmTableModel(alarmList);
		JLabel errorLabel = new JLabel("");
		add(errorLabel, "cell 2 2");
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date startDate = (Date) datePickerStart.getModel().getValue();
				Date endDate = (Date) datePickerStart2.getModel().getValue();
				if(startDate==null || endDate==null){;
					errorLabel.setText(" Tarih secimi yapiniz");
					return;
				}
				
				alarmList = alarmService.search(ConverterUtil.modifyDate(startDate, 0, 0, 0), ConverterUtil.modifyDate(endDate, 23, 59, 59));
				AlarmTableModel model = new AlarmTableModel(alarmList);
				table.setModel(model);
			}
		});
		add(btnNewButton, "flowx,cell 0 2");
		table = new JTable(modelTable);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setRowHeight(100);
		add(scrollPane, "cell 2 3,grow");
		
		
		
		
		//table = new JTable();
		
		
		
		
	}

}
