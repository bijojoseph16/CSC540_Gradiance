package code;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import dbconnect.Connect;
import oracle.jdbc.OracleTypes;

public class Queries{
	static final String getstudents="Select * from student";
	static final String checkInstructor = " select count(*) as \"prof_exists\" from Professor where userid=? and password=?";
	static final String getInstructorByUIdPass = " select * from Professor where userid=? and password=?";
	static final String getInstructorByUId = "select * from  Professor where professor_id=? ";
	
	static final String checkStudent = " select count(*) as \"exists\" from student where userid=? and password=?";
	static final String getStudentByUIdPass = " select * from student where userid=? and password=?";
	static final String getStudentByUId = " select * from student where student_id=? ";
	static final String checkIfUgStudent = " select count(*) as \"ug_student\" from ug where student_id=?";
	static final String checkIfPgStudent = " select count(*) as \"pg_student\" from pg where student_id=?";
	static final String getUgStudentCourses = " select c.c_id, c.course_id, c.course_name from ug_enrolled uge, course c where uge.student_id=? "
												+ "and uge.course_id = c.c_id";
	static final String getPgStudentCourses = " select c.c_id, c.course_id, c.course_name from pg_enrolled pge, course c where pge.student_id=? "
												+ "and pge.course_id = c.c_id";
	
	static final String getStudentPastHwforCourse = "select sse.ex_id, sse.attempt_number, sse.points_earned, sse.time_of_submission"
			+ " from student_submits_exercise sse, course_has_exercise c, enrollments p where"
			+ " sse.ex_id = c.exercise_id and c.course_id = p.course_id and sse.student_id = p.student_id and p.student_id=? and p.course_id=?";
		
	static final String getStudentCurrtHwforCourse = "select e.ex_id as HW_ID, e.exercise_name, e.num_questions, e.pts_for_correct, e.pts_for_incorrect, e.scoring_policy, e.ex_mode, e.retries, "
			+ "(e.retries - (select count(*) from student_submits_exercise sse, course_has_exercise c1, enrollments p1 where sse.ex_id = c1.exercise_id "
			+ "and c1.course_id = p1.course_id and sse.ex_id = e.ex_id and sse.student_id = p1.student_id and p1.student_id=? and p1.course_id=?)) as retries_left "
			+ "from exercise e, course_has_exercise c, enrollments p, exercise_has_duration exd "
			+ "where e.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id=? and p.course_id=?"
			+ "and e.ex_id = exd.ex_id and exd.startdate < to_timestamp(sysdate) and exd.enddate > to_timestamp(sysdate)";

	static final String getStudentScoreQuery = "DECLARE  " + 
			"   ss_policy varchar(20); " + 
			"   f_score number; " + 
			"   max_score integer; " + 
			"   last_score integer; " + 
			"   avg_score number; " + 
			"   ee_id integer; " + 
			"   ss_id integer; " + 
			"   cc_id integer; " +  
			"begin " + 
			"   ss_id := ?; " + 
			"   cc_id := ?; " + 
			"   ee_id := ?; " + 
			"   SELECT scoring_policy " + 
			"   into ss_policy " + 
			"     FROM exercise " + 
			"     WHERE ex_id = ee_id; " + 
			"    select max(sse.points_earned) into max_score " + 
			"    from student_submits_exercise sse, course_has_exercise c, enrollments p " + 
			"    where sse.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id = sse.student_id and p.student_id=ss_id and p.course_id=cc_id and sse.ex_id = ee_id; " + 
			"    select sse.points_earned into last_score " + 
			"    from student_submits_exercise sse, course_has_exercise c, enrollments p " + 
			"    where sse.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id = sse.student_id and p.student_id=ss_id and p.course_id=cc_id and sse.ex_id = ee_id and " + 
			"    sse.attempt_number = (select max(sse1.attempt_number) " + 
			"    from student_submits_exercise sse1, course_has_exercise c1, enrollments p1 " + 
			"    where sse1.ex_id = c1.exercise_id and c1.course_id = p1.course_id and p1.student_id = sse1.student_id and p1.student_id=ss_id and p1.course_id=cc_id and sse1.ex_id = ee_id);     " +  
			"    select avg(sse.points_earned) into avg_score " + 
			"    from student_submits_exercise sse, course_has_exercise c, enrollments p " + 
			"    where sse.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id = sse.student_id and p.student_id=ss_id and p.course_id=cc_id and sse.ex_id = ee_id; " + 
			"   IF ss_policy = 'Latest_Attempt' THEN " + 
			"      f_score := last_score; " + 
			"   ELSIF ss_policy = 'Average_Attempt' THEN " + 
			"      f_score := avg_score; " + 
			"   ELSIF ss_policy = 'Maximum_Score' THEN " + 
			"      f_score := max_score; " + 
			"   END IF; " +
			"   ? := f_score; " + 
			"END;";
	
	static final String fetchExDetails = "select * from exercise where ex_id = ?";
	
	static final String fetchExQuestions = "select ehq.question_id, q.text, p.parameters, p.answer, q.solution, q.question_level, q.hint "
			+ "from exercise_has_question ehq, question q, parameter p "
			+ "where ehq.ex_id=? and ehq.question_id = q.question_id and q.question_id = p.question_id "
			+ "and (p.param_id = (select floor(dbms_random.value(1,4)) as num from dual) or p.param_id = 0)";	
	
	static final String fetchAdaptiveExQuestions = "select ehq.question_id, q.text, p.parameters, p.answer, q.solution, q.question_level, q.hint "
			+ "from ex_has_ques ehq, question q, parameter p "
			+ "where ehq.exercise_id=? and ehq.question_id = q.question_id and q.question_id = p.question_id "
			+ "and (p.param_id = (select floor(dbms_random.value(1,4)) as num from dual) or p.param_id = 0)";	
	
