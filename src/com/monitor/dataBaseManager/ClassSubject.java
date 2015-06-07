package com.monitor.dataBaseManager;

public class ClassSubject {
	private int classId;
	private int subjectId;
	private String SubjectName;
	private int realized;
	private int month;
	private int year;

	public ClassSubject( int classId, int subjectId,int realized, int month, int year) {
	
		this.classId = classId;
		this.subjectId = subjectId;
		this.realized = realized;
		this.month = month;
		this.year = year;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getRealized() {
		return realized;
	}

	public void setRealized(int realized) {
		this.realized = realized;
	}


	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getSubjectName() {
		return SubjectName;
	}

	public void setSubjectName(String subjectName) {
		SubjectName = subjectName;
	}
}
