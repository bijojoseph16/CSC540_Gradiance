package code;
import java.util.*;
import java.util.regex.Pattern;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import dbconnect.Connect;

public class Instructor {
    
     static int instId = 0;
     /*
     Functionalities
     
     *[Done] View Profile
     *[Done] View Course
       -[Done] View Course Name
       -[Done] View Course Start Date
       -[Pending] View Exercise
       -[Pending] Add Exercise
       -[Done] View TA
       -[Done] Add TA
       -[Done] Enroll Student
       -[Done] Drop Student
       -[Done] Go Back
     *[Done] Add Course
     *[Done]Enroll Student
     *[Done]Drop Student
     *[Done]Search Qusetion in QB
     *[Done]Add Question in QB
    
     */
    
     /*
      * Show Instructor homepage with options
      */
	
	
	
     public static void showHomePage(Scanner ip, int instructorID) {
    	 	
    	 	instId = instructorID;
         System.out.println("*****Instructor Homepage******");
         System.out.println("1.View Profile");
         System.out.println("2. View Course/Add Course");
         System.out.println("3. Enroll a Student/Drop a Student");     
         System.out.println("4. Search Question in Question Bank/Add Question in Question Bank");
         System.out.println("5. Logout");
         
 
         try {
             //Take User input
             System.out.print("Choice : ");
             int choice = Integer.parseInt(ip.next());
             
             if(1 == choice) {
                 Instructor.viewProfile(ip, instructorID);
                 return;
                 
             } else if(2 == choice){
                 Instructor.viewOrAddCourse(ip, instructorID);
                 return;
                 
             } else if(3 == choice) {
                 Instructor.enrollOrDropStudentFromInstructorLogin(ip, instructorID);
                 return;
                 
             } else if(4 == choice) {
                 Instructor.searchOrAddQuestionInQB(ip,instructorID);
                 return;
                 
             }else if(5 == choice) {
                 Login.startPage(ip);
                 return;
                 
             }
             else {
                 System.out.println("Invalid input");
                 Instructor.showHomePage(ip, instructorID);
                 return;                 
             }
             
     }catch(NumberFormatException e) {
         System.out.println("Invalid input");
         Instructor.showHomePage(ip, instructorID);
         return;
         
     }catch(Exception e) {
         System.out.println("Invalid input");
         Instructor.showHomePage(ip, instructorID);
         return;
     }

     }

     /**********Functions called from showHomePage*********************/
     
     /*
      * Show Instructor Profile
      * No need to check if instructor exists as it is check during login
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
  
             //Take User input
             System.out.println("Enter 0 Go Back");
             System.out.print("Choice: ");
             int choice = Integer.parseInt(ip.next());
             
             if(0 == choice) {
                 Instructor.showHomePage(ip, instructorID);
                 return;
                 
             }else {
                 System.out.println("Invalid input.");
                 Instructor.viewProfile(ip, instructorID);
                 return;
             }
             
         }catch(Exception e) {
             System.out.println("Something went wrong please try again.");
             //e.printStackTrace();
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
             Instructor.viewCourseMain(ip, instructorID);
             return;
           }
           else if(2 == choice) {
             Instructor.addCourse(ip, instructorID);
             return;
           }
           else if(0 == choice) {
             Instructor.showHomePage(ip, instructorID);
             return;
           }
           else {
             System.out.println("Please enter valid input");
           }
           
         }catch(NumberFormatException e) {
             //Invalid user inputs are handled
             System.out.println("Please enter valid input");
             
         }catch(Exception e) {
             System.out.println("Please enter valid input");
         }
         
         Instructor.viewOrAddCourse(ip, instructorID);
         return;         
     }
     
     /*
      * Enroll or drop a student from course the instructor teaches
      */
     public static void enrollOrDropStudentFromInstructorLogin(Scanner ip, int instructorID) {
         PreparedStatement ps = null;
         ResultSet rs = null;
         PreparedStatement psCourseExists = null;
         ResultSet rsCourseExists = null;
         try {
             int cID;
             int callerFlag = 1;
             System.out.print("Enter 0 to Go back\n");
             System.out.print("Enter Course ID(Ex CSC540):");
             
             String courseID = ip.next();
             if(courseID.equals("0")) {
                 Instructor.showHomePage(ip, instructorID);
                 return;
             }
             
             psCourseExists = Connect.getConnection().prepareStatement(Queries.instructorCanViewCourse);
             psCourseExists.setInt(1, instructorID);
             psCourseExists.setString(2, courseID);
             rsCourseExists = psCourseExists.executeQuery();
             rsCourseExists.next();
             
             if(1 == rsCourseExists.getInt("course_exists")) {
               System.out.println("0. Go Back");
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
                 return;
               }
               else if(2 == choice) {
                 Instructor.dropStudent(ip, cID, instructorID, callerFlag);
                 return;
                 
               } else if(0 == choice) {
                   Instructor.showHomePage(ip, instructorID);
                   return;
               }
               else {
                 System.out.println("Invalid input");     
               }
             } else {
                 System.out.println("No permission to view this course");
             }
                   
         } catch(NumberFormatException e) {
             System.out.println("Enter valid input");
             //Instructor.showHomePage(ip, instructorID);
         }
         catch (Exception e) {
             //e.printStackTrace();
             System.out.println("Enter valid input");
             
         } finally {
             Connect.close(ps);
             Connect.close(rs);
             Connect.close(psCourseExists);
             Connect.close(rsCourseExists);
         }
         
