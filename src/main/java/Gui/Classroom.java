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
@Table(name = "Classroom")
public class Classroom {
	@Id
	@Column(name = "class_id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String class_id;
	
	@Column(name = "building")
	private String building;
	
	@Column(name = "floor")
	private int floor;
/*
	@ManyToMany
	@JoinTable(name = "clasroom_heldIn", joinColumns = { @JoinColumn(name = "class_id") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_class_id") })
	private Set<HeldIn> classroom_heldIn;
	
	public Set<HeldIn> getClassroom_heldIn() {
		return classroom_heldIn;
	}

	public void setClassroom_heldIn(Set<HeldIn> classroom_heldIn) {
		this.classroom_heldIn = classroom_heldIn;
	}
*/
	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}
	
}
