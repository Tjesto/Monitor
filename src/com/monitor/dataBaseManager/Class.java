package com.monitor.dataBaseManager;

import java.sql.Date;

public class Class {
	private int id;
	private String name;
	private String type;
	private String begin;
	private String end;
	private String educator;

	public Class() {
	}

	public Class(String name, String type, String begin, String end,
			String educator) {
		this.name = name;
		this.type = type;
		this.begin = begin;
		this.end = end;
		this.educator = educator;
	}

	public Class(int id, String name, String type, String begin, String end,
			String educator) {
		this.setId(id);
		this.name = name;
		this.type = type;
		this.begin = begin;
		this.end = end;
		this.educator = educator;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public void setEnd(int end) {
		this.end = Integer.toString(end);
	}
	public void setBegin(int begin) {
		this.begin = Integer.toString(begin);
	}

	public void setEnd(Date end) {
		this.end = end.toString();
	}

	public void setBegin(Date begin) {
		this.begin = begin.toString();
	}

	public String getEducator() {
		return educator;
	}

	public void setEducator(String educator) {
		this.educator = educator;
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
