package guru.springframework.swing.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class FileUtil {
	public static Boolean createDirectory(String id){
		 final Logger logger = Logger.getLogger(FileUtil.class);
		
		File file1 = new File(AyonixConstants.IMAGE_PATH+"//"+id);
		if(!file1.mkdir()){
			logger.info("directory create error.id="+id);
			return false;
		}
		
		File file3 = new File(AyonixConstants.IMAGE_PATH+"//"+id+"//alarmImages");
		if(!file3.mkdir()){
			logger.info("directory create error.id="+id);
			return false;
		}
		
//		File file2 = new File(AyonixConstants.IMAGE_PATH+"//"+id+"//"+file.getFileName());
//		if(!file2.createNewFile()){
//			addMessage("file create error");
//			return;
//		}
		
		return true;
	}
	
	public static void createFile(File file2,BufferedImage img) throws IOException{
		ImageIO.write(img, "png", file2);
	}
}
