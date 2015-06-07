package com.monitor.dataBaseManager;

public class Subject {
	private int id;
	private String name;
	private int min;
	private String notes;
	
	public Subject(){}
	public Subject(String name, int min, String notes) {
		this.name = name;
		this.min = min;
		this.notes = notes;
	}
	
	public Subject(int id, String name, int min, String notes) {
		this.id = id;
		this.name = name;
		this.min = min;
		this.notes = notes;
	}
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMin(String min) {
		this.min = Integer.parseInt(min);
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
}
