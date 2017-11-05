package code;
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
       -[Pending] View Exercise
       -[Pending] Add Exercise
       -[Done] View TA
       -[Pending] Add TA
       -[Done] Enroll Student
       -[Done] Drop Student
       -[Done] Go Back
     *[Done] Add Course
     *[Done]Enroll Student
     *[Done]Drop Student
     *[Pending]Search Qusetion in QB
     *[Pending]Add Question in QB
    
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
                 Instructor.searchOrAddQuestionInQB(ip,instructorID);
                 
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
             //Instructor.viewOrAddCourse(ip, instructorID);
             
         }catch(Exception e) {
             e.printStackTrace();
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
                 return;
             }
             else if(2 == choice) {
                 Instructor.dropStudent(ip, cID, instructorID, callerFlag);
                 return;
             }
             else {
                 System.out.println("Invalid input");     
             }
                   
         } catch(NumberFormatException e) {
             System.out.println("Enter valid input");
             //Instructor.showHomePage(ip, instructorID);
         }
         catch (Exception e) {
             e.printStackTrace();
         } finally {
             Connect.close(ps);
             Connect.close(rs);
         }
         
         Instructor.showHomePage(ip, instructorID);
         return;
     }

     public static void searchOrAddQuestionInQB(Scanner ip,int instructorID) {
         System.out.println("Press 1 to Search question in question bank");
         System.out.println("Press 2 to Add question to question bank");
         System.out.println("Press 0 to go back");
         
         int arg=ip.nextInt();
         
         if(arg==1) {
        	 searchQuestionInQB(ip,instructorID);
         }
         
         else if(arg==2) {
        	 addQuestionInQB(ip,instructorID);
         }
         
         else if(arg==0) {
        	 Instructor.showHomePage(ip, instructorID);
         }
     }

     /************Functions called from viewOrAddCourse****************************/
     /*
      * Show the relevant details for course
      */
     public static void viewCourse(Scanner ip, int instructorID) {
         //Ask course ID
         int cID;
     
         try {
           System.out.println("*****View Course*****");
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
             //System.out.println("Course unique idenrifier " + cID);
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
               return;
               
             } else if(2 == choice) {
                 Instructor.viewOrAddTA(ip, cID);
                 
             } else if (3 == choice) {
                 Instructor.enrollOrDropStudentFromViewCourse(ip, cID, instructorID);
                 return;
                 
             } else {
                 System.out.println("Invalid input.");               
             }
           }
           else {
               System.out.println("Course does not exist.");
               Connect.close(psCourseExists);
               Connect.close(rsCourseExists);
               //Instructor.viewCourse(ip, instructorID);
           }
           
         } catch (NumberFormatException e) {
             System.out.println("Please enter valid input");
             //Instructor.viewCourse(ip, instructorID);
         } catch (Exception e) {
             e.printStackTrace();
         }
         Instructor.viewCourse(ip, instructorID);
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
                 //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (SQLException e) {
                   if(e.getSQLState().startsWith("23")) {
                       System.out.println("Could not enroll UG Student, he is currently enrolled in the course\n");
                       
                   }
                   
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
                 //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                 
               } catch (SQLException e) {
                   if(e.getSQLState().startsWith("23")) {
                       System.out.println("Could not enroll PG Student, he is currently enrolled in the course\n");
                   }
                   
                   //Need to catch this PL-SQL exception
                   else {
                     System.out.println("Could not enroll PG Student as he is TA");
                     //Instructor.enrollOrDropStudent(ip);
                     //e.printStackTrace();
                   }
                   
               } catch(Exception e){
                   System.out.println("Could not enroll PG Student");
                   e.printStackTrace();
               }
                 
                 finally {
                   Connect.close(psPGStudent);
                   Connect.close(rsPGStudent);
                   Connect.close(psEnrollPGStudent);
                   
               }
           }
           else {
               System.out.println("Could not execute query as student does not exist");
               //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
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
                  psDropUGStudent = Connect.getConnection().prepareStatement(Queries.dropUGStudent);
                  psDropUGStudent.setInt(1, studentID);
                  psDropUGStudent.setInt(2, cID);
                  psDropUGStudent.executeQuery();
                  System.out.println("UG Student dropped successfully\n");
                  //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                  
                } catch (SQLException e) {
                    System.out.println("Could not drop UG Student as he is not enrolled in course\n");
                    e.printStackTrace();
                    
                } catch (Exception e) {
                    System.out.println("Could not drop UG Student\n");
                    e.printStackTrace();
                    
                } finally {
                    Connect.close(psUGStudent);
                    Connect.close(rsUGStudent);
                    Connect.close(psDropUGStudent);
                    
                }
            }
            else if(1 == rsPGStudent.getInt("pg_student")) {
                try {
                  psDropPGStudent = Connect.getConnection().prepareStatement(Queries.dropPGStudent);
                  psDropPGStudent.setInt(1, studentID);
                  psDropPGStudent.setInt(2, cID);
                  psDropPGStudent.executeQuery();
                  System.out.println("PG Successfully dropped Student");
                  //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
                  
                } catch (SQLException e) {
                    System.out.println("Could not enroll PG Student as he is not enrolled in the course");
                    e.printStackTrace();
                    
                } catch(Exception e){
                    System.out.println("Could not enroll PG Student");
                    e.printStackTrace();
                    
                } finally {
                    Connect.close(psPGStudent);
                    Connect.close(rsPGStudent);
                    Connect.close(psDropPGStudent);
                    
                }
            }
            else {
                System.out.println("Could not execute query as student does not exist");
                //Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
            }
            
            Instructor.goBackAfterEnrollOrDrop(ip, callerFlag, instructorID);
            
          } catch (Exception e) {
              e.printStackTrace();
          } 
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
      public static void searchQuestionInQB(Scanner ip,int instructorID) {
    	  ip.nextLine();
          System.out.println("1. Search by question id ");
          System.out.println("2. Search by topic name");
          System.out.println("0. Go Back");
          
          int arg=ip.nextInt();
          
          if(arg==1) {
        	  System.out.println("Enter question id");
        	   
        	  int qid=ip.nextInt();
        	  
        	  try {
    	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(Queries.searchQuestion);
    	          pstmt.setInt(1, qid);
    	          ResultSet rs=pstmt.executeQuery();
    	          
    	          showResultsSet(rs);
    		 }
    		 catch(SQLException e) {
    	          	e.printStackTrace();
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
    	          	e.printStackTrace();
    	          }
        	  System.out.println("Press 0 to go back");
        	  int arg3=ip.nextInt();
              
              if(arg3==0) {
            	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
              }
        	  
          }
          
          else if(arg==0) {
        	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
          }
      }
       
      /*
       * Instructor can add a question 
       */
      public static void addQuestionInQB(Scanner ip,int instructorID) {
    	  ip.nextLine();
          System.out.println("1. Enter question id");
          int qid=ip.nextInt();
          System.out.println("2. Enter question text");
          String qtext=ip.nextLine();
          System.out.println("3. Enter solution of the question");
          String qsoln=ip.nextLine();
          System.out.println("4. Enter difficulty level of the question");
          int qdif=ip.nextInt();
          System.out.println("5. Enter hint");
          String hint=ip.nextLine();
          System.out.println("6. Enter question type(0 for fixed and 1 for parametrized");
          int qtype=ip.nextInt();
          System.out.println("7. Enter topic name");
          String topic=ip.nextLine();
          
          try {
	          PreparedStatement pstmt=Connect.getConnection().prepareStatement(Queries.addQuestion);
	          PreparedStatement pstmt2=Connect.getConnection().prepareStatement(Queries.addQuestiontoTopic);
	          
	          pstmt.setInt(1, qid);
	          pstmt.setString(2, qtext);
	          pstmt.setString(3, qsoln);
	          pstmt.setInt(4, qdif);
	          pstmt.setString(5, hint);
	          pstmt.setInt(6, qtype);
	          pstmt2.setString(1,topic);
	          pstmt2.setInt(2,qid);
	          ResultSet rs=pstmt.executeQuery();
	          ResultSet rs2=pstmt2.executeQuery();
	          
	          System.out.println("Question has been added to question bank!");
		 }
		 catch(SQLException e) {
	          	e.printStackTrace();
	          }
          
          System.out.println("Press 0 to go back");
          int arg=ip.nextInt();
          
          if(arg==0) {
        	  Instructor.searchOrAddQuestionInQB(ip, instructorID);
          }
         
      }

      /**************Functions called from viewCourse**********************************/
      
      public static void viewOrAddExercise(Scanner ip, int c_id) {
          
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
            
          } catch (NumberFormatException e) {
              System.out.println("Enter valid Input:");
              Instructor.viewOrAddTA(ip, c_id);
              return;
              
          } catch (Exception e) {
              e.printStackTrace();
              
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
              }
              else {
                  System.out.println("Invalid input");
              }
                    
          } catch(NumberFormatException e) {
              System.out.println("Enter valid input");
              //Instructor.showHomePage(ip, instructorID);
          }
          catch (Exception e) {
              e.printStackTrace();
          } finally {
              Connect.close(ps);
              Connect.close(rs);
              
          }
          //Go back to view Course page on succes/failure
          Instructor.viewCourse(ip, instructorID);
          return;
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
              
              //What if there are two TA's for course
              while(rsTA.next()) {
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
              e.printStackTrace();
          } catch (NumberFormatException e) {
              System.out.println("Invalid Input");
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              Connect.close(psTA);
              Connect.close(rsTA);
          }
          Instructor.viewOrAddTA(ip, c_id);  
          return;
      }
      
      //Check should be put on PG_ENROLLED table so that a student 
      //cannot ne both TA and enrolled for the course
      public static void addTA(Scanner ip, int c_id) {
          
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
   
}
