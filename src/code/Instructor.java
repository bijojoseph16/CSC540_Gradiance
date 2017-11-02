package code;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Pattern;
import java.sql.*;

import dbconnect.Connect;

public class Instructor {

     /*
      * Show Instructor homepage with options
      */
     public static void showHomePage(Scanner ip, int instructorID) {
         System.out.println("*****Instructor Homepage******");
         System.out.println("1.View Profile");
         System.out.println("2. View Course/Add Course");
         //Doubtful, as students can only be added when we have
         //a course ID
         System.out.println("3. Enroll a Student/Drop a Student");     
         System.out.println("4. Search Question in Question Bank/Add Question in Question Bank");
         System.out.println("5. Logout");
         
         //Only 1, 2 are valid inputs
         try {
             //Take User input
             System.out.print("Choice : ");
             int choice = Integer.parseInt(ip.next());
             
             if(1 == choice) {
                 Instructor.viewProfile(ip, instructorID);
                 
             } else if(2 == choice){
                 Instructor.viewOrAddCourse(ip, instructorID);
                 
             } else if(3 == choice) {
                 Instructor.enrollOrDropStudent(ip, instructorID);
                 
             } else if(4 == choice) {
                 Instructor.searchOrAddQuestionInQB(ip);
                 
             }else if(5 == choice) {
                 Login.startPage(ip);
                 
             }
             else {
                 System.out.println("Invalid input");
                 Instructor.showHomePage(ip, instructorID);
                 
             }
             
     }catch(NumberFormatException e) {
         System.out.println("Invalid input");
         Instructor.showHomePage(ip, instructorID);                 
     }catch(Exception e) {
         e.printStackTrace();
     }

     }

     /**********Functions called from showHomePage*********************/
     
