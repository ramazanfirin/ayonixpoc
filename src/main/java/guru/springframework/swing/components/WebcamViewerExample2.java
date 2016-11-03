package guru.springframework.swing.components;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

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
import guru.springframework.swing.components.listener.IpCameraListener;
import guru.springframework.swing.components.person.PersonListPanel;


/**
 * Proof of concept of how to handle webcam video stream from Java
 * 
 * @author Bartosz Firyn (SarXos)
 */
@Component
public class WebcamViewerExample2 extends JFrame implements Runnable, UncaughtExceptionHandler, ItemListener {

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
	JPanel capture = new JPanel();
	
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	private AlarmPanel alarmPanel = null;
	private JButton startButton = new JButton("start identification");
	FaceID sdk;

	public Boolean startFaceIdentification= false;  
	  long last;
	  Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	  PersonListPanel personListPanel ;
	  AlarmReportPanel alarmReportPanel;
	  
	public WebcamViewerExample2() throws HeadlessException {
		super();
		sdk = new FaceID("C:\\Program Files (x86)\\Ayonix\\FaceID\\data\\engine");
	}
	
	public static class MyCompositeDriver extends WebcamCompositeDriver {
		public MyCompositeDriver() {
			add(new WebcamDefaultDriver());
			add(new IpCamDriver());
		}
	}
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

//	private void createMenuBar() {
//
//        JMenuBar menubar = new JMenuBar();
//        ImageIcon icon = new ImageIcon("exit.png");
//
//        JMenu file = new JMenu("File");
//        file.setMnemonic(KeyEvent.VK_F);
//
//        JMenuItem eMenuItem = new JMenuItem("Exit", icon);
//        eMenuItem.setMnemonic(KeyEvent.VK_E);
//        eMenuItem.setToolTipText("Exit application");
//        eMenuItem.addActionListener((ActionEvent event) -> {
//            System.exit(0);
//        });
//        
//        JMenuItem enrollmentMenu = new JMenuItem("Enrollmenr", icon);
//        enrollmentMenu.addActionListener((ActionEvent event) -> {
//        	enrollmentDialog.setSize(300, 500);
//        	enrollmentDialog.setVisible(true);
//        	System.out.println(context);
//            
//        });
//        
//        file.add(eMenuItem);
//        file.add(enrollmentMenu);
//        
//        menubar.add(file);
//
//        setJMenuBar(menubar);
//    }
	

	@PostConstruct
	public void init() throws Exception{
		
		setupMPEGCameraList();
		run();
		System.out.println("sdsdsd");
		ipCameraService = context.getBean(IpCameraService.class);
		enrollmentDialog = new EnrollmentDialog(this, "Enrollment", "Envollment");
		personListPanel = new PersonListPanel(context);
		
		
		mainJTabbedPane.addTab("Person List",personListPanel);
		alarmReportPanel = new AlarmReportPanel(context);
		mainJTabbedPane.addTab("Alarm List",alarmReportPanel);
		
		//createMenuBar();
		prepareData();
		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
		
		
		Thread t = new Thread() {

			@Override
			public void run() {
				panel.start();
			}
		};
		t.setName("example-starter");
		t.setDaemon(false);
		t.setUncaughtExceptionHandler(this);
		t.start();
	}
	
	@Override
	public void run() {
		last = new Date().getTime();
		Webcam.addDiscoveryListener(new IpCameraDiscoveryListener());

		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		capture.setLayout(new BorderLayout());
		mainJTabbedPane.addTab("Capture",capture);
//		mainJTabbedPane.addTab("Enrollment",);
		add(mainJTabbedPane, BorderLayout.NORTH);
//		mainJTabbedPane.addTab("Person List",personListPanel);
		
		picker = new WebcamPicker();
		picker.addItemListener(this);
		webcam = picker.getSelectedWebcam();
		//webcam  = (Webcam)picker.getItemAt(1);
		
		startButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		        startFaceIdentification=!startFaceIdentification;
		        if(startFaceIdentification){
		        	IpCameraListener cameraListener = new IpCameraListener(sdk, personMap, context);
		        	webcam.addWebcamListener(cameraListener);
		        	webcam.setImageTransformer(cameraListener);
		        	startButton.setText("stop identificaiton");
		        }else{
		        	WebcamListener as[] = webcam.getWebcamListeners();
		        	for (int i = 0; i < as.length; i++) {
		        		webcam.removeWebcamListener(as[i]);
		        		webcam.setImageTransformer(null);
					}
		        	startButton.setText("start identificaiton");
		        }
		        System.out.println("startFaceIdentification:"+startFaceIdentification);
		    }
		});
		
		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		//webcam.setViewSize(WebcamResolution.VGA.getSize());
		panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);
		panel.setFPSLimit(5d);
		panel.setFPSLimited(true);
		panel.setImageSizeDisplayed(true);
		
		alarmPanel = new AlarmPanel();
		JScrollPane scroll = new JScrollPane(alarmPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel ass= new JPanel();
		ass.add(startButton,BorderLayout.NORTH);
		ass.add(alarmPanel,BorderLayout.CENTER);
		
		capture.add(picker, BorderLayout.NORTH);
		capture.add(panel, BorderLayout.CENTER);
		capture.add(alarmPanel,BorderLayout.EAST);
		capture.add(startButton,BorderLayout.SOUTH);
		//capture.add(ass,BorderLayout.SOUTH);
		pack();
		setVisible(true);
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
		SwingUtilities.invokeLater(new WebcamViewerExample2());
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

				capture.remove(panel);

				//webcam.removeWebcamListener(this);
				WebcamListener as[] = webcam.getWebcamListeners();
	        	for (int i = 0; i < as.length; i++) {
	        		webcam.removeWebcamListener(as[i]);
				}
				webcam.close();

				webcam = (Webcam) e.getItem();
				//webcam.setViewSize(WebcamResolution.SXGA.getSize());
				//webcam.addWebcamListener(this);
				IpCameraListener cameraListener = new IpCameraListener(sdk, personMap, context);
				webcam.addWebcamListener(cameraListener);
				webcam.setImageTransformer(cameraListener);
				
				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);
				panel.setFPSDisplayed(true);
				panel.setFPSLimit(5d);
				panel.setFPSLimited(true);
				panel.setImageSizeDisplayed(true);

				capture.add(panel, BorderLayout.CENTER);
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

	public AlarmPanel getAlarmPanel() {
		return alarmPanel;
	}

	public void setAlarmPanel(AlarmPanel alarmPanel) {
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
}
