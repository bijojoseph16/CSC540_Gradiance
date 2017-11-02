package code;

import java.sql.*;

import java.util.*;
import dbconnect.Connect;

public class TA{

	public static void showHomePage(Scanner ip,int studentid) throws NoSuchElementException {
	while(true) {
		 System.out.println("1. View Profile");
		 System.out.println("2. View Course");
		 System.out.println("3. Logout");
		 
		 
		 int choice=ip.nextInt();
		 
		 switch(choice) {
		 case 1: TA.showProfile(ip,studentid);
		
		 case 2: TA.showCourse(ip,studentid);
			 
		 case 3: TA.logout(studentid);	 
		 
		 }
	}

	}
	
	public static void showProfile(Scanner ip,int studentid) {
		
		   String getstudentinfo="Select * from student where student_id=" +studentid;
		  try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(getstudentinfo);
	          ResultSet rs=pstmt.executeQuery();
	          
	          while(rs.next()) {
	        	  System.out.println("1. First Name :");
	          	System.out.print(rs.getString("firstname"));
	          	System.out.println("2. Last Name :");
	          	System.out.print(rs.getString("lastname"));
	          	System.out.println("3. Userid :");
	          	System.out.print(rs.getString("userId"));
	          	//Connect.close(pstmt);
	          	
	          }
	          }
	          catch(SQLException e) {
	          	e.printStackTrace();
	          }
		  
//			System.out.println("2. Last Name :");
//			final String getlastname="Select lastname from student where student_id=" +studentid;
//			System.out.println("3. Student Id :" + studentid);
//			
			System.out.println("0. Go Back");
//			
			int gb=ip.nextInt();
//			
			if(gb==0) {
				TA.showHomePage(ip,studentid);
			}
		  
		
	}
	
	public static void showCourse(Scanner ip,int studentid) {
		System.out.println("Please enter Course ID: ");
		//Scanner scan=new Scanner(System.in);
		int courseinput=ip.nextInt();
		
		String getcourseinfo="Select * from course where c_id="+courseinput;
		String getcoursesd="Select * from course_has_duration where course_id="+courseinput;
		 try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(getcourseinfo);
	          ResultSet rs=pstmt.executeQuery();
	          
	          PreparedStatement pstmt2=Connect.getConnection().prepareStatement(getcoursesd);
	          ResultSet rs2=pstmt2.executeQuery();
	         while(rs.next()) {
	        	 System.out.println("1.Course Name: ");
	        	 System.out.print(rs.getString("course_name"));
	         }
	         while(rs2.next()) {
		System.out.println("2.Start Date: ");
		System.out.print(rs2.getString("start_date"));
		System.out.println("3. End Date: ");
		System.out.print(rs2.getString("end_date"));
		
		 }
		 }
		 catch(SQLException e) {
	          	e.printStackTrace();
	          }
		 	System.out.println("4. View Exercise");
			System.out.println("5. Enroll/Drop a Student");
			System.out.println("6. View Report");
			System.out.println("0. Go Back");
		int option=ip.nextInt();
		switch(option) {
		case 0: TA.showHomePage(ip,studentid);
		case 4: TA.viewExercise(ip,studentid,courseinput);
		//case 5: 
		case 6: TA.viewReport(ip,courseinput,studentid);
		}
		
	}

	public static void logout(int studentid) {
	
	
	}
	
	public static void viewExercise(Scanner ip,int studentid,int courseinput) {
		System.out.println("0. Go Back");
		String getexercise="Select * from exercise where ex_id in (select exercise_id from course_has_exercise where course_id="+courseinput+")";
		 try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(getexercise);
	          ResultSet rs=pstmt.executeQuery();
	          
	          showResultsSet(rs);
		 }
		 catch(SQLException e) {
	          	e.printStackTrace();
	          }
		int gbsc=ip.nextInt();
		
		if(gbsc==0) {
			TA.showCourse(ip,studentid);
		}
		
	}
	 public static void showResultsSet(ResultSet rs) {
	    	ResultSetMetaData rsmd;
			try {
				int numRows = 0;
				rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				while (rs.next()) {
					numRows += 1;
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = rs.getString(i);
						System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
					}
					System.out.println("");
		    	   }
				
				if(0 == numRows) {
					System.out.println("No Records found !!!");
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	public static void enrollDropStudent() {
	
	
	}

	public static void viewReport(Scanner ip,int course_id,int studentid) {
		
	String getReport="Select * from student_submits_exercise s, course_has_exercise c where c.course_id= "+course_id+"and s.ex_id=c.exercise_id";
	 try {
         PreparedStatement pstmt=Connect.getConnection().prepareStatement(getReport);
         ResultSet rs=pstmt.executeQuery();
         
         showResultsSet(rs);
	 }
	 catch(SQLException e) {
         	e.printStackTrace();
         }
	 System.out.println("Press 0 to go back");
	 int x=ip.nextInt();
	 if(x==0) {
		 TA.showCourse(ip,studentid);
	 }
	}

	

}