     /*
      * Show Instructor Profile
      */
     public static void viewProfile(Scanner ip, int instructorID) {
         //Show First Name, Last Name and Employee ID         
         System.out.println("*************** Instructor Profile ****************");
         System.out.println("Instructor Id: " + instructorID);
         
         try {
             //set this values using PreparedStatement for the instructor
             PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.getInstructorByUId);
             ps.setInt(1,instructorID);
             ResultSet results = ps.executeQuery();  
             results.next();
             System.out.println("Firstname: " + results.getString("firstname"));
             System.out.println("Lastname: " + results.getString("lastname"));
             System.out.println("Username: " + results.getString("userid"));
             Connect.close(ps);
             Connect.close(results);
  
             //Take Use input
             System.out.println("Enter 0 Go Back");
             System.out.print("Choice: ");
             int choice = Integer.parseInt(ip.next());
             
             if(0 == choice) {
                 Instructor.showHomePage(ip, instructorID);
             }else {
                 System.out.println("Invalid input.");
                 Instructor.viewProfile(ip, instructorID);
             }
             
         }catch(Exception e) {
             e.printStackTrace();
         }

     }
     
     /*
      * View or Add Course
      */
     public static void viewOrAddCourse(Scanner ip, int instructorID) {
         try {
             
           System.out.println("1.View Course");
           System.out.println("2.Add Course");
           System.out.println("Press 0 to Go Back");
           System.out.print("Choice: ");
         
           //Todo
           //Insert an assertion such that user will only enter an interger
           
           int choice = Integer.parseInt(ip.next());
         
           if(1 == choice) {
             Instructor.viewCourse(ip, instructorID);
           }
           else if(2 == choice) {
             Instructor.addCourse(ip, instructorID);
           }
           else if(0 == choice) {
             Instructor.showHomePage(ip, instructorID);    
           }
           else {
             System.out.println("Please enter valid input");
             Instructor.viewOrAddCourse(ip, instructorID);
           }
           
         }catch(NumberFormatException e) {
             System.out.println("Please enter valid input");
             Instructor.viewOrAddCourse(ip, instructorID);
             
         }catch(Exception e) {
             e.printStackTrace();
         }
         
     }
     
     /*
      * Enroll or drop a student from course the instructor teaches
      */
     public static void enrollOrDropStudent(Scanner ip, int instructorID) {
         try {
             System.out.println("1. Enroll a Student from course");
             System.out.println("2. Drop a Student from course");
             
         } catch(NumberFormatException e) {
             System.out.println("Enter valid input");
             Instructor.enrollOrDropStudent(ip, instructorID);
         }
         catch (Exception e) {
             e.printStackTrace();
         }
     }

     public static void searchOrAddQuestionInQB(Scanner ip) {
         
     }
     /************Functions called from viewOrAddCourse*******************/
     /*
      * Show the relevant details for course
      */
     public static void viewCourse(Scanner ip, int instructorID) {
         //Ask course ID
         int cID;
         try {
           System.out.println("Enter Course ID:");
           // if valid show course details
           //Show Course details
           // - Start and End Date
         
           // else display error message 
           String courseID = ip.next();
         
           PreparedStatement psCourseExists = Connect.getConnection().prepareStatement(Queries.courseExists);
           psCourseExists.setString(1, courseID);
           ResultSet rsCourseExists = psCourseExists.executeQuery();
           rsCourseExists.next();
         
           //If the course exists show the course name
           //start date and end date.
           //Also show additional options
           //If course does not exist show error message 
           //and ask for new courseID
           if(1 == rsCourseExists.getInt("course_exists")) {
         
             PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.getCourseByCourseID);
             ps.setString(1, courseID);
             ResultSet results = ps.executeQuery();
             results.next();
             
             System.out.println("Course Name: " + results.getString("Course_name"));
             cID = results.getInt("c_ID");
             System.out.println("Course unique idenrifier " + cID);
             PreparedStatement psCourseDuration = Connect.getConnection().prepareStatement(Queries.getCourseDuration);
             psCourseDuration.setInt(1, cID);
             ResultSet resultsCourseDuration = psCourseDuration.executeQuery();
             resultsCourseDuration.next();
         
             String startDate = resultsCourseDuration.getString("start_date");
             String endDate = resultsCourseDuration.getString("end_date");
      
             System.out.println("Start Date: " + startDate);
             System.out.println("End Date: " + endDate);
                  
             Connect.close(ps);
             Connect.close(results);
             Connect.close(psCourseDuration);
             Connect.close(resultsCourseDuration);
             Connect.close(psCourseExists);
             Connect.close(rsCourseExists);
             
             //Take User input
             System.out.println("");
             System.out.println("1.View exercise/Add exercise");
             System.out.println("2.View TA/Add TA");
             System.out.println("Enter 0 Go Back");
             System.out.print("Choice: ");
             int choice = Integer.parseInt(ip.next());
         
             if(0 == choice) {
               Instructor.viewOrAddCourse(ip, instructorID);
             } else {
                 System.out.println("Invalid input.");
                 Instructor.viewCourse(ip, instructorID);
             }
           }
           else {
               System.out.println("Course does not exist.");
               Connect.close(psCourseExists);
               Connect.close(rsCourseExists);
               Instructor.viewCourse(ip, instructorID);
           }
           
         } catch (NumberFormatException e) {
             System.out.println("Please enter valid input");
             Instructor.viewCourse(ip, instructorID);
         } catch (Exception e) {
             e.printStackTrace();
         }
       
     }

     /*
      * Add a course accept courseID, courseName,
      * the level of course, maxStudents,
      * startDate and endDate
      */
     public static void addCourse(Scanner ip, int instructorID) {

         PreparedStatement psAddCourse = null;
         PreparedStatement psAddCourseDuration = null;
         PreparedStatement psGetCourseByC_ID = null;
         ResultSet rsGetCourseByC_ID = null;
         PreparedStatement psAddDuration = null;
         
         ip.nextLine();
         try {
             
           System.out.println("Enter CourseID:");
           String courseID = ip.nextLine();

           System.out.println("Course Name:");
           String courseName = ip.nextLine();
 
           System.out.println("Enter CourseLevel (Grad/Undergrad):");
           String courseLevel = ip.nextLine();
           
           System.out.println("Enter maximum number of students:");
           String maxStudents = ip.nextLine();
           
           psAddCourse = Connect.getConnection().prepareStatement(Queries.addCourse);
           psAddCourse.setString(1, courseID);
           psAddCourse.setString(2,courseName);
           psAddCourse.setString(3, courseLevel);
           psAddCourse.setInt(4, Integer.parseInt(maxStudents));
           psAddCourse.executeQuery();
                      
           //Ask date to satisfy the default format requirement
           //Default: YYYY-MM-DD 
           System.out.println("Enter Course Start Date(yyyy-mm-dd):");
           String startDate = ip.nextLine();         
           System.out.println("Enter Course End Date(yyyy-mm-dd):");
           String endDate = ip.nextLine();
           
           psAddDuration = Connect.getConnection().prepareStatement(Queries.addDuration);
           psAddDuration.setDate(1, java.sql.Date.valueOf(startDate));
           psAddDuration.setDate(2, java.sql.Date.valueOf(endDate));
           psAddDuration.executeQuery();
                  

           //If the course exists procceed to add adding the
           //course in course has duration.
           //If course does not exist show error message 
           //and ask for new courseID      
           psGetCourseByC_ID = Connect.getConnection().prepareStatement(Queries.getCourseByCourseID);
           psGetCourseByC_ID.setString(1, courseID);
           rsGetCourseByC_ID = psGetCourseByC_ID.executeQuery();
           rsGetCourseByC_ID.next();
           
           //After a course has been added to course table and duration added to duratio table
           //Add c_id and duration to course has duration. If this is not followed 
           //there will be integrity constraint violations.
           int cID = rsGetCourseByC_ID.getInt("c_ID");
           
           
           psAddCourseDuration = Connect.getConnection().prepareStatement(Queries.addCourseDuration);
           psAddCourseDuration.setInt(1, cID);
           psAddCourseDuration.setDate(2, java.sql.Date.valueOf(startDate));
           psAddCourseDuration.setDate(3, java.sql.Date.valueOf(endDate));             
           psAddCourseDuration.executeQuery();
           
           Connect.close(psAddCourse);
           Connect.close(psAddDuration);
           Connect.close(psAddCourseDuration);
           Connect.close(psGetCourseByC_ID);
           Connect.close(rsGetCourseByC_ID);
           
           System.out.println("Course has been successfully added");
           int choice ;
           
           do {
             System.out.println("Press 0 to Go back");
             choice = Integer.parseInt(ip.nextLine());
             if(choice == 0) {
                 Instructor.viewOrAddCourse(ip, instructorID);
             }
           }while (choice != 0);
           
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("Could not insert Course, Press Enter to try again");
             Instructor.addCourse(ip, instructorID);
             
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             /*
             Connect.close(psAddCourse);
             Connect.close(rsAddCourse);
             Connect.close(psCourseExists);
             Connect.close(rsCourseExists);
             Connect.close(psAddCourseDuration);
             Connect.close(rsAddCourseDuration);
             Connect.close(psGetCourseByC_ID);
             Connect.close(rsGetCourseByC_ID);
             */
         }
     }

     /**********Functions called from viewCourse*****************/
          
     /*
      * View Exercises in a Course
      */
     public static void viewExercise() {
         //Display all exercises related to course
         //Ask for exercise ID
         System.out.println("Enter Exercise ID");
         //if valid show details of exercise 
         
         //else ask to renter Ecercise ID
         
         System.out.println("1.Add Question to exercise");
         System.out.println("2.Remove Question from exercise");
         System.out.println("0.To go back");
     }

     /*
      * Add Exercise to a Course
      */
     public static void addExercise() {
         //Select 0 to go back
         //Ask for exercise type
         //Ask number of questions in execise
         //
     }
     
     /*
      * View information of TA
      */
     public static void viewTA() {
         
     }
     
     /*
      * 
      */
     public static void addTA() {
         
     }
     
     /*
      * 
      */
     public static void viewReport() {
         
     }     
     /**********Functions called from view Exercise ********/
     public static void showExerciseDetails(int exID) {
         
     }
     public static void addQuestionToExercise(int exID) {
         
     }
     public static void removeQuestionFromExercise(int exID) {
         
     }

     /***********Function called from Enroll/Drop student or from ViewCourse******/
     /*
      * Enroll or drop a student from Course
      */
     public static void enrollStudent(Scanner ip, int c_id) {
      
     }
     
     /*
      * Drop a student from Course
      */
      public static void dropStudent(Scanner ip, int c_id) {
      
     }
     
      /**************Functions called from SearchOrAddQuestionInDB******************/
      /*
       * Instructor can search a question 
       */
      public static void searchQuestionInQB(Scanner ip, int qID) {
          
      }
       
      /*
       * Instructor can add a question 
       */
      public static void addQuestionInQB(Scanner ip) {
          
      }


   
}
