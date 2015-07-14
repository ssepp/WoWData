create table Region (regionId integer PRIMARY KEY, externalidentifier varchar(200));

insert into Region (regionId, externalidentifier) values (-1, 'EU');
insert into Region (regionId, externalidentifier) values (-2, 'US');

alter table auction add foreign key (regionId) references Public.Region(regionId);