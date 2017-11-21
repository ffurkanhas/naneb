package Gui;

import javax.persistence.*;

@Entity
@Table(name = "Student_Club")
public class StudentClub {
	@Id
	@Column(name = "club_name", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String club_name;

	public String getClub_name() {
		return club_name;
	}
	/*
	@ManyToMany
	@JoinTable(name = "follows", joinColumns = { @JoinColumn(name = "club_name") }, inverseJoinColumns = {
			@JoinColumn(name = "student_id") })
	private Set<Takes> follows;
	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}*/
	public String toString() {
		return this.club_name;
	}
}
