package ServerApp;

import Neo4J.PsqlToNeo4JHelper;
import htmlparse.BisParser;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerApp {
    ProcessExecutor pe;
    private static String postgresUserName = "postgres";
    private static String postgresPassword = "postgres";
    private String neo4JUserName = "neo4j";
    private String neo4JPassword = "neo4j";
    public static void main(String args[]){

        ServerApp sa = new ServerApp();
        Scanner kyb = new Scanner(System.in);
        String answer;
        System.out.println("*******If you run without sudo permission please run with sudo*******");
        System.out.print("Do you want to install PostgreSql? (yes/no)");
        answer = kyb.nextLine();
        if(answer.charAt(0) == 'y'){
            sa.installPostgreSql();
            sa.createDatabase(sa.postgresUserName,sa.postgresPassword);
        }
        else if(answer.charAt(0) == 'n'){
            System.out.print("Enter your Psql username: ");
            sa.postgresUserName = kyb.nextLine();
            System.out.print("Enter you Psql password: ");
            sa.postgresPassword = kyb.nextLine();
            sa.createDatabase(sa.postgresUserName,sa.postgresPassword);
        }
        System.out.print("Do you want to install Neo4J? (yes/no)");
        answer = kyb.nextLine();
        if(answer.charAt(0) == 'y'){
            sa.installNeo4J();
        }
        else if(answer.charAt(0) == 'n'){
            System.out.print("Enter your Neo4J username: ");
            sa.neo4JUserName = kyb.nextLine();
            System.out.print("Enter you Neo4J password: ");
            sa.neo4JPassword = kyb.nextLine();
        }
        sa.runBisParser();
        sa.changeHeapSize();
        sa.runPsqlToNeo4J();
        sa.changeHeapSizeToDefault();
    }

    public ServerApp(){
        pe = new ProcessExecutor();
    }

    public void installPostgreSql(){
        try {
            aptUpdate();
            System.out.println("PostgreSql is installing...");
            String installResult = pe.command("sudo","apt-get","install","postgresql","postgresql-contrib").readOutput(true).execute().outputUTF8();
            System.out.println(installResult + "\nPostgreSql installation is finished");
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void aptUpdate(){
        try {
            String updateResult = pe.command("sudo","apt-get","update").readOutput(true).execute().outputUTF8();
            System.out.println(updateResult);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void installNeo4J(){
        try {
            String neo4JRepoAdd = pe.command("sudo","wget","-O-","http://debian.neo4j.org/neotechnology.gpg.key").readOutput(true).execute().outputUTF8();
            String neo4JRepoAddEx = pe.command("sudo","apt-get","add","-").readOutput(true).execute().outputUTF8();
            aptUpdate();
            String neo4JInstall = pe.command("sudo","apt-get","install","neo4j").readOutput(true).execute().outputUTF8();
            System.out.println(neo4JRepoAdd + "\n" + neo4JRepoAddEx + "\n" + neo4JInstall);
            changeHeapSize();
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void startNeo4J(){
        try {
            String neo4JStart = pe.command("sudo","service","neo4j","start").readOutput(true).execute().outputUTF8();
            System.out.println("Waiting for neo4J");
            TimeUnit.MINUTES.sleep(5);
            System.out.println(neo4JStart);
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void stopNeo4J(){
        try {
            String neo4JStop = pe.command("sudo","service","neo4j","stop").readOutput(true).execute().outputUTF8();
            Thread.sleep(1000);
            System.out.println(neo4JStop);
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase(String userName,String password){
        try {
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", userName, password);
            Statement statement = c.createStatement();
            statement.executeUpdate("CREATE DATABASE naneb");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeHeapSize(){
        try {
            stopNeo4J();
            String resultInitHeapSize = pe.command("sudo","sed","-i","-e","s/#dbms.memory.heap.initial_size=512m/dbms.memory.heap.initial_size=2048m/g","/etc/neo4j/neo4j.conf",">","/tmp/neo4j.conf").readOutput(true).execute().outputUTF8();
            String resultMaxHeapSize = pe.command("sudo","sed","-i","-e","s/#dbms.memory.heap.max_size=512m/dbms.memory.heap.max_size=2048m/g","/etc/neo4j/neo4j.conf",">","/tmp/neo4j.conf").readOutput(true).execute().outputUTF8();
            startNeo4J();
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void changeHeapSizeToDefault(){
        try {
            stopNeo4J();
            String resultInitHeapSize = pe.command("sudo","sed","-i","-e","s/dbms.memory.heap.initial_size=2048m/#dbms.memory.heap.initial_size=2048m/g","/etc/neo4j/neo4j.conf",">","/tmp/neo4j.conf").readOutput(true).execute().outputUTF8();
            String resultMaxHeapSize = pe.command("sudo","sed","-i","-e","s/dbms.memory.heap.max_size=2048m/#dbms.memory.heap.max_size=2048m","/etc/neo4j/neo4j.conf",">","/tmp/neo4j.conf").readOutput(true).execute().outputUTF8();
            startNeo4J();
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void runBisParser(){
        BisParser bp = new BisParser();
        String[] args = {"0","/bis.html"};
        try {
            bp.bisParserRunner(args,postgresUserName,postgresPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runPsqlToNeo4J(){
        PsqlToNeo4JHelper converter = new PsqlToNeo4JHelper();
        Long timeBegin = System.currentTimeMillis();
        converter.setSettings(neo4JUserName,neo4JPassword);
        converter.psqlSetSettings(postgresUserName,postgresPassword);
        converter.run();
        System.out.println("Exporting csv files and creating graph database took " + (System.currentTimeMillis() - timeBegin) / 1000 + " seconds.");
    }
}