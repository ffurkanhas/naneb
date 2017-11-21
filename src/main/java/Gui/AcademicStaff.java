package Gui;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "academic_staff")
public class AcademicStaff {
	@Id
	@Column(name = "as_id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int as_id;
	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "m_init")
	private String m_init;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name ="dept")
	private String dept;

	@ManyToMany
	@JoinTable(name = "offers", joinColumns = { @JoinColumn(name = "as_id") }, inverseJoinColumns = {
			@JoinColumn(name = "code") })
	private Set<Course> courses;
	
	public int getAs_id() {
		return as_id;
	}

	public void setAs_id(int as_id) {
		this.as_id = as_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getM_init() {
		return m_init;
	}

	public void setM_init(String m_init) {
		this.m_init = m_init;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
}
