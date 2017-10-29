package code;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dbconnect.Connect;

public class Student {

	//show Student homepage
	public static void showHomePage(Scanner ip, int studentId) {
		
		System.out.println("*****Student Homepage******");
        System.out.println("1.View Profile");
        System.out.println("2. View Course");
        System.out.println("3. Logout");
        
        try {
        	
	        	//Take Use input
	        	System.out.print("Choice : ");
	        	int choice = Integer.parseInt(ip.next());
	        	
	        	if(1 == choice) {
	        		Student.showProfile(ip, studentId);
	        	}else if(2 == choice){
	        		Student.showCourse(ip, studentId);
	        	}else if(3 == choice) {
	        		Login.startPage(ip);
	        	}else {
	        		System.out.println("Invalid input");
	        		Student.showHomePage(ip, studentId);
	        	}
        		
        }catch(Exception e) {
        	
        }

	}
	
	//show student Profile Page
	public static void showProfile(Scanner ip, int studentId) {
		
		//Show firstName, LastName and Id
		
		System.out.println("*************** Student Profile ****************");
		System.out.println("Student Id: " + studentId);
		
		try {
			//set this values using PreparedStatement for the instructor
			PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.getStudentByUId);
			ps.setInt(1,studentId);
			ResultSet results = ps.executeQuery();	
			results.next();
			System.out.println("Firstname: " + results.getString("firstname"));
			System.out.println("Lastname: " + results.getString("lastname"));
			System.out.println("Username: " + results.getString("userid"));
			
			//Take Use input
			System.out.println("Enter 0 Go Back");
			System.out.print("Choice: ");
			int choice = Integer.parseInt(ip.next());
			
			if(0 == choice) {
				Student.showHomePage(ip, studentId);
			}else {
				System.out.println("Invalid input. Enter 0 to Go Back");
				Student.showProfile(ip, studentId);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//show student course
	public static void showCourse(Scanner ip, int studentId) {
		//Show firstName, LastName and Id
		
		System.out.println("*************** Student's Course ****************");
		
		
		try {
			//set this values using PreparedStatement for the instructor
			PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.getStudentByUId);
			ps.setInt(1,studentId);
			ResultSet results = ps.executeQuery();	
			results.next();
			System.out.print("Student Id: " + studentId);
			System.out.print(", Firstname: " + results.getString("firstname"));
			System.out.print(", Lastname: " + results.getString("lastname"));
			System.out.println(", Username: " + results.getString("userid"));
			
			//set this values using PreparedStatement for the instructor
			PreparedStatement psUg = Connect.getConnection().prepareStatement(Queries.checkIfUgStudent);
			PreparedStatement psPg = Connect.getConnection().prepareStatement(Queries.checkIfPgStudent);
			psUg.setInt(1,studentId);ResultSet resUg = psUg.executeQuery();resUg.next();
			psPg.setInt(1,studentId);ResultSet resPg = psPg.executeQuery();resPg.next();
			int cntUg = resUg.getInt("ug_student");
			int cntPg = resPg.getInt("pg_student");
			
			if(cntUg == 1) {
				
				PreparedStatement psUgCourses = Connect.getConnection().prepareStatement(Queries.getUgStudentCourses);
				psUgCourses.setInt(1, studentId);
				showResultsSet(psUgCourses.executeQuery());
				ResultSet resUgCourses = psUgCourses.executeQuery();
				
				List<Integer> courseList = new ArrayList<Integer>();
				while(resUgCourses.next()) {
					courseList.add(resUgCourses.getInt("c_id"));	
				}
				
				/*for (int i = 0; i < courseList.size(); ++i)
					System.out.println(courseList.get(i));
				*/
				
				//Take Use input
				System.out.println("0. Go Back");
				System.out.println("1. Select a course Id to view details");
				System.out.print("Choice: ");
				
				
				int choice = Integer.parseInt(ip.next());
				
				if(0 == choice) {
					Student.showHomePage(ip, studentId);
				}else if(courseList.contains(choice)){
					Student.showCourseHW(ip, studentId, choice);
				}else {
					System.out.println("Invalid input. Enter again");
					Student.showCourse(ip, studentId);
				}
				
			}else if(cntPg == 1) {
				
				PreparedStatement psPgCourses = Connect.getConnection().prepareStatement(Queries.getPgStudentCourses);
				psPgCourses.setInt(1, studentId);
				showResultsSet(psPgCourses.executeQuery());
				ResultSet resPgCourses = psPgCourses.executeQuery();
				
				List<Integer> courseList = new ArrayList<Integer>();
				while(resPgCourses.next()) {
					courseList.add(resPgCourses.getInt("c_id"));	
				}
				
				/*for (int i = 0; i < courseList.size(); ++i)
					System.out.println(courseList.get(i));
				*/
				
				//Take Use input
				System.out.println("0. Go Back");
				System.out.println("1. Select a course Id to view details");
				System.out.print("Choice: ");
				
				
				int choice = Integer.parseInt(ip.next());
				
				if(0 == choice) {
					Student.showHomePage(ip, studentId);
				}else if(courseList.contains(choice)){
					Student.showCourseHW(ip, studentId, choice);
				}else {
					System.out.println("Invalid input. Enter again");
					Student.showCourse(ip, studentId);
				}
				
			}else {
				
				System.out.println("The student is not enrolled in any course");
				Student.showHomePage(ip, studentId);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//show studentCourseHW
	public static void showCourseHW(Scanner ip, int studentId, int courseId) {
		
		System.out.println("***************** Student HW options ******************");
		System.out.println("0. Go Back");
		System.out.println("1. Current Open HW");
		System.out.println("2. Past Closed HW");
		System.out.println("Choice: ");
		
		int choice = ip.nextInt();
		
		if(choice == 0) {
			Student.showCourse(ip, studentId);
		}else if(choice == 1) {
			Student.showCurrHW(ip, studentId, courseId);
		}else if(choice == 2) {
			Student.showPastHW(ip, studentId, courseId);
		}else {
			System.out.println("Invalid Input. Enter Again");
			Student.showCourseHW(ip, studentId, courseId);
		}
		
	}
	
	//show studentHW
	public static void showPastHW(Scanner ip, int studentId, int courseId) {
		
		System.out.println("************* Student's Past HWs ***************");
		
		try {

			PreparedStatement psPgCourses = Connect.getConnection().prepareStatement(Queries.getUgStudentPastHwforCourse);
			psPgCourses.setInt(1, studentId);
			psPgCourses.setInt(2, courseId);
			showResultsSet(psPgCourses.executeQuery());
			//ResultSet resPgCourses = psPgCourses.executeQuery();
			
			System.out.println("Press 0 to Go Back");
			
			int choice = ip.nextInt();
			if(0 == choice) {
				Student.showCourseHW(ip, studentId, courseId);
			}else {
				System.out.println("Invalid Input. Enter your choice again");
				Student.showPastHW(ip, studentId, courseId);
			}
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	//show student pastHW
	public static void showCurrHW(Scanner ip, int studentId, int courseId) {
		
			System.out.println("************* Student's Current HW ***************");
		
		try {

			PreparedStatement psPgCourses = Connect.getConnection().prepareStatement(Queries.getPgStudentCourses);
			psPgCourses.setInt(1, studentId);
			showResultsSet(psPgCourses.executeQuery());
			ResultSet resPgCourses = psPgCourses.executeQuery();
			
			List<Integer> hwList = new ArrayList<Integer>();
			while(resPgCourses.next()) {
				hwList.add(resPgCourses.getInt("c_id"));	
			}
			
			System.out.println("Press 0 to Go Back");
			System.out.println("Press the HW no. to attempt");
			
			int choice = ip.nextInt();
			if(0 == choice) {
				Student.showCourseHW(ip, studentId, courseId);
			}else if(hwList.contains(choice)) {
				
			}else {
				System.out.println("Invalid Input. Enter your choice again");
				Student.showCurrHW(ip, studentId, courseId);
			}
		
		}catch(Exception e) {
			
			
		}
		
	}
	
	//attempt Given HW
	public static void attemptHW(Scanner ip, int studentId, int courseId) {
		
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
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
