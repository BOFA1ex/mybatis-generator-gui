create table dbconfig
(
    id          BIGINT PRIMARY KEY auto_increment,
    dbschema    varchar(256) not null,
    dbtype      varchar(256),
    description varchar(256),
    dburl       varchar(256),
    dbdrive     varchar(256),
    dbname      varchar(256),
    dbpassword  varchar(256),
    create_date timestamp,
    status      varchar(32)  not null
);

insert into DBCONFIG (DBSCHEMA, DBTYPE, DESCRIPTION, DBURL, DBDRIVE, DBNAME, DBPASSWORD,
                      CREATE_DATE, STATUS)
values ('mysql-test', 'mysql', NULL, 'jdbc:mysql://localhost:3306/test', 'com.mysql.cj.jdbc.Driver',
        'root', 'sunbofan123', null, '0');
