package guru.springframework.swing.components;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import ayonix.AynxFace;
import ayonix.FaceID;
import guru.springframework.domain.Afid;
import guru.springframework.domain.IpCamera;
import guru.springframework.domain.Person;
import guru.springframework.services.AfidService;
import guru.springframework.services.IpCameraService;
import guru.springframework.services.ProductService;
import guru.springframework.swing.components.alarm.AlarmReportPanel;
import guru.springframework.swing.components.enrollment.EnrollmentDialog;
import guru.springframework.swing.components.listener.IpCameraDiscoveryListener;
import guru.springframework.swing.components.mainFrame.AlarmLivePanel;
import guru.springframework.swing.components.mainFrame.ImagePanel;
import guru.springframework.swing.components.person.PersonListPanel;
import guru.springframework.swing.concurent.IdentifyThread;
import guru.springframework.swing.dto.FaceMatchResultDTO;
import guru.springframework.swing.dto.LastFrameDTO;
import guru.springframework.swing.util.AyonixConstants;
import guru.springframework.swing.util.WebcamViewerUtil;


/**
 * Proof of concept of how to handle webcam video stream from Java
 * 
 * @author Bartosz Firyn (SarXos)
 */
@Component
public class WebcamViewerExample2 extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(WebcamViewerExample2.class);
	private static float MATCH_TRASHOLD=0.8f;
	
	@Autowired(required=true)
    ProductService productService;
	
	@Autowired(required=true)
    IpCameraService ipCameraService;
	
	@Autowired
	private ApplicationContext context;
	
	JTabbedPane mainJTabbedPane= new JTabbedPane();
	EnrollmentDialog enrollmentDialog;
	JPanel capture ;
	PersonListPanel personListPanel ;
	AlarmReportPanel alarmReportPanel;
	AlarmLivePanel alarmPanel = null;
	
	
	
	
	private JButton startButton = new JButton("start identification");
	public Boolean startFaceIdentification= false;  
	long last;
	  
	  
	  Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	  FaceID sdk;
	  
	public WebcamViewerExample2() throws HeadlessException {
		super();
		sdk = new FaceID("C:\\Program Files (x86)\\Ayonix\\FaceID\\data\\engine");
		setLayout(new GridLayout(1,1));
		
	}
	
	
	
	


	@PostConstruct
	public void init() throws Exception{
		//run();
		System.out.println("sdsdsd");
		ipCameraService = context.getBean(IpCameraService.class);
		
		enrollmentDialog = new EnrollmentDialog(this, "Enrollment", "Envollment");
		personListPanel = new PersonListPanel(context);
		alarmReportPanel = new AlarmReportPanel(context);
		alarmPanel = new AlarmLivePanel(context);
		capture = new JPanel();
		capture.setLayout(new GridLayout(1, 1));
		
		mainJTabbedPane.addTab("Capture",capture);
		mainJTabbedPane.addTab("Person List",personListPanel);
		mainJTabbedPane.addTab("Alarm List",alarmReportPanel);
		
		
		JPanel leftPanel = new JPanel();
		   //leftPanel.setBackground(Color.red);
		   leftPanel.setLayout(new FlowLayout());
		   leftPanel.setPreferredSize(new Dimension(500, 800));
		   TitledBorder title = BorderFactory.createTitledBorder("Camera List");
		   leftPanel.setBorder(title);
		   
		   JPanel rigthPanel = new JPanel();
		   //rigthPanel.setBackground(Color.blue);
		   rigthPanel.setLayout(new BorderLayout());
		   TitledBorder title2 = BorderFactory.createTitledBorder("Alarm List");
		   rigthPanel.setBorder(title2);
		
		   ImagePanel panel = new ImagePanel("Camera 1");
		   leftPanel.add(panel);
		   
		   ImagePanel panel2 = new ImagePanel("Camera 2");
		   leftPanel.add(panel2);
		   
		   ImagePanel panel3 = new ImagePanel("Camera 3");
		   leftPanel.add(panel3);
		   
		   ImagePanel panel4 = new ImagePanel("Camera 4");
		   leftPanel.add(panel4);
		   
		   rigthPanel.add(alarmPanel);
		
		   prepareData();   
		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
	    add(mainJTabbedPane);
		capture.add(leftPanel);
		capture.add(rigthPanel);
		
		
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		
		
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		
		CaptureThread captureThread = new CaptureThread(sdk,webcam, 200l,panel,context,personMap);
		captureThread.start();
		
		CaptureThread captureThread2 = new CaptureThread(sdk,webcam, 30l,panel2,context,personMap);
		captureThread2.start();
//		
		CaptureThread captureThread3 = new CaptureThread(sdk,webcam, 30l,panel3,context,personMap);
		captureThread3.start();
		
		CaptureThread captureThread4 = new CaptureThread(sdk,webcam, 30l,panel4,context,personMap);
		captureThread4.start();
}
	
	
	
	


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new WebcamViewerExample2());
	}

	
	//******************************************
	
	public static class MyCompositeDriver extends WebcamCompositeDriver {
		public MyCompositeDriver() {
			add(new WebcamDefaultDriver());
			add(new IpCamDriver());
		}
	}
	static {
		Webcam.setDriver(new MyCompositeDriver());
	}
	
	public void setupMPEGCameraList() throws Exception{
		Collection<IpCamera> list= ipCameraService.getAll();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			IpCamera ipCamera = (IpCamera) iterator.next();
			if(ipCamera.getType().equals("MJPEG")){
				IpCamDeviceRegistry.register(ipCamera.getName(), ipCamera.getConnectionURL(), IpCamMode.PUSH,new IpCamAuth(ipCamera.getUsername(), ipCamera.getPassword()));
			}
		}
	}
	
	
	public void prepareData(){
		AfidService service = context.getBean(AfidService.class);
		List<Afid> list = service.getAll();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Afid afid = (Afid) iterator.next();
			personMap.put(afid.getAfid(), afid.getPerson());
		}
		if(list.size()>0){
			for (int i = 0; i < 1000000; i++) {
				personMap.put(list.get(0).getAfid(), list.get(0).getPerson());
			}
		}
		System.out.println(personMap.size());
	}
	//********************************************

	//******************************************
	public AlarmLivePanel getAlarmPanel() {
		return alarmPanel;
	}

	public void setAlarmPanel(AlarmLivePanel alarmPanel) {
		this.alarmPanel = alarmPanel;
	}

	public EnrollmentDialog getEnrollmentDialog() {
		return enrollmentDialog;
	}

	public void setEnrollmentDialog(EnrollmentDialog enrollmentDialog) {
		this.enrollmentDialog = enrollmentDialog;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}






	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

