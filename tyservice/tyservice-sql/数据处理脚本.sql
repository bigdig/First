SELECT * FROM ty_stafflist for update;
SELECT * FROM sys_dept for update;
SELECT * FROM sys_position for update;
SELECT * FROM sys_area for update;


--1.1 更新ty_stafflist
update ty_stafflist a set dept_id = (select id from sys_dept b where a.dept_name = b.dept_name);
--补充遗漏部门数据
SELECT distinct dept_name FROM ty_stafflist where trim(dept_id) is null;
--1.2
update ty_stafflist a set position_id = (select id from sys_position b where a.position = b.position_name);
--补充遗漏职位数据
SELECT distinct position FROM ty_stafflist where trim(position_id) is null;
--1.3
update ty_stafflist a set area_id = (select id from sys_area b where a.area = b.area_name);
update ty_stafflist a set work_areaid = (select id from sys_area b where a.area = b.area_name);

--  创新
update ty_stafflist a set work_areaid = '5' where id in (SELECT id FROM ty_stafflist a where a.position like '%创新%');
--update ty_stafflist a set work_areaid = (select area_code from sys_area b where a.area = b.area_name);
--  多地
SELECT a.area,length(a.area),substr(a.area,0,2),substr(a.area,length(a.area)-1,2),a.area_id,a.work_areaid FROM ty_stafflist a where a.area like '%工作地%';
update ty_stafflist a set area_id = (select id from sys_area b where substr(a.area,0,2) = b.area_name)  where a.area like '%工作地%';
update ty_stafflist a set work_areaid = (select id from sys_area b where substr(a.area,length(a.area)-1,2) = b.area_name)  where a.area like '%工作地%';

--1.4创建sys_user
SELECT * FROM sys_user for update;
--DELETE FROM  sys_user_role where user_id <>'1';
insert into sys_user
select id,user_id,' ',tel,'0 ',staff_name,' ','0',dept_id,'0','1',' ',sysdate,'1',sysdate,'1','1001' from ty_stafflist;
--1.5创建sys_role
SELECT * FROM sys_positionrole;
SELECT * FROM sys_position;
SELECT * FROM sys_role ;
SELECT * FROM sys_user_role;

insert into sys_user_role (USER_ID, ROLE_ID, ENABLE_, REMARK_, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)
select a.id,c.role_id,null,null, '1', to_date('20-10-2016 18:49:05', 'dd-mm-yyyy hh24:mi:ss'), '1', to_date('20-10-2016 18:49:05', 'dd-mm-yyyy hh24:mi:ss')
 from ty_stafflist a,sys_position b,sys_positionrole c where a.position_id = b.id and b.id = c.position_id;
-- values ('1', 'sysmng', null, null, '1', to_date('20-10-2016 18:49:05', 'dd-mm-yyyy hh24:mi:ss'), '1', to_date('20-10-2016 18:49:05', 'dd-mm-yyyy hh24:mi:ss'));

--2.分类
SELECT * FROM ty_label where cat_id='0' for update;
SELECT * FROM ty_label where cat_id<>'0' for update;



--3服务机构
SELECT * FROM ty_serviceorg  for update;--九泰基金管理有限公司
SELECT * FROM ty_serviceorg where org_name='九泰基金管理有限公司';
SELECT org_name,COUNT（*） FROM ty_serviceorg group by org_name
update ty_serviceorg set org_level=decode(org_level,'第一梯队','1','第二梯队','2','第三梯队','3','4');
update ty_serviceorg set cust_status=decode(cust_status,'拟签约','1','潜在客户','2','已签约','3','4');
update ty_serviceorg set cust_cat=decode(cust_cat,'保险资管','1','公募','2','券商资管','3','券商自营','4','私募','5','信托','6','7');
update ty_serviceorg a set saler_id = (select id from ty_STAFFLIST b where a.service_saler = b.staff_name);
update ty_serviceorg a set saler_id = (select id from ty_STAFFLIST b where substr(a.service_saler,0,instr(a.service_saler,'、',1,1)-1) = b.staff_name) where instr(a.service_saler,'、',1,1)>0;


--4.服务机构客户
SELECT * FROM ty_orgcustomer for update;

update ty_orgcustomer a set saler_id = (select id from ty_STAFFLIST b where a.service_saler = b.staff_name);
update ty_orgcustomer a set org_id = (select id from ty_SERVICEORG b where a.org_name = b.org_name and a.service_saler=b.service_saler) ;

--5.标签
--客户会议
SELECT * FROM  ty_meeting for update;
update ty_meeting a set org_id = (select id from ty_SERVICEORG b where a.org_name = b.org_simple_name and rownum=1) ;
update ty_meeting a set customer_id = (select id from ty_orgcustomer b where a.cust_name = b.cust_name and rownum=1 ) ;

