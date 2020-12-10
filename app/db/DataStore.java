package db;

import models.Observation;
import models.Whale;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataStore {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String URL = "jdbc:h2:mem";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String WHALE_CONNECTION = "jdbc:h2:~/whale";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";



    public ResultData setup(boolean dropTables) throws IOException, SQLException {
        Connection dbConnection = null;
        try {
            dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        boolean pass = true;
        try {
            stmt = dbConnection.createStatement();
            if(dropTables){
                stmt.execute("DROP TABLE WHALES IF EXISTS");
            }
            stmt.execute("CREATE TABLE IF NOT EXISTS WHALES(id int primary key, species varchar(255), " +
                    "weight integer, " +
                    "gender varchar(255))");
            stmt.close();
            stmt2 = dbConnection.createStatement();
            if(dropTables) {
                stmt2.execute("DROP TABLE OBSERVATIONS IF EXISTS");
            }
            stmt2.execute("CREATE TABLE IF NOT EXISTS OBSERVATIONS(id int primary key, location varchar(255), " +
                    "numWhales integer, " +
                    "date varchar(255), time varchar(255))");
            stmt2.close();
            stmt3 = dbConnection.createStatement();
            if(dropTables) {
                stmt3.execute("DROP TABLE SIGHTINGS IF EXISTS");
            }
            stmt3.execute("CREATE TABLE IF NOT EXISTS SIGHTINGS(whale_id int, " +
                    "observation_id int," +
                    "PRIMARY KEY(whale_id, observation_id)" +
                    ")");
            stmt3.close();
            dbConnection.commit();
        } catch (Exception e) {
            pass = false;
            e.printStackTrace();
        } finally {
            try {
                dbConnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        assert(pass);

        return new ResultData(this.getWhales(), this.getObservations());
    }

    public void addWhale(Whale w) throws SQLException {
        Connection dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        //String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        PreparedStatement pStmt = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        boolean pass = true;
        try {
            String sql = "INSERT INTO WHALES(id, species, weight, gender) VALUES(?,?,?,?)";
            System.out.println("sql:"+sql);
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setInt(1,(int)w.id);
            pStmt.setString(2,w.species);
            pStmt.setInt(3,(int)w.weight);
            pStmt.setString(4,w.gender);
            System.out.println("+pStmt.toString():"+pStmt.toString());
            int res = pStmt.executeUpdate();
            System.out.println("res:"+res);
        } catch (SQLException e) {
            pass = false;
            String err = "Exception Message " + e.getLocalizedMessage();
            System.out.println(err);
        } catch (Exception e) {
            pass = false;
            e.printStackTrace();
        } finally {
            try {
                dbConnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        assert(pass);
    }

    public void addWhales(List<Whale> newWhales) throws SQLException {
        Connection dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        //String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        PreparedStatement pStmt = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        boolean pass = true;
        try {
            for(Whale w : newWhales){
                String sql = "INSERT INTO WHALES(id, species, weight, gender) VALUES(?,?,?,?)";
                System.out.println("sql:"+sql);
                pStmt = dbConnection.prepareStatement(sql);
                pStmt.setInt(1,(int)w.id);
                pStmt.setString(2,w.species);
                pStmt.setInt(3,(int)w.weight);
                pStmt.setString(4,w.gender);
                System.out.println("+pStmt.toString():"+pStmt.toString());
                int res = pStmt.executeUpdate();
                System.out.println("res:"+res);
            }
            pStmt.close();
            dbConnection.close();
        } catch (SQLException e) {
            pass = false;
            String err = "Exception Message " + e.getLocalizedMessage();
            System.out.println(err);
        } catch (Exception e) {
            pass = false;
            e.printStackTrace();
        } finally {
            try {
                dbConnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        assert(pass);
    }

    public List<Whale> getWhales() throws IOException, SQLException {
        Connection dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        List<Whale> whaleList = new ArrayList<>();
        String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        System.out.println(getClass().getClassLoader().getResource("logging.properties"));
        //Connection connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        boolean pass = true;
        try (var con = dbConnection;
             var stm = con.createStatement();
             var rs = stm.executeQuery("SELECT * from WHALES")) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String species = rs.getString(2);
                int weight = rs.getInt(3);
                String gender = rs.getString(4);
                Whale w = new Whale(species, weight, gender);;
                whaleList.add(w);
            }
        } catch (SQLException ex) {
            pass = false;
            var lgr = Logger.getLogger(DataStore.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        assert(pass);
        dbConnection.close();
        return whaleList;
    }

    public void addObservation(Observation o) throws SQLException {
        Connection dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        //String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        PreparedStatement pStmt = null;
        PreparedStatement pStmt2 = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        boolean pass = true;
        try {
            String sql = "INSERT INTO OBSERVATIONS(id, location, numWhales, date, time) VALUES(?,?,?,?,?)";
            System.out.println("sql:"+sql);
            pStmt = dbConnection.prepareStatement(sql);
            pStmt.setInt(1,(int)o.id);
            pStmt.setString(2,o.location);
            pStmt.setInt(3,(int)o.whales.size());
            pStmt.setString(4,o.date);
            pStmt.setString(5,o.time);
            System.out.println("+pStmt.toString():"+pStmt.toString());
            int res = pStmt.executeUpdate();
            System.out.println("res:"+res);
            for(Whale w:o.whales){
                String sql2 = "INSERT INTO SIGHTINGS(whale_id, observation_id) VALUES(?,?)";
                pStmt2 = dbConnection.prepareStatement(sql2);
                pStmt2.setInt(1,(int)w.id);
                pStmt2.setInt(2,(int)o.id);
                System.out.println("+pStmt2.toString():"+pStmt2.toString());
                res = pStmt2.executeUpdate();
                System.out.println("res:"+res);
            }
        } catch (SQLException e) {
            pass = false;
            String err = "Exception Message " + e.getLocalizedMessage();
            System.out.println(err);
        } catch (Exception e) {
            pass = false;
            e.printStackTrace();
        } finally {
            try {
                dbConnection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        assert(pass);
    }

    /*
        TODO:
            OPTIMIZE THIS QUERY
            Can do this with one SQL Statement

            We will need an sql statement that joins the OBSERVATIONS and SIGHTINGS
             so that each observation has a list of whales that was observed
    */
    public List<Observation> getObservations() throws IOException, SQLException {
        Connection dbConnection = DriverManager.getConnection(WHALE_CONNECTION, DB_USER, DB_PASSWORD);
        List<Observation> observationList = new ArrayList<>();
        HashMap<Integer, Observation> obMap = new HashMap<Integer, Observation>();
        String logPath = String.valueOf((getClass().getClassLoader().getResource("logging.properties")));
        System.out.println(getClass().getClassLoader().getResource("logging.properties"));
        //Connection connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        boolean pass = true;
        try (var con = dbConnection;
             var stm = con.createStatement();
             /*
                We will need an sql statement that joins the OBSERVATIONS and SIGHTINGS
                so that each observation has a list of whales that was observed
             */
             var rs = stm.executeQuery("SELECT * from SIGHTINGS S " +
                     "LEFT JOIN OBSERVATIONS O ON S.observation_id=O.id " +
                     "LEFT JOIN WHALES W ON W.id=whale_id " +
                     "ORDER BY S.observation_id ")) {
            while (rs.next()) {
                System.out.println("rs.findColumn(\"id\"):"+rs.findColumn("id"));
                System.out.println("rs.findColumn(\"species\"):"+rs.findColumn("species"));
                System.out.println("rs.findColumn(\"weight\"):"+rs.findColumn("weight"));
                System.out.println("rs.findColumn(\"gender\"):"+rs.findColumn("gender"));
                System.out.println("rs.findColumn(\"location\"):"+rs.findColumn("location"));
                System.out.println("rs.findColumn(\"date\"):"+rs.findColumn("date"));
                System.out.println("rs.findColumn(\"time\"):"+rs.findColumn("time"));
                int id = rs.getInt(rs.findColumn("id"));
                String species = rs.getString(rs.findColumn("species"));
                int weight = rs.getInt(rs.findColumn("weight"));
                String gender = rs.getString(rs.findColumn("gender"));
                String location = rs.getString(rs.findColumn("location"));
                String date = rs.getString(rs.findColumn("date"));
                String time = rs.getString(rs.findColumn("time"));
                Whale w = new Whale(species, weight, gender);
                Observation o =  new Observation(new ArrayList<>(), date, time, location);
                if(obMap.containsKey(Integer.valueOf((int)o.id))){
                    Observation ob = obMap.get(Integer.valueOf((int)o.id));
                    ob.whales.add(w);
                }else {
                    Observation ob = new Observation(new ArrayList<>(), date, time, location);
                    ob.whales.add(w);
                    obMap.put(Integer.valueOf((int)o.id), ob);
                }
            }
        } catch (SQLException ex) {
            pass = false;
            var lgr = Logger.getLogger(DataStore.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        assert (pass);
        dbConnection.close();
        System.out.println("observationList.size():"+observationList.size());
        observationList = new ArrayList(obMap.values());
        return observationList;
    }

}
