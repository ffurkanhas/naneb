package Neo4J;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Neo4JCsvImporter {

    private String neo4JUrl = "bolt://localhost:7687";
    private String neo4JUserName = "neo4j";
    private String neo4JPassword = "123456";
    private String csvPaths = "/tmp/";
    private String neo4JImportPath = "/var/lib/neo4j/import";
    private Driver driver;

    public void copyCsvToNeo4J(ArrayList<String> tableNames){
        ProcessExecutor pe = new ProcessExecutor();
        for(String tableName:tableNames){
            try {
                pe.command("sudo", "cp", csvPaths + tableName + ".csv", neo4JImportPath).execute();
                pe.command("sudo", "chown","neo4j:neo4j",neo4JImportPath + "/" + tableName + ".csv").execute();
                pe.command("sudo", "chmod","755",neo4JImportPath + "/" + tableName + ".csv").execute();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectNeo4JRunQueries(){
        driver = GraphDatabase.driver( neo4JUrl, AuthTokens.basic( neo4JUserName, neo4JPassword ) );
        runQueries();
        driver.close();
    }

    private void runQueries(){
        Session session = driver.session();

        session.run("LOAD CSV WITH HEADERS FROM \"file:///student.csv\" AS student\n" +
                "CREATE (s:Student)\n" +
                "SET s = student");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///course.csv\" AS course\n" +
                "CREATE (n:Course)\n" +
                "SET n = course");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///takes.csv\" AS csvTakes\n" +
                "MATCH (s:Student),(course:Course)\n" +
                "WHERE s.student_id = csvTakes.fk_student_id and course.code = csvTakes.fk_course_code\n" +
                "MERGE (courseSection:CourseSection {course_code: csvTakes.fk_course_code, section: csvTakes.section})\n" +
                "MERGE (s)-[:takesSection]->(courseSection)\n" +
                "MERGE (s)-[:takesCourse]->(course)");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///classroom.csv\" AS csvClass\n" +
                "CREATE (n:Classroom)\n" +
                "SET n = csvClass");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///held_in.csv\" AS csvHeldIn\n" +
                "MATCH (c:Course)\n" +
                "WHERE c.code = csvHeldIn.fk_course_code\n" +
                "MATCH (classes:Classroom)\n" +
                "WHERE classes.class_id = csvHeldIn.fk_class_id\n" +
                "CREATE (courseP:CourseProgram)\n" +
                "SET courseP = csvHeldIn\n" +
                "CREATE (c)-[:held_in {start_at: csvHeldIn.start_at, end_at:csvHeldIn.end_at, dayofweek: csvHeldIn.dayofweek, section: csvHeldIn.section}]->(classes)");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///academic_staff.csv\" AS csvAcademicStaff\n" +
                "CREATE (as:AcademicStaff)\n" +
                "SET as = csvAcademicStaff");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///offers.csv\" AS csvOffers\n" +
                "MATCH (as:AcademicStaff),(c:Course)\n" +
                "WHERE as.as_id = csvOffers.fk_staff_id and c.code = csvOffers.fk_course_code\n" +
                "MERGE (as)-[:offers]->(c)");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///student_club.csv\" AS csvStudentClub\n" +
                "CREATE (n:StudentClub)\n" +
                "SET n = csvStudentClub");

        session.run("LOAD CSV WITH HEADERS FROM \"file:///follows.csv\" AS csvFollows\n" +
                "MATCH (s:Student),(sc:StudentClub)\n" +
                "WHERE s.student_id = csvFollows.fk_student_id and sc.club_name = csvFollows.fk_club_name\n" +
                "MERGE (s)-[:follows]->(sc)");

        session.close();
    }
}
