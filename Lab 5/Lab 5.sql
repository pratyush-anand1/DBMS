#1
select cl.name from class cl where cl.room="20 AVW" or cl.name in(select en.cname from enrolled en group by en.cname having count(*) >= 5);
#2
select room,count(*) from class group by room;
#3 (Note:Here,we needed to group by both attributes fname,fid as we know that we can only display x and agg_fn(y) if grouped by attribute x) 
select fname,fid,count(*) as number_of_classes from class natural join faculty group by fid,fname UNION select fname,fid, 0 as number_of_classes from faculty where fid not in (select fid from class);
#4 (Note:natural join can't be written as cname and name are different column names)
select name,room,count(*) as number_of_students from class,enrolled where class.name = enrolled.cname group by name,room UNION select name,room,0 as number_of_students from class where name not in (select cname from enrolled);
#5
select f.fname from faculty f where deptid=20 and f.fid in (select cl.fid from class cl where room="R128");
#6
select major,max(age) as maximum_age from student group by major;
#7
select s.sname as name from student s where s.sname like "%son%" union select f.fname from faculty f where f.fname like "%son%";
#8
select supervisor from supervisiors where name="Ravi";
#9
select supervisor from supervisiors where name in (select supervisor from supervisiors where name = "Ravi") ;
#10
select distinct s.name as supervisior,t.name as name from supervisiors as s,supervisiors as t where s.name = t.supervisor;
 