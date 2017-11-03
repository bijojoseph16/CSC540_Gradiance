package code;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Pattern;
import java.sql.*;

import dbconnect.Connect;

public class Instructor {
    
     /*
     Functionalities
     
     *[Done] View Profile
     *[] View Course
       -[Done] View Course Name
       -[Done] View Course Start Date
       -[] View Exercise
       -[] Add Exercise
       -[] View TA
       -[] Add TA
       -[] Enroll Student
       -[] Drop Student
       -[Done] Go Back
     *[Done] Add Course
     *[]Enroll Student
     *[]Drop Student
     *[]Search Qusetion in QB
     *[]Add Question in QB
    
     */
    
     /*
      * Show Instructor homepage with options
      */
     public static void showHomePage(Scanner ip, int instructorID) {
         System.out.println("*****Instructor Homepage******");
         System.out.println("1.View Profile");
         System.out.println("2. View Course/Add Course");
         //Doubtful, as students can only be added when we have
         //a course ID.So I do ask for courseID
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
                 //This long function name is justifed as 
                 //it from where 
                 Instructor.enrollOrDropStudentFromInstructorLogin(ip, instructorID);
                 
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
             //Invalid user inputs are handled
             System.out.println("Please enter valid input");
             Instructor.viewOrAddCourse(ip, instructorID);
             
         }catch(Exception e) {
             e.printStackTrace();
         }
         
     }
     
     /*
      * Enroll or drop a student from course the instructor teaches
      */
     public static void enrollOrDropStudentFromInstructorLogin(Scanner ip, int instructorID) {
         PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             int cID;
             int callerFlag = 1;
             System.out.print("Enter Course ID:");
             String courseID = ip.next();
             System.out.println("1. Enroll a Student in course");
             System.out.println("2. Drop a Student from course");
             System.out.print("Choice:");
             //Get the unique c_id to delete student from appropriate course
             ps = Connect.getConnection().prepareStatement(Queries.getCourseByCourseID);
             ps.setString(1, courseID);
             rs = ps.executeQuery();
             rs.next();             
             cID = rs.getInt("c_ID");
             
             int choice = Integer.parseInt(ip.next());
             
             if(1 == choice) {
                 Instructor.enrollStudent(ip, cID, instructorID, callerFlag);
             }
             else if(2 == choice) {
                 Instructor.dropStudent(ip, cID, instructorID, callerFlag);
             }
             else {
                 System.out.println("Invalid input");
                 Instructor.showHomePage(ip, instructorID);
             }
                   
         } catch(NumberFormatException e) {
             System.out.println("Enter valid input");
             Instructor.showHomePage(ip, instructorID);
         }
         catch (Exception e) {
             e.printStackTrace();
         } finally {
             Connect.close(ps);
             Connect.close(rs);
         }
     }

     public static void searchOrAddQuestionInQB(Scanner ip) {
         
     }

     /************Functions called from viewOrAddCourse****************************/
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
             System.out.println("3.Enroll/Drop a Student");
             System.out.println("4.View Report");
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


     /**********Functions called from view Exercise ******************************/
     public static void showExerciseDetails(int exID) {
         
     }
     public static void addQuestionToExercise(int exID) {
         
     }
     public static void removeQuestionFromExercise(int exID) {
         
     }

     /***********Function called from Enroll/Drop student or from ViewCourse******/
     /*
      * Enroll or drop a student from Course
      * callerFlag is user to identify the method that calls enrollStudent
      * It is used to automatically return to the method that called it.
      * @param callerFlag = 1, then the method was called form Instructor-> logged in page
      * @param callerFlag = 2, then the method was called from Instructor-> viewProfile 
      */
     public static void enrollStudent(Scanner ip, int cID, int instructorID, int callerFlag) {
         PreparedStatement psUGStudent = null;
         ResultSet rsUGStudent = null;
         PreparedStatement psPGStudent = null;
         ResultSet rsPGStudent = null;
         PreparedStatement psEnrollUGStudent = null;
         PreparedStatement psEnrollPGStudent = null;
         
         try {
           System.out.print("Enter Student ID:");
           int studentID = Integer.parseInt(ip.next());
           System.out.print("Enter Student's first name:");
           String studentFirstName = ip.next();
           System.out.print("Enter Student's last name:");
           String studentLastName = ip.next();
           
           psUGStudent = Connect.getConnection().prepareStatement(Queries.checkIfUgStudent);
           psUGStudent.setInt(1, studentID);
           rsUGStudent = psUGStudent.executeQuery();
           rsUGStudent.next();
           
           psPGStudent = Connect.getConnection().prepareStatement(Queries.checkIfPgStudent);
           psPGStudent.setInt(1, studentID);
           rsPGStudent = psPGStudent.executeQuery();
           rsPGStudent.next();

           if(1 == rsUGStudent.getInt("ug_student")) {
               try {
                 psEnrollUGStudent = Connect.getConnection().prepareStatement(Queries.enrollUGStudent);
                 psEnrollUGStudent.setInt(1, studentID);
                 psEnrollUGStudent.setInt(2, cID);
                 psEnrollUGStudent.executeQuery();
                 System.out.println("UG Student enrolled successfully\n");
                 Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (Exception e) {
                   System.out.println("Could not enroll UG Student\n");
                   //Instructor.enrollOrDropStudent(ip);
                   e.printStackTrace();
               } finally {
                   Connect.close(psUGStudent);
                   Connect.close(rsUGStudent);
                   Connect.close(psEnrollUGStudent);
                   
               }
           }
           else if(1 == rsPGStudent.getInt("pg_student")) {
               try {
                 psEnrollPGStudent = Connect.getConnection().prepareStatement(Queries.enrollPGStudent);
                 psEnrollPGStudent.setInt(1, studentID);
                 psEnrollPGStudent.setInt(2, cID);
                 psEnrollPGStudent.executeQuery();
                 System.out.println("PG Successfully enrolled Student");
                 Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (SQLException e) {
                   System.out.println("Could not enroll PG Student as he is TA");
                   //Instructor.enrollOrDropStudent(ip);
                   //e.printStackTrace();
                   
               } catch(Exception e){}
                 
                 finally {
                   Connect.close(psPGStudent);
                   Connect.close(rsPGStudent);
                   Connect.close(psEnrollPGStudent);
                   
               }
           }
           else {
               System.out.println("Could not execute query as student does not exist");
               Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
           }
           Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
          /*
           //If UG_enrolled references UG and PG_enrolled refrences PG
           //then this check can be done on database side, we need not do it in
           //application
           try {
               psEnrollUGStudent = Connect.getConnection().prepareStatement(Queries.enrollUGStudent);
               psEnrollUGStudent.setInt(1, studentID);
               psEnrollUGStudent.setInt(2, cID);
               psEnrollUGStudent.executeQuery();               
           } catch(SQLException e) {
               if (e.getSQLState().startsWith("23")) {
                   Connect.close(psEnrollUGStudent);
                   try {
                       //Before this you will have to add code
                       //Code that checks if a TA is not currently enrolled in that 
                       //course
                       psEnrollPGStudent = Connect.getConnection().prepareStatement(Queries.enrollPGStudent);
                       psEnrollPGStudent.setInt(1, studentID);
                       psEnrollPGStudent.setInt(2, cID);
                       psEnrollPGStudent.executeQuery();                                          
                   } catch(SQLException ex) {
                       System.out.println("Student does not exist");
                       ex.printStackTrace();
                   } finally {
                       Connect.close(psEnrollPGStudent);
                   }
               } finally {
                   Connect.close(psEnrollUGStudent);
               }
               else {
                   System.out.println("Invalid Input");
                   e.printStackTrace();
               }
           }
          */
           
         } catch (Exception e) {
             e.printStackTrace();
         }         
     }
     
     /*
      * Drop a student from Course
      */
      public static void dropStudent(Scanner ip, int c_id, int instructorID, int callerFlag) {

          //On Success return to the caller method
          Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);

     }
      /*
       * Helper method to go back to appropriate screen after succes/failure in
       * add/drop student
       */
      public static void goBackAfterEnrollOrDrop(Scanner ip,int callerFlag, int instructorID) {
          //On Success or if student does not exist return to the caller method
          if(callerFlag == 1) {
            Instructor.showHomePage(ip, instructorID);
          }
          else if(callerFlag == 2) {
            Instructor.viewCourse(ip, instructorID);
          }          
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

      /**************Functions called from viewCourse**********************************/
      
      public static void viewOrAddExercise(Scanner ip, int c_id) {
          
      }
      
      public static void viewOrAddTA(Scanner ip, int c_id) {
          
      }
      
      public static void enrollOrDropStudent(Scanner ip, int c_id) {
          
      }

      /******************Functions called from viewOrAddExercise**************************/
      /*
       * View Exercises in a Course
       */
      public static void viewExercise(Scanner ip, int c_id) {
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
      public static void addExercise(Scanner ip, int c_id) {
          //Select 0 to go back
          //Ask for exercise type
          //Ask number of questions in execise
          //
      }
      
      /*
       * 
       */
      public static void viewReport() {
          
      }     
      
      /***********Functions called from viewOrAddTA******************************************/
      public static void viewTA(Scanner ip, int c_id) {
          
      }
      
      //Check should be put on PG_ENROLLED table so that a student 
      //cannot ne both TA and enrolled for the course
      public static void addTA(Scanner ip, int c_id) {
          
      }
      
      
   
}
