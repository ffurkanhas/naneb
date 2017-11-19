package htmlparse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OrtamParser {

  public static final String ORTAM_TOPLULUK_URL = "https://ortam.etu.edu.tr/topluluklar";
  public static final String ORTAM_URL = "https://ortam.etu.edu.tr";
  private HashMap<String, Integer> followerCount;
  private HashMap<String, ArrayList<String>> followingStudents;

  public HashMap<String, Integer> getFollowerCount()
  {
    return followerCount;
  }

  public void setFollowerCount(HashMap<String, Integer> followerCount)
  {
    this.followerCount = followerCount;
  }

  public HashMap<String, ArrayList<String>> getFollowingStudents()
  {
    return followingStudents;
  }

  public void setFollowingStudents(HashMap<String, ArrayList<String>> followingStudents)
  {
    this.followingStudents = followingStudents;
  }

  public OrtamParser()
  {
    followerCount = new HashMap<>();
    followingStudents = new HashMap<>();
  }

  public void parse() throws IOException
  {
    System.out.println("Begin fetching student club info...");
    Long timeBegin = System.currentTimeMillis();
    Document mainPage = getPage(ORTAM_TOPLULUK_URL);
    Elements clubHtml = mainPage.getElementsByAttributeValue("class", "content").get(0)
        .getElementsByAttributeValue("class", "div-table__tbody").get(0).children();
    int size = clubHtml.size();
    int count = 0;
    for (Element row : clubHtml)
    {
      parseRow(row);
      if(count % (size/20) == 0)
        System.out.println("%"+(int)(count*1f/size*100));
      count++;
    }
    System.out.println("Fetching student club info took " + (System.currentTimeMillis()-timeBegin)/1000 + " seconds.");
  }

  private void parseRow(Element row) throws IOException
  {
    String[] tokens = row.text().split(" ");
    String clubName = "";
    int followedBy = 0;
    for (String s : tokens)
    {
      if (stringIsNumber(s))
      {
        followedBy = Integer.parseInt(s);
        break;
      }
      clubName += (s + " ");
    }
    clubName = clubName.trim();
    clubName = clubName.replaceAll("'", "");
    // System.out.println(clubName + " " + followedBy); /* Debug Print */
    /* Get club page's url */
    String cu = row.child(0).child(0).attr("href");
    cu = ORTAM_URL + cu;
    followingStudents.put(clubName, getFollowers(cu));
    followerCount.put(clubName, followedBy);
  }

  private ArrayList<String> getFollowers(String clubUrl) throws IOException
  {
    Document club = getPage(clubUrl);
    /* Followers are listed in children of pg-3 div */
    Element listPage = club.getElementById("pg-3");
    Elements userList = listPage.getElementsByAttributeValue("class", "user-list__cell");
    ArrayList<String> ret = new ArrayList<>();
    for (Element usr : userList)
    {
      ret.add(usr.text());
      //System.out.println(usr.text()); /* Debug Print */
    }
    return ret;
  }

  private Document getPage(String url) throws IOException
  {
    Connection.Response cresp = Jsoup.connect(url).method(Connection.Method.GET).execute();
    return cresp.parse();
  }

  private boolean stringIsNumber(String s)
  {
    for (char c : s.toCharArray())
    {
      if (!Character.isDigit(c)) return false;
    }
    return true;
  }

  /* Not a good implementation of unit testing */
  public static void main(String[] args) throws IOException
  {
    OrtamParser op = new OrtamParser();
    op.parse();
  }

}