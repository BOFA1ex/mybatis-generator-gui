create table dbconfig
(
    id            BIGINT PRIMARY KEY auto_increment,
    dbconnectname varchar(256),
    dbschema      varchar(256) not null,
    dbtype        varchar(256) not null,
    description   varchar(256),
    dburl         varchar(256) not null,
    dbdrive       varchar(256) not null,
    dbname        varchar(256) not null,
    dbpassword    varchar(256) not null,
    create_date   timestamp,
    status        varchar(1)   not null
);

insert into DBCONFIG (DBSCHEMA, DBCONNECTNAME, DBTYPE, DESCRIPTION, DBURL, DBDRIVE, DBNAME, DBPASSWORD,
                      CREATE_DATE, STATUS)
values ('test', 'mysql-test', 'mysql', NULL, 'jdbc:mysql://localhost:3306/test', 'com.mysql.cj.jdbc.Driver',
        'root', 'sunbofan123', null, '0');
