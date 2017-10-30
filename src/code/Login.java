package code;
import java.sql.*;

import java.util.*;

import javax.swing.JOptionPane;

import dbconnect.Connect;

public class Login {
	

    /*********************************************************************
     * Function: startPage
     * Arguments: Scanner ip 
     * Returns: void
     * Description:Method to display the initial menu options to the user. 
     ********************************************************************/
    public static void startPage(Scanner ip) throws NoSuchElementException{
    	
        System.out.println("\n*****Gradience*****");
        System.out.println("1. Login as Instructor");
        System.out.println("2. Login as TA");
        System.out.println("3. Login as Student");
        System.out.println("4. Exit");
                
        try {
	        	//Take Use input
	        	System.out.print("Choice : ");
	        	int choice = Integer.parseInt(ip.next());
	        	
	        	if(1 == choice) {
	        		//Enter username and password
	        		System.out.println("Enter Username: ");
		        	String username = ip.next();
		        	System.out.println("Enter Password: ");
		        	String password = ip.next();
		        	
	        		//set this values using PreparedStatement for the instructor
		        	PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.checkInstructor);
		        	ps.setString(1,username);
		        	ps.setString(2,password);
		        	ResultSet results = ps.executeQuery();	
		        results.next();
		        	int i=results.getInt("prof_exists");
		        	if(i==1)
		        	{
		        	 	System.out.println("Professor-  Username and Password exists");
		        		PreparedStatement psInst=Connect.getConnection().prepareStatement(Queries.getInstructorByUIdPass);
		        		psInst.setString(1,username);
			        	psInst.setString(2,password);
			        showResultsSet(psInst.executeQuery());
			        	ResultSet rs=psInst.executeQuery();
			        rs.next();
			        	int instructorId = rs.getInt("professor_id");
			        	Instructor.showHomePage(ip, instructorId);	
		        	}
		        	else{
		        		System.out.println("Incorrect Username or Password ");
		        		Login.startPage(ip);
		        	}
		        	
	        	}else if(2 == choice) {
	        		//Enter username and password
	        		System.out.println("Enter Username: ");
		        	String username = ip.next();
		        	System.out.println("Enter Password: ");
		        	String password = ip.next();
	        		//set this values using PreparedStatement for the TA
		        	PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.checkTA);
		        	ps.setString(1,username);
		        	ps.setString(2,password);
		        	ResultSet results = ps.executeQuery();	
		        results.next();
		        	int i=results.getInt("ta_exists");
		        	if(i==1)
		        	{
		        	 	System.out.println("TA -  Username and Password exists");
		        		PreparedStatement psInst=Connect.getConnection().prepareStatement(Queries.getTAByUIdPass);
		        		psInst.setString(1,username);
			        	psInst.setString(2,password);
			        showResultsSet(psInst.executeQuery());
			        	ResultSet rs=psInst.executeQuery();
			        	rs.next(); 
			        	int TaId = rs.getInt("Student_id");
			        	TA.showHomePage(ip);
			        	
		        	}
		        	else{
		        		System.out.println("Incorrect Username or Password ");
		        		Login.startPage(ip);
		        	}
	
	        	}else if(3 == choice) {
	        		//Enter username and password
	        		System.out.println("Enter Username: ");
		        	String username = ip.next();
		        	System.out.println("Enter Password: ");
		        	String password = ip.next();
		        	
	        		//set this values using PreparedStatement for the student
		        	PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.checkStudent);
		        	ps.setString(1,username);
		        	ps.setString(2,password);
		        	ResultSet results = ps.executeQuery();	
		        results.next();
		        	int i=results.getInt("exists");
		        	if(i == 1)
		        	{
		        	 	System.out.println("Student -  Username and Password exists");
		        		PreparedStatement psInst=Connect.getConnection().prepareStatement(Queries.getStudentByUIdPass);
		        		psInst.setString(1,username);
			        	psInst.setString(2,password);
			        	showResultsSet(psInst.executeQuery());
			        	ResultSet rs=psInst.executeQuery();
			        	rs.next();
			        	int StudentId = rs.getInt("Student_id");
			        	Student.showHomePage(ip, StudentId);
		        	}
		        	else{
		        		
		        		System.out.println("Incorrect Username or Password ");
		        		Login.startPage(ip);
		        	}
	
	        	}else if(4 == choice) {
	        		//Exiting
	        		System.out.println("Exiting...");
	        		Connect.closeConnection();
	        		
	        	}else {
	        		System.out.println("Invalid Input");
	        		Login.startPage(ip);
	        	}

        }catch(SQLException e) {
        		e.printStackTrace();
        }
    }
    
    /********************************************************
     * Function: showResultsSet
     * Arguments: ResultSet 
     * Returns: void
     * Description: To display the records in the resultset
     *******************************************************/
    public static void showResultsSet(ResultSet rs) {
    	ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
				}
				System.out.println("");
	    	   }
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
