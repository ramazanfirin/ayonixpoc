package guru.springframework.swing.components;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigDecimal;
import java.util.Collection;
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

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

import ayonix.AynxFace;
import ayonix.AynxImage;
import ayonix.FaceID;
import guru.springframework.domain.Afid;
import guru.springframework.domain.Alarm;
import guru.springframework.domain.IpCamera;
import guru.springframework.domain.Person;
import guru.springframework.domain.Product;
import guru.springframework.services.AfidService;
import guru.springframework.services.AlarmService;
import guru.springframework.services.IpCameraService;
import guru.springframework.services.ProductService;
import guru.springframework.swing.components.alarm.AlarmReportPanel;
import guru.springframework.swing.components.enrollment.EnrollmentDialog;
import guru.springframework.swing.components.person.PersonListPanel;
import guru.springframework.swing.concurent.CallableTask;
import guru.springframework.swing.concurent.ResultDto;
import guru.springframework.swing.dto.FaceMatchResultDTO;
import guru.springframework.swing.dto.LastFrameDTO;
import guru.springframework.swing.util.AyonixConstants;
import guru.springframework.swing.util.WebcamViewerUtil;


/**
 * Proof of concept of how to handle webcam video stream from Java
 * 
 * @author Bartosz Firyn (SarXos)
 */
//@Component
public class WebcamViewerExampleCalisiyor extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(WebcamViewerExampleCalisiyor.class);
	private static float MATCH_TRASHOLD=0.8f;
	
	@Autowired(required=true)
    ProductService productService;
	
	@Autowired(required=true)
    IpCameraService ipCameraService;
	
	@Autowired
	private ApplicationContext context;
	
	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}




	JTabbedPane mainJTabbedPane= new JTabbedPane();
	EnrollmentDialog enrollmentDialog;
	JPanel capture = new JPanel();
	
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	private AlarmPanel alarmPanel = null;
	private JButton startButton = new JButton("start identification");
	  FaceID sdk;

	public Boolean startFaceIdentification= false;  
	  
//	  Vector<byte[]> afids = new Vector<byte[]>();
	  
	  
	  long last;
	  
	  Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	  
//	  Vector<byte[]> lastFrameAfids = new Vector<byte[]>();
	  Map<byte[],LastFrameDTO> lastFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	  
//	  List<byte[]> currentFrameAfidList = new ArrayList<byte[]>();
	  Map<byte[],LastFrameDTO> currentFramePersonMap = new HashMap<byte[],LastFrameDTO>();
	  
//	public static ApplicationContext getContext(){
//		ApplicationContext applicationContext = context;
//		return applicationContext;
//	}
	  
	  PersonListPanel personListPanel ;
	  AlarmReportPanel alarmReportPanel;
	  
	public WebcamViewerExampleCalisiyor() throws HeadlessException {
		super();
		sdk = new FaceID("C:\\Program Files (x86)\\Ayonix\\FaceID\\data\\engine");
		//loadImageDirectory();
		
		run();
	}
	
	public static class MyCompositeDriver extends WebcamCompositeDriver {

		public MyCompositeDriver() {
			add(new WebcamDefaultDriver());
			add(new IpCamDriver());
		}
	}

	// register custom composite driver
	static {
		Webcam.setDriver(new MyCompositeDriver());
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

	private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        
        JMenuItem enrollmentMenu = new JMenuItem("Enrollmenr", icon);
        enrollmentMenu.addActionListener((ActionEvent event) -> {
        	enrollmentDialog.setSize(300, 500);
        	enrollmentDialog.setVisible(true);
        	System.out.println(context);
            
        });
        
        file.add(eMenuItem);
        file.add(enrollmentMenu);
        
        menubar.add(file);

        setJMenuBar(menubar);
    }
	
//	public void loadImageDirectory(){
//		String directoryName="C:\\Users\\ETR00529\\Desktop\\ayoniximage";
//		File directory = new File(directoryName);
//		File[] fList = directory.listFiles();
//        for (File file : fList){
//            try {
//				System.out.println(file.getName());
//				AynxImage img = sdk.LoadImage(file.getAbsolutePath());
//				AynxFace[] faces = sdk.DetectFaces(img);
//				sdk.PreprocessFace(faces[0]);
//				byte[] afidi1 = sdk.CreateAfid(faces[0]);
//				afids.add(afidi1);
//				personMap.put(afidi1,file.getName());
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.out.println("filename error "+file.getName());
//			}
//        }
//        addDummyData(new File(directoryName+"\\Lee.jpg"));
//	}

