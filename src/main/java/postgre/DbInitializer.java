package postgre;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import htmlparse.Util.StudentBundle;

public class DbInitializer {

  private Connection c;
  private Statement stmt;
  
  private ArrayList<StudentBundle> sb;
  private HashMap<String,ArrayList<String>> takes;
  private HashMap<String,ArrayList<String>> offers;
  private ArrayList<String[]> courseClass = new ArrayList<>();
  private HashSet<String> allCourses = new HashSet<>();
  private HashMap<String,Integer> followerCount;
  private HashMap<String, ArrayList<String>> followingStudents;
  private String url = "jdbc:postgresql://localhost:5432/naneb";
  private String userName = "postgres";
  private String password = "postgres";

  public DbInitializer(){

  }

  public DbInitializer(ArrayList<StudentBundle> sb, HashMap<String,ArrayList<String>> takes
      ,ArrayList<String[]> heldIn,HashSet<String> courses, HashMap<String,ArrayList<String>> offers,
      HashMap<String,Integer> followerCount, HashMap<String, ArrayList<String>> followingStudents)
  {
    this.sb = sb;
    this.takes = takes;
    this.courseClass = heldIn;
    this.allCourses = courses;
    this.offers = offers;
    this.followerCount = followerCount;
    this.followingStudents = followingStudents;
  }

  public void setSettings(String userName, String password){
    this.userName = userName;
    this.password = password;
  }

  public void initialize()
  {
    c = connect(url,userName,password);
    try
    {
      if(sb != null)
        insertStudent();
      if(allCourses != null)
        insertCourse();
      if(takes != null)
        insertTakes();
      if(courseClass != null)
        insertHeldIn(); /*Insert all classrooms and held_ins*/
      if(offers != null)
        insertOffers();
      if(followerCount != null && followingStudents != null)
        insertFollows();
        
      c.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  private void insertFollows() throws SQLException
  {
    stmt = c.createStatement();
    for(String s : followingStudents.keySet())
    {
      int fc = followerCount.get(s);
      insertStudentClub(s,fc);
      for(String no : followingStudents.get(s))
      {
        String squery = "INSERT INTO follows VALUES(" +
            Integer.parseInt(no) + "," +
            "'" + s + "'" +
            ");"
            ;
        System.out.println(squery); /* Debug Print */
        stmt.executeUpdate(squery);
      }
    }
    stmt.close();
  }
  
  private void insertStudentClub(String cName, int fc) throws SQLException
  {
    String squery = "INSERT INTO student_club VALUES(" +
        "'" + cName + "'," +
        fc +
        ");"
        ;
    System.out.println(squery); /* Debug Print */
    stmt.executeUpdate(squery);
  }
  
  private void insertAcademicStaff(String iName,int staffId) throws SQLException
  {
    /* Insert each instructor */
    String[] nTokens = iName.split(" ");
    String squery = "INSERT INTO academic_staff VALUES(" +
        "'" + nTokens[0] + "'," +
        "'" + (nTokens.length > 2 ? nTokens[1] : "") + "'," +
        "'" + (nTokens.length > 2 ? nTokens[nTokens.length-1] : nTokens[1]) + "'," +
        "'" + /*Department*/"'," +
        staffId +
        ");"
        ;
    System.out.println(squery); /*Debug Print*/
    stmt.executeUpdate(squery);
  }
  
  private void insertOffers() throws SQLException
  {
    stmt = c.createStatement();
    /* Insert each instructor and courses offered by them */
    for(String iName : offers.keySet())
    {
      int staffId = iName.hashCode();
      insertAcademicStaff(iName,staffId);
      for(String course : offers.get(iName))
      {
        String squery = "INSERT INTO offers VALUES(" +
            staffId + "," +
            "'" + course + "'" +
            ");"
            ;
        System.out.println(squery); /*Debug Print*/
        stmt.executeUpdate(squery);
      }
    }
    stmt.close();
  }
  
  private void insertCourse() throws SQLException
  {
    stmt = c.createStatement();
    /* Insert each course */
    for(String s : allCourses)
    {
      String squery = "INSERT INTO course VALUES(" +
          "'" + /*course name*/"" + "'," +
          "'" + s + "'" +
          ");"
          ;
      System.out.println(squery); /*Debug Print*/
      stmt.executeUpdate(squery);
    }
    stmt.close();
  }
  
  private void insertHeldIn() throws SQLException
  {
    stmt = c.createStatement();
    /* To keep inserted class ids */
    HashSet<String> classes = new HashSet<>();
    
    for(String[] cc : courseClass)
    {
      String classroom = cc[1];
      /* Insert classroom if we haven't inserted it already */
      if(!classes.contains(classroom))
      {
        classes.add(classroom);
        insertClassroom(Integer.MIN_VALUE, Integer.MIN_VALUE, classroom);
      }
      String squery = "INSERT INTO held_in VALUES(" +
                      "'" + cc[3] + "'," +
                      "'" + cc[4] + "'," +
                      "'" + cc[0].split("-")[0] + "'," +
                      "'" + classroom + "'," +
                      "'" + cc[2] + "'," +
                            Integer.parseInt(cc[0].split("-")[1]) +
                      ");";
      System.out.println(squery);
      stmt.executeUpdate(squery);
    }
    stmt.close();
  }
  
  private void insertClassroom(int floor, int building, String classroom) throws SQLException
  {
    String squery = "INSERT INTO classroom VALUES(" +
                    (floor>-3 ? floor : "NULL")+ "," +
                    (building>-1 ? building : "NULL") + "," +
                    "'" + classroom +"'"+
                    ");";
    System.out.println(squery);
    stmt.executeUpdate(squery);
  }
  
  private void insertStudent() throws SQLException
  {
    stmt = c.createStatement();
    /* Insert all students */
    for(StudentBundle s : sb)
    {
      String[] nTokens = s.getNameSeperate();
      String squery = "INSERT INTO student VALUES(" +
                      "'" + nTokens[0] + "'," +
                      "'" + nTokens[1] + "'," +
                      "'" + nTokens[2] + "'," +
                      "'" + s.getDept()+ "',"  +
                      s.getNo()        + ""  +
                      ");"
                      ;
      System.out.println(squery); /*Debug Print*/
      stmt.executeUpdate(squery);
    }
    stmt.close();
  }
  
  private void insertTakes() throws SQLException
  {
    stmt = c.createStatement();
    /* For each course every student takes
     * insert the appropriate "takes" entry
     */
    for(String no : takes.keySet())
    {
      for(String crs : takes.get(no))
      {
        String squery = "INSERT INTO takes VALUES(" +
                              crs.split("-")[1] + "," +
                                       "'" + no + "'," +
                        "'" + crs.split("-")[0] + "'" +
                        ");"
                        ;
        System.out.println(squery); /*Debug Print*/
        stmt.executeUpdate(squery);
      }
    }
    stmt.close();
  }
  
  /**
   * Connect to our NANEB database as a superuser.
   * PLEASE ENTER CORRECT CONNECTION INFO
   * @return c
   */
  private Connection connect(String url, String userName, String password)
  {
    Connection c = null;
    try
    {
      Class.forName("org.postgresql.Driver");
      c = DriverManager.getConnection(url,userName,password);
     } catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("Opened database successfully");
    return c;
  }

  public Connection getConnect(){
    return connect(url,userName,password);
  }
}