package com.monitor.dataBaseManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseManager {

	private static final String connectionString = "jdbc:sqlite:base.db";

	private static DataBaseManager instance = null;

	private void dropBase() {
		System.out.println("dropBase");
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			String sql = "DROP TABLE CLASS ";
			;
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = con.createStatement();
			sql = "DROP TABLE SUBJECT";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = con.createStatement();
			sql = "DROP TABLE CLASS_SUBJECTS ";
			stmt.executeUpdate(sql);
			stmt.close();

			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void init() {
		System.out.println("init");
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			String sql = "CREATE TABLE CLASS "
					+ " (ID 		   INTEGER  PRIMARY KEY, "
					+ " TEXT_ID        TEXT  	NOT NULL,"
					+ " KIND           TEXT  	NOT NULL, "
					+ " BEGIN          TEXT  	NOT NULL, "
					+ " END            TEXT  	NOT NULL, "
					+ " EDUCATOR       TEXT     NOT NULL, "
					+ " CONSTRAINT uniqeClassConstraint "
					+ " UNIQUE (TEXT_ID,KIND,BEGIN,END,EDUCATOR) ON CONFLICT ABORT);"

			;
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("init class created");

			stmt = con.createStatement();
			sql = "CREATE TABLE SUBJECT "
					+ "(ID             INTEGER   PRIMARY KEY, "
					+ " TEXT_ID	       TEXT  NOT NULL,"
					+ " MIN            INT   NOT NULL, "
					+ " NOTES          TEXT,  "
					+ " CONSTRAINT uniqeSubjectConstraint "
					+ " UNIQUE (TEXT_ID,MIN) ON CONFLICT ABORT);";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("init SUBJECT created");
			stmt = con.createStatement();
			sql = "CREATE TABLE CLASS_SUBJECTS "
					+ "(ID_CLASS	   INT   NOT NULL,"
					+ " ID_SUBJECT     INT   NOT NULL, "
					+ " REALIZED       INT   NOT NULL, "
					+ " MONTH          INT   NOT NULL, "
					+ " YEAR           INT   NOT NULL, "
					+ "PRIMARY KEY (ID_CLASS, ID_SUBJECT,MONTH,YEAR)" + ")";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("init CLASS_SUBJECTS created");
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	ArrayList<ClassSubject> getClassSubjects() {
		System.out.println("getClassSubjects");
		ArrayList<ClassSubject> classSubjectsList = new ArrayList<ClassSubject>();
		ClassSubject c = null;

		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CLASS_SUBJECTS");
			while (rs.next()) {
				c = new ClassSubject(rs.getInt("ID_CLASS"),
						rs.getInt("ID_SUBJECT"), rs.getInt("REALIZED"),
						rs.getInt("MONTH"), rs.getInt("YEAR"));
				classSubjectsList.add(c);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return classSubjectsList;

	}

	ArrayList<ClassSubject> getClassSubjectsForYearAndClass(int year,
			int idClass) {
		System.out.println("getClassSubjectsForYearAndClass year = " + year + " idclass " + idClass);
		ArrayList<ClassSubject> classSubjectsList = new ArrayList<ClassSubject>();
		ClassSubject c = null;

		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM CLASS_SUBJECTS WHERE YEAR = "
							+ year + " AND ID_CLASS = " + idClass);
			while (rs.next()) {
				c = new ClassSubject(rs.getInt("ID_CLASS"),
						rs.getInt("ID_SUBJECT"), rs.getInt("REALIZED"),
						rs.getInt("MONTH"), rs.getInt("YEAR"));
				classSubjectsList.add(c);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return classSubjectsList;

	}

	boolean insertClassSubjects(ClassSubject c) {
		System.out.println("insertClassSubjects");

		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			String sql = "INSERT INTO CLASS_SUBJECTS (ID_CLASS,ID_SUBJECT,REALIZED,MONTH,YEAR)"
					+ "VALUES ("
					+ c.getClassId()
					+ ","
										+ c.getSubjectId()
					+ ","
					+ c.getRealized()
					+ ","
					+ c.getMonth()
					+ ","
					+ c.getYear()+ ");";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = con.createStatement();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
			return false;
		}

		return true;
	}

	public ArrayList<ClassSubject> getSubjectsForClass(int classId, int year) {

		System.out.println("getSubjectsForClass BEGIN");
		ArrayList<ClassSubject> result = new ArrayList<ClassSubject>();
		ArrayList<Subject> subjectsList = instance.getSubjects();

		ArrayList<ClassSubject> csl = instance.getClassSubjectsForYearAndClass(
				year, classId);
		System.out.println("DEBUG 1 size " + csl.size());
		for (ClassSubject cs : csl) {
			if (cs.getClassId() == classId) {
				for (Subject s : subjectsList) {
					if (s.getId() == cs.getSubjectId()) {
						System.out.println("ONE MORE RESULT !!");
						cs.setSubjectName(s.getName());
						result.add(cs);
					}
				}
			}
		}
		
		System.out.println("getSubjectsForClass END");

		return result;

	}

	ArrayList<Subject> getSubjects() {
		System.out.println("getSubjects");
		ArrayList<Subject> subjectsList = new ArrayList<Subject>();
		Subject s = null;

		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM SUBJECT");
			while (rs.next()) {
				s = new Subject(rs.getInt("ID"), rs.getString("TEXT_ID"),
						rs.getInt("MIN"), rs.getString("NOTES"));
				subjectsList.add(s);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return subjectsList;

	}

	boolean insertSubject(Subject s) {
		System.out.println("insertSubject");
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();

			String sql = "INSERT INTO SUBJECT (TEXT_ID,MIN,NOTES)"
					+ "VALUES ('" + s.getName() + "','" + s.getMin() + "','"
					+ s.getNotes() + "');";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = con.createStatement();
			sql = "SELECT last_insert_rowid() AS LAST";
			ResultSet rs = stmt.executeQuery(sql);
			s.setId(rs.getString("LAST"));
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
			return false;
		}

		return true;
	}

	public ArrayList<Class> getClasses() {
		System.out.println("getClasses");
		ArrayList<Class> classList = new ArrayList<Class>();
		Class c = null;

		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CLASS");
			while (rs.next()) {
				/*
				 * String name, String type, String begin, String end, String
				 * educator
				 */
				c = new Class(rs.getInt("ID"), rs.getString("TEXT_ID"),
						rs.getString("KIND"), rs.getString("BEGIN"),
						rs.getString("END"), rs.getString("EDUCATOR"));
				classList.add(c);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println("getClasses classList.size:" + classList.size());
		return classList;

	}

	boolean insertClass(Class c) {
		System.out.println("insertClass");
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(connectionString);

			stmt = con.createStatement();

			String sql = "INSERT INTO CLASS (TEXT_ID,KIND,BEGIN,END,EDUCATOR)"
					+ "VALUES ('" + c.getName() + "','" + c.getType() + "','"
					+ c.getBegin() + "','" + c.getEnd() + "','"
					+ c.getEducator() + "');";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = con.createStatement();
			sql = "SELECT last_insert_rowid() AS LAST";
			ResultSet rs = stmt.executeQuery(sql);
			c.setId(rs.getString("LAST"));
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				return false;
			}
			return false;
		}

		return true;
	}

	private DataBaseManager() {

		this.init();

	}

	public static DataBaseManager getInstance() {
		System.out.println("getInstance");
		if (instance == null) {
			instance = new DataBaseManager();
		}
		return instance;
	}

	public HashMap<Integer, String> getClassesNames() {
		System.out.println("getClassesNames");
		HashMap<Integer, String> s = new HashMap<Integer, String>();

		ArrayList<Class> cl = instance.getClasses();
		for (int i = 0; i < cl.size(); i++) {
			s.put(cl.get(i).getId(), cl.get(i).getName() + " "
					+ cl.get(i).getType());
		}

		System.out.println("getClassesNames s.size:" + s.size());
		return s;
	}

	public static void main(String[] args) {

		System.out.println("main DATA BASE MANAGER");
		DataBaseManager manager = getInstance();

		Subject s = new Subject();
		s.setName("Matematyka");
		s.setMin(30);
		s.setNotes("Podstawówka");
		manager.insertSubject(s);
		
		s.setName("Polski");
		s.setMin(30);
		s.setNotes("Podstawówka");
		manager.insertSubject(s);

		Class c = new Class();
		c.setBegin(2003);
		c.setEnd(2006);
		c.setName("I-IIIa");
		c.setType("podstawowa");
		c.setEducator("Brunhilda Zgred");
		manager.insertClass(c);

		c.setBegin(2007);
		c.setEnd(2010);
		c.setName("I-IIIb");
		c.setType("podstawowa");
		c.setEducator("Brunhilda Zgred2");
		manager.insertClass(c);

		s = null;

		ArrayList<Subject> sl = manager.getSubjects();

		s = sl.get(0);
		System.out.println("SUBJECT NAME" + s.getName());
		System.out.println("SUBJECT MIN" + s.getMin());
		System.out.println("SUBJECT ID" + s.getId());
		System.out.println("SUBJECT NOTES" + s.getNotes());

		ArrayList<Class> cl = manager.getClasses();

		c = cl.get(0);
		System.out.println("CLASS NAME " + c.getName());
		System.out.println("CLASS TYPE " + c.getType());
		System.out.println("CLASS BEGIN " + c.getBegin());
		System.out.println("CLASS EDUCATOR " + c.getEducator());
		System.out.println("CLASS END " + c.getEnd());
		System.out.println("CLASS ID " + c.getId());

		ClassSubject cs = new ClassSubject(1, 1, 1, 1, 2003);
		manager.insertClassSubjects(cs);
		
		cs = new ClassSubject(1, 1, 2, 2, 2004);
		manager.insertClassSubjects(cs);
		
		cs = new ClassSubject(1, 1, 3, 3, 2005);
		manager.insertClassSubjects(cs);

		cs = new ClassSubject(2, 2, 1, 1, 1);
		manager.insertClassSubjects(cs);

		ArrayList<ClassSubject> csl = manager.getClassSubjects();
		cs = csl.get(0);
		System.out.println("CLASS getClassId " + cs.getClassId());
		System.out.println("CLASS getSubjectId " + cs.getSubjectId());
		System.out.println("CLASS getRealized " + cs.getRealized());

		/* manager.dropBase(); */
	}
}