//	public void addDummyData(File file ){
//		AynxImage img = sdk.LoadImage(file.getAbsolutePath());
//		AynxFace[] faces = sdk.DetectFaces(img);
//		sdk.PreprocessFace(faces[0]);
//		byte[] afidi1 = sdk.CreateAfid(faces[0]);
//		for (int i = 0; i < 1000000; i++) {
//			afids.add(afidi1);
//			personMap.put(afidi1,file.getName());
//		}
//	}
	@PostConstruct
	public void init(){
		System.out.println("sdsdsd");
		ipCameraService = context.getBean(IpCameraService.class);
		enrollmentDialog = new EnrollmentDialog(this, "Enrollment", "Envollment");
		personListPanel = new PersonListPanel(context);
		
		
		mainJTabbedPane.addTab("Person List",personListPanel);
		alarmReportPanel = new AlarmReportPanel(context);
		mainJTabbedPane.addTab("Alarm List",alarmReportPanel);
		
		createMenuBar();
		prepareData();
		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
		try {
			setupMPEGCameraList();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
	public void run() {
		last = new Date().getTime();
//		sdk = new FaceID("C:\\Program Files (x86)\\Ayonix\\FaceID\\data\\engine");
//		String file1="C:\\Users\\ETR00529\\Desktop\\ramazan.png";
//		AynxImage img = sdk.LoadImage(file1);
//		AynxFace[] faces = sdk.DetectFaces(img);
//		sdk.PreprocessFace(faces[0]);
//		byte[] afidi1 = sdk.CreateAfid(faces[0]);
//		afids.add(afidi1);
		
		
		
		Webcam.addDiscoveryListener(this);

		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		
		
		capture.setLayout(new BorderLayout());
		mainJTabbedPane.addTab("Capture",capture);
//		mainJTabbedPane.addTab("Enrollment",);
		add(mainJTabbedPane, BorderLayout.NORTH);
//		mainJTabbedPane.addTab("Person List",personListPanel);
		
		
		addWindowListener(this);

		picker = new WebcamPicker();
		picker.addItemListener(this);

		webcam = picker.getSelectedWebcam();
		
		startButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		        startFaceIdentification=!startFaceIdentification;
		        if(startFaceIdentification)
		        	startButton.setText("stop identificaiton");
		        else
		        	startButton.setText("start identificaiton");
		        System.out.println("startFaceIdentification:"+startFaceIdentification);
		    }
		});
		
		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.addWebcamListener(WebcamViewerExampleCalisiyor.this);
		
		
		panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);
		panel.setFPSLimit(5d);
		panel.setFPSLimited(true);
		panel.setImageSizeDisplayed(true);
		
		
		alarmPanel = new AlarmPanel();
		
		JButton showDialogButton = new JButton("Save");
	
		showDialogButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    // display/center the jdialog when the button is pressed
			  Product shirt = new Product();
		        shirt.setDescription("ramazan");
		        shirt.setPrice(new BigDecimal("18.95"));
		        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
		        shirt.setProductId("235268845711068308");
				productService.saveProduct(shirt);
				System.out.println("kaydedildi");
		  }
		});
		
		capture.add(picker, BorderLayout.NORTH);
		capture.add(panel, BorderLayout.CENTER);
		capture.add(alarmPanel,BorderLayout.EAST);
		capture.add(startButton,BorderLayout.SOUTH);
