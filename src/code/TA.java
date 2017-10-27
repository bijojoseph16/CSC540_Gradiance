package code;

import java.sql.*;

import java.util.*;
import dbconnect.Connect;

public class TA{

	public static void showHomePage(Scanner ip) throws NoSuchElementException {
	while(true) {
		 System.out.println("1. View Profile");
		 System.out.println("2. View Course");
		 System.out.println("3. Logout");
		 
		 
		 int choice=ip.nextInt();
		 
		 switch(choice) {
		 case 1: TA.showProfile();
		
		 case 2: TA.showCourse();
			 
		 case 3: TA.logout();
			 
		 
		 
		 }
	}

	}
	
	public static void showProfile() {
		
		
	}
	
	public static void showCourse() {
		
		
	}

	public static void logout() {
	
	
	}
	
	public static void viewExercise() {
		
		
	}

	public static void enrollDropStudent() {
	
	
	}

	public static void viewReport() {
	
	
	}

	public static void goBack() {
	
	
	}

}
