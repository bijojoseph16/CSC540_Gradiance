package dbconnect;

import java.sql.*;
import java.util.*;

public class Connect {
    static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
    static Connection conn = null;
    static Statement stmt = null;
    
    public static Connection getConnection() {
        if(conn == null) {
            try {
            //Load the Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Scanner sc = new Scanner(System.in);
      
            //Login into Oracle database
            System.out.println("Enter Login credientials for oracle database");
            System.out.println("Enter Unity ID");
            String username = sc.next();
            System.out.println("Enter password");
            String password = sc.next();
           
            //Establish connection to database
            conn = DriverManager.getConnection(jdbcURL,username,password );
            
            // Create a statement object that will be sending your
            // SQL statements to the DBMS
            stmt = conn.createStatement();
            
            //Success
            System.out.println("Connection Established");
            
            
            } catch (Throwable oops) {;
                //oops.printStackTrace();
                //oops.getMessage();
                System.out.println("invalid username/password; logon denied");
                getConnection();
            }
        }
        
        return conn;      
    }
    
    public static Statement getStatement() {
        if(stmt == null) {
            getConnection();
        }
        return stmt;
    }
    
    //Close statement
    public static void close(Statement stmt) {
        if(stmt != null) {
            try {
                stmt.close();
            }catch(Throwable whatever) {
                
            }
        }
    }

    public static void closeStatement() {
        if(stmt != null) {
            try {
                stmt.close();
            }catch(Throwable whatever) {
                
            }
        }
    }

    //Close Connection
    public static void close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            }catch(Throwable whatever) {
                
            }
        }
    }

    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            }catch(Throwable whatever) {
                
            }
        }
    }

    //Close Resultset
    static void close(ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            }catch(Throwable whatever) {
                
            }
         }
    }
    
    
}
