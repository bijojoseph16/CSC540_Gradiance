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
			 case 1: TA.showProfile(ip,studentid);return;
			 case 2: TA.showCourse(ip,studentid); return;
			 case 3: TA.logout(studentid,ip); return;
			 default:
				 System.out.println("Invalid Input. Try again...");
			 }
		}
	}
	
	public static void showProfile(Scanner ip,int studentid) {
		
		  String getstudentinfo="Select * from student where student_id=" +studentid;
		  try {
	          
			  PreparedStatement pstmt=Connect.getConnection().prepareStatement(getstudentinfo);
	          ResultSet rs=pstmt.executeQuery();
	          
		          while(rs.next()) {
		        	  	System.out.print("1. First Name :");
		          	System.out.println(rs.getString("firstname"));
		          	System.out.print("2. Last Name :");
		          	System.out.println(rs.getString("lastname"));
		          	System.out.print("3. Userid :");
		          	System.out.println(rs.getString("userId"));
		          }
	          }
	          catch(SQLException e) {
	          	e.printStackTrace();
	          }
		  
			System.out.println("0. Go Back");			
			int gb=ip.nextInt();
			if(gb==0) {
				TA.showHomePage(ip,studentid);
				return;
			}
	}
	
	public static void showCourse(Scanner ip,int studentid) {
		
		System.out.println("Please enter Course ID: ");
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
		        	 System.out.println(rs.getString("course_name"));
	         }
	         
	         while(rs2.next()) {
				System.out.println("2.Start Date: ");
				System.out.println(rs2.getString("start_date"));
				System.out.println("3. End Date: ");
				System.out.println(rs2.getString("end_date"));			
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
			case 5: TA.enrollDropStudent(ip,courseinput,studentid); 
			case 6: TA.viewReport(ip,courseinput,studentid);
		}
		
	}

	public static void logout(int studentid,Scanner ip) {
		Login.startPage(ip);
	
	}
	
	public static void viewExercise(Scanner ip,int studentid,int courseinput) {
		System.out.println("0. Go Back");
		String getExCnt = "Select count(*) as \"exCnt\" from exercise where ex_id in (select exercise_id from course_has_exercise where course_id="+courseinput+")";
		String getexercise="Select * from exercise where ex_id in (select exercise_id from course_has_exercise where course_id="+courseinput+")";
		 try {
			  PreparedStatement ps = Connect.getConnection().prepareStatement(getExCnt);
			  ResultSet rs1 = ps.executeQuery();
			  rs1.next();
			  if(rs1.getInt("exCnt") > 0) {
				  
			  }
			 
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
				return;
			}else {
				System.out.println("Invalid Input. Try Again");
				TA.viewExercise(ip, studentid, courseinput);
				return;
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

	public static void enrollDropStudent(Scanner ip,int course,int studentid) {
	System.out.println("1. Enroll student");
	System.out.println("2. Drop Student");
	System.out.println("0. Go Back");
	
	int x=ip.nextInt();
	switch(x) {
	case 1: TA.enrollStudent(ip,course, studentid);
	case 2: TA.dropStudent(ip,course, studentid);
	case 0: TA.showCourse(ip,studentid);
	
	}
	
	}
	
	public static void enrollStudent(Scanner ip,int course, int studentid) {
		System.out.println("1. Enter Student Id for enrollment");
		int id=ip.nextInt();
		 String checkIfUgStudent = " select count(*) as ug_student from ug where student_id="+id;
		 String addUgStudent="Insert into ug_enrolled (student_id,course_id) values ("+id+","+course+")";
		 String checkIfPgStudent = " select count(*) as pg_student from pg where student_id="+id;
		 String addPgStudent="Insert into pg_enrolled (student_id,course_id) values ("+id+","+course+")";
		 try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(checkIfUgStudent);
	          ResultSet rs=pstmt.executeQuery();
	         while(rs.next()) {
	          if(rs.getString("ug_student").equals("1")) {
	        	  PreparedStatement pstmt2=Connect.getConnection().prepareStatement(addUgStudent);
		          ResultSet rs2=pstmt2.executeQuery();
		          System.out.println("Undergrad Student enrolled");
	 		 }
	          else {
	        	  PreparedStatement pstmt3=Connect.getConnection().prepareStatement(checkIfPgStudent);
		          ResultSet rs3=pstmt3.executeQuery();
		          while(rs3.next()) {
		        	  PreparedStatement pstmt4=Connect.getConnection().prepareStatement(addPgStudent);
			          ResultSet rs4=pstmt4.executeQuery();
		          System.out.println("Postgrad Student enrolled");
		          }
	          }
	          
		 }
		 }
		 catch(SQLException e) {
	          	e.printStackTrace();
	          }
		 System.out.println("Press 0 to go back");
		 int x=ip.nextInt();
		 if(x==0) {
			 TA.showHomePage(ip, studentid);
		 }
		
	}
	
	
	public static void dropStudent(Scanner ip,int course, int studentid) {
		System.out.println("1. Enter Student Id for student to be dropped");
		int id=ip.nextInt();
		 String checkIfUgStudent = " select count(*) as ug_student from ug where student_id="+id;
		 String deleteUgStudent="Delete from ug_enrolled where student_id="+ id+" and course_id="+course;
		 String checkIfPgStudent = " select count(*) as pg_student from pg where student_id="+id;
		 String deletePgStudent="Delete from pg_enrolled where student_id="+id+" and course_id="+course;
		 try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(checkIfUgStudent);
	          ResultSet rs=pstmt.executeQuery();
	         while(rs.next()) {
	          if(rs.getString("ug_student").equals("1")) {
	        	  PreparedStatement pstmt2=Connect.getConnection().prepareStatement(deleteUgStudent);
		          ResultSet rs2=pstmt2.executeQuery();
		          System.out.println("Undergrad Student deleted");
	 		 }
	          else {
	        	  PreparedStatement pstmt3=Connect.getConnection().prepareStatement(checkIfPgStudent);
		          ResultSet rs3=pstmt3.executeQuery();
		          while(rs3.next()) {
		        	  PreparedStatement pstmt4=Connect.getConnection().prepareStatement(deletePgStudent);
			          ResultSet rs4=pstmt4.executeQuery();
		          System.out.println("Postgrad Student deleted");
		          }
	          }
	          
		 }
		 }
		 catch(SQLException e) {
	          	e.printStackTrace();
	          }
		 System.out.println("Press 0 to go back");
		 int x=ip.nextInt();
		 if(x==0) {
			 TA.showHomePage(ip, studentid);
		 }

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
