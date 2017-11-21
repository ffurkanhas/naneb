package htmlparse;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import htmlparse.Util.CourseBundle;
import htmlparse.Util.StudentBundle;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import postgre.DbInitializer;

public class BisParser {

  private PrintWriter pw = null;

  private String save_file = "VeryBigFile.txt";
  private String etubis_url = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
  private String etubis_ders_programi = "http://kayit.etu.edu.tr/Ders/Ders_prg.php";

  private HashSet<String> allCourses = new HashSet<>();
  private ArrayList<String[]> courseClass = new ArrayList<>();
  private HashMap<String, ArrayList<String>> takes = new HashMap<>();
  private HashMap<String, ArrayList<String>> offers = new HashMap<>();

  /**
   *
   * @param args
   *          source(0=file, 1=www-URL) and uri seperated by a space.
   * @throws IOException
   */
  public void bisParserRunner(String[] args,String userName, String password) throws IOException {
    if (args.length != 2)
    {
      System.out.println("Wrong args size!");
      System.exit(0);
    }
    Document doc = null;
    if (args[0].equals("0"))
    {
      InputStream in = getClass().getResourceAsStream(args[1]);

      File bisFile = new File(args[1]);
      try {
        doc = Jsoup.parse(in, null,"");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    else
    {
      URL sdb = null;
      try {
        sdb = new URL(args[1]);
        doc = Jsoup.parse(sdb, 10000);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    Scanner sc = new Scanner(System.in);
    System.out.println("Options:\n" + "1: Fetch everything and initialize database\n"
            + "2: Initialize \"student\" only\n" + "3: Initialize \"course\" only \n"
            + "4: Initialize \"takes\" (requires \"student\" and \"course\" to be initialized)\n"
            + "5: Initialize \"offers\" and \"academic_staff\" (requires \"course\" to be initialized)\n"
            + "6: Initialize \"held_in\" and \"classroom\" (requires \"course\" to be initialized)\n"
            + "7: Initialize \"follows\" and \"student_club\" (requires \"student\" to be initialized)");

    ArrayList<StudentBundle> students = getStudentInfo(doc);
    HashMap<String, Integer> fc = null;
    HashMap<String, ArrayList<String>> fs = null;
    OrtamParser op;

    switch (sc.nextLine()) {
      case "1":
        if (!new File("VeryBigFile.txt").exists()) createBigFile(students);
        readEverything("VeryBigFile.txt");
        fetchAcademicStaff();
        op = new OrtamParser();
        op.parse();
        fc = op.getFollowerCount();
        fs = matchNamesWithIds(students, op.getFollowingStudents());
        break;
      case "2":
        if (!new File("VeryBigFile.txt").exists()) createBigFile(students);
        readEverything("VeryBigFile.txt");
        allCourses = null;
        courseClass = null;
        offers = null;
        takes = null;
        break;
      case "3":
        if (!new File("VeryBigFile.txt").exists()) createBigFile(students);
        readEverything("VeryBigFile.txt");
        students = null;
        courseClass = null;
        offers = null;
        takes = null;
        break;
      case "4":
        if (!new File("VeryBigFile.txt").exists()) createBigFile(students);
        readEverything("VeryBigFile.txt");
        students = null;
        allCourses = null;
        courseClass = null;
        offers = null;
        break;
      case "5":
        fetchAcademicStaff();
        students = null;
        allCourses = null;
        takes = null;
        courseClass = null;
        break;
      case "6":
        if (!new File("VeryBigFile.txt").exists()) createBigFile(students);
        readEverything("VeryBigFile.txt");
        offers=null;
        students = null;
        allCourses = null;
        takes = null;
        break;
      case "7":
        op = new OrtamParser();
        op.parse();
        fc = op.getFollowerCount();
        fs = matchNamesWithIds(students, op.getFollowingStudents());
        allCourses = null;
        courseClass = null;
        offers = null;
        takes = null;
        students = null;
        break;
    }
    sc.close();
    DbInitializer dbinit = new DbInitializer(students, takes, courseClass, allCourses, offers, fc, fs);
    dbinit.setSettings(userName,password);
    try {
      dbinit.createTables();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    dbinit.initialize();
  }

  

  private HashMap<String, ArrayList<String>> matchNamesWithIds(ArrayList<StudentBundle> students,
                                                               HashMap<String, ArrayList<String>> fs)
  {
    HashMap<String, ArrayList<String>> clubStuNo = new HashMap<>();
    for (String club : fs.keySet())
    {
      ArrayList<String> no = new ArrayList<>();
      for (String s : fs.get(club))
      {
        int count = 0;
        for (StudentBundle stu : students)
        {
          if (s.equals(stu.getName()))
          {
            // System.out.println(s + " matched with " + stu.getNo()); /* Debug Print */
            no.add(stu.getNo());
            count++;
          }
        }
        if (count > 1) System.out.println(count + " student numbers for student with name: " + s);
      }
      System.out.println("Matched " + no.size() + " students for club " + club + " which actually has " 
            + fs.get(club).size() + " followers.");
      clubStuNo.put(club, no);
    }
    return clubStuNo;
  }

  private void fetchAcademicStaff() throws IOException
  {
    System.out.println("Start fetching academic staff...");
    Long timeBegin = System.currentTimeMillis();
    Connection.Response etubis = Jsoup.connect(etubis_url).method(Connection.Method.GET).execute();
    etubis.charset("ISO-8859-9");
    Document document = etubis.parse();
    Elements options = document.getElementsByAttributeValue("name", "dd_hoca").get(0).children();
    int size = options.size();
    int count = 0;
    for (Element option : options)
    {
      String iName = option.text();
      Document docSchedule = Jsoup.connect(etubis_ders_programi).data("dd_hoca", option.val())
          .data("btn_hoca", "Programı Göster").cookies(etubis.cookies()).post();
      // System.out.println("---------------\n"+option.text()); /* Debug Print
      // */
      offers.put(iName, parseInstructorSchedule(docSchedule));
      if (count % (size / 20) == 0) System.out.println("%" + (int) (count * 1f / size * 100));
      count++;
    }
    System.out.println("Fetching academic staff took " + (System.currentTimeMillis() - timeBegin) / 1000 + " seconds.");
  }

  private ArrayList<String> parseInstructorSchedule(Document doc)
  {
    ArrayList<String> list = new ArrayList<>();
    Element table = null;
    if (doc.select("table").size() > 0)
      table = doc.select("table").get(0);
    else
      return null;
    Elements rows = table.select("tr");
    int col_size = rows.get(1).select("td").size();
    for (int i = 0; i < col_size; i++)
    {
      for (int j = 1; j < rows.size(); j++)
      {
        String box = rows.get(j).select("td").get(i).text();
        /* If there is a lesson scheduled */
        if (!box.equals("-"))
        {
          String course;
          /* Remove all spaces for some reason */
          String zipped = box.replaceAll(" ", "");
          int ind = zipped.indexOf("Derslik:");
          // System.out.println(zipped); /* Debug print */
          String[] tokens = zipped.substring(0, ind).split("-");
          course = tokens[0];
          /*
           * İKT103 messes with my formatting so I mess with it here?
           */
          if (course.equals("İKT103")) course = "İKT103İ";
          if (!list.contains(course)) list.add(course);
        }
      }
    }
    // for(String s : list) /* Debug Print */
    // System.out.println(s);
    return list;
  }

  private void createBigFile(ArrayList<StudentBundle> students) throws IOException
  {
    FileOutputStream fos = new FileOutputStream(save_file, false);
    pw = new PrintWriter(fos);

    long begin_ref = System.currentTimeMillis();
    int count = 0;
    int milestone = students.size() / 100;
    System.out.println("Start fetching scheduling and course info...");
    for (StudentBundle s : students)
    {
      /*
       * Might not need to "GET" for each student but this is how we currently
       * get the schedule for a student number
       */
      Connection.Response etubis = Jsoup.connect(etubis_url).method(Connection.Method.GET).execute();
      etubis.charset("ISO-8859-9");
      String no = s.getNo();
      Document document = Jsoup.connect(etubis_ders_programi).data("ogrencino", no)
          .data("btn_ogrenci", "Programı Göster").cookies(etubis.cookies()).post();
      /* Parse the schedule of student s */
      ArrayList<String> ret = retrieveCourses(s, document);
      writeDown("STUNO:" + no, pw);
      /*
       * Print everything down if student s is enrolled in AT LEAST ONE course
       */
      if (ret != null) for (String crs : ret)
        writeDown(crs, pw);
      /* Edgy progress meter */
      if (count % milestone == 0) System.out.println("%" + (int) (count * 1f / students.size() * 100));
      count++;
    }
    pw.close();
    System.out.println(
        "Fetching student and course info took " + (System.currentTimeMillis() - begin_ref) / 100 + " seconds.");
  }

  private ArrayList<StudentBundle> getStudentInfo(Document doc)
  {
    /*
     * https://stackoverflow.com/questions/24772828/how-to-parse-html-table-
     * using-jsoup
     */
    Element table = doc.select("table").get(0);
    Elements rows = table.select("tr");

    ArrayList<StudentBundle> students = new ArrayList<>();

    /* Fetch all student related info */
    for (int i = 1; i < rows.size() - 1; i++)
    { /* First row has the column names, last row has one element */
      Element row = rows.get(i);
      Elements cols = row.select("td");
      if (cols.get(2).text().equals("Ara Sınıfta") && (cols.get(6).text().equals("Lisans")
          || cols.get(6).text().equals("Yüksek Lisans") || cols.get(6).text().equals("Doktora")))
      {
        String id = cols.get(0).text();
        String[] name = new String[3];
        /* Setting locale saves lives */
        String nText = cols.get(1).text().toLowerCase(Locale.forLanguageTag("TR"));
        String[] tokenized = nText.split(" ");
        name[0] = properSize(tokenized[0]);
        name[1] = tokenized.length > 2 ? properSize(tokenized[1]) : "";
        name[2] = properSize(tokenized[tokenized.length - 1]);
        String dept = cols.get(4).text();
        StudentBundle sb = new StudentBundle(id, name[0], name[1], name[2], dept);
        students.add(sb);
      }
    }
    return students;
  }

  private String properSize(String s)
  {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  private void writeDown(String in, PrintWriter pw)
  {
    try
    {
      pw.println(in);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private ArrayList<String> retrieveCourses(StudentBundle s, Document doc) throws FileNotFoundException
  {
    Element table = null;
    if (doc.select("table").size() > 0)
      table = doc.select("table").get(0);
    else
      return null;
    ArrayList<String> retrieved = new ArrayList<String>();
    Elements rows = table.select("tr");
    int col_size = rows.get(1).select("td").size();
    String day = "ThisShouldNotHappen";
    for (int i = 0; i < col_size; i++)
    {
      switch (i) {
      case 0:
        day = "Pazartesi";
        break;
      case 1:
        day = "Salı";
        break;
      case 2:
        day = "Çarşamba";
        break;
      case 3:
        day = "Perşembe";
        break;
      case 4:
        day = "Cuma";
        break;
      case 5:
        day = "Cumartesi";
        break;
      case 6:
        day = "Pazar";
        break;
      }
      for (int j = 1; j < rows.size(); j++)
      {
        String box = rows.get(j).select("td").get(i).text();
        /* If there is a lesson scheduled */
        if (!box.equals("-"))
        {
          String[] course_class = new String[5];
          /* Remove all spaces for some reason */
          String zipped = box.replaceAll(" ", "");
          int ind = zipped.indexOf("Derslik:");
          int offset = "Derslik:".length();
          /* Ignore conflicts in a schedule */
          if (zipped.substring(ind + offset).contains("Derslik:")) continue;
          // System.out.println(zipped); /* Debug print */
          String[] tokens = zipped.substring(0, ind).split("-");
          String courseId = tokens[0];
          String section = tokens[1];
          /*
           * İKT103 messes with my formatting so I mess with it here?
           */
          if (courseId.equals("İKT103"))
          {
            courseId = "İKT103İ";
            section = tokens[2];
          }
          /* Classroom is indicated after substring "Derslik:" in the box */
          String classRoom = zipped.substring(ind + offset);
          allCourses.add(courseId);
          /* Some of the info below is unnecessary but... */
          course_class[0] = courseId + "-" + section;
          course_class[1] = classRoom;
          course_class[2] = day;
          course_class[3] = (8 + (j - 1)) + ":30";
          course_class[4] = (9 + (j - 1)) + ":20";
          CourseBundle cb = new CourseBundle(courseId, section);
          s.addCourse(cb);
          // System.out.println(courseId + " " + section); /*Debug print*/
          retrieved.add(course_class[0] + " " + course_class[1] + " " + course_class[2] + " " + course_class[3] + " "
              + course_class[4]);
        }
      }
    }
    return retrieved;
  }

  private void readEverything(String filePath) throws FileNotFoundException
  {
    System.out.println("Found VeryBigFile.txt, fetching student and course info from disk.");
    Scanner sc = new Scanner(new FileInputStream(filePath));
    /*
     * For each course map its start-end times, used for calculating actual
     * start-end time
     */
    HashMap<String, ArrayList<Integer>> ccmap = new HashMap<>();
    /* This is basically the takes relation for each student instance */
    HashSet<String> stuTakes = null;
    String stuId = null;
    while (sc.hasNextLine())
    {
      String line = sc.nextLine();
      if (line.startsWith("STUNO"))
      {
        if (stuId != null)
        {
          ArrayList<String> val = new ArrayList<>();
          // System.out.print(stuId+ ": "); /* Debug print */
          for (String course : stuTakes)
          {
            val.add(course);
            /* Add course to all courses */
            allCourses.add(course.split("-")[0]);
            // System.out.print(course+", "); /* Debug print */
          }
          // System.out.println(); /* Debug print */
          takes.put(stuId, val);
        }
        stuTakes = new HashSet<>();
        stuId = line.split(":")[1];
        continue;
      }
      String[] tokens = line.split(" ");
      if (stuTakes != null) {
        stuTakes.add(tokens[0]);
      }
      String key = tokens[0] + " " + tokens[1] + " " + tokens[2];
      if (ccmap.get(key) != null)
      {
        ArrayList<Integer> temp = ccmap.get(key);
        temp.add(Integer.parseInt(tokens[3].split(":")[0]));
        ccmap.put(key, temp);
      }
      else
      {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(Integer.parseInt(tokens[3].split(":")[0]));
        ccmap.put(key, temp);
      }

    }
    /* Merge schedule */
    for (String key : ccmap.keySet())
    {
      ArrayList<Integer> hours = ccmap.get(key);
      String[] cc = new String[5];
      String[] tokKey = key.split(" ");
      cc[0] = tokKey[0];
      cc[1] = tokKey[1];
      cc[2] = tokKey[2];

      int iMax = Integer.MIN_VALUE, iMin = Integer.MAX_VALUE;
      for (Integer i : hours)
      {
        if (i > iMax) iMax = i;
        if (i < iMin) iMin = i;
      }
      cc[3] = iMin + ":30";
      cc[4] = (iMax + 1) + ":30";
      // System.out.println(cc[0] + " " + cc[1] + " " + cc[2] + " " + cc[3] + "
      // " + cc[4]); /* Debug print */
      courseClass.add(cc);
    }
    sc.close();
    System.out.println("Fetched student and course info.");
  }

  public String getSave_file() {
    return save_file;
  }

  public void setSave_file(String save_file) {
    this.save_file = save_file;
  }

  public String getEtubis_url() {
    return etubis_url;
  }

  public void setEtubis_url(String etubis_url) {
    this.etubis_url = etubis_url;
  }

  public String getEtubis_ders_programi() {
    return etubis_ders_programi;
  }

  public void setEtubis_ders_programi(String etubis_ders_programi) {
    this.etubis_ders_programi = etubis_ders_programi;
  }
}