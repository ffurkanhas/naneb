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
@Table(name="Offers")
public class HeldIn {
	@Id
	@Column(name = "fk_course_code", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String fk_course_code;
	
	@Column(name="fk_class_id")
	private String fk_class_id;
	
	@Column(name = "start_at")
	private String start_at;
	
	@Column(name="end_at")
	private String end_at;
	
	@Column(name="dayofweek")
	private String dayofweek;

	@ManyToMany
	@JoinTable(name = "course_heldIn", joinColumns = { @JoinColumn(name = "fk_course_code") }, inverseJoinColumns = {
			@JoinColumn(name = "code") })
	private Set<Course> heldIn_course;
	
	@ManyToMany
	@JoinTable(name = "clasroom_heldIn", joinColumns = { @JoinColumn(name = "fk_class_id") }, inverseJoinColumns = {
			@JoinColumn(name = "class_id") })
	private Set<Classroom> heldIn_classroom;
	
	public Set<Course> getHeldIn_course() {
		return heldIn_course;
	}

	public void setHeldIn_course(Set<Course> heldIn_course) {
		this.heldIn_course = heldIn_course;
	}

	public Set<Classroom> getHeldIn_classroom() {
		return heldIn_classroom;
	}

	public void setHeldIn_classroom(Set<Classroom> heldIn_classroom) {
		this.heldIn_classroom = heldIn_classroom;
	}

	public String getFk_course_code() {
		return fk_course_code;
	}

	public void setFk_course_code(String fk_course_code) {
		this.fk_course_code = fk_course_code;
	}

	public String getFk_class_id() {
		return fk_class_id;
	}

	public void setFk_class_id(String fk_class_id) {
		this.fk_class_id = fk_class_id;
	}

	public String getStart_at() {
		return start_at;
	}

	public void setStart_at(String start_at) {
		this.start_at = start_at;
	}

	public String getEnd_at() {
		return end_at;
	}

	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}

	public String getDayofweek() {
		return dayofweek;
	}

	public void setDayofweek(String dayofweek) {
		this.dayofweek = dayofweek;
	}
	
}
*/