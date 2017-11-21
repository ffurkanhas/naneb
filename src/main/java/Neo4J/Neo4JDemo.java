package Neo4J;

import org.neo4j.driver.v1.*;

public class Neo4JDemo {
    static String neo4JUrl = "bolt://localhost:7687";
    static String neo4JUserName = "neo4j";
    static String neo4JPassword = "123456";
    static Driver driver;

    public static double dersIliskisi(String ogrenciNo1, String ogrenciNo2){
        Session session = driver.session();
        Record record = null;
        StatementResult result = session.run(
                "MATCH (s:Student {student_id:'" + ogrenciNo1 + "'})-[rel:DersIliskisi]-(s2:Student {student_id:'" +ogrenciNo2 +"'}) RETURN rel");
        while ( result.hasNext() ) {
            record = result.next();
        }
        session.close();
        return record.get(0).get("len").asDouble();
    }

    public static double sectionIliskisi(String ogrenciNo1, String ogrenciNo2){
        Session session = driver.session();
        Record record = null;
        StatementResult result = session.run(
                "MATCH (s:Student {student_id:'" + ogrenciNo1 + "'})-[rel:SectionIliskisi]-(s2:Student {student_id:'" +ogrenciNo2 +"'}) RETURN rel");
        while ( result.hasNext() ) {
            record = result.next();
        }
        session.close();
        return record.get(0).get("len").asDouble();
    }

    public static double kulupIliskisi(String ogrenciNo1, String ogrenciNo2){
        Session session = driver.session();
        Record record = null;
        StatementResult result = session.run(
                "MATCH (s:Student {student_id:'" + ogrenciNo1 + "'})-[rel:KulupIliskisi]-(s2:Student {student_id:'" +ogrenciNo2 +"'}) RETURN rel");
        while ( result.hasNext() ) {
            record = result.next();
        }
        session.close();
        return record.get(0).get("len").asDouble();
    }

    public static double bolumIliskisi(String ogrenciNo1, String ogrenciNo2){
        Session session = driver.session();
        Record record = null;
        StatementResult result = session.run(
                "MATCH (s:Student {student_id:'" + ogrenciNo1 + "'})-[rel:BolumIliskisi]-(s2:Student {student_id:'" +ogrenciNo2 +"'}) RETURN rel");
        while ( result.hasNext() ) {
            record = result.next();
        }
        session.close();
        return record.get(0).get("len").asDouble();
    }

    public static double toplamIliski(String ogrenciNo1,String ogrenciNo2){
        double sonuc = dersIliskisi(ogrenciNo1,ogrenciNo2);
        double sonuc2 = sectionIliskisi(ogrenciNo1,ogrenciNo2);
        double sonuc3 = kulupIliskisi(ogrenciNo1,ogrenciNo2);
        double sonuc4 = bolumIliskisi(ogrenciNo1,ogrenciNo2);
        return sonuc + sonuc2 + sonuc3 + sonuc4;
    }

    public static double ogrenciGpa(String ogrenciNo){
        Session session = driver.session();
        Record record = null;
        StatementResult result = session.run(
                "MATCH (s:Student {student_id:'" + ogrenciNo + "'}) RETURN s");
        while ( result.hasNext() ) {
            record = result.next();
        }
        session.close();
        return record.get(0).get("gpa").asDouble();
    }

/*
MATCH p=shortestPath(
  (s1:Student {student_id:"141301028"})-[*]-(s2:Student {student_id:"141101024"})
)
RETURN p
 */

}
