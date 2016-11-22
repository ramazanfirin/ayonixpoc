package guru.springframework.swing.components;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


/**
 * Proof of concept of how to handle webcam video stream from Java
 * 
 * @author Bartosz Firyn (SarXos)
 */
//@Component
public class WebcamViewer6 extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(WebcamViewer6.class);
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
	
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;
	
	
	private JButton startButton = new JButton("start identification");
	public Boolean startFaceIdentification= false;  
	long last;
	  
	  
	  Map<byte[],Person> personMap = new HashMap<byte[],Person>();
	  FaceID sdk;
	  
	public WebcamViewer6() throws HeadlessException {
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
		   
		setTitle("Java Webcam Capture POC-Enrollment size="+personMap.size());
	
		add(leftPanel);
		add(rigthPanel);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
}
	
	
	public void run() {
		try {
			setupMPEGCameraList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		
		
		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		webcam.setViewSize(WebcamResolution.VGA.getSize());
		panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);
		panel.setFPSLimit(5d);
		panel.setFPSLimited(true);
		panel.setImageSizeDisplayed(true);
		panel.setFillArea(true);
		
		alarmPanel = new AlarmLivePanel(context);
		//JScrollPane scroll = new JScrollPane(alarmPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel ass= new JPanel();
		ass.setLayout(new FlowLayout());
		ass.add(alarmPanel);
		ass.add(panel);
		
		
		capture.add(picker, BorderLayout.NORTH);
		capture.add(ass, BorderLayout.CENTER);
		//capture.add(panel,BorderLayout.EAST);
		capture.add(startButton,BorderLayout.SOUTH);
		
		prepareData();
		
		pack();
		setVisible(true);
}
	


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new WebcamViewer6());
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
	
	
	
}
