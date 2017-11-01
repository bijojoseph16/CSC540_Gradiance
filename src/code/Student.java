package code;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javafx.util.Pair;
import oracle.jdbc.OracleTypes;
import dbconnect.Connect;

public class Student {

	final static int ST_LEVEL = 3;
	final static int MIN_LEVEL = 1;
	final static int MAX_LEVEL = 6;
	
	//show Student homepage
	public static void showHomePage(Scanner ip, int studentId) {
		
		System.out.println("*****Student Homepage******");
        System.out.println("1. View Profile");
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
				System.out.println("Or Select a course Id to view details");
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
				System.out.println("Or Select a course Id to view details");
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

			PreparedStatement psPgCourses = Connect.getConnection().prepareStatement(Queries.getStudentPastHwforCourse);
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

			PreparedStatement psPgCourses = Connect.getConnection().prepareStatement(Queries.getStudentCurrtHwforCourse);
			psPgCourses.setInt(1, studentId); 
			psPgCourses.setInt(2, courseId);
			psPgCourses.setInt(3, studentId);
			psPgCourses.setInt(4, courseId);
			showResultsSet(psPgCourses.executeQuery());
			ResultSet resPgCourses = psPgCourses.executeQuery();
			
			Map<Integer, Integer> hwMap = new HashMap<Integer, Integer>();
			Map<Integer,Integer> exRets = new HashMap<Integer,Integer>();
			
			while(resPgCourses.next()) {
				
				//add the current hws in the list and no. of retries left
		        hwMap.put(resPgCourses.getInt("hw_id"), resPgCourses.getInt("retries_left"));
		        exRets.put(resPgCourses.getInt("hw_id"), resPgCourses.getInt("retries"));
		        
		        //No need to fetch student scores if he has not attempted the HW
		        if(resPgCourses.getInt("retries_left") != resPgCourses.getInt("retries")) {
		        		CallableStatement cs2 = Connect.getConnection().prepareCall(Queries.getStudentScoreQuery);
			        cs2.setInt(1, studentId);
			        cs2.setInt(2, courseId);
			        cs2.setInt(3, resPgCourses.getInt("hw_id"));
			        cs2.registerOutParameter(4, Types.INTEGER);
			        cs2.execute();
			        System.out.println("Curent Score for HW_ID "+ resPgCourses.getInt("hw_id") +" :" + cs2.getInt(4));
			        cs2.close(); 
		        }else {		        			
		        		System.out.println("Curent Score for HW_ID "+ resPgCourses.getInt("hw_id") +" :No Submissions so far");
		        }
			}
			
			System.out.println("Press 0 to Go Back");
			System.out.println("Press the HW no. to attempt (only if # retries > 0)");
			
			int choice = ip.nextInt();
			if(0 == choice) {
				Student.showCourseHW(ip, studentId, courseId);
			}else if(hwMap.containsKey(choice) && hwMap.get(choice) > 0) {
					
					System.out.println("Opening HW " + choice + " for Attempt # " + exRets.get(choice));
					Student.attemptHW(ip, studentId, courseId, choice, exRets.get(choice) - hwMap.get(choice) + 1);
			}else {
				System.out.println("Invalid Input. Enter your choice again");
				Student.showCurrHW(ip, studentId, courseId);
			}
		
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	//attempt Given HW
	public static void attemptHW(Scanner ip, int studentId, int courseId, int ex_id, int attemptNo) {
		
		System.out.println("************* Attempt HW " + ex_id + "****************");

		try {
			
			
			PreparedStatement exDets = Connect.getConnection().prepareStatement(Queries.fetchExDetails);
			exDets.setInt(1, ex_id); 
			ResultSet resExDets = exDets.executeQuery();
			resExDets.next();
			
			int pts_per_correct = resExDets.getInt("pts_for_correct");
			int pts_per_wrong = resExDets.getInt("pts_for_incorrect");
			int num_ques = resExDets.getInt("num_questions");
			String s_policy = resExDets.getString("scoring_policy");
			int retries = resExDets.getInt("retries");
			String mode = resExDets.getString("ex_mode");
			
			if(mode.equalsIgnoreCase("S")) {

				PreparedStatement exQues = Connect.getConnection().prepareStatement(Queries.fetchExQuestions);
				exQues.setInt(1, ex_id); 
				//showResultsSet(exQues.executeQuery());
				ResultSet resExQues = exQues.executeQuery();

				int q_cnt = 0;
				Map<Integer, Map<String, String>> q_details  = new HashMap<Integer, Map<String, String> >();
				int grade = 0;

				while(resExQues.next()) {
					Map<String, String> q_det = new HashMap<String, String>();

					q_cnt += 1;

					//show question 
					System.out.println(q_cnt + ") " + resExQues.getString("text"));
					System.out.println("Parameters: " + resExQues.getString("parameters"));

					//Get response
					System.out.println("Enter you Answer (comma separated answer of more than one apply): ");
					String yourAnswer = ip.next();

					//Store student response
					q_det.put("text", resExQues.getString("text"));
					q_det.put("params", resExQues.getString("parameters"));
					q_det.put("answer", resExQues.getString("answer"));
					q_det.put("s_answer", yourAnswer);
					q_det.put("a_status", (yourAnswer.equalsIgnoreCase(resExQues.getString("answer"))?"Correct":"Incorrect Answer"));
					q_det.put("solution", resExQues.getString("solution"));
					q_det.put("hint", resExQues.getString("hint"));
					q_det.put("level", resExQues.getString("question_level"));

					//evaluate
					if (yourAnswer.equalsIgnoreCase(resExQues.getString("answer"))) {
						grade += pts_per_correct;
					}else {
						grade += pts_per_wrong;
					}
					q_details.put(q_cnt, q_det);	
					System.out.println();
				}

				System.out.println("Generating your score...");
				System.out.println("Updating your submission ...");
				PreparedStatement exSubmit = Connect.getConnection().prepareStatement(Queries.updateStudentExeriseSubmission);
				exSubmit.setInt(1, studentId);
				exSubmit.setInt(2, ex_id);
				exSubmit.setInt(3, attemptNo);
				exSubmit.setInt(4, grade);
				exSubmit.executeQuery();

				System.out.println("----------------------------------------------------------------------------------------");
				System.out.println("Score for this Attempt is: " + grade);

				System.out.println("Review HW Below");
				Iterator it = q_details.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					Map<Integer, String> vals = (Map<Integer, String>) pair.getValue();
					System.out.println(pair.getKey() + ") " 
							+ vals.get("text") + 
							"\nParameters: " + vals.get("params") +
							" Answer: " + vals.get("answer") + 
							" Your Answer: " + vals.get("s_answer") +
							" Status: " + vals.get("a_status") +
							" Solution: " + vals.get("solution") +
							" Hint: " + vals.get("hint"));
					it.remove(); // avoids a ConcurrentModificationException
					System.out.println();
				}
				System.out.println("----------------------------------------------------------------------------------------");

			}else if(mode.equalsIgnoreCase("A")) {
				
				
				PreparedStatement adptExQues = Connect.getConnection().prepareStatement(Queries.fetchAdaptiveExQuestions);
				adptExQues.setInt(1, ex_id); 
				//showResultsSet(adptExQues.executeQuery());
				ResultSet resExQues = adptExQues.executeQuery();
				
				//create question bank from the received question set
				HashMap<Integer, HashSet<HashMap<String,String>>> qBank = new HashMap<Integer, HashSet<HashMap<String,String>>>();
				
				createLocalQuestionBank(resExQues, qBank);
				
				int numQues = num_ques; int level = ST_LEVEL;
				int q_cnt = 0; int grade = 0;
				Map<Integer, Map<String, String>> q_details  = new HashMap<Integer, Map<String, String> >();
				
				while(numQues > 0) {
					
					level=level>=MAX_LEVEL?MAX_LEVEL:level<= MIN_LEVEL?MIN_LEVEL:level;
					
					numQues--; q_cnt++;
					HashMap<String, String> curQ; 
					Map<String, String> q_det = new HashMap<String, String>();
					
					//show question 
					if(qBank.get(level).isEmpty()) {
						System.out.println("Not sufficient questions available for desired level. Exiting the exercise");
						break;	
					}else {
						curQ = qBank.get(level).iterator().next();
						System.out.println(q_cnt + ") " + curQ.get("TEXT"));
						System.out.println("Parameters: " + curQ.get("PARAMETERS"));
						qBank.get(level).remove(curQ);
					}

					//Get response
					System.out.println("Enter you Answer (comma separated answer of more than one apply): ");
					String yourAnswer = ip.next();

					//Store student response
					q_det.put("text", curQ.get("TEXT"));
					q_det.put("params", curQ.get("PARAMETERS"));
					q_det.put("answer", curQ.get("ANSWER"));
					q_det.put("s_answer", yourAnswer);
					q_det.put("a_status", (yourAnswer.equalsIgnoreCase(curQ.get("ANSWER"))?"Correct":"Incorrect Answer"));
					q_det.put("solution", curQ.get("SOLUTION"));
					q_det.put("hint", curQ.get("HINT"));
					q_det.put("level", curQ.get("QUESTION_LEVEL"));

					//evaluate
					if (yourAnswer.equalsIgnoreCase(curQ.get("ANSWER"))) {
						grade += pts_per_correct;
						level++;
					}else {
						grade += pts_per_wrong;
						level--;
					}
					q_details.put(q_cnt, q_det);	
					System.out.println();
				}
				
				System.out.println("Generating your score...");
				System.out.println("Updating your submission ...");
				PreparedStatement exSubmit = Connect.getConnection().prepareStatement(Queries.updateStudentExeriseSubmission);
				exSubmit.setInt(1, studentId);
				exSubmit.setInt(2, ex_id);
				exSubmit.setInt(3, attemptNo);
				exSubmit.setInt(4, grade);
				exSubmit.executeQuery();

				System.out.println("----------------------------------------------------------------------------------------");
				System.out.println("Score for this Attempt is: " + grade);

				System.out.println("Review HW Below");
				Iterator<Entry<Integer, Map<String, String>>> it = q_details.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					Map<Integer, String> vals = (Map<Integer, String>) pair.getValue();
					System.out.println(pair.getKey() + ") " 
							+ vals.get("text") + 
							"\nParameters: " + vals.get("params") +
							" Answer: " + vals.get("answer") + 
							" Your Answer: " + vals.get("s_answer") +
							" Status: " + vals.get("a_status") +
							" Solution: " + vals.get("solution") +
							" Hint: " + vals.get("hint"));
					it.remove(); // avoids a ConcurrentModificationException
					System.out.println();
				}
				System.out.println("----------------------------------------------------------------------------------------");
			}
			System.out.println("Press 0 to Go Back");
			ip.nextInt();
			System.out.println("Exiting HW Portal");
			Student.showCourseHW(ip, studentId, courseId);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Create a local question bank for Adaptive Exercises
	private static void createLocalQuestionBank(ResultSet rs,
			HashMap<Integer, HashSet<HashMap<String, String>>> qBank) {
	// TODO Auto-generated method stub
    	ResultSetMetaData rsmd;
    		try {
			int numRows = 0;
			rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {		
				HashMap<String, String> question  = new HashMap<String, String>();
				int q_level = 0;
				numRows += 1;
				for (int i = 1; i <= columnsNumber; i++) {
					//if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					//System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
					question.put(rsmd.getColumnName(i), columnValue);
					
					if(rsmd.getColumnName(i).equalsIgnoreCase("question_level")) {
						q_level = Integer.parseInt(columnValue);
					}
				}
				//Add the question to the question Bank
				if(qBank.containsKey(q_level)){
					qBank.get(q_level).add(question);
				}else {
					HashSet<HashMap<String, String>> hs = new HashSet<HashMap<String, String>>();
					hs.add(question);
					qBank.put(q_level, hs);
				}
	    	   }
			
			if(0 == numRows) {
				System.out.println("No Records found !!!");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