	static final String updateStudentExeriseSubmission = "INSERT INTO STUDENT_SUBMITS_EXERCISE (" +  
			"  STUDENT_ID, " + 
			"  EX_ID, " + 
			"  ATTEMPT_NUMBER, " + 
			"  POINTS_EARNED, " + 
			"  TIME_OF_SUBMISSION " + 
			")" + 
			"VALUES " + 
			"(" + 
			"  ?," + 
			"  ?," + 
			"  ?," + 
			"  ?," + 
			"  to_timestamp(sysdate)" + 
			")";
	
	static final String checkTA = "Select count(*) as \"ta_exists\" from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";
	static final String getTAByUIdPass = "Select * from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";

	//Query to get course information given course ID
	static final String getCourseByCourseID = "Select * from course where course_id = ?";
    static final String getCourseDuration = "Select TO_CHAR(start_date, 'MM/DD/YYYY') as \"start_date\",TO_CHAR(end_date, 'MM/DD/YYYY') as \"end_date\" From course_has_duration Where course_id = ?";
    static final String courseExists = "Select count(*) as \"course_exists\" from course where course_id = ?";
    
    //Query to add course
    static final String addCourse = "Insert into course (course_id, course_name,course_level,max_students) values (?,?,?,?)";
    //static final String addCourseDuration = "Insert into course_has_duration(course_id, start_date, end_date)"
    //        + " values(?, to_date('?','YYYY-MM-DD HH24:MI:SS'),to_date('?','YYYY-MM-DD HH24:MI:SS')";
    
    
    static final String durationExists = "Select count(*) as \"duration_exists\" from duration where start_date = ? and end_date = ?";
    static final String addDuration = "Insert into duration(start_date, end_date)"
            + " values(?,?)";
	/*Take care when adding a question to an exercise that the question picked to add actually belongs to one of the topics from that course
	 * (select unique t.question_id from topic_has_question t, course_has_exercise c, course_has_topic ct 
   where c.course_id = ct.course_id and ct.topic_id = t.topic_id and c.exercise_id = EX_ID)*/
	
	/*Example PL/SQL for reference
	 * 
	 String mysql = "CREATE OR REPLACE FUNCTION studentCount114( s_id NUMBER) " + 
		        				"RETURN number IS " + 
		        				"   stud_cnt number := 0; " + 
		        				"BEGIN " + 
		        				"   SELECT count(*) into stud_cnt " + 
		        				"   FROM student where student_id = s_id; " + 
		        				"   RETURN stud_cnt; " + 
		        				"END;";
		        		
		        		String mysql1 = "CREATE OR REPLACE FUNCTION studentCount115 " + 
		        				"RETURN SYS_REFCURSOR IS " + 
		        				"   stud_cnt SYS_REFCURSOR; " + 
		        				"BEGIN " +
		        				"open stud_cnt for " + 
		        				"   SELECT * " + 
		        				"   FROM student; " + 
		        				"   RETURN stud_cnt; " + 
		        				"END;";
		        		
		        		PreparedStatement exSubmit1 = Connect.getConnection().prepareStatement(mysql);
		    			exSubmit1.executeQuery();
		        		
		    			CallableStatement cstmt = Connect.getConnection().prepareCall("{? = call studentCount114(?)}");
					cstmt.registerOutParameter(1, Types.INTEGER);
					cstmt.setInt(2, studentId);
					cstmt.executeUpdate();
			        int cancel= cstmt.getInt(1);
			        System.out.print("Student Count is  "+cancel);
			        
			        
			        
			        PreparedStatement exSubmit11 = Connect.getConnection().prepareStatement(mysql1);
					exSubmit11.executeQuery();
		    		
					CallableStatement cstmt1 = Connect.getConnection().prepareCall("{? = call studentCount115()}");
					cstmt1.registerOutParameter(1, OracleTypes.CURSOR);
				
					cstmt1.executeUpdate();
					// get cursor and cast it to ResultSet
					ResultSet rs = (ResultSet) cstmt1.getObject(1);
					showResultsSet(rs); 
	 
	 * */

    static final String addCourseDuration = "Insert into course_has_duration(course_id, start_date, end_date)"
            + " values(?,?,?)";
    
    //Query to enroll a UG Student
    static final String enrollUGStudent = "Insert into ug_enrolled(student_id, course_id) values(?,?)";
    
    //Query to enroll a PG Student if student is TA in a course
    //then he will not be enrolled.This only work if a student
    //is TA dor only one course
    static final String enrollPGStudent = "declare" + 
                                            " isTA int; "+
                                            " sid int; "+
                                            " cid int; " +
                                            " ta_ex exception; "+ 
                                            " begin " +
                                              " sid := ?;" +
                                              " cid := ?;"+ 
                                              " select count(*) into isTA " + 
                                              " from PG " + 
                                              " where student_id = sid and ta_course = cid; " + 
                                              " if isTA > 0 then " + 
                                                " raise ta_ex; " +
                                              " else " +
                                                " Insert into pg_enrolled(student_id, course_id) " +
                                                " values (sid, cid); " + 
                                              " end if; "+
                                              "end;";
                                              
                                              //" exception " +
                                                //" when ta_ex then " +
                                                //" dbms_output.put_line('Student is TA for course'); " + 
                                            //" end; "; 
                                            

    //Trigger to autoincrement course count
    //The trigger has to be created at the time of table creation, so that
    //c_id_seq will have latest count
    static final String trg_c_id = "create sequence c_id_seq;"
            + "create trigger trg_c_id " + 
            "     before insert on course " + 
            "     for each row " + 
            "   begin " + 
            "    select c_id_seq.nextval " + 
            "       into :new.c_id " + 
            "       from dual; " + 
            "   end; ";
}