--将meeting 表的标签插入ty_customerlabel
--DELETE FROM ty_customerlabel;
SELECT * FROM ty_customerlabel for update;
insert into ty_customerlabel
select id||'01',customer_id,label1,'' from ty_meeting where trim(customer_id) is not null and trim(label1) is not null;
insert into ty_customerlabel
select id||'02',customer_id,label2,'' from ty_meeting where trim(customer_id) is not null and trim(label2) is not null;
insert into ty_customerlabel
select id||'03',customer_id,label3,'' from ty_meeting where trim(customer_id) is not null and trim(label3) is not null;
--创建临时表
DELETE FROM tmp_customerlabel;
insert into tmp_customerlabel
select rownum,'',labs from (
       SELECT distinct label_name as labs FROM ty_customerlabel where instr(label_name,'、')>0
       );

SELECT * FROM tmp_customerlabel order by to_number(id);
--一行转多行
DECLARE
    x number;
    leng number;
  BEGIN
    x := 1;
    select count(1) into leng from tmp_customerlabel;
    WHILE x <= leng LOOP
      insert into tmp_customerlabel 
        select b.ID||lpad(b.lev, 4, '0') ,
               b.label_s,
               a.label_name
          from tmp_customerlabel a
          inner join 
          (SELECT distinct T.id,
                       REGEXP_SUBSTR(T.label_name, '[^、]+', 1, LEVEL) as label_s, --返回第level次匹配的结果
                       level as lev
                  FROM (select * from tmp_customerlabel WHERE id = x ) T
                 where REGEXP_COUNT(T.label_name, '、') > 0  --包含斜杠的分隔符
                CONNECT BY LEVEL <= REGEXP_COUNT(T.label_name, '、') + 1 --循环次数
                 ORDER BY T.id) b
           on a.id = b.id;
           
        x := x + 1;

    END LOOP;
    commit;
  END;

--标签表拆分
insert into ty_customerlabel
SELECT a.id||b.id,a.customer_id,b.label_s,'' FROM ty_customerlabel a ,tmp_customerlabel b 
       where instr(a.label_name,'、')>0
         and a.label_name = b.label_name
         and trim(b.label_s) is not null;
DELETE FROM  ty_customerlabel where instr(label_name,'、')>0;
--清除重复数据
DELETE FROM tmp_clabel;
insert into tmp_clabel  
SELECT * FROM (
select min(to_number(id)) as id ,customer_id,label_name,count(1) as lc from ty_customerlabel group by customer_id,label_name
) WHERE lc>1 order by lc desc;
insert into tmp_clabel
  SELECT id,customer_id,label_name,0 FROM ty_customerlabel a 
    where exists(select 1 from tmp_clabel b WHERE a.customer_id=b.customer_id and b.label_name=a.label_name and a.id<>b.id);
SELECT * FROM tmp_clabel order by customer_id,label_name,lc;
DELETE FROM tmp_clabel WHERE lc>0;
DELETE FROM ty_customerlabel a where a.id in (SELECT id FROM tmp_clabel )
-- 
--  
--添加‘其他’标签
insert into ty_label
SELECT 100000000+ rownum,'000000001','其他类别',label_name from(
  SELECT distinct label_name as label_name FROM ty_customerlabel a WHERE label_name not in (select label_name from ty_label));
 
--更新ty_customerlabel的label_id
update ty_customerlabel a set a.label_id=(select b.id from ty_label b where b.label_name= a.label_name and rownum=1) 
       where exists(select 1 from ty_label b where a.label_name = b.label_name);

SELECT * FROM ty_customerlabel a order by customer_id;--完美世界
SELECT * FROM ty_label a WHERE cat_id='000000001'; 

-- 刷新客户标签冗余字段remark
DELETE FROM tmp_agglabel;
insert into tmp_agglabel 
select * from (
select customer_id,listagg(label_name,' ')  within GROUP (order by label_id) as alabel
from ty_customerlabel  
group by customer_id
);

update ty_orgcustomer a set mark = (select alabel from tmp_agglabel b WHERE  b.customer_id = a.id)

SELECT * FROM ty_orgcustomer;

--更改msgContent 字段类型
alter table ty_messagelog add ( msg_content1 clob null);
update ty_messagelog set msg_content1 = msg_content ;
alter table ty_messagelog  drop column msg_content; 
alter table ty_messagelog rename column msg_content1 to msg_content;

--msgContent clob字段
alter table ty_messageaudit add ( msg_content1 clob null);
update ty_messageaudit set msg_content1 = msg_content ;
alter table ty_messageaudit  drop column msg_content; 
alter table ty_messageaudit rename column msg_content1 to msg_content;

