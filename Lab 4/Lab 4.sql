#1
 select st.sname from student st where st.age=( SELECT MIN(s.age) FROM Student s WHERE s.major = 'Finance' OR s.snum IN (SELECT en.snum FROM Class cl, Enrolled en, Faculty fa WHERE en.cname = cl.name AND cl.fid = fa.fid AND fa.fname = 'Linda Davis'));
#2
select f.fname from faculty f where f.fid in (select fid from class group by fid having count(*) = (select count(distinct room) from class));
#3
select f.fname from faculty f where f.fid in (select fid from class group by fid having count(*) <= all(select count(*) fid from class group by fid));
#4
select fname from faculty except select fname from faculty natural join class;
#5
select s.level,s.age from student s group by s.age,s.level having s.level in (select s1.level from student s1 where s1.age = s.age group by s1.level,s1.age having count(*) >= all(select count(*) from student s2 where s1.age = s2.age group by s2.level,s2.age));
#6
select cl.name from class cl where cl.room="R128" and cl.name in (select cname from class,enrolled where class.name = enrolled.cname group by name having count(*) >= 1);
#7
select cl.name,cl.meets_at from class cl where cl.name in (select cname from class,enrolled where class.name = enrolled.cname group by name having count(*) >= 1);
#8
#Note: enrolled natural join class would be an anomaly as the name of common attribute i.e., the name of course is not same for both relations 
select s.sname from student s where s.level="JR" and s.snum in(select snum from enrolled,class where enrolled.cname = class.name and room="R128");
#9
select sname from student where age>18 and level="SR" and not major like "%Engineering";
#10
select name from class except select cname from enrolled;