package Gui;/*import java.util.Set;

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
@Table(name = "Takes")
public class Takes {
	@Id
	@Column(name = "fk_student_id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fk_student_id;
	
	@Column(name ="section")
	private int section;

	@Column(name="fk_course_code")
	private String fk_course_code;
	
	@ManyToMany
	@JoinTable(name = "student_takes", joinColumns = { @JoinColumn(name = "fk_student_id") }, inverseJoinColumns = {
			@JoinColumn(name = "student_id") })
	private Set<Student> takes_students;
	
	@ManyToMany
	@JoinTable(name = "course_takes", joinColumns = { @JoinColumn(name = "fk_course_code") }, inverseJoinColumns = {
			@JoinColumn(name = "code") })
	private Set<Course> takes_course;
	
	public Set<Student> getTakes_students() {
		return takes_students;
	}

	public void setTakes_students(Set<Student> takes_students) {
		this.takes_students = takes_students;
	}

	public Set<Course> getTakes_course() {
		return takes_course;
	}

	public void setTakes_course(Set<Course> takes_course) {
		this.takes_course = takes_course;
	}
	public int getFk_student_id() {
		return fk_student_id;
	}

	public void setFk_student_id(int fk_student_id) {
		this.fk_student_id = fk_student_id;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getFk_course_code() {
		return fk_course_code;
	}

	public void setFk_course_code(String fk_course_code) {
		this.fk_course_code = fk_course_code;
	}

	
}*/
