package database;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class OtherFunctions {

	public static ArrayList<String> from = new ArrayList<String>();
	public static ArrayList<String> where = new ArrayList<String>();
	public static EntityManagerFactory emf;
	public static String userName;

	public static String[] getStudentClubs(){
		String[] st = null;
		EntityManager em = emf.createEntityManager();
		List<StudentClub> clubs = em.createQuery("Select f from StudentClub as f", StudentClub.class).getResultList();
		st = new String[clubs.size()];
		int i =0;
		for(StudentClub f : clubs) {
			System.out.println(f.getClub_name());
			st[i]=f.getClub_name();
			i++;
		}
		em.close();
		Arrays.sort(st);
		return st;
	}

	public static String[] getCourses(){
		String[] st = null;
		EntityManager em = emf.createEntityManager();
		List<Course> courses = em.createQuery("Select f from Course as f", Course.class).getResultList();
		st = new String[courses.size()];
		int i =0;
		for(Course f : courses){
			System.out.println(f.getCode());
			st[i]=f.getCode();
			i++;
		}
		Arrays.sort(st);
		return st;
	}

	public static HashMap<String, Integer> getAcademic(){
		HashMap<String, Integer> staffMap = new HashMap<String, Integer>();
		EntityManager em = emf.createEntityManager();
		List<AcademicStaff> staff= em.createQuery("SELECT f FROM AcademicStaff as f", AcademicStaff.class).getResultList();
		int i =0;
		for(AcademicStaff f : staff) {
			String name = f.getFirst_name() + " " + f.getM_init() + " " + f.getLast_name();
			System.out.println(name);
			System.out.println(f.getAs_id());
			staffMap.put(name, f.getAs_id());
			i++;
		}
		em.close();
		return staffMap;
	}

	public static String[] getDept(){
		ArrayList<String> depts = new ArrayList<String>();
		EntityManager em = emf.createEntityManager();
		List<Student> students= em.createQuery("SELECT f FROM Student as f", Student.class).getResultList();
		for(Student f : students) {
			if(!depts.contains(f.getDept()))
				depts.add(f.getDept());
		}
		em.close();
		String[] str = new String[depts.size()];
		depts.toArray(str);
		Arrays.sort(str);
		return str;
	}

	public static String[] getStudentsTakeCourse(String code) {
		ArrayList<String> numbers = new ArrayList<String>();
		String q = "SELECT DISTINCT f.student_id FROM STUDENT as f, TAKES as t WHERE t.fk_student_id=f.student_id AND t.fk_course_code = '" +code+"';";
		String rs = execute(q);
		while(rs.indexOf('\n')>0) {
			numbers.add(rs.substring(0, rs.indexOf('\n')));
			rs = rs.substring(rs.indexOf('\n')+1);
		}
		String[] result = new String[numbers.size()];
		numbers.toArray(result);
		return result;
	}

	private static void courseQuery(ArrayList<String> courses) {
		Iterator<String> it;
		it = courses.iterator();
		int i =0;
		while (it.hasNext()){
			String course = it.next();
			String takesId = "t" + i;
			from.add("Takes as " + takesId);
			where.add(takesId + ".fk_student_id = s.student_id AND " + takesId + ".fk_course_code = '" + course +"'");
			i++;
		}
	}

	private static void staffQuery(ArrayList<Integer> staffids) {
		Iterator<Integer> it;
		it = staffids.iterator();
		int i =0;
		while (it.hasNext()){
			String staff_id = it.next() + "";
			if(staff_id.length()<1) break;
			String takesId = "ts" + i;
			String offersId = "o" + i;
			from.add("Takes as " + takesId);
			from.add("Offers as " + offersId);
			String whereStmt = offersId + ".fk_course_code = " + takesId + ".fk_course_code AND " + offersId + ".fk_staff_id = '" + staff_id + "' AND "
					+ takesId + ".fk_student_id = s.student_id";
			where.add(whereStmt);
			i++;
		}
	}

	private static void studentClubQuery(ArrayList<String> clubs) {
		Iterator<String> it;
		it = clubs.iterator();
		int i =0;
		while (it.hasNext()){
			String club = it.next();
			if(club.length()<1) continue;
			String followsId = "f" + i;
			from.add("Follows as " + followsId);
			where.add(followsId + ".fk_student_id = s.student_id AND " + followsId + ".fk_club_name = '" + club +"'");
			i++;
		}
	}

	private static void deptQuery(ArrayList<String> depts) {
		Iterator<String> it;
		it = depts.iterator();
		int i =0;
		String stmt="";
		if(depts.size()>1) stmt += "(";
		while (it.hasNext()){
			stmt += "s.dept ='" + it.next() + "'";
			if(it.hasNext()) stmt+= " OR ";
		}
		if(depts.size()>1) stmt += ")";
		where.add(stmt);
	}

	private static String queryCreator() {
		String fromPart ="";
		Iterator<String> iterator = from.iterator();
		while(iterator.hasNext()) {
			fromPart += iterator.next();
			if(iterator.hasNext()) fromPart += " , ";
		}
		String wherePart = "";
		Iterator<String> it = where.iterator();
		while(it.hasNext()) {
			String stmt = it.next();
			if(stmt.length()<1) {
				if(wherePart.length()>4 && wherePart.substring(wherePart.length()-4,wherePart.length()).equals("AND "))
					wherePart = wherePart.substring(0, wherePart.length()-4);
				continue;
			}
			wherePart += stmt;
			System.out.println(stmt);
			if(it.hasNext()) wherePart += " AND ";
		}
		String q="SELECT DISTINCT s FROM " + fromPart ;
		if(wherePart.length()>1)
			q+=" WHERE " + wherePart;
		q+=";";
		from.clear();
		where.clear();
		return q;
	}

	private static String execute (String q) {
		Connection c = null;
		Statement stmt = null;
		String result="";
		try{
			String url = OtherFunctions.emf.getProperties().get("javax.persistence.jdbc.url").toString();
			String password = OtherFunctions.emf.getProperties().get("javax.persistence.jdbc.password").toString();

			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection(url.trim(),
							userName, password);
			stmt = c.createStatement();
			System.out.println("Opened database successfully");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try{
			ResultSet rs = stmt.executeQuery(q);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) { System.out.print(",  "); result +=",  ";}
					String columnValue = rs.getString(i);
					System.out.println(columnValue + "\t");
					result +=columnValue + "\t";
				}
				result += "\n";
				System.out.print("");
			}
			return result;
		}catch(SQLException e){
			System.out.println("Gecersiz query.");
			e.printStackTrace();
		}
		return null;
	}

	public static String getFinalResult(String f_name, String m_init, String l_name,String year, ArrayList<String> depts, ArrayList<String>courses, ArrayList<Integer> staffids, ArrayList<String> clubs){

		from.add("Student as s");
		if(f_name.length()>1) {
			String stmt = "(s.first_name = '" + f_name + "'";
			if(m_init.length()<1)
				stmt += " OR s.m_init = '" + f_name + "'";
			stmt+=")";
			where.add(stmt);
		}
		if(m_init.length()>1) {
			String stmt = "(s.m_init = '" + m_init + "'";
			if(m_init.length()<1)
				stmt += " OR s.f_name = '" + m_init + "'";
			stmt+=")";
			where.add(stmt);
		}
		if(l_name.length()>1)
			where.add("s.last_name = '" + l_name + "'");
		if(year.length()==2)
			where.add("(s.student_id BETWEEN " + year +"0000000 AND " + year + "9999999)" );
		courseQuery(courses);
		staffQuery(staffids);
		studentClubQuery(clubs);
		deptQuery(depts);
		String q = queryCreator();
		System.out.println(q);

		return execute(q);
	}

	public static String getStudentName(String studentNo){
		String getNameQuery = "Select f.first_name,f.m_init,f.last_name From Student as f where f.student_id = " + studentNo + " ;";
		Connection c = null;
		Statement stmt = null;
		String result="";
		try{
			String url = OtherFunctions.emf.getProperties().get("javax.persistence.jdbc.url").toString();
			String password = OtherFunctions.emf.getProperties().get("javax.persistence.jdbc.password").toString();

			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection(url.trim(),
							userName, password);
			stmt = c.createStatement();
			System.out.println("Opened database successfully");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try{
			ResultSet rs = stmt.executeQuery(getNameQuery);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					result +=columnValue + " ";
				}
			}
			return result;
		}catch(SQLException e){
			System.out.println("Gecersiz query.");
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToFile(String s)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new File("neo4jquery.txt"));
			pw.println(s);
			pw.close();
		}
		catch(IOException ioe)
		{
			//do nothing
		}
	}
}