class CaptureThread extends Thread{
	final static Logger logger = Logger.getLogger(CaptureThread.class);
	Webcam webcam;
	private volatile boolean stop = false;
	Long duration;
	ImagePanel imagePanel;
	FaceID sdk;
	ApplicationContext context;
	
	Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	Map<byte[],LastFrameDTO> lastFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	Map<byte[],LastFrameDTO> currentFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	
		public CaptureThread(FaceID sdk,Webcam webcam,Long duration,ImagePanel imagePanel,ApplicationContext context,Map<byte[],Person> personMap) {
			super();
			this.webcam = webcam;
			this.duration = duration;
			this.imagePanel = imagePanel;
			this.webcam.open();
			this.sdk = sdk;
			this.context = context;
			this.personMap = personMap;
					
		}
	   
	   public void stopCamera() {
		   //webcam.close();
	   }

		public void run() {
			while (!stop) {
				try {
					logger.info("request arrived");
					BufferedImage bufferedImage = webcam.getImage();
					updateJPanel(bufferedImage);;
					AynxFace[] faces = WebcamViewerUtil.getFaces(sdk,bufferedImage);
					if(faces.length!=0){
						  
					
					for (int i = 0; i < faces.length; i++) {
						byte[] afidi1 = WebcamViewerUtil.getAfid(sdk,faces[i]);
          				boolean result= isPresentInLastFrame(afidi1);
						if(!result){
							new IdentifyThread(sdk, afidi1, personMap.keySet(), bufferedImage, webcam.getName(),personMap,context).start();
						}
						i++;
					 
					}
					}
					lastFramePersonMap.clear();
					lastFramePersonMap.putAll(currentFramePersonMap);
					
					
					
					Thread.sleep(duration);
					//System.out.println("bitti");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stop) {
				stopCamera();
				System.out.println("thread stopped");

			}
		}
		
		public boolean isPresentInLastFrame(byte[] afid){
			Vector<byte[]> list = new Vector<byte[]>();
			if(lastFramePersonMap.size()>0){
				list.addAll(lastFramePersonMap.keySet());
				FaceMatchResultDTO faceMatchResultDTO = WebcamViewerUtil.compare(sdk, afid, list);
	  	        if(faceMatchResultDTO.getScore()>AyonixConstants.MATCH_TRASHOLD){
	  	    	   LastFrameDTO dto = lastFramePersonMap.get(faceMatchResultDTO.getAfid());
	  	    	   if(System.currentTimeMillis()-dto.getCurrentMiliseconds()<3000){
//	  	    		 if(logger.isDebugEnabled())
	  	    			// logger.info(" önceki frame de var Isım:"+dto.getName());
	  	    			currentFramePersonMap.put(afid,dto );
	  	    		  return true;
	  	    	  }
	  	    		  
	  	      }
	  	        
			} 
			currentFramePersonMap.put(afid, new LastFrameDTO("", System.currentTimeMillis()));
			return false ;
		}
		
		public void updateJPanel(BufferedImage bufferedImage){
			// bufferedImage = drawFace(bufferedImage);
			imagePanel.setImage(bufferedImage);
			imagePanel.revalidate();
			imagePanel.repaint();
			
			if(imagePanel.getDialog().isVisible()){
				imagePanel.getDialog().setImage(bufferedImage);
				System.out.println("fullscreen ok");
			}
		}
		public BufferedImage drawFace(BufferedImage image){
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

