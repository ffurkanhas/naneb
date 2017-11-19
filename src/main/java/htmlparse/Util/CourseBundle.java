package htmlparse.Util;

public class CourseBundle {
    private String id;
    private String section;

    public CourseBundle(String id, String section)
    {
        this.id = id;
        this.section = section;
    }

    public String getId()
    {
        return id;
    }

    public String getSection()
    {
        return section;
    }

    @Override
    public int hashCode()
    {
        return (id + section).hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        CourseBundle cbo = (CourseBundle) obj;
        return cbo.id.equals(this.id) && cbo.section.equals(this.section);
    }
}