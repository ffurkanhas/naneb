package Gui;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Course")
public class Course {
	@Id
	@Column(name = "code", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String code;
	
	@Column(name = "course_name")
	private String course_name;

	/*
	@ManyToMany
	@JoinTable(name = "course_takes", joinColumns = { @JoinColumn(name = "code") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_course_code") })
	private Set<Takes> course_takes;
	

	@ManyToMany
	@JoinTable(name = "course_heldIn", joinColumns = { @JoinColumn(name = "code") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_course_code") })
	private Set<HeldIn> course_heldIn;
	*/
	@ManyToMany
	@JoinTable(name = "offers", joinColumns = { @JoinColumn(name = "code") }, inverseJoinColumns = {
			@JoinColumn(name = "as_id") })
	private Set<AcademicStaff> academics;
/*
	public Set<Takes> getCourse_takes() {
		return course_takes;
	}

	public void setCourse_takes(Set<Takes> course_takes) {
		this.course_takes = course_takes;
	}

	public Set<HeldIn> getCourse_heldIn() {
		return course_heldIn;
	}

	public void setCourse_heldIn(Set<HeldIn> course_heldIn) {
		this.course_heldIn = course_heldIn;
	}
*/
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
}
