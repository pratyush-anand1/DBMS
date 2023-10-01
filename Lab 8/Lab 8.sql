#1
create trigger cal_credits_update after update on enrolled
for each row
begin
declare credit_count integer default 0;
select sum(case when grade = 'F' or grade is null then 0 else credits end) into credit_count from (select * from enrolled join class on enrolled.cname = class.name) as T where T.snum = NEW.snum;
update student set total_credits = credit_count where student.snum = NEW.snum;
end

create trigger cal_credits_insert after insert on enrolled
for each row
begin
declare credit_count integer default 0;
select sum(case when grade = 'F' or grade is null then 0 else credits end) into credit_count from (select * from enrolled join class on enrolled.cname = class.name) as T where T.snum = NEW.snum;
update student set total_credits = credit_count where student.snum = NEW.snum;
end

#2
create trigger insert_students after update on student
for each row
begin
case when new.total_credits >= 10 then insert into ready_students values (new.snum); else delete from ready_students where snum = new.snum ;end case;
end //