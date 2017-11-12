# CSC540_Gradiance
Gradiance like application for CSC540.

# SQL Quereies

###  1. Find names of all students of CSC540 that attempted Hw 1 but did not attend Hw2.
  
  The query below is similar, it find all the students in CSC440 that have taken HW2 but not HW4  (minus in Oracle is similar to except)
  
  ``` sql
  select distinct s.student_id, s.firstname, s.lastname
from course_has_exercise c_ex, course c, student s, student_submits_exercise s_ex
where c.course_id = 'CSC440' and c.c_id = c_ex.course_id and s.student_id = s_ex.student_id and s_ex.ex_id = 2

minus

select distinct s.student_id, s.firstname, s.lastname
from course_has_exercise c_ex, course c, student s, student_submits_exercise s_ex
where c.course_id = 'CSC440' and c.c_id = c_ex.course_id and s.student_id = s_ex.student_id and s_ex.ex_id = 4;
 ```
###  2. 

###  3.List all the courses and the number of students enrolled
```sql
select c.course_id, c.course_name, count(e.student_id) as Students_Enrolled
from course c, enrollments e
where c.c_id = e.course_id
group by c.course_id, c.course_name;
```
