package guru.springframework.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="Person")

public class Person implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;
	
	@Column
	private String surname;
	
	@Column
	private String mugshot;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true)
	@JoinColumn(name="PERSON_ID")
	private Collection<Afid> afidList = new LinkedHashSet<Afid>();
	
//	@ManyToOne
//	PersonCategory personCategory = new PersonCategory();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Collection<Afid> getAfidList() {
		return afidList;
	}

	public void setAfidList(Collection<Afid> afidList) {
		this.afidList = afidList;
	}

	public String getMugshot() {
		return mugshot;
	}

	public void setMugshot(String mugshot) {
		this.mugshot = mugshot;
	}

//	public PersonCategory getPersonCategory() {
//		return personCategory;
//	}
//
//	public void setPersonCategory(PersonCategory personCategory) {
//		this.personCategory = personCategory;
//	}


	
	
	
}
