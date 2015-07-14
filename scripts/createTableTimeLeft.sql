create table TimeLeft (timeLeftId integer PRIMARY KEY, externalidentifier varchar(200));

insert into TimeLeft (timeLeftId, externalidentifier) values (-1, 'Short');
insert into TimeLeft (timeLeftId, externalidentifier) values (-2, 'Medium');
insert into TimeLeft (timeLeftId, externalidentifier) values (-3, 'Long');
insert into TimeLeft (timeLeftId, externalidentifier) values (-4, 'Very long');

alter table auction add foreign key (timeLeftId) references Public.TimeLeft(timeLeftId);