         Instructor.showHomePage(ip, instructorID);
         return;
     }

     public static void searchOrAddQuestionInQB(Scanner ip,int instructorID) {
         
         try {
           System.out.println("Press 1 to Search question in question bank");
           System.out.println("Press 2 to Add question to question bank");
           System.out.println("Press 0 to go back");
         
           int arg = Integer.parseInt(ip.next());
         
           if(arg ==1) {
        	 searchQuestionInQB(ip,instructorID);
        	 return;
           }
         
           else if(arg==2) {
        	 addQuestionInQB(ip,instructorID);
        	 return;
           }
         
           else if(arg==0) {
        	 Instructor.showHomePage(ip, instructorID);
        	 return;
           }
         } catch (NumberFormatException e) {
             System.out.println("Invalid Input");
             Instructor.searchOrAddQuestionInQB(ip, instructorID);
             return;
             
         } catch (Exception e) {
             //e.printStackTrace();
             System.out.println("Enter valid input");
             Instructor.searchOrAddQuestionInQB(ip, instructorID);
             return;

         }
     }

     /************Functions called from viewOrAddCourse****************************/
     /*
      * Show the relevant details for course
      */
     public static void viewCourseMain(Scanner ip, int instructorID) {
         //Ask course ID
         int cID;
         //Show the instructor all courses he has created
         PreparedStatement psCoursesCreatedByInstructor = null;
         ResultSet rsCoursesCreatedByInstructor = null;
         try {
             psCoursesCreatedByInstructor = Connect.getConnection().prepareStatement(Queries.viewCoursesCreated);
             psCoursesCreatedByInstructor.setInt(1, instructorID);
             rsCoursesCreatedByInstructor = psCoursesCreatedByInstructor.executeQuery();
             
             Instructor.showResultsSet(rsCoursesCreatedByInstructor);
         } catch (Exception e) {
             System.out.println("Invalid input");
         } finally {
             
         }
         try {
           System.out.println("*****View Course*****");
           System.out.println("Enter 0 to Go Back OR");
           System.out.println("Enter Course ID from courses listed above:");
           
           // if valid show course details
           //Show Course details
           // - Start and End Date
         
           // else display error message 
           String courseID = ip.next();
           //System.out.println(courseID);
           if(courseID.equals("0")) {
        	   	Instructor.showHomePage(ip, instructorID);
        	   	return;
           }
         
           PreparedStatement psCourseExists = Connect.getConnection().prepareStatement(Queries.courseExists);
           psCourseExists.setString(1, courseID);
           psCourseExists.setInt(2, instructorID);
           ResultSet rsCourseExists = psCourseExists.executeQuery();
           rsCourseExists.next();
           int courseExists =  rsCourseExists.getInt("course_exists");
           System.out.println(courseExists);
           Connect.close(psCourseExists);
           Connect.close(rsCourseExists);
           //If the course exists show the course name
           //start date and end date.
           //Also show additional options
           //If course does not exist show error message 
           //and ask for new courseID
           if(1 == courseExists) {
         
             PreparedStatement ps = Connect.getConnection().prepareStatement(Queries.getCourseByCourseID);
             
             ps.setString(1, courseID);
             ResultSet results = ps.executeQuery();
             results.next();
             
             System.out.println("Course Name: " + results.getString("Course_name"));
             cID = results.getInt("c_ID");
             //System.out.println("Course unique identifier " + cID);
             PreparedStatement psCourseDuration = Connect.getConnection().prepareStatement(Queries.getCourseDuration);
             psCourseDuration.setInt(1, cID);
             ResultSet resultsCourseDuration = psCourseDuration.executeQuery();
             resultsCourseDuration.next();
             //System.out.println("Getting course duration");
             String startDate = resultsCourseDuration.getString("start_date");
             String endDate = resultsCourseDuration.getString("end_date");
      
             System.out.println("Start Date: " + startDate);
             System.out.println("End Date: " + endDate);
                  
             Connect.close(ps);
             Connect.close(results);
             Connect.close(psCourseDuration);
             Connect.close(resultsCourseDuration);
             
             //Go to View course menu
             Instructor.viewCourse(ip, cID, instructorID);
             return;
      
           }
           else {
               System.out.println("Course does not exist/ Not created by You");
               Connect.close(psCourseExists);
               Connect.close(rsCourseExists);
               //Instructor.viewCourse(ip, instructorID);
           }
           
         } catch (NumberFormatException e) {
             System.out.println("Please enter valid input");
             //Instructor.viewCourse(ip, instructorID);
         } catch (Exception e) {
             System.out.println("Invalid input.");
             e.printStackTrace();
         }
         Instructor.viewCourseMain(ip, instructorID);
         return;
       
     }

     public static void viewCourse(Scanner ip, int cID, int instructorID) {
         //Ask course ID

           System.out.println("*****View Course Menu*****");
                 
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
               Instructor.viewCourseMain(ip, instructorID);
               return;
               
             } else if(1 == choice) {
            	Instructor.viewOrAddExercise(ip, cID, instructorID);
            	return;
            	 
             } if(2 == choice) {
                 Instructor.viewOrAddTA(ip, cID);
                 return;
                 
             } else if (3 == choice) {
                 Instructor.enrollOrDropStudentFromViewCourse(ip, cID, instructorID);
                 return;
                 
             }else if (4 == choice) {
                 Instructor.viewReport(ip, cID, instructorID);
                 return;
                 
             } else {
                 System.out.println("Invalid input.");
                 
             }
           
          
         Instructor.viewCourse(ip, cID, instructorID);
         return;
       
     }

     
     
     /*
      * Add a course accept courseID, courseName,
      * the level of course, maxStudents,
      * startDate and endDate
      */
     public static void addCourse(Scanner ip, int instructorID) {

         PreparedStatement psAddCourse = null;
         PreparedStatement psAddCourseDuration = null;
         PreparedStatement psInstructorCreatesCourse = null;
         
         PreparedStatement psDurationExists = null;
         ResultSet rsDurationExists = null;
         
         PreparedStatement psGetCourseByC_ID = null;
         ResultSet rsGetCourseByC_ID = null;
         PreparedStatement psAddDuration = null;
         
         PreparedStatement getMaxCourseId = null;
         ResultSet rsgetMaxCourseId = null;
         
         ip.nextLine();
         try {
           
        	  
        	  getMaxCourseId =  Connect.getConnection().prepareStatement(Queries.countOfCourses);
        	  rsgetMaxCourseId = getMaxCourseId.executeQuery();
        	  rsgetMaxCourseId.next();
        	  
        	  int valCId = rsgetMaxCourseId.getInt("maxCourseNum") + 1;
        	  System.out.println("Auto generated Id for course is " + valCId);
        	  
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
           psAddCourse.setInt(3,valCId);
           psAddCourse.setString(4, courseLevel);
           psAddCourse.setInt(5, Integer.parseInt(maxStudents));
           psAddCourse.executeQuery();
                      
           
           System.out.println("Adding topics for this course ");
           while(true) {
        	   	
        	   	System.out.println("Enter a topic from the following list");
        	   	PreparedStatement avlTopic = Connect.getConnection().prepareStatement(Queries.getTopicIdsForCourse);
        	   	avlTopic.setInt(1, valCId);
        	   	showResultsSet(avlTopic.executeQuery());
        	   	int topicId = ip.nextInt();
        	   	PreparedStatement psEx = Connect.getConnection().prepareStatement(Queries.addTopicToCourse);
        	   	psEx.setInt(1, valCId);
        	   	psEx.setInt(2, topicId);
        	   	psEx.executeQuery();
        	   	ip.nextLine();
        	   	System.out.println("Do you want to add more topics (y/n)");
        	   	
        	   	String input = ip.nextLine();
        	   	
        	   	if(input.equals("y")) {
        	   		continue;
        	   	}else {
        	   		break;
        	   	}
        	   		
           }
           
           
           //Ask date to satisfy the default format requirement
           //Default: YYYY-MM-DD 
           System.out.println("Enter Course Start Date(yyyy-mm-dd):");
           String startDate = ip.nextLine();         
           System.out.println("Enter Course End Date(yyyy-mm-dd):");
           String endDate = ip.nextLine();
           
           psDurationExists = Connect.getConnection().prepareStatement(Queries.durationExists);
           psDurationExists.setDate(1, java.sql.Date.valueOf(startDate));
           psDurationExists.setDate(2, java.sql.Date.valueOf(endDate));
           rsDurationExists = psDurationExists.executeQuery();
           rsDurationExists.next();
           
           if(0 == rsDurationExists.getInt("duration_exists")) {
             psAddDuration = Connect.getConnection().prepareStatement(Queries.addDuration);
             psAddDuration.setDate(1, java.sql.Date.valueOf(startDate));
             psAddDuration.setDate(2, java.sql.Date.valueOf(endDate));
             psAddDuration.executeQuery();
             Connect.close(psAddCourseDuration);
           }
                  

           //If the course exists procceed to add adding the
           //course in course has duration.
           //If course does not exist show error message 
           //and ask for new courseID      
           psGetCourseByC_ID = Connect.getConnection().prepareStatement(Queries.getCourseByCourseID);
           psGetCourseByC_ID.setString(1, courseID);
           rsGetCourseByC_ID = psGetCourseByC_ID.executeQuery();
           rsGetCourseByC_ID.next();
           
           //After a course has been added to course table and duration added to duration table
           //Add c_id and duration to course has duration. If this is not followed 
           //there will be integrity constraint violations.
           int cID = rsGetCourseByC_ID.getInt("c_ID");
           
           
           psAddCourseDuration = Connect.getConnection().prepareStatement(Queries.addCourseDuration);
           psAddCourseDuration.setInt(1, cID);
           psAddCourseDuration.setDate(2, java.sql.Date.valueOf(startDate));
           psAddCourseDuration.setDate(3, java.sql.Date.valueOf(endDate));             
           psAddCourseDuration.executeQuery();
           
           psInstructorCreatesCourse = Connect.getConnection().prepareStatement(Queries.instructorCreatesCourse);
           psInstructorCreatesCourse.setInt(1, cID);
           psInstructorCreatesCourse.setInt(2, instructorID);
           psInstructorCreatesCourse.executeQuery();
           
           Connect.close(psAddCourse);
           Connect.close(psAddDuration);
           //Connect.close(psAddCourseDuration);
           Connect.close(psGetCourseByC_ID);
           Connect.close(rsGetCourseByC_ID);
           Connect.close(psInstructorCreatesCourse);
           Connect.close(psDurationExists);
           Connect.close(rsDurationExists);
           
           System.out.println("Course has been successfully added");
           int choice ;
           
           do {
             System.out.println("Press 0 to Go back");
             choice = Integer.parseInt(ip.nextLine());
             if(choice == 0) {
                 Instructor.viewOrAddCourse(ip, instructorID);
                 return;
             }
           }while (choice != 0);
           
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println("Could not insert Course, Press Enter to try again");
             Instructor.viewOrAddCourse(ip, instructorID);
             return;
             
         } catch (Exception e) {
        	 	e.printStackTrace();
             System.out.println("Could not insert Course, Press Enter to try again");
             Instructor.viewOrAddCourse(ip, instructorID);
             return;

         } finally {
 
         }
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
           
           psUGStudent = Connect.getConnection().prepareStatement(Queries.checkIfUgStudentGivenName);
           psUGStudent.setInt(1, studentID);
           psUGStudent.setString(2, studentFirstName);
           psUGStudent.setString(3, studentLastName);
           rsUGStudent = psUGStudent.executeQuery();
           rsUGStudent.next();
           
           psPGStudent = Connect.getConnection().prepareStatement(Queries.checkIfPgStudentGivenName);
           psPGStudent.setInt(1, studentID);
           psPGStudent.setString(2, studentFirstName);
           psPGStudent.setString(3, studentLastName);

           rsPGStudent = psPGStudent.executeQuery();
           rsPGStudent.next();

           if(1 == rsUGStudent.getInt("ug_student")) {
               try {
                 psEnrollUGStudent = Connect.getConnection().prepareStatement(Queries.enrollUGStudent);
                 psEnrollUGStudent.setInt(1, studentID);
                 psEnrollUGStudent.setInt(2, cID);
                 psEnrollUGStudent.executeQuery();
                 System.out.println("UG Student enrolled successfully\n");
                 //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (SQLException e) {
                   if(e.getSQLState().startsWith("23")) {
                       System.out.println("Could not enroll UG Student, he is currently enrolled in the course\n");
                       
                   }
                   
               } catch (Exception e) {
                   System.out.println("Could not enroll UG Student\n");                   
                   
               } finally {
                   Connect.close(psUGStudent);
                   Connect.close(rsUGStudent);
                   Connect.close(psEnrollUGStudent);
                   
               }
           }
           else if(1 <= rsPGStudent.getInt("pg_student")) {
               System.out.println("PG student enrollment");
               try {
                 psEnrollPGStudent = Connect.getConnection().prepareStatement(Queries.enrollPGStudent);
                 psEnrollPGStudent.setInt(1, studentID);
                 psEnrollPGStudent.setInt(2, cID);
                 psEnrollPGStudent.executeQuery();
                 System.out.println("PG Successfully enrolled Student");
                 //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (SQLException e) {
                   if(e.getSQLState().startsWith("23")) {
                       System.out.println("Could not enroll PG Student, he is currently enrolled in the course\n");
                   
                   }
                   //Need to catch this PL-SQL exception
                   else {
                     System.out.println("Could not enroll PG Student as he is TA");

                   }
                   
               } catch(Exception e){
                   System.out.println("Could not enroll PG Student");
                   
               }
                 
                 finally {
                   Connect.close(psPGStudent);
                   Connect.close(rsPGStudent);
                   Connect.close(psEnrollPGStudent);
                   
               }
           }
           else {
               System.out.println("Could not execute query as student does not exist/ details entered are incorrect.");

           }
           Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID, cID);
         } catch (Exception e) {
             System.out.println("Invalid input");
             //e.printStackTrace();
         }         
     }
     
     /*
      * Drop a student from Course
      */
      public static void dropStudent(Scanner ip, int cID, int instructorID, int callerFlag) {

          //On Success return to the caller method
          PreparedStatement psUGStudent = null;
          ResultSet rsUGStudent = null;
          PreparedStatement psPGStudent = null;
          ResultSet rsPGStudent = null;
          PreparedStatement psDropUGStudent = null;
          PreparedStatement psDropPGStudent = null;
          
          try {
            System.out.print("Enter Student ID:");
            int studentID = Integer.parseInt(ip.next());
            System.out.print("Enter Student's first name:");
            String studentFirstName = ip.next();
            System.out.print("Enter Student's last name:");
            String studentLastName = ip.next();
            
            psUGStudent = Connect.getConnection().prepareStatement(Queries.checkIfUgStudentGivenName);
            psUGStudent.setInt(1, studentID);
            psUGStudent.setString(2, studentFirstName);
            psUGStudent.setString(3, studentLastName);
            rsUGStudent = psUGStudent.executeQuery();
            rsUGStudent.next();
            
            psPGStudent = Connect.getConnection().prepareStatement(Queries.checkIfPgStudentGivenName);
            psPGStudent.setInt(1, studentID);
            psPGStudent.setString(2, studentFirstName);
            psPGStudent.setString(3, studentLastName);
            rsPGStudent = psPGStudent.executeQuery();
            rsPGStudent.next();

            if(1 == rsUGStudent.getInt("ug_student")) {
                try {
                  psDropUGStudent = Connect.getConnection().prepareStatement(Queries.dropUGStudent);
                  psDropUGStudent.setInt(1, studentID);
                  psDropUGStudent.setInt(2, cID);
                  psDropUGStudent.executeQuery();
                  System.out.println("UG Student dropped successfully\n");
                  
                  
                } catch (SQLException e) {
                    System.out.println("Could not drop UG Student as he is not enrolled in course\n");
                    
                    
                } catch (Exception e) {
                    System.out.println("Could not drop UG Student\n");  
                    
                } finally {
                    Connect.close(psUGStudent);
                    Connect.close(rsUGStudent);
                    Connect.close(psDropUGStudent);
                    
                }
            }
            else if(1 <= rsPGStudent.getInt("pg_student")) {
                try {
                  psDropPGStudent = Connect.getConnection().prepareStatement(Queries.dropPGStudent);
                  psDropPGStudent.setInt(1, studentID);
                  psDropPGStudent.setInt(2, cID);
                  psDropPGStudent.executeQuery();
                  System.out.println("PG Successfully dropped Student");
                  
                } catch (SQLException e) {
                    System.out.println("Could not enroll PG Student as he is not enrolled in the course");
                    
                } catch(Exception e){
                    System.out.println("Could not enroll PG Student");
                    
                } finally {
                    Connect.close(psPGStudent);
                    Connect.close(rsPGStudent);
                    Connect.close(psDropPGStudent);
                    
                }
            }
            else {
                System.out.println("Could not execute query as student does not exist/Details entered are incorrect.");
            }
            
          } catch (Exception e) {
              System.out.println("Invalid input");
              //e.printStackTrace();
          }          
          Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID, cID);
          return;

     }
      /*
       * Helper method to go back to appropriate screen after succes/failure in
       * add/drop student
       */
      public static void goBackAfterEnrollOrDrop(Scanner ip,int callerFlag, int instructorID, int cID) {
          //On Success or if student does not exist return to the caller method
          if(callerFlag == 1) {
            Instructor.showHomePage(ip, instructorID);
            return;
          }
          else if(callerFlag == 2) {
            Instructor.viewCourse(ip, cID, instructorID);
            return;
            }          
      }
      
     
      /**************Functions called from SearchOrAddQuestionInDB******************/
      /*
       * Instructor can search a question 
       */
      public static void searchQuestionInQB(Scanner ip,int instructorID) {
    	  
          try {
            ip.nextLine();
            System.out.println("1. Search by question id ");
            System.out.println("2. Search by topic name");
            System.out.println("0. Go Back");
          
            int arg = Integer.parseInt(ip.next());
          
            if(arg==1) {
        	  System.out.println("Enter question id");
        	   
        	  int qid = Integer.parseInt(ip.next());
        	  
        	  try {
    	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(Queries.searchQuestionByQId);
    	          pstmt.setInt(1, qid);
    	          ResultSet rs=pstmt.executeQuery();  	          
    	          showResultsSet(rs);
    	          
    		 }
    		 catch(SQLException e) {
    		        System.out.println("Query cannot be executed.");
    	          	//e.printStackTrace();
    	          }
        	  
        	  System.out.println("Press 0 to go back");
        	  int arg2=ip.nextInt();
              
              if(arg2==0) {
            	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
              }
          }
          
          else if(arg==2) {
        	  ip.nextLine();
        	  System.out.println("Enter topic name");
        	  String topic=ip.nextLine();
        	  
        	  try {
    	          PreparedStatement pstmt2=Connect.getConnection().prepareStatement(Queries.searchQuestionbyTopic);
    	          pstmt2.setString(1,topic);
    	          ResultSet rs=pstmt2.executeQuery();
    	          
    	          showResultsSet(rs);
    		 }
    		 catch(SQLException e) {
    		        System.out.println("Query cannot be executed.");
    	          	//e.printStackTrace();
    	          }
        	  System.out.println("Press 0 to go back");
        	  int arg3=ip.nextInt();
              
              if(arg3==0) {
            	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
              }
        	  
          }
          
          else if(arg==0) {
        	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
        	  return;
          }
          } catch (NumberFormatException e) {
              System.out.println("Invalid Input");
              Instructor.searchQuestionInQB(ip, instructorID);
              return;
              
          } catch (Exception e) {
              System.out.println("Invalid Input");
              //e.printStackTrace();
          }
      }
       
      /*
       * Instructor can add a question 
       */
      public static void addQuestionInQB(Scanner ip,int instructorID) {
          
          try {
    	    ip.nextLine();
    	    int paramID;
    	    String parameters = "";
    	    String qsoln = "";
    	    String hint = "";
    	    String qans = "";
    	   
    	    PreparedStatement psAddQuestionToQB = null;
    	    PreparedStatement psAddQuestionToQuestionHasTopic = null;
    	    PreparedStatement psAddQuestionToParameter = null;
    	    PreparedStatement psListTopics = null;
    	    ResultSet rsListTopics = null;
    	    
            System.out.println("1. Enter question id");
            int qid=Integer.parseInt(ip.nextLine());
          
            System.out.println("2. Enter question type(0 for fixed and 1 for parametrized");
            int qtype = Integer.parseInt(ip.nextLine());
          
            System.out.println("3. Enter question text");
            String qtext = ip.nextLine();
            
            //Right now we only allow the instructor to add 4 options
            System.out.println("Please enter number of options. After entering each option press Enter.");
            int numOptions = Integer.parseInt(ip.nextLine());
            for(int i = 0; i < numOptions; i++) {
                qtext += "\n" + String.valueOf((char)(97 + i)) + ") " + ip.nextLine();
            }
            
            //System.out.print(qtext);
            System.out.println();
            System.out.println("4. Enter difficulty level of the question(Scale 1 - 6)");
            int qdif = Integer.parseInt(ip.nextLine());

            //Solution is a generic solution not the actual answer
            System.out.println("5. Enter solution of the question");
            qsoln = ip.nextLine();
            
            //To reach the answer
            System.out.println("6. Enter hint");
            hint = ip.nextLine();

            try {
                psAddQuestionToQB = Connect.getConnection().prepareStatement(Queries.addQuestion);
                //psAddQuestionToQuestionHasTopic = Connect.getConnection().prepareStatement(Queries.addQuestiontoTopic);
                
                psAddQuestionToQB.setInt(1, qid);
                psAddQuestionToQB.setString(2, qtext);
                psAddQuestionToQB.setString(3, qsoln);
                psAddQuestionToQB.setInt(4, qdif);
                psAddQuestionToQB.setString(5, hint);
                psAddQuestionToQB.setInt(6, qtype);

                psAddQuestionToQB.executeQuery();
                
            } catch (Exception e) {
            		System.out.println("Could not add question. invalid Input");
            		
            } finally {
                Connect.close(psAddQuestionToQB);
            }
            
            //List all the topics that question could beong to 
            System.out.println("Please select the Topic ID the question belongs to from the list:");
            try {
               psListTopics =  Connect.getConnection().prepareStatement(Queries.listTopics);
               rsListTopics = psListTopics.executeQuery();
               Instructor.showResultsSet(rsListTopics);
            } catch(Exception e) {
            		System.out.println("Could not add question. invalid Input");
                //e.printStackTrace();
            } finally {
               Connect.close(psListTopics);
               Connect.close(rsListTopics);
            }
  
            //Select the topic ID's the question can belong to
            while(true) {
                System.out.println("4. Enter topicID the question falls under");
                int topicID = Integer.parseInt(ip.nextLine());
                try {
                    psAddQuestionToQuestionHasTopic = Connect.getConnection().prepareStatement(Queries.addQuestiontoTopic);
                    psAddQuestionToQuestionHasTopic.setInt(1,topicID);
                    psAddQuestionToQuestionHasTopic.setInt(2,qid);
                    psAddQuestionToQuestionHasTopic.executeQuery();

                } catch(Exception e) {
                    System.out.println("Invalid input.");
                    //e.printStackTrace();
                } finally {
                    Connect.close(psAddQuestionToQuestionHasTopic);
                }
                System.out.println("Enter 0 to stop else press 1 to continue adding more Topic ID's.");
                int choice = Integer.parseInt(ip.nextLine());
                if(0 == choice) {
                    break;
                }
                else {
                    continue;
                }
              
              }
 
            if(qtype == 0) {
              try {
                  
                  System.out.println("8. Enter answer");
                  qans = ip.nextLine();
                  paramID = 0;
                  parameters = "NA";

                  psAddQuestionToParameter = Connect.getConnection().prepareStatement(Queries.addQuestionToParameter);
                  psAddQuestionToParameter.setInt(1, qid);
                  psAddQuestionToParameter.setInt(2, paramID);
                  psAddQuestionToParameter.setString(3, parameters);
                  psAddQuestionToParameter.setString(4, qans);         
                  
                  psAddQuestionToParameter.executeQuery();
                  
                  System.out.println("Question has been added to question bank!");
               }
               catch(SQLException e) {
                    System.out.println("Could not add question to QB");
                    //e.printStackTrace();
               } finally {
                    Connect.close(psAddQuestionToParameter);
               }
             
             int arg;
             do {
               System.out.println("Press 0 to go back");
               arg = Integer.parseInt(ip.nextLine());
              
               if(arg==0) {
                 Instructor.searchOrAddQuestionInQB(ip, instructorID);
                 return;
               }
             } while(arg != 0);
                           
            }
            
            else if(qtype == 1) {
                System.out.println("You selected parametrized question.Please enter parameters");
                System.out.println("Enter combinations of parameters for the question: ");
                paramID = 1;
                int paramIDIndex = Integer.parseInt(ip.nextLine());
                                
                //Assumption only 3 parameters possible
                for(int i = 0; i < paramIDIndex; i++) {
                    try {
                        System.out.println("Enter substitution for each parameter(Ex x = 1, y = 0..).");
                        parameters = ip.nextLine();
                        System.out.println("Enter answer for this parameter combination, it should be present in the option.");
                        qans = ip.nextLine();
                        try {
                            
                            psAddQuestionToParameter = Connect.getConnection().prepareStatement(Queries.addQuestionToParameter);
                            psAddQuestionToParameter.setInt(1, qid);
                            psAddQuestionToParameter.setInt(2, paramID);
                            psAddQuestionToParameter.setString(3, parameters);
                            psAddQuestionToParameter.setString(4, qans);
                            
                            psAddQuestionToParameter.executeQuery();
                            
                            System.out.println("Question parameters set have been added.");
                            paramID++;
                         }
                         catch(SQLException e) {
                              System.out.println("Could not add question to QB");
                              //e.printStackTrace();
                         } finally {
                             Connect.close(psAddQuestionToParameter);
                        }
                        
                        
                    } catch (Exception e) {
                    		System.out.println("Could not add question to QB");
                        //e.printStackTrace();
                    }
                
                }
                int arg;
                do {
                  System.out.println("Press 0 to go back");
                  arg = Integer.parseInt(ip.nextLine());
                 
                  if(arg==0) {
                    Instructor.searchOrAddQuestionInQB(ip, instructorID);
                    return;
                  }
                } while(arg != 0);
            }
          } catch (Exception e) {
              System.out.println("Invalid Input");
              //e.printStackTrace();
              Instructor.addQuestionInQB(ip, instructorID);
              return;
          }
          
      }

      /**************Functions called from viewCourse**********************************/
      
      public static void viewOrAddExercise(Scanner ip, int c_id, int instructor_id) {
         
    	  try {
              System.out.println("0:Go Back");
              System.out.println("1.View Exercise");
              System.out.println("2.Add Exercise");
              System.out.print("Choice:");
              
              int choice = Integer.parseInt(ip.next());
              System.out.println();
              if(1 == choice) {
                Instructor.viewExercise(ip, c_id, instructor_id); 
                return;
              }
              else if (2 == choice) {
               
            	  	Instructor.addExercise(ip, c_id, instructor_id);
                return;
              
              }else if(0 == choice) {
            	  		
            	  		Instructor.viewCourse(ip, c_id, instructor_id);
            	  		return;
              
              }else {
	            	  	
            	  		System.out.println("Invalid Input. Enter again");
	            	  	Instructor.viewOrAddExercise(ip, c_id, instructor_id);
	            	  	return;
              }
              
            } catch (NumberFormatException e) {
                System.out.println("Enter valid Input:");
                Instructor.viewOrAddExercise(ip, c_id, instructor_id);
                return;
                
            } catch (Exception e) {
                System.out.println("Invalid input");
                //e.printStackTrace();
                
            }
      }
      
      /*
       * @param ip - Scanner to take user input
       * @param c_id - Unique course Identifer
       * 
       */
      public static void viewOrAddTA(Scanner ip, int c_id) {
          try {
            System.out.println("0:Go Back");
            System.out.println("1.View TA");
            System.out.println("2.Add TA");
            System.out.print("Choice:");
            
            int choice = Integer.parseInt(ip.next());
            System.out.println();
            if(1 == choice) {
              Instructor.viewTA(ip, c_id); 
              return;
            }
            else if (2 == choice) {
              Instructor.addTA(ip, c_id);
              return;
            }
            else if(0 == choice) {
              Instructor.viewCourse(ip, c_id, instId); 
              return;
            }
            
          } catch (NumberFormatException e) {
              System.out.println("Enter valid Input:");
              Instructor.viewOrAddTA(ip, c_id);
              return;
              
          } catch (Exception e) {
              System.out.println("Enter valid Input:");
              Instructor.viewOrAddTA(ip, c_id);
              return;
              
          }
          
      }
      
      public static void enrollOrDropStudentFromViewCourse(Scanner ip, int c_id, int instructorID) {
          PreparedStatement ps = null;
          ResultSet rs = null;
          try {
              //As we call this method from viewCourse, we already have
              //the courses unique identifier
              int cID = c_id;
              int callerFlag = 2;
              System.out.println("0. Go Back");
              System.out.println("1. Enroll a Student in course");
              System.out.println("2. Drop a Student from course");
              System.out.print("Choice:");
              
              int choice = Integer.parseInt(ip.next());
              
              if(1 == choice) {
                  Instructor.enrollStudent(ip, cID, instructorID, callerFlag);
                  return;
              }
              else if(2 == choice) {
                  Instructor.dropStudent(ip, cID, instructorID, callerFlag);
                  return;
              }else if(0 == choice) {
                  Instructor.viewCourse(ip, cID, instructorID);
                  return;
                  
              }else {
                 System.out.println("Invalid input");
              }
                    
          } catch(NumberFormatException e) {
              System.out.println("Enter valid input");
              //Instructor.showHomePage(ip, instructorID);
          }
          catch (Exception e) {
              //e.printStackTrace();
              System.out.println("Invalid input");
          } finally {
              Connect.close(ps);
              Connect.close(rs);
              
          }
          //Go back to view Course page on succes/failure
          Instructor.viewCourse(ip, c_id, instructorID);
          return;
      }

      /******************Functions called from viewOrAddExercise**************************/
      /*
       * View Exercises in a Course
       */
      public static void viewExercise(Scanner ip, int c_id, int instructor_id) {
          //Display all exercises related to course
          //Ask for exercise ID
    		System.out.println("*************** Course Exercises ****************");
    		PreparedStatement ps = null; PreparedStatement psExCheck = null;
    		ResultSet results = null; ResultSet resExCheck = null;
    		try {
    			
    			ps = Connect.getConnection().prepareStatement(Queries.viewExercise);
    			ps.setInt(1,c_id);
    			results = ps.executeQuery();
    			Instructor.showResultsSet(ps.executeQuery());
    			//results.next();
    			System.out.println("Enter Exercise ID to Explore"); 
    			System.out.println("Enter 0 to Go Back");
    			System.out.print("Choice:");
            int choiceEx = Integer.parseInt(ip.next());
            
            //go back
            if(choiceEx == 0) {
            		Instructor.viewOrAddExercise(ip, c_id, instructor_id);
            		return;
            }
            
            psExCheck = Connect.getConnection().prepareStatement(Queries.doesExerciseExist);
            psExCheck.setInt(1,choiceEx);
            psExCheck.setInt(2,c_id);
            Instructor.showResultsSet(psExCheck.executeQuery());
            resExCheck = psExCheck.executeQuery();
            resExCheck.next();

  			  if(0 == resExCheck.getInt("ex_exists")) {
  				  System.out.println("Invalid Exercise Id entered. Please re-enter");
  				  Instructor.viewExercise(ip, c_id, instructor_id);
  				  return;
  				
  			  }else {
  			  
  				  while(true) {
		    	          //if valid show details of exercise 
		    	          //else ask to renter Excercise ID
		    	          System.out.println("1.Add Question to exercise");
		    	          System.out.println("2.Remove Question from exercise");
		    	          System.out.println("3.View the questions in the exercise (Existing questions in the Exercise)");
		    	          System.out.println("0.To go back");
		    	          System.out.print("Choice:");
		                  int choice = Integer.parseInt(ip.next());
		              
		               if(3 == choice) {
		                  Instructor.showExerciseDetails(ip, choiceEx, c_id, instructor_id);
		                  return;
		              }
		              else if(1 == choice) {
		                  Instructor.addQuestionToExercise(ip, choiceEx, c_id, instructor_id);
		                  return;
		              
		              }else if(2 == choice) {
		                  Instructor.removeQuestionFromExercise(ip, choiceEx, c_id, instructor_id);
		                  return;
		              }else if(0 == choice) {
		            	  
		            	  	 Instructor.viewOrAddExercise(ip, c_id, instructor_id);
		            	  	 return;
		              
		              }else {
		                  System.out.println("Invalid input. Please re-enter a valid input");
		              }  
		               
  				  }
  			  }
    			
    		}catch(Exception e) {
    			System.out.println(e);
    			
    		}finally {
                Connect.close(ps);
                Connect.close(psExCheck);
                Connect.close(results);
                Connect.close(resExCheck);
                
            }
          
      }
      
      /**********Functions called from view Exercise ******************************/
      public static void showExerciseDetails(Scanner ip, int exID, int c_id, int instructor_id) {
          
    	  			System.out.println("********************* Exercise Details ************************");
    	  			
    	  			PreparedStatement ps = null;
    	  			ResultSet rs = null;
    	  			
    	  			try {
    	  				
    	  				ps = Connect.getConnection().prepareStatement(Queries.viewExerciseQuestions);
    	  				ps.setInt(1, exID);
    	  				rs = ps.executeQuery();
    	  				System.out.println("**************** Details for HW " + exID + " ****************");
    	  				while(rs.next()) {
    	  					System.out.println("Question No: " + rs.getInt("QUESTION_ID"));
    	  					String qtype = rs.getInt("QTYPE")==1?"Parameterized":"Fixed";
    	  					System.out.println("Question Type: " + qtype);
    	  					System.out.println("Question Text: \n" + rs.getString("TEXT"));
    	  					System.out.println("Parameters: " + rs.getString("PARAMETERS"));
    	  					System.out.print("Answer: "  + rs.getString("ANSWER"));
    	  					System.out.print("\t Solution: "  + rs.getString("SOLUTION"));
    	  					System.out.println("\t HINT: "  + rs.getString("HINT"));
    	  					System.out.println();
    	  				}
    	  				//Instructor.showResultsSet(ps.executeQuery());
    	  				
    	  				System.out.println("0: Go Back");
    	  				System.out.println("Choice: ");
    	  				int choice = Integer.parseInt(ip.next());
    	  				
    	  				if(0 == choice) {
    	  					Instructor.viewExercise(ip, c_id, instructor_id);
    	  					return;
    	  				}else {
    	  					System.out.println("Invalid Input, Enter Again...");
    	  					Instructor.showExerciseDetails(ip, exID, c_id, instructor_id);
    	  					
    	  				}

    	  			}catch(Exception e){
    	  				System.out.println(e);
    	  			}finally {
    	  				Connect.close(ps);
    	  				Connect.close(rs);
    	  			}
    	  		
    	  
      }
      
      
      public static void addQuestionToExercise(Scanner ip,int exID, int c_id, int instructor_id) {
    	  
    	  		PreparedStatement ps = null; PreparedStatement ps1 = null;PreparedStatement ps2 = null; PreparedStatement ps3 = null;
    	  		PreparedStatement ps4 = null; PreparedStatement ps5 = null;PreparedStatement ps6 = null; PreparedStatement ps7 = null;
    	  		ResultSet rs = null;	 ResultSet rs1 = null; ResultSet rs2 = null;	 ResultSet rs3 = null;
    	  		ResultSet rs4 = null;	 ResultSet rs5 = null; ResultSet rs6 = null;	 ResultSet rs7 = null;
    	  		
    	  		try {
    	  			
    	  			ps = Connect.getConnection().prepareStatement(Queries.doesExerciseExist);
    	  			ps.setInt(1, exID);
    	  			ps.setInt(2, c_id);
    	  			rs = ps.executeQuery();
    	            rs.next();

    	  			  if(0 == rs.getInt("ex_exists")) {
    	  				  System.out.println("Invalid Exercise Id entered. Please re-enter");
    	  				  Instructor.viewExercise(ip, c_id, instructor_id);
    	  				  return;
    	  				
    	  			  }else {
    	  				  ps1 = Connect.getConnection().prepareStatement(Queries.viewExerciseDetails);
    	  				  ps1.setInt(1, exID);
    	  				  rs1 = ps1.executeQuery();
    	  				  rs1.next();
    	  				  
    	  				  if(rs1.getString("EX_MODE").equals("S")) {
    	  					  System.out.println("The selected exercise is a standard exercise");
    	  					  System.out.println("Maximum Allowed Question in this exercise are " + rs1.getInt("NUM_QUESTIONS"));
    	  					  
    	  					  System.out.print("No. of Questions in the exercise = ");
    	  					  ps2 = Connect.getConnection().prepareStatement(Queries.getCountExQues);
    	  					  ps2.setInt(1, rs1.getInt("EX_ID"));
    	  					  rs2 = ps2.executeQuery(); rs2.next();
    	  					  System.out.println(rs2.getInt("exQuesCnt"));
    	  					  System.out.println("No. of questions that can be further added = " + Integer.toString(rs1.getInt("NUM_QUESTIONS")-rs2.getInt("exQuesCnt")));
    	  					  
    	  					  ps3 = Connect.getConnection().prepareStatement(Queries.getExMappedQues);
    	  					  ps3.setInt(1, exID);
    	  					  rs3 = ps3.executeQuery(); //rs3.next();
    	  					  
    	  					  ps5 = Connect.getConnection().prepareStatement(Queries.getUnmappedQuesCnt);
    	  					  ps5.setInt(1, exID);
  	  					  ps5.setInt(2, exID);
  	  					  rs5 = ps5.executeQuery(); rs5.next();
  	  					  
    	  					  
    	  					  ps4 = Connect.getConnection().prepareStatement(Queries.getExUnmappedQues);
    	  					  ps4.setInt(1, exID);
    	  					  ps4.setInt(2, exID);
    	  					  rs4 = ps4.executeQuery(); //rs4.next();
    	  					  
    	  					  if(rs2.getInt("exQuesCnt") > 0) {
    	  					  
    	  						  System.out.println("View current questions(y/n)");
    	  						  String c = ip.next();
    	  						  if(c.equalsIgnoreCase("y")) {
    	  							  Instructor.showResultsSet(rs3);
    	  						  }else {
    	  							  //do nothing
    	  						  }
    	  						  
    	  					  }
    	  					  
    	  					List<Integer> list = null;
    	  					  if(rs5.getInt("leftQues") > 0) {
    	  						  System.out.println("Question Ids that can be added are");
    	  						   list = new ArrayList<Integer>();
    	  						  
    	  						  while(rs4.next()) {
    	  							  System.out.println(rs4.getInt("question_id"));
    	  							  list.add(rs4.getInt("question_id"));
    	  						  }
    	  						  
    	  						  System.out.println("View potential Questions Text (y/n)");
  	  						  String c = ip.next();
  	  						  if(c.equalsIgnoreCase("y")) {
  	  							  Instructor.showResultsSet(ps4.executeQuery());
  	  						  }else {
  	  							  //do nothing
  	  						  }
    	  					  }
    	  					  
    	  					  System.out.println("Adding Questions now");
    	  					  
    	  					  int curCnt = rs1.getInt("NUM_QUESTIONS")-rs2.getInt("exQuesCnt");
    	  					  
    	  					  while(curCnt > 0) {
    	  						  
    	  						  System.out.println("Enter 0 to Go Back Or Enter a question Id to add from " + list.toString());
    	  						  System.out.println("Choice: ");
    	  						  int choiceQ = ip.nextInt();
    	  						  
    	  						  if(choiceQ == 0) {
    	  							  System.out.println("Going Back");
    	  							  Instructor.viewOrAddExercise(ip, c_id, instructor_id);
    	  							  return;
    	  						  }else {

    	  							  if(list.contains(choiceQ)) {
    	  								  
    	  								  ps6 = Connect.getConnection().prepareStatement(Queries.insertExQuestion);
    	  								  ps6.setInt(1, exID);
    	  								  ps6.setInt(2, choiceQ);
    	  								  ps6.executeQuery();
    	  								  list.remove(list.indexOf(choiceQ));
    	  							  }else {
    	  								  System.out.println("The entered question is not available. Try again");
    	  								  continue;
    	  							  }
    	  							  
    	  						  }
    	  						  
    	  						  curCnt--;
    	  						  
    	  					  }
    	  					  
    	  					  System.out.println("Maximum possible no. of questions have been added to the Exercise");
    	  					  Instructor.viewOrAddExercise(ip, c_id, instructor_id);
    	  					  
    	  				  }else {
    	  					  
    	  					  System.out.println("The exercise is an adaptive one !!! ");
    	  					  System.out.println("No need to add any question. Questions will be randomly selected from relevant topics ");
    	  					  Instructor.viewOrAddExercise(ip, c_id, instructor_id);
    	  					  return;
    	  					  
    	  				  }
    	  			  }

    	  			
    	  		}catch(Exception e){
    	  		    System.out.println("Invalid input.");
    	  			//e.printStackTrace();
    	  		}finally {
    	  			Connect.close(ps);
    	  			Connect.close(ps1);
    	  			Connect.close(ps2);
    	  			Connect.close(ps3);
    	  			Connect.close(ps4);
    	  			Connect.close(ps5);
    	  			Connect.close(ps6);
    	  			Connect.close(rs);
    	  			Connect.close(rs1);
    	  			Connect.close(rs2);
    	  			Connect.close(rs3);
    	  			Connect.close(rs4);
    	  			Connect.close(rs5);
    	  			Connect.close(rs6);
    	  		}
          
      }
      
      
      public static void removeQuestionFromExercise(Scanner ip,int exID, int c_id, int instructor_id) {
          
    	  	System.out.println("************* Removing Questions from the Exercise *******************");
    	  	PreparedStatement ps = null; PreparedStatement ps1 = null;PreparedStatement ps2 = null; PreparedStatement ps3 = null;
    	  	PreparedStatement ps4 = null; PreparedStatement ps5 = null;PreparedStatement ps6 = null; PreparedStatement ps7 = null;
    	  	ResultSet rs = null;	 ResultSet rs1 = null; ResultSet rs2 = null;	 ResultSet rs3 = null;
    	  	ResultSet rs4 = null;	 ResultSet rs5 = null; ResultSet rs6 = null;	 ResultSet rs7 = null;

    	  	try {

    	  		ps = Connect.getConnection().prepareStatement(Queries.doesExerciseExist);
    	  		ps.setInt(1, exID);
    	  		ps.setInt(2, c_id);
    	  		rs = ps.executeQuery();
    	  		rs.next();

    	  		if(0 == rs.getInt("ex_exists")) {
    	  			System.out.println("Invalid Exercise Id entered. Please re-enter");
    	  			Instructor.viewExercise(ip, c_id, instructor_id);
    	  			return;

    	  		}else {
    	  			ps1 = Connect.getConnection().prepareStatement(Queries.viewExerciseDetails);
    	  			ps1.setInt(1, exID);
    	  			rs1 = ps1.executeQuery();
    	  			rs1.next();

    	  			if(rs1.getString("EX_MODE").equals("S")) {
    	  				System.out.println("The selected exercise is a standard exercise");
    	  				System.out.println("Maximum Allowed Question in this exercise are " + rs1.getInt("NUM_QUESTIONS"));

    	  				System.out.print("No. of Questions in the exercise = ");
    	  				ps2 = Connect.getConnection().prepareStatement(Queries.getCountExQues);
    	  				ps2.setInt(1, rs1.getInt("EX_ID"));
    	  				rs2 = ps2.executeQuery(); rs2.next();
    	  				System.out.println(rs2.getInt("exQuesCnt"));
    	  				System.out.println("No. of questions that can be removed = " + rs2.getInt("exQuesCnt"));

    	  				ps3 = Connect.getConnection().prepareStatement(Queries.getExMappedQues);
    	  				ps3.setInt(1, exID);
    	  				rs3 = ps3.executeQuery(); //rs3.next();


    	  				List<Integer> list = null;
    	  				if(rs2.getInt("exQuesCnt") > 0) {
    	  					System.out.println("Question Ids that can be removed are");
    	  					list = new ArrayList<Integer>();

    	  					while(rs3.next()) {
    	  						System.out.println(rs3.getInt("question_id"));
    	  						list.add(rs3.getInt("question_id"));
    	  					}
    	  					
    	  					System.out.println("View all current questions ? (y/n)");
    	  					String c = ip.next();
    	  					if(c.equalsIgnoreCase("y")) {
    	  						Instructor.showResultsSet(ps3.executeQuery());
    	  					}else {
    	  						//do nothing
    	  					}

    	  				}

    	  				System.out.println("Removing Questions now");

    	  				int curCnt = rs1.getInt("NUM_QUESTIONS")-rs2.getInt("exQuesCnt");

    	  				while(curCnt < rs1.getInt("NUM_QUESTIONS")) {

    	  					System.out.println("Enter 0 to Go Back Or Enter a question Id to remove from " + list.toString());
    	  					System.out.println("Choice: ");
    	  					int choiceQ = ip.nextInt();

    	  					if(choiceQ == 0) {
    	  						System.out.println("Going Back");
    	  						Instructor.viewOrAddExercise(ip, c_id, instructor_id);
    	  						return;
    	  					}else {
    	  						
    	  						if(list.contains(choiceQ)) {

    	  							ps6 = Connect.getConnection().prepareStatement(Queries.deleteExQuestion);
    	  							ps6.setInt(1, exID);
    	  							ps6.setInt(2, choiceQ);
    	  							ps6.executeQuery();
    	  							list.remove(list.indexOf(choiceQ));
    	  						}else {
    	  							System.out.println("The entered question is not available. Try again");
    	  							continue;
    	  						}
    	  					}
    	  					curCnt++;
    	  				}

    	  				System.out.println("The Exercise is empty now !!!");
    	  				Instructor.viewOrAddExercise(ip, c_id, instructor_id);

    	  			}else {

    	  				System.out.println("The exercise is an adaptive one !!! ");
    	  				System.out.println("No need to remove/add any question. Questions will be randomly selected from relevant topics ");
    	  				Instructor.viewOrAddExercise(ip, c_id, instructor_id);
    	  				return;

    	  			}
    	  		}


    	  	}catch(Exception e){
    	  		//e.printStackTrace();
    	  		System.out.println("Could not remove question from exercise");
    	  	}finally {
    	  		Connect.close(ps);
    	  		Connect.close(ps1);
    	  		Connect.close(ps2);
    	  		Connect.close(ps3);
    	  		Connect.close(ps4);
    	  		Connect.close(ps5);
    	  		Connect.close(ps6);
    	  		Connect.close(rs);
    	  		Connect.close(rs1);
    	  		Connect.close(rs2);
    	  		Connect.close(rs3);
    	  		Connect.close(rs4);
    	  		Connect.close(rs5);
    	  		Connect.close(rs6);
    	  	}

    	  
      }

      /*
       * Add Exercise to a Course
       */
      public static void addExercise(Scanner ip, int c_id, int instructor_id) {
          //Select 0 to go back
          //Ask for exercise type
          //Ask number of questions in execise
          //
    	      //Show firstName, LastName and Id
  		
  		System.out.println("*************** Adding Homework ****************");
  		PreparedStatement ps = null; PreparedStatement ps1 = null; PreparedStatement ps2 = null; PreparedStatement ps3 = null;
  		PreparedStatement ps4 = null; PreparedStatement ps5 = null; PreparedStatement ps6 = null; PreparedStatement ps7 = null;
  		ResultSet rs = null; ResultSet rs1 = null; ResultSet rs2 = null;
  		
  		try {
  			ps = Connect.getConnection().prepareStatement(Queries.countOfExercises);
  			rs = ps.executeQuery(); rs.next();
  			int lastExId = rs.getInt("maxExNum");
  			int newExId = lastExId + 1;
  			System.out.println("Generated Exercise ID is " + newExId);
  			System.out.println("Fill up the other enteries as shown in the example for each input asked");
  			
  			System.out.println("Enter the name of HW (HW" + newExId + " is preferred) or press 0 to Go Back");
  			String hwName = ip.next();
  			 
  			if(hwName.equals("0")) {
  				Instructor.viewExercise(ip, c_id, instructor_id);
  				return;
  			}
  			//Enter number of questions
  			int numQues = 0;
  			while(true) {
	  			
  				System.out.println("Enter the number of questions in the Exercise or press 0 to Go Back");
	  			numQues = ip.nextInt();
	  			
	  			PreparedStatement pstemp1 = Connect.getConnection().prepareStatement(Queries.numQuesInTable);
	  			ResultSet rstemp1 = pstemp1.executeQuery();
	  			rstemp1.next();
	  			
	  			if(numQues == 0) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			
	  			if(numQues < 1 || numQues > rstemp1.getInt("numQues")) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  				break;
	  			}
  			}
  			
  			//Enter points for correct Answer
  			int ptCorr = 0;
  			while(true) {
	  			
  				System.out.println("Enter Points for Correct Answer (a positive number between [1,5] eg. 3) or 0 to Go Back");
  	  			ptCorr = ip.nextInt();
	  			
	  			if(ptCorr == 0) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			
	  			if(ptCorr < 1 || ptCorr > 5) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  				break;
	  			}
  			}
  			
  			
  			
  			//Enter points for incorrect answer
  			int ptIncorr = 0;
  			while(true) {
	  			ip.nextLine();
  				System.out.println("Enter Points for Incorrect Answer (add 0 or negative number eg. -1) or g to Go Back");
  				
  				String ptIncorrS = ip.nextLine();
  				//ptIncorr = ip.nextInt();
	  			
	  			if(ptIncorrS.equalsIgnoreCase("g")) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			ptIncorr = Integer.parseInt(ptIncorrS);
	  			if(ptIncorr > 0 || (-1)*ptIncorr >= ptCorr) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  				break;
	  			}
  			}
  			
  			//Enter Scoring Policy	
  			String policy = "";
  			while(true) {
  				System.out.println("Enter the scoring Policy number (eg. 1 for Latest_Attempt, 2 for Average_Attempt, 3 for Maximum_Score) or 0 to Go Back");
  				int policyNum = 0;
  				policyNum = ip.nextInt();
  				//policy = ip.next();
	  			
	  			if(policyNum == 0) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			
	  			if(policyNum > 3 || policyNum <= 0) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  				if(policyNum == 1) {
	  					policy = "Latest_Attempt";
	  				}else if(policyNum == 2) {
	  					policy = "Average_Attempt";
	  				}else if(policyNum == 3) {
	  					policy = "Maximum_Score";
	  				}
	  				break;
	  			}
  			}
  			
  			
  			int retries = 0;
  			while(true) {
	  			
  				System.out.println("Enter the no. of tries between [1,2147483647] (eg. 3) or 0 to Go Back");
  				retries = ip.nextInt();
	  			
	  			if(retries == 0) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			
	  			if(retries < 0 || retries > 2147483647) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  				break;
	  			}
  			}
  			
  			
  			//Enter the mode for this question
  			String mode = "";
  			while(true) {
  				
  				System.out.println("Enter Mode(Adaptive or Standard) of the Exercise (eg. A or S) or 0 to Go Back");
  				mode = ip.next();
	  			
	  			if(mode.equals("0")) {
	  				Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
	  			}
	  			
	  			if(!mode.equalsIgnoreCase("A") && !mode.equalsIgnoreCase("S")) {
	  				System.out.println("Invalid Input. Re-enter");
	  				continue;
	  			}else {
	  			
	  				break;
	  			}
  			}
  			
  			
  			
  			System.out.println("Define a duration for the Exercise");
  			System.out.println("The format followed for timestamp is 'YYYY-MM-DD HH24:MI:SS'. Input accordingly");
  			System.out.println("Enter Start TimeStamp for this exercise (eg. 2017-10-16 00:00:00.000)");
  			
  			
  			
  			String stDate = "";
  			String stTime = "";
  			String stDateTs = "";
  			
  			while(true) {
  				
  				System.out.println("Enter Start Date (eg. 2017-10-16) or 0 to Go Back");
  				SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-mm-dd");
  				stDate = ip.next();
  				
  				if(stDate.equals("0")) {
  					Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
  				}
  				
	  	  	     try {   
	  	  	    	 		dateFormat.parse(stDate);
	  	  	     }
	  	  	     	catch(ParseException e) {
	  	  	     	System.out.println("Incorrect Date Format Entered. Enter as YYYY-MM-DD. please re-enter");
	  	  	     	continue;
	  	  	     }
	  	  	     break;
  			}
  			
  			while(true){
  				
	  			System.out.println("Enter Start Time (eg. 00:00:00.000) or 0 to Go Back");
	  			SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm:ss");
	  			stTime = ip.next();
	  			
	  			if(stTime.equals("0")) {
  					Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
  				}
	  			
		  	    	try {   
			    	 		timeFormat.parse(stTime);
			     }
			     	catch(ParseException e) {
			     	System.out.println("Incorrect Time Format Entered. Enter as HH:MM:SS. please re-enter");
			     	continue;
			     }
	  	        
		     		break;
  			}
  			
  			stDateTs = stDate + " " + stTime;
	  	    	
  			System.out.println("Enter End TimeStamp for this exercise (eg. 2017-10-16 00:00:00.000)");
  			
  			
  			String endDate = "";
  			String endTime = "";
  			String endDateTs = "";
  			
  			while(true) {
  				
  				System.out.println("Enter End Date (eg. 2017-10-16) or 0 to Go Back");
  				SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-mm-dd");
  				endDate = ip.next();
  				
  				if(endDate.equals("0")) {
  					Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
  				}
  				
	  	  	     try {   
	  	  	    	 		dateFormat.parse(endDate);
	  	  	     }
	  	  	     	catch(ParseException e) {
	  	  	     	System.out.println("Incorrect Date Format Entered. Enter as YYYY-MM-DD. please re-enter");
	  	  	     	continue;
	  	  	     }
	  	  	     break;
  			}
  			
  			while(true){
  				
	  			System.out.println("Enter End Time (eg. 00:00:00.000) or to Go Back");
	  			SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm:ss");
	  			endTime = ip.next();
	  			
	  			if(endTime.equals("0")) {
  					Instructor.viewExercise(ip, c_id, instructor_id);
	  				return;
  				}
	  			
		  	    	try {   
			    	 		timeFormat.parse(endTime);
			     }
			     	catch(ParseException e) {
			     	System.out.println("Incorrect Time Format Entered. Enter as HH:MM:SS. please re-enter");
			     	continue;
			     }
	  	        
		     		break;
  			}
  			
  			endDateTs = endDate + " " + endTime;
  			
  			
  			System.out.println("We are adding this exercise to the Database... Please wait");
  			
  			//insert statement to add exercise
  			ps1 = Connect.getConnection().prepareStatement(Queries.insertEx);
  			ps1.setInt(1, newExId);
  			ps1.setString(2, hwName);
  			ps1.setInt(3, numQues);
  			ps1.setInt(4, ptIncorr);
  			ps1.setInt(5, ptCorr);
  			ps1.setString(6, policy);
  			ps1.setInt(7, retries);
  			ps1.setString(8, mode);
  			ps1.executeQuery();
  			
  			//insert statement to add exercise duration if it does not exist
  			ps5 = Connect.getConnection().prepareStatement(Queries.checkDurationExist);
  			ps5.setTimestamp(1, java.sql.Timestamp.valueOf(stDateTs));
  			ps5.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTs));
  			rs1 = ps5.executeQuery(); rs1.next();
  			
  			if(rs1.getInt("dateExists")  == 0) {
	  		
  				ps4 = Connect.getConnection().prepareStatement(Queries.insertDuration);
	  			ps4.setTimestamp(1, java.sql.Timestamp.valueOf(stDateTs));
	  			ps4.setTimestamp(2, java.sql.Timestamp.valueOf(endDateTs));
	  			ps4.executeQuery();
	  			
  			}
  			
  			ps2 = Connect.getConnection().prepareStatement(Queries.insertExDuration);
  			ps2.setInt(1, newExId);
  			ps2.setTimestamp(2, java.sql.Timestamp.valueOf(stDateTs));
  			ps2.setTimestamp(3, java.sql.Timestamp.valueOf(endDateTs));
  			ps2.executeQuery();
  			
  			//insert statement to define exercise course relationship
  			ps3 = Connect.getConnection().prepareStatement(Queries.insertExCourseRelation);
  			ps3.setInt(1, c_id);
  			ps3.setInt(2, newExId);
  			ps3.executeQuery();
  			
  		//insert statement to define exercise course relationship
  			ps7 = Connect.getConnection().prepareStatement(Queries.insertExInstrRelation);
  			ps7.setInt(1, newExId);
  			ps7.setInt(2, instructor_id);
  			ps7.executeQuery();
  			
  			System.out.println("Exercise Updated in the database... Returning");
  			Instructor.viewExercise(ip, c_id, instructor_id);
  			
  		}catch(Exception e) {
  			//e.printStackTrace();
  			System.out.println("Could not add exercise to database");
  			
  		}finally {
  			Connect.close(ps1); Connect.close(rs);
  			Connect.close(ps2);
  			Connect.close(ps3);
  			Connect.close(ps4);
  			Connect.close(ps5);
  			Connect.close(rs1);
  		}
    	  
      }
      
      /*
       * 
       */
      public static void viewReport(Scanner ip, int c_id, int instructorID) {
    	  
    	  	System.out.println("*************** Display Student Report ****************");
    		PreparedStatement ps = null; PreparedStatement ps1 = null;
    		ResultSet rs = null; ResultSet rs1 = null; ResultSet rs2 = null;
	
    		try{
    			
    			ps1 = Connect.getConnection().prepareStatement(Queries.studentInCourse);
    			ps1.setInt(1, c_id);
    			System.out.println("Students Enrolled in the Course");
    			Instructor.showResultsSet(ps1.executeQuery());
    			
    			System.out.println("Progress Report");
    			ps = Connect.getConnection().prepareStatement(Queries.showStudentReportForCourse);
    			ps.setInt(1, c_id);
    			Instructor.showResultsSet(ps.executeQuery());
    			
    			while(true) {
	    			System.out.println("0. Go Back");
	    			int choice = ip.nextInt();
	    			
	    			if(choice == 0) {
	    				Instructor.viewCourse(ip, c_id, instructorID);
	    				return;
	    			}else {
	    				System.out.println("Invalid Input. Try Again");
	    			}
    			}
    			
    			
    		}catch(Exception e){
    			System.out.println("Error occured cannot view report");
    			//e.printStackTrace();
    			
    		}finally {
    			
    			Connect.close(ps);
    			
    		}
      
      }     
      
      /***********Functions called from viewOrAddTA******************************************/
      
      /*
       * Display the name 
       */
      public static void viewTA(Scanner ip, int c_id) {
          PreparedStatement psTA = null;
          ResultSet rsTA = null;
          try {
              psTA = Connect.getConnection().prepareStatement(Queries.getTAOfCourse);
              psTA.setInt(1, c_id);
              rsTA = psTA.executeQuery();
              /*
              if (!rsTA.isBeforeFirst() ) {    
                  System.out.println("No TA assigned.\n"); 
              }*/ 
              //What if there are two TA's for course
              while(rsTA.next()) {
                System.out.println("TA detail:");
                int studentID = rsTA.getInt(1);
                String studentFirstName = rsTA.getString(2);
                String studentLastName = rsTA.getString(3);              
                System.out.println("Student ID:"+studentID);
                System.out.println("First Name:"+studentFirstName);
                System.out.println("Last Name:"+studentLastName);
                System.out.println();
              }
              
          } catch(SQLException e) {
              System.out.println("There is no TA for this course");
              
          } catch (NumberFormatException e) {
              System.out.println("Invalid Input");
              
          } catch (Exception e) {
              System.out.println("Invalid Input");
              
          } finally {
              Connect.close(psTA);
              Connect.close(rsTA);
          }
          
          Instructor.viewOrAddTA(ip, c_id);  
          return;
      }
      
      //Check should be put on PG_ENROLLED table so that a student 
      //cannot be both TA and enrolled for the course
      public static void addTA(Scanner ip, int c_id) {
          PreparedStatement psIsPG = null;
          PreparedStatement psAddTA = null;
          ResultSet rsIsPG = null;
          
          try {
            System.out.print("Enter Student ID:");
            int studentID = Integer.parseInt(ip.next());
            System.out.print("Enter Student's first name:");
            String studentFirstName = ip.next();
            System.out.print("Enter Student's last name:");
            String studentLastName = ip.next();
            
            psIsPG = Connect.getConnection().prepareStatement(Queries.checkIfPgStudentGivenName);
            psIsPG.setInt(1, studentID);
            psIsPG.setString(2, studentFirstName);
            psIsPG.setString(3, studentLastName);

            
            rsIsPG = psIsPG.executeQuery();
            rsIsPG.next();

            if(1 <= rsIsPG.getInt("pg_student")) {
                try {
                  psAddTA = Connect.getConnection().prepareStatement(Queries.addTA);
                  psAddTA.setInt(1, studentID);
                  psAddTA.setInt(2, c_id);
                  psAddTA.executeQuery();
                  System.out.println("Successfully Added TA");
                  
                  
                } catch (SQLException e) {
                    if(e.getSQLState().startsWith("23")) {
                        System.out.println("Could not add TA,  he is already TA for a course\n");
                    }
                    
                    //Need to catch this PL-SQL exception
                    else {
                      System.out.println("Could not add TA as he is enrolled in the course ");
                      
                    }
                    
                } catch(Exception e){
                    System.out.println("Could not add TA");
                    
                }
                  
                  finally {
                    Connect.close(psIsPG);
                    Connect.close(rsIsPG);
                    Connect.close(psAddTA);
                    
                }
            }
            else {
                System.out.println("Could not execute query as student does not exist/is a UG student/Incorrect details entered.");
                
            }
            
            Instructor.viewOrAddTA(ip, c_id);
            return;
            
          } catch (Exception e) {
              System.out.println("Could not execute query as student does not exist/is a UG student/Incorrect details entered.");
              
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
  		    System.out.println("Error in result set display.");
  			//e.printStackTrace();
  		}
      }

   
}
