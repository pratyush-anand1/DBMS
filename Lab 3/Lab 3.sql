use university;
#1
SELECT level,avg(age) from university.student group by level;
#2
SELECT level,avg(age) from university.student group by level HAVING NOT level="JR";
SELECT level,average_age from (select level,avg(age) as average_age from university.student group by level)as T where not level="JR";
#3
SELECT fname,count(meets_at) from university.faculty natural join university.class group by fname;
SELECT fname,count(meets_at) from university.faculty,university.class where university.faculty.fid = university.class.fid group by fname;
#4
SELECT sname,count(sname) from university.student natural join university.enrolled where cname = "Database Systems" group by sname except (SELECT sname,count(sname) from university.student natural join university.enrolled where cname="Operating System Design" group by sname);
SELECT sname,count(sname) from student,enrolled where student.snum = enrolled.snum and cname = "Database Systems" group by sname except (SELECT sname,count(sname) from student,enrolled where student.snum = enrolled.snum and cname="Operating System Design" group by sname);
#5
SELECT cname,avg(age) from university.student natural join university.enrolled group by cname having count(snum) > 2;
SELECT cname,avg(age),count(sname) from university.student,university.enrolled where university.student.snum = university.enrolled.snum group by cname having count(sname) > 2;
#6
select fid,count(fid) from university.faculty natural join university.class group by fid having count(fid) > 1;
#7
select snum,count(snum) from student natural join enrolled group by snum having count(snum)>1; 
select student.snum,count(student.snum) from student,enrolled where student.snum = enrolled.snum group by snum having count(student.snum)>1;
#when not using natural join always mention name of table from where the column in derieved
#8
select * from student order by age;
#9
select sname,snum from student where major like "%Engineering";
#10
 select major,count(sname) from student group by major having major like "%Engineering";