create database mafia;

use mafia;

drop table roominfo;

create table roominfo(
                         id int not null auto_increment,
                         code varchar(10) not null unique,
                         playerNum int not null,
                         useReporter boolean,
                         usePsychopath boolean,
                         primary key(id)
);

drop table player;

create table player(
                       id int not null auto_increment,
                       nickname varchar(10) not null,
                       isHost boolean,
                       code varchar(10),
                       primary key(id),
                       foreign key(code)
                           references roominfo(code) on delete cascade
);
