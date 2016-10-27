package guru.springframework.swing.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import ayonix.AynxFace;
import ayonix.AynxImage;
import ayonix.FaceID;
import guru.springframework.swing.dto.FaceMatchResultDTO;

public class WebcamViewerUtil {
	
	final static Logger logger = Logger.getLogger(WebcamViewerUtil.class);
	public static byte[] convertToByteArray(BufferedImage image) throws IOException{
		long currentMilisecondsStart = System.currentTimeMillis();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( image, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		
//		if(logger.isDebugEnabled())
//			logger.debug("duration="+(System.currentTimeMillis()-currentMilisecondsStart));

		return imageInByte;
	}
	
public static FaceMatchResultDTO compare(FaceID sdk, byte[] query,Vector<byte[]> afids){
	float[] scores = new float[afids.size()];
    int[] indexes = new int[afids.size()];
    sdk.MatchAfids(query, afids, scores, indexes);
    int index = sort(scores);
    float score  = scores[index];
    byte[] matchedAfid = afids.get(index);
    
    FaceMatchResultDTO dto = new FaceMatchResultDTO(matchedAfid, score);
    return dto;
    
}

public static byte[] createAfid(FaceID sdk,String path) throws Exception{
	AynxImage img = sdk.LoadImage(path);
	AynxFace[] faces = sdk.DetectFaces(img);
	if(faces.length>1)
		throw new Exception("more then 1 face");
	AynxFace face = faces[0];
	sdk.PreprocessFace(face);
	byte[] query = sdk.CreateAfid(face);
	return query;
}

public static AynxFace[] getFaces(FaceID sdk,BufferedImage image) throws IOException{
	byte[] imageInByte = WebcamViewerUtil.convertToByteArray(image);
    AynxImage img = sdk.LoadImage(imageInByte);
	AynxFace[] faces = sdk.DetectFaces(img);
	return faces;
}

public static byte[] getAfid(FaceID sdk,AynxFace face){
	 sdk.PreprocessFace(face);
	 byte[] afidi1 = sdk.CreateAfid(face);
	 return afidi1;
}

public static int sort(float[] scores){
	
	float value = Float.MIN_VALUE;
	int index = Integer.MIN_VALUE;
	
	for(int i =0;i<scores.length;i++) {
	
	            if(scores[i] > value) {
	            	value = scores[i];

	            	index = i;

	            }
	
	        }

	return index;
}
}
