package guru.springframework.swing.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import guru.springframework.domain.Person;

public class ConverterUtil {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static Person convertToPerson(String name,String surname,String category){
		return null;
	}
	
	public static String convertDate(Date date){
		System.out.println(date);
		return dateFormat.format(date);
	}
	
	public static ImageIcon readImageIIcon(String file) throws Exception{
		BufferedImage img= ImageIO.read(new File(file));
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		return imageIcon;
	}
	
	public static Date modifyDate(Date date,int hour,int minute,int seconds){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, seconds);
		
		return c.getTime();
	}
	public static void main(String[] args) {
		System.out.println(convertDate(new Date()));
	}
	
//	public void loadImageDirectory(){
//	String directoryName="C:\\Users\\ETR00529\\Desktop\\ayoniximage";
//	File directory = new File(directoryName);
//	File[] fList = directory.listFiles();
//    for (File file : fList){
//        try {
//			System.out.println(file.getName());
//			AynxImage img = sdk.LoadImage(file.getAbsolutePath());
//			AynxFace[] faces = sdk.DetectFaces(img);
//			sdk.PreprocessFace(faces[0]);
//			byte[] afidi1 = sdk.CreateAfid(faces[0]);
//			afids.add(afidi1);
//			personMap.put(afidi1,file.getName());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("filename error "+file.getName());
//		}
//    }
//    addDummyData(new File(directoryName+"\\Lee.jpg"));
//}

//public void addDummyData(File file ){
//	AynxImage img = sdk.LoadImage(file.getAbsolutePath());
//	AynxFace[] faces = sdk.DetectFaces(img);
//	sdk.PreprocessFace(faces[0]);
//	byte[] afidi1 = sdk.CreateAfid(faces[0]);
//	for (int i = 0; i < 1000000; i++) {
//		afids.add(afidi1);
//		personMap.put(afidi1,file.getName());
//	}
//}
}
