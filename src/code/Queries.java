package code;

public class Queries{
	static final String getstudents="Select * from student";
	static final String checkInstructor = " select count(*) as \"prof_exists\" from Professor where userid=? and password=?";
	static final String getInstructorByUIdPass = " select * from Professor where userid=? and password=?";
	static final String checkStudent = " select count(*) as \"exists\" from student where userid=? and password=?";
	static final String getStudentByUIdPass = " select * from student where userid=? and password=?";
	static final String checkTA = "Select count(*) as \"ta_exists\" from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";
	static final String getTAByUIdPass = "Select * from student s, pg p where userid=? and password=? and s.student_id = p.student_id and p.ta_course <> 0";
}
