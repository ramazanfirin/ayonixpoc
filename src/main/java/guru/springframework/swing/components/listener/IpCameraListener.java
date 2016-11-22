package guru.springframework.swing.components.listener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;

import ayonix.AynxFace;
import ayonix.FaceID;
import guru.springframework.domain.Person;
import guru.springframework.swing.concurent.IdentifyThread;
import guru.springframework.swing.dto.FaceMatchResultDTO;
import guru.springframework.swing.dto.LastFrameDTO;
import guru.springframework.swing.util.AyonixConstants;
import guru.springframework.swing.util.WebcamViewerUtil;

public class IpCameraListener implements WebcamListener,WebcamImageTransformer{

	private static final JHGrayFilter GRAY = new JHGrayFilter();
	FaceID sdk ;
	final static Logger logger = Logger.getLogger(IpCameraListener.class);
	
	Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	Map<byte[],LastFrameDTO> lastFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	Map<byte[],LastFrameDTO> currentFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	
	AynxFace[] faces;
	
	ApplicationContext context;
	//String webcamName;
	public IpCameraListener(FaceID sdk,Map<byte[],Person> personMap,ApplicationContext context) {
		super();
		this.sdk=sdk;
		this.personMap = personMap;
		this.context = context;
	    //this.webcamName = webcamName;
	}

	@Override
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open:"+we.getSource().getName());
		
	}

	@Override
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed:"+we.getSource().getName());
		
	}

	@Override
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed:"+we.getSource().getName());
		
	}

	@Override
	public void webcamImageObtained(WebcamEvent we) {
//		logger.info("request arrived");
//		currentFramePersonMap.clear();
//		
//		try {
//			AynxFace[] faces = WebcamViewerUtil.getFaces(sdk,we.getImage());
//			if(faces.length==0){
//				  return;
//			}
//		
//		
//			for (int i = 0; i < faces.length; i++) {
//				 byte[] afidi1 = WebcamViewerUtil.getAfid(sdk,faces[i]);
//
//				boolean result= isPresentInLastFrame(afidi1);
//				 if(!result){
//			new IdentifyThread(sdk, afidi1, personMap.keySet(), we.getImage(), we.getSource().getName(),personMap,context).start();
//				 }
//				 i++;
//				 //logger.info(Thread.currentThread().getName()+" i="+i);
//				 
//			}
//			lastFramePersonMap.clear();
//			lastFramePersonMap.putAll(currentFramePersonMap);
////			clearLastFrame(currentFrameAfidList);
//			
//		
//		   }catch (Exception e) {
//				logger.error(e,e);
//			}
		

		
		
	}

	
	public boolean isPresentInLastFrame(byte[] afid){
		Vector<byte[]> list = new Vector<byte[]>();
		if(lastFramePersonMap.size()>0){
			list.addAll(lastFramePersonMap.keySet());
			FaceMatchResultDTO faceMatchResultDTO = WebcamViewerUtil.compare(sdk, afid, list);
  	        if(faceMatchResultDTO.getScore()>AyonixConstants.MATCH_TRASHOLD){
  	    	   LastFrameDTO dto = lastFramePersonMap.get(faceMatchResultDTO.getAfid());
  	    	   if(System.currentTimeMillis()-dto.getCurrentMiliseconds()<3000){
//  	    		 if(logger.isDebugEnabled())
  	    			// logger.info(" önceki frame de var Isım:"+dto.getName());
  	    			currentFramePersonMap.put(afid,dto );
  	    		  return true;
  	    	  }
  	    		  
  	      }
  	        
		} 
		currentFramePersonMap.put(afid, new LastFrameDTO("", System.currentTimeMillis()));
		return false ;
	}

	@Override
	public BufferedImage transform(BufferedImage image) {
		logger.info("transform arrived");
		
		return processFrame(image);
//		return GRAY.filter(image, null);
	}
	
	public BufferedImage processFrame(BufferedImage image){
		
			try {
				AynxFace[] faces = WebcamViewerUtil.getFaces(sdk,image);
				for (int i = 0; i < faces.length; i++) {
					Graphics2D g2d = image.createGraphics();
				    
					   // Draw on the buffered image
					   g2d.setColor(Color.red);
					   g2d.draw(new Rectangle(faces[0].getLocation()));
					   g2d.dispose();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return image;
	}
}
