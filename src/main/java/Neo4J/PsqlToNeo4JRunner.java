package Neo4J;

public class PsqlToNeo4JRunner {

    public static void main(String args[]){

        PsqlToNeo4JHelper converter = new PsqlToNeo4JHelper();
        Long timeBegin = System.currentTimeMillis();
        converter.run();
        System.out.println("Exporting csv files and creating graph database took " + (System.currentTimeMillis() - timeBegin) / 1000 + " seconds.");
    }

}
