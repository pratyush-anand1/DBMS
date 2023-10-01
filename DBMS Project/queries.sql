#a
select *from rent_property natural join address where year_of_construction>2020;
#b
select *from address natural join sale_property where selling_price>=3000000 and selling_price<=50000000;
#c
select *from address natural join rent_property where monthly_rent>=3000 and location='G.S.road' and bedrooms>=2;
#d
with details(aid,aname,price) as (select aid,name,sum(selling_price) from agent natural join sold natural join sale_property where year_of_construction = 2020 group by aid,name) select aname,price from details where price = (select max(price) from details);
#e
with details(aid,aname,avg_price,avg_time) as (select aid,name,avg(selling_price), datediff(date_of_sell,date_of_availability) from sold natural join agent natural join selling_dates natural join sale_property group by aid,name,date_of_sell,date_of_availability) select * from details;
#f
select * from address natural join sale_property where selling_price = (select max(selling_price) from sale_property);
select * from address natural join rent_property where monthly_rent = (select max(monthly_rent) from rent_property);