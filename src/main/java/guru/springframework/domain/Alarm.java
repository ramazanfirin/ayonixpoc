package guru.springframework.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Alarm")
public class Alarm {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	Person person;
	
	@ManyToOne
	IpCamera ipCamera;

	@Column
	Date insertDate;
	
	@Column
	Float score;
	
	@Column
	String alarmImage;
	
	@Column
	String className;
	
	@Column
	String lecture;
	
	@Column
	String teacher;
	
	@Column
	String cameraName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public IpCamera getIpCamera() {
		return ipCamera;
	}

	public void setIpCamera(IpCamera ipCamera) {
		this.ipCamera = ipCamera;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getAlarmImage() {
		return alarmImage;
	}

	public void setAlarmImage(String alarmImage) {
		this.alarmImage = alarmImage;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLecture() {
		return lecture;
	}

	public void setLecture(String lecture) {
		this.lecture = lecture;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	
}
