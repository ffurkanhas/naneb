package Neo4J;

import postgre.DbInitializer;

import java.sql.*;
import java.util.ArrayList;

public class PsqlToNeo4JHelper {

    private Statement stmt;
    private Connection c;
    private ArrayList<String> tableNames;
    private String neo4JUserName = "neo4j";
    private String neo4JPassword = "123456";
    private String psqlUserName = "postgres";
    private String psqlPassword = "postgres";


    public void run(){
        Neo4JCsvImporter neo4JCsvImporter = new Neo4JCsvImporter();
        neo4JCsvImporter.setSettings(neo4JUserName,neo4JPassword);
        DbInitializer naneb = new DbInitializer();
        naneb.setSettings(psqlUserName,psqlPassword);
        c = naneb.getConnect();
        try{
            stmt = c.createStatement();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        createCsv();
        neo4JCsvImporter.copyCsvToNeo4J(tableNames);
        neo4JCsvImporter.connectNeo4J();
        neo4JCsvImporter.runQueries();
    }

    private void createCsv(){

        tableNames = getTableNames();
        for(String tableName:tableNames){
            String squery = "COPY (select * From " + tableName +
                    ") TO '/tmp/" + tableName + ".csv' DELIMITER ',' CSV HEADER;";
            System.out.println(squery); /*Debug Print*/
            try {
                stmt.executeUpdate(squery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> getTableNames(){
        ArrayList<String> tableNames = new ArrayList<String>();
        DatabaseMetaData dbmd;
        try{
            dbmd = c.getMetaData();
            ResultSet tables = dbmd.getTables(null, null, "%", new String[] { "TABLE" });
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return tableNames;
    }

    public void setSettings(String neo4JUserName,String neo4JPassword){
        this.neo4JUserName = neo4JUserName;
        this.neo4JPassword = neo4JPassword;
    }

    public void psqlSetSettings(String psqlUserName,String psqlPassword){
        this.psqlUserName = psqlUserName;
        this.psqlPassword = psqlPassword;
    }
}
