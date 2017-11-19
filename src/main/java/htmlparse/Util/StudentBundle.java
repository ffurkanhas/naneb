package htmlparse.Util;

import java.util.HashSet;

/*
* CLARIFICATION: StudentBundle.<courses> is never used for any reason at
* anywhere at all.
*/
public class StudentBundle {

    private String id, fname, minit, lname, dept;
    private HashSet<CourseBundle> courses;

    public StudentBundle(String id, String fname, String minit, String lname, String dept)
    {
        this.id = id;
        this.fname = fname;
        this.minit = minit;
        this.lname = lname;
        this.dept = dept;
        courses = new HashSet<>();
    }

    public String getNo()
    {
        return id;
    }

    public String getName()
    {
        return fname + " " + (minit.length()>0 ? minit + " " : "")  + lname;
    }

    public String[] getNameSeperate()
    {
        return new String[] { fname, minit, lname };
    }

    public String getDept()
    {
        return dept;
    }

    public HashSet<CourseBundle> getCourses()
    {
        return courses;
    }

    public void addCourse(CourseBundle cb)
    {
        courses.add(cb);
    }

}