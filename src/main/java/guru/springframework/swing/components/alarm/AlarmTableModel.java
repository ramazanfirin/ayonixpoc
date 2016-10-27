package guru.springframework.swing.components.alarm;
 
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import guru.springframework.domain.Alarm;
import guru.springframework.swing.util.ConverterUtil;
 
public class AlarmTableModel extends AbstractTableModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final List<Alarm> alarmList;
     
    private final String[] columnNames = new String[] {
            "Id","Date", "Name", "Surname","Mugshor", "Image","Score","Camera"
    };
    private final Class[] columnClass = new Class[] {
        Integer.class,String.class ,String.class, String.class, Icon.class,Icon.class,Float.class,String.class
    };
 
    public AlarmTableModel(List<Alarm> employeeList)
    {
        this.alarmList = employeeList;
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
        return alarmList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Alarm row = alarmList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            //return ConverterUtil.convertDate(row.getInsertDate());
        	return row.getInsertDate().toString();
        }
        else if(2 == columnIndex) {
            return row.getPerson().getName();
        }
        else if(3 == columnIndex) {
            return row.getPerson().getSurname();
        }
        
        else if(4 == columnIndex) {
        	try {
				return ConverterUtil.readImageIIcon(row.getAlarmImage());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
        }
        
        else if(5 == columnIndex) {
        	try {
				return ConverterUtil.readImageIIcon(row.getPerson().getMugshot());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
        }
        
        else if(6 == columnIndex) {
            return row.getScore();
        }
        
        else if(7 == columnIndex) {
            //return row.getIpCamera().getName();
        	return row.getCameraName();
        }
        return null;
    }
}