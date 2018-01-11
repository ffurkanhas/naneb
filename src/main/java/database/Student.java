package database;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Student")
public class Student {
	@Id
	@Column(name = "student_id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int student_id;
	
	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "m_init")
	private String m_init;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name ="dept")
	private String dept;

	@ManyToMany
	@JoinTable(name = "takes", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = {
			@JoinColumn(name = "code") })
	private Set<Course> takenCourses;
	/*
	
	public Set<Takes> getStudent_takes() {
		return student_takes;
	}

	public void setStudent_takes(Set<Takes> student_takes) {
		this.student_takes = student_takes;
	}
*/

	public Set<Course> getTakenCourses() {
		return takenCourses;
	}

	public void setTakenCourses(Set<Course> takenCourses) {
		this.takenCourses = takenCourses;
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
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
