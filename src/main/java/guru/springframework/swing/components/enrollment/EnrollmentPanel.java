package guru.springframework.swing.components.enrollment;

import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ayonix.FaceID;
import guru.springframework.domain.Afid;
import guru.springframework.domain.Person;
import guru.springframework.services.AfidService;
import guru.springframework.services.PersonService;
import guru.springframework.swing.components.PersonPanel;
import guru.springframework.swing.util.AyonixConstants;
import guru.springframework.swing.util.FileUtil;
import guru.springframework.swing.util.WebcamViewerUtil;
import guru.springframework.swing.verifier.StringVerifier;
@Component
public class EnrollmentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int IMG_WIDTH = 100; 
	int IMG_HEIGHT = 100;
	
	JScrollPane scrollFrame = null;
	JDialog parent;
	
	private JLabel name= new JLabel("Name");
	private JLabel surname=new JLabel("Surname");
	
	private JTextField jsurname=new JTextField();
	private JTextField jname=new JTextField();
	private JTextField surnameText;
	private JTextField nameText;
	private JFileChooser fileChooser;
	
	BufferedImage img;
	String selectedImageFile;
	
	Person person;
	
	@Autowired(required=true)
    public PersonService personService;

	@Autowired(required=true)
    public AfidService afidService;
	
	@Autowired
	private ApplicationContext context;
	
	
	private Person currentPerson;
	
	FaceID sdk = new FaceID("C:\\Program Files (x86)\\Ayonix\\FaceID\\data\\engine");
	ImageIcon imageIcon;
	JLabel lblNewLabel_1;
	
	Boolean update;
	Boolean imageUpdateDone;
	
	public EnrollmentPanel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EnrollmentPanel(ApplicationContext context,JDialog parent) {
		super();
		this.context = context;
		personService = context.getBean(PersonService.class);
		afidService = context.getBean(AfidService.class);
		this.parent=parent;
//		productService =WebcamViewerExample2.getContext().getBean(ProductService.class);
		setLayout(new net.miginfocom.swing.MigLayout("", "[27px][86px,grow]", "[20px][][][]"));
		
		JLabel lblNewLabel = new JLabel("Name");
		add(lblNewLabel, "cell 0 0,alignx trailing,aligny center");
		lblNewLabel.setInputVerifier(new StringVerifier());
		
		nameText = new JTextField();
		add(nameText, "cell 1 0,growx");
		nameText.setColumns(10);
		
		JLabel lblSurname = new JLabel("Surname");
		add(lblSurname, "cell 0 1,alignx trailing");
		
		surnameText = new JTextField();
		add(surnameText, "cell 1 1,growx");
		surnameText.setColumns(10);
		
		JLabel lblMugshot = new JLabel("Mugshot");
		add(lblMugshot, "cell 0 2");
		
				fileChooser = new JFileChooser();
				JLabel errorLabel = new JLabel("");
				add(errorLabel, "cell 1 3");
				
				JButton btnNewButton = new JButton("Kaydet");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							if(StringUtils.isEmpty(nameText.getText()) || StringUtils.isEmpty(surnameText.getText()) || img==null)
								errorLabel.setText("Tüm alanları doldurunuz");

						
							person.setName(nameText.getText());
							person.setSurname(surnameText.getText());
							personService.save(person);
							
							if(!update){
								if(!FileUtil.createDirectory(person.getId().toString())){
									errorLabel.setText("Directory error");
									return;
								}
							}
							
							if(imageUpdateDone){
								if(update){
									person.getAfidList().clear();
									afidService.deleteAll(person.getAfidList());
									//dosyayı da sil
								}
								String path = AyonixConstants.IMAGE_PATH+"//"+person.getId()+"//"+selectedImageFile;
								FileUtil.createFile(new File(path), img);
								person.setMugshot(path);
							
								Afid afid = new Afid();
								afid.setImageLocation(path);
								afid.setAfid(WebcamViewerUtil.createAfid(sdk, path));
								person.getAfidList().add(afid);
							}
							
							
							
							personService.save(person);
							//getParent().getParent().getParent().getParent().setVisible(false);//getConta
							parent.setVisible(false);
						}  catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							errorLabel.setText(e.getMessage());
						}
//					System.out.println("productService="+productService);	
					}
					
				});
				add(btnNewButton, "cell 0 3");
				
				lblNewLabel_1 = new JLabel("");
				add(lblNewLabel_1, "flowx,cell 1 2");
				
				JButton btnSe = new JButton("Seç");
				btnSe.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(fileChooser.showOpenDialog(btnSe.getParent())== JFileChooser.APPROVE_OPTION){
							fileChooser.getSelectedFile();
							try {
								selectedImageFile = fileChooser.getSelectedFile().getName();
								img = ImageIO.read(fileChooser.getSelectedFile());
								imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
								lblNewLabel_1.setIcon(imageIcon);
								imageUpdateDone=true;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
				});
				add(btnSe, "cell 1 2");
				
				
	}
	
	public void reset(){
		person = new Person();
		nameText.setText("");
		surnameText.setText("");
		img=null;
		lblNewLabel_1.setIcon(null);
		update =false;
		imageUpdateDone = false;
	}
	
	public void update(Person person) throws IOException{
		nameText.setText(person.getName());
		surnameText.setText(person.getSurname());
		img = ImageIO.read(new File(person.getMugshot()));
		imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
		lblNewLabel_1.setIcon(imageIcon);
		update=true;
		imageUpdateDone = false;
	}
	
	
	
	public void addImage(BufferedImage image,String name,float score,String cameraName){
		//add(new ImageIcon(image));
        PersonPanel panel = new PersonPanel(image, name,score,cameraName);
        scrollFrame.add(panel);
        add(panel);
        //scrollFrame.revalidate();
        revalidate();
      
	}

	public EnrollmentPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public EnrollmentPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public EnrollmentPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public Person getCurrentPerson() {
		return currentPerson;
	}

	public void setCurrentPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