//		add(showDialogButton,BorderLayout.SOUTH);
		
		pack();
		
		
		setVisible(true);

		Thread t = new Thread() {

			@Override
			public void run() {
				//webcam.setViewSize(WebcamResolution.VGA.getSize());
				panel.start();
			}
		};
		t.setName("example-starter");
		t.setDaemon(false);
		t.setUncaughtExceptionHandler(this);
		t.start();
	

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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new WebcamViewerExampleCalisiyor());
	}

	@Override
	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
	}

	@Override
	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed");
		 sdk.Close();
	}

	@Override
	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
	}
	
	public boolean checkDuration(){
		if(System.currentTimeMillis()-last<1000){
			System.out.println("zaman dolmamısi");
			last = System.currentTimeMillis();
			return false;
		}
			return true;
	}
	
	public boolean isPresentInLastFrame(byte[] afid){
		Vector<byte[]> list = new Vector<byte[]>();
		if(lastFramePersonMap.size()>0){
			list.addAll(lastFramePersonMap.keySet());
			FaceMatchResultDTO faceMatchResultDTO = WebcamViewerUtil.compare(sdk, afid, list);
  	        if(faceMatchResultDTO.getScore()>MATCH_TRASHOLD){
  	    	   LastFrameDTO dto = lastFramePersonMap.get(faceMatchResultDTO.getAfid());
  	    	   if(System.currentTimeMillis()-dto.getCurrentMiliseconds()<3000){
  	    		// if(logger.isDebugEnabled())
  	    			// logger.debug(Thread.currentThread().getName()+" önceki frame de var Isım:"+dto.getName());
  	    			currentFramePersonMap.put(afid,dto );
  	    		  return true;
  	    	  }
  	    		  
  	      }
  	        
		} 
		currentFramePersonMap.put(afid, new LastFrameDTO("", System.currentTimeMillis()));
		return false ;
	}

	public void createAlarm(FaceMatchResultDTO faceMatchResultDTO,BufferedImage image,String cameraName) throws IOException{
		String name="UNKNOWN";
		float score = 0f;
		if(faceMatchResultDTO.getScore()>MATCH_TRASHOLD){ 
			Person person = personMap.get(faceMatchResultDTO.getAfid());  
			score = faceMatchResultDTO.getScore();
			alarmPanel.addImage(image,name,score,cameraName);
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
		}
		
	    
	 
          
	}
	
	public void clearLastFrame(List<byte[]> list){
//      lastFrameAfids.clear();
//	  lastFrameAfids.addAll(list);
	  
	  lastFramePersonMap.clear();
	  for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		byte[] bs = (byte[]) iterator.next();
		lastFramePersonMap.put(bs, new LastFrameDTO("", System.currentTimeMillis()));
	}
	  
	}
	
	public AynxFace[] getFaces(BufferedImage image) throws IOException{
		byte[] imageInByte = WebcamViewerUtil.convertToByteArray(image);
        AynxImage img = sdk.LoadImage(imageInByte);
		AynxFace[] faces = sdk.DetectFaces(img);
		return faces;
	}
	
	public byte[] getAfid(AynxFace face){
		 sdk.PreprocessFace(face);
		 byte[] afidi1 = sdk.CreateAfid(face);
		 return afidi1;
	}
	
	@Override
	public void webcamImageObtained(WebcamEvent we) {
		//logger.info(Thread.currentThread().getName());
		if(!startFaceIdentification)
			return;
//		if(!checkDuration())
//			return;
		
		logger.info("request arrived");
//		currentFrameAfidList.clear();
		currentFramePersonMap.clear();
		
		try {
			AynxFace[] faces = getFaces(we.getImage());
			if(faces.length==0){
//				  if(logger.isDebugEnabled())
//					  logger.debug("No face detected. camera name:"+we.getSource().getName());
				  return;
			}
		
			//logger.info("face detection tamamlandi");
			for (int i = 0; i < faces.length; i++) {
				 byte[] afidi1 = getAfid(faces[i]);
				
//				 logger.info(Thread.currentThread().getName()+" cagri basliyor "+ faces.length);
				 
				boolean result= isPresentInLastFrame(afidi1);
				 //boolean result = true;
//				 logger.info(Thread.currentThread().getName()+" result:"+result+" i:"+i);
				 
				 if(!result){
					 
//					 logger.info(Thread.currentThread().getName()+" önceki frame de yok");

			   
				new IdentifyThread(sdk, afidi1, personMap.keySet(), we.getImage(), we.getSource().getName()).start();
				 }
				 i++;
				 //logger.info(Thread.currentThread().getName()+" i="+i);
				 
			}
			lastFramePersonMap.clear();
			lastFramePersonMap.putAll(currentFramePersonMap);
//			clearLastFrame(currentFrameAfidList);
			
		
		   }catch (Exception e) {
				logger.error(e,e);
			}
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		webcam.close();
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
//		System.out.println("webcam viewer resumed");
//		panel.resume();
	}

	@Override
	public void windowIconified(WindowEvent e) {
//		System.out.println("webcam viewer paused");
//		panel.pause();
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(String.format("Exception in thread %s", t.getName()));
		e.printStackTrace();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != webcam) {
			if (webcam != null) {

				panel.stop();

				remove(panel);

				webcam.removeWebcamListener(this);
				webcam.close();

				webcam = (Webcam) e.getItem();
				//webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);
				panel.setFPSDisplayed(true);

				add(panel, BorderLayout.CENTER);
				pack();

				Thread t = new Thread() {

					@Override
					public void run() {
						panel.start();
					}
				};
				t.setName("example-stoper");
				t.setDaemon(true);
				t.setUncaughtExceptionHandler(this);
				t.start();
			}
		}
	}

	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.addItem(event.getWebcam());
		}
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.removeItem(event.getWebcam());
		}
	}
	
	
	
	
	class IdentifyThread extends Thread {
		
		FaceID sdk;
		byte[] query;
		Vector<byte[]> afids = new Vector<byte[]>();
		BufferedImage image;
		String cameraNAme;

		int parallelizm=4;
		ExecutorService executorService = Executors.newFixedThreadPool(parallelizm);
		
		
		public IdentifyThread(FaceID sdk, byte[] query, Set<byte[]> afids,BufferedImage image,String cameraNAme) {
			super();
			this.sdk = sdk;
			this.query = query;
			this.afids.addAll(afids);
			this.image = image;
			this.cameraNAme = cameraNAme;
		}

		public void run(){
			try {
				long s = System.currentTimeMillis();
				
				Set<Callable<ResultDto>> callables =getTaskList(afids,query);
				List<Future<ResultDto>> futures = executorService.invokeAll(callables);
				//FaceMatchResultDTO faceMatchResultDTO = WebcamViewerUtil.compare(sdk, query, afids);
				FaceMatchResultDTO faceMatchResultDTO = sort(futures); 
				createAlarm(faceMatchResultDTO, image,cameraNAme);
				 long d = System.currentTimeMillis();
				 logger.info("thread duration="+(d-s));
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
	}




	public EnrollmentDialog getEnrollmentDialog() {
		return enrollmentDialog;
	}

	public void setEnrollmentDialog(EnrollmentDialog enrollmentDialog) {
		this.enrollmentDialog = enrollmentDialog;
	}

	public AlarmPanel getAlarmPanel() {
		return alarmPanel;
	}

	public void setAlarmPanel(AlarmPanel alarmPanel) {
		this.alarmPanel = alarmPanel;
	}
}
