package guru.springframework.swing.components.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import guru.springframework.domain.Person;
import guru.springframework.services.PersonService;
import guru.springframework.swing.components.WebcamViewerExample2;
import guru.springframework.swing.components.renderer.ButtonEditor;
import guru.springframework.swing.components.renderer.ButtonRenderer;
import net.miginfocom.swing.MigLayout;


public class PersonListPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTable table;
	
	@Autowired(required=true)
	ApplicationContext context;
	
	@Autowired(required=true)
	public PersonService personService;
	public List<Person> personList = new ArrayList<Person>();
	
	
	public PersonListPanel(ApplicationContext context) {
		setLayout(new MigLayout("", "[][grow]", "[][][][grow][]"));
		
		personService = context.getBean(PersonService.class);
		WebcamViewerExample2 a = context.getBean(WebcamViewerExample2.class);
		a.getContext().getBean(PersonService.class);
	
		JLabel lblIsim = new JLabel("İsim");
		add(lblIsim, "cell 0 0,alignx trailing");
		
		textField = new JTextField();
		add(textField, "cell 1 0,growx");
		textField.setColumns(10);
		
		JLabel lblSoyisim = new JLabel("Soyisim");
		add(lblSoyisim, "cell 0 1,alignx trailing");
		
		textField_1 = new JTextField();
		add(textField_1, "cell 1 1,growx");
		textField_1.setColumns(10);
		
		JButton btnAra = new JButton("Ara");
		btnAra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				personList = personService.getAll();
				PersonTableModel model = new PersonTableModel(personList);
				table.setModel(model);
			}
		});
		add(btnAra, "cell 0 2");
		
		PersonTableModel model = new PersonTableModel(personList);
		
		JButton btnNewButton = new JButton("Yeni Kayıt");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WebcamViewerExample2 a = context.getBean(WebcamViewerExample2.class);
				a.getEnrollmentDialog().getEnrollmentPanel().reset();
				a.getEnrollmentDialog().setVisible(true);
			}
		});
		add(btnNewButton, "cell 1 2");
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setRowHeight(100);
		
//		table.getColumn("Update").setCellRenderer(new ButtonRenderer());
//        table.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox()));
		
		 //  Create button column
//        ButtonColumn buttonColumn = new ButtonColumn(table, 4);
		
//		table = new JTable(new DefaultTableModel(
//			new Object[][] {
//			},
//			new String[] {
//			}
//		));
//	    
//		table.setModel(model);
	    
		add(scrollPane, "cell 1 3,grow");
		
		JButton btnUpdate = new JButton("update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = table.getSelectedRow();
				System.out.println(rowIndex);
				Person person = personList.get(rowIndex);
				WebcamViewerExample2 a = context.getBean(WebcamViewerExample2.class);
				try {
					a.getEnrollmentDialog().getEnrollmentPanel().update(person);
					a.getEnrollmentDialog().setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		add(btnUpdate, "flowx,cell 1 4");
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int rowIndex = table.getSelectedRow();
				Person person = personList.get(rowIndex);
				personService.delete(person);
				personList.remove(rowIndex);
				//table.getModel().fireTableDataChanged();
				table.repaint();
			}
		});
		add(btnDelete, "cell 1 4");
	}

}
