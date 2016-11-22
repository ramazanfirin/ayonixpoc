package guru.springframework.swing.concurent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import ayonix.FaceID;
import guru.springframework.controllers.GreetingController;
import guru.springframework.domain.Alarm;
import guru.springframework.domain.Person;
import guru.springframework.services.AlarmService;
import guru.springframework.swing.components.WebcamViewerExample2;
import guru.springframework.swing.components.alarm.AlarmNotificationDialog;
import guru.springframework.swing.components.listener.IpCameraListener;
import guru.springframework.swing.dto.FaceMatchResultDTO;
import guru.springframework.swing.util.AyonixConstants;

public	class IdentifyThread extends Thread {
	
	final static Logger logger = Logger.getLogger(IdentifyThread.class);
		
		FaceID sdk;
		byte[] query;
		Vector<byte[]> afids = new Vector<byte[]>();
		BufferedImage image;
		String cameraNAme;

		Map<byte[],Person> personMap = new HashMap<byte[],Person>();
		ApplicationContext context;
		
		int parallelizm=1;
		ExecutorService executorService = Executors.newFixedThreadPool(parallelizm);
		
		
		public IdentifyThread(FaceID sdk, byte[] query, Set<byte[]> afids,BufferedImage image,String cameraNAme,Map<byte[],Person> personMap,ApplicationContext context) {
			super();
			this.sdk = sdk;
			this.query = query;
			this.afids.addAll(afids);
			this.image = image;
			this.cameraNAme = cameraNAme;
			this.personMap = personMap;
			this.context = context;
		}

		public void run(){
			try {
				long s = System.currentTimeMillis();
				
				Set<Callable<ResultDto>> callables =getTaskList(afids,query);
				List<Future<ResultDto>> futures = executorService.invokeAll(callables);
				//FaceMatchResultDTO faceMatchResultDTO = WebcamViewerUtil.compare(sdk, query, afids);
				long d = System.currentTimeMillis();
				 logger.info("thread duration="+(d-s));
				FaceMatchResultDTO faceMatchResultDTO = sort(futures); 
				createAlarm(faceMatchResultDTO, image,cameraNAme);
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		public FaceMatchResultDTO sort(List<Future<ResultDto>> futures) throws Exception{
			float a = 0;
			Future<ResultDto> resultFuture=null;
			
			for (Iterator iterator = futures.iterator(); iterator.hasNext();) {
				Future<ResultDto> future = (Future<ResultDto>) iterator.next();
				float b =future.get().getScore();
				if(b>=a){
					a=b;
					resultFuture = future;
				}
			}
			
			FaceMatchResultDTO dto = new FaceMatchResultDTO(resultFuture.get().getAfid(), resultFuture.get().getScore());
			return dto;
		}
		
		public Set<Callable<ResultDto>> getTaskList(Vector<byte[]> database,byte[] query){
			Set<Callable<ResultDto>> callables = new HashSet<Callable<ResultDto>>();
		    int size= database.size()/parallelizm;
		    FaceID _sdk=null;
		    String engine="";
			for (int i = 0; i < (parallelizm-1); i++) {

				CallableTask task = new CallableTask(sdk,"",query,database.subList(i*size, (i+1)*size));
				callables.add(task);
			}
			
			CallableTask task = new CallableTask(sdk,engine,query,database.subList((parallelizm-1)*size, database.size()));
			callables.add(task);
			
			return  callables;
	}
		public void createAlarm(FaceMatchResultDTO faceMatchResultDTO,BufferedImage image,String cameraName) throws IOException{
			String name="UNKNOWN";
			float score = 0f;
			if(faceMatchResultDTO.getScore()>AyonixConstants.MATCH_TRASHOLD){ 
				Person person = personMap.get(faceMatchResultDTO.getAfid());  
				score = faceMatchResultDTO.getScore();
				
				Alarm alarm = new Alarm();
				alarm.setInsertDate(new Date());
				alarm.setScore(faceMatchResultDTO.getScore());
				
				String path1 = "//alarmImages//"+System.currentTimeMillis();
				String path2 = AyonixConstants.IMAGE_PATH+"//"+person.getId()+path1+".jpg";
				
				ImageIO.write(image, "png", new File(path2));
				alarm.setAlarmImage(path2);
				
				alarm.setCameraName(cameraName);
				
				alarm.setPerson(person);
				
				AlarmService service = context.getBean(AlarmService.class);
				service.save(alarm);
				
				WebcamViewerExample2 a = context.getBean(WebcamViewerExample2.class);
				a.getAlarmPanel().addAlarm(alarm);
				
				AlarmNotificationDialog ab = context.getBean(AlarmNotificationDialog.class);
				ab.getAlarmNotificationPanel().prepareAlarmValue(alarm);
				ab.setVisible(true);
				
				GreetingController controller = context.getBean(GreetingController.class);
				controller.fireGreeting(alarm);
			}
			
		    
		 
	          
		}
	}

