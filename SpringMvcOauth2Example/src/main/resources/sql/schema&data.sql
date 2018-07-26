create database television;

use television;



create table  category(

categoryId int not null auto_increment,
name varchar(30) not null,
primary key(categoryId)

);

create table video(
videoId int not null auto_increment,
name varchar(30) not null,
description varchar(255),
image varchar(40),
categoryId int not null,
primary key(videoId),
constraint fk_video_category foreign key(categoryId)
references television.Category(categoryId)
on delete no action
on update no action
);

create table channel(

channelId int not null auto_increment,
name varchar(30) not null,
primary key(channelId)

);

create table playlist(

playlistId int not null auto_increment,
name varchar(30) not null,
primary key(playlistId)

);

create table user(
userId int not null auto_increment,
first_name varchar(30) not null,
last_name varchar(30) not null,
gender varchar(30) not null,

primary key(userId)

);


create table playlistvideos(

playlistId int not null,
videoId int not null,
constraint fk_pl_video foreign key(videoId)
references television.video(videoId)
on delete no action
on update no action
,
constraint fk_this_playlist foreign key(playlistId)
references television.playlist(playlistId)
on delete no action
on update no action
);


create table channelvideos(

channelId int not null,
videoId int not null,
constraint fk_chn_video foreign key(videoId)
references television.video(videoId)
on delete no action
on update no action
,
constraint fk_tv_channel foreign key(channelId)
references television.channel(channelId)
on delete no action
on update no action

);


insert into category values(1,"drama");
insert into category values(2,"comedy");
insert into category values(3,"historical");
insert into category values(4,"adventure");

insert into video values(1,"Bill Gates biography","this video shows biography of Bill Gates","img/billgates.png",3);
insert into video values(2,"Tarzan","good adventure of little boy which grew in forest with animals","img/tarzan.png",1);
insert into video values(3,"Island of Diamonds","involving drama adventure historical video showing old times tresure on some island","img/island.png",4);
insert into video values(4,"Bill and diamonds","comedy genre film which shows how many diamonds bill gathered in his life","img/bildiams.png",2);


insert into channel values(1,"comedies");
insert into channel values(2,"cartoon");
insert into channel values(3,"scientific");
insert into channel values(4,"movies");


insert into playlist values(1,"Satishs playlist");
insert into playlist values(2,"Jams's playlist");
insert into playlist values(3,"Aruns playlist");


insert into playlistvideos values (1,1);
insert into playlistvideos values (1,3);
insert into playlistvideos values (2,2);
insert into playlistvideos values (2,3);
insert into playlistvideos values (3,1);
insert into playlistvideos values (3,4);


insert into channelvideos values (1,1);
insert into channelvideos values (1,2);
insert into channelvideos values (1,3);
insert into channelvideos values (1,4);
insert into channelvideos values (2,2);
insert into channelvideos values (2,3);
insert into channelvideos values (3,3);
insert into channelvideos values (3,2);
insert into channelvideos values (4,1);
insert into channelvideos values (4,3);
insert into channelvideos values (4,2);


select c.name as "channel name",v.name as "video name",ca.name as "category" from channel c, 
video v, category ca , channelvideos chv where chv.channelId = c.channelId and 
chv.videoId = v.videoId and v.categoryId = ca.categoryId and c.channelId = 1;

select c.name as "channel name",v.name as "video name",ca.name as "category" from channel c, 
video v, category ca , channelvideos chv where chv.channelId = c.channelId and 
chv.videoId = v.videoId and v.categoryId = ca.categoryId order by c.name;


