package code;
import java.util.*;

public class Instructor {

     /*
      * Show Instructor homepage with options
      */
     public static void showHomePage() {
         System.out.println("*****Instructor Homepage******");
         System.out.println("1.View Profile");
         System.out.println("2. View Course");
         System.out.println("3. Add Course");
         //Doubtful, as students can only be added when we have
         //a course ID
         System.out.println("4. Enroll a Student");
         System.out.println("5. Drop a Student");
         System.out.println("6. Search Question in Question Bank");
         System.out.println("7. Add Question in Question Bank");
         System.out.println("8. Logout");
     }

     /**********Functions called from showHomePage*********************/
     
     /*
      * Show Instructor homepage with options
      */
     public static void viewProfile() {
         //Show First Name, lastname and employee ID
         System.out.println("Enter 0 to go back");
     }
     
     /*
      * Show all the courses or Add a new Course
      */
     public static void viewCourse() {
         //Ask course ID 
         System.out.println("Enter Course ID");
         // if valid show course details
         //Show Course details
         // - Start and End Date
         
         // else display error message 

         System.out.println("1.View exercise");
         System.out.println("2.Add exercise");
         System.out.println("3.View TA");
         System.out.println("4.Add TA");
         System.out.println("");
     }

     /*
      * Add a course
      */
     public static void addCourse(Scanner ip) {
         System.out.println("Enter CourseID");
         System.out.println("Course Name");
         System.out.println("Enter Start Date");
         System.out.println("Enter End Date");
     }
     /*
      * Enroll or drop a student from Course
      */
     public static void enrollStudent() {
      
     }
     
     /*
      * Drop or drop a student from Course
      */
     public static void dropStudent() {
      
     }

     /*
      * Instructor can search a question 
      */
     public static void searchQuestioninQB() {
         
     }
     
     /*
      * Instructor can add a question 
      */
     public static void addQuestioninQB() {
         
     }

     /*
      * 
      */
     public static void logOut() {
         
     }
     
     /**********Functions called from viewCourse*****************/
     
     /*
      * //show all the relevant course details
      */
     public static void showExisitingCourse() {
         
     }

     
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

}
