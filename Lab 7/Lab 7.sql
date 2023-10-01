#1
create function fcount(facName varchar(20))
returns integer
deterministic
begin
declare f_count integer;
select count(*) into f_count from enrolled where cname in(select distinct name from class natural join faculty where faculty.fname = facName);
return f_count;
end //

#2
create function scount(cName varchar(40))
returns integer
deterministic
begin
declare s_count integer;
select count(*) into s_count from enrolled where grade = "F" and enrolled.cname = cName;
return s_count;
end //