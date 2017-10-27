package code;
import java.sql.*;

import java.util.*;
import dbconnect.Connect;

public class Login {

    // Method to display the initial menu options to the user.
    public static void startPage(Scanner ip) throws NoSuchElementException{
    	
        System.out.println("\n*****Gradience*****");
            System.out.println("1. Admin/Student Login");
            System.out.println("2. Exit");
            System.out.print("Choice : ");
            try {
            PreparedStatement pstmt=Connect.getConnection().prepareStatement(Queries.getstudents);
            ResultSet rs=pstmt.executeQuery();
            
            while(rs.next()) {
            	
            	System.out.println(rs.getString(2));
            	
            	
            }
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
    }
}
