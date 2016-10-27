package guru.springframework.swing.components.person;
 
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import guru.springframework.domain.Person;
 
public class PersonTableModel extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final List<Person> personList;
     
    private final String[] columnNames = new String[] {
            "Id", "Name", "Surname", "Mugshot"	
    };
    private final Class[] columnClass = new Class[] {
        Integer.class, String.class, String.class, Icon.class
    };
 
    public PersonTableModel(List<Person> employeeList)
    {
        this.personList = employeeList;
    }
     
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount()
    {
        return personList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Person row = personList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getName();
        }
        else if(2 == columnIndex) {
            return row.getSurname();
        }
        else if(3 == columnIndex) {
        	BufferedImage img=null;
			try {
				img = ImageIO.read(new File(row.getMugshot()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
//			JLabel label = new JLabel(imageIcon);
			return imageIcon;
        	
        	
     }
        
        return null;
    }
    
   

    
}