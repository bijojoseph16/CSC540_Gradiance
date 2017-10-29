package code;

public class Queries{
	static final String getstudents="Select * from student";
	static final String checkInstructor = " select count(*) as \"prof_exists\" from Professor where userid=? and password=?";
	static final String getInstructorByUIdPass = " select * from Professor where userid=? and password=?";
	
	
	static final String checkStudent = " select count(*) as \"exists\" from student where userid=? and password=?";
	static final String getStudentByUIdPass = " select * from student where userid=? and password=?";
	static final String getStudentByUId = " select * from student where student_id=? ";
	static final String checkIfUgStudent = " select count(*) as \"ug_student\" from ug where student_id=?";
	static final String checkIfPgStudent = " select count(*) as \"pg_student\" from pg where student_id=?";
	static final String getUgStudentCourses = " select c.c_id, c.course_id, c.course_name from ug_enrolled uge, course c where uge.student_id=? "
												+ "and uge.course_id = c.c_id";
	static final String getPgStudentCourses = " select c.c_id, c.course_id, c.course_name from pg_enrolled pge, course c where pge.student_id=? "
												+ "and pge.course_id = c.c_id";
	
	static final String getUgStudentPastHwforCourse = "select sse.ex_id, sse.attempt_number, sse.points_earned, sse.time_of_submission"
			+ " from student_submits_exercise sse, course_has_exercise c, ug_enrolled p where"
			+ " sse.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id=? and p.course_id=?";
	
	static final String getPgStudentPastHwforCourse = "select sse.ex_id, sse.attempt_number, sse.points_earned, sse.time_of_submission"
			+ " from student_submits_exercise sse, course_has_exercise c, pg_enrolled p where"
			+ " sse.ex_id = c.exercise_id and c.course_id = p.course_id and p.student_id=? and p.course_id=?";

	
	static final String checkTA = "Select count(*) as \"ta_exists\" from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";
	static final String getTAByUIdPass = "Select * from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";
	
	
}
