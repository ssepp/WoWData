create table RealmData (realmNameRegion varchar(200), lastModified Timestamp);

insert into RealmData select externalIdentifier||'EU', '2000-01-01 12:00:00' from realm;
insert into RealmData select externalIdentifier||'US', '2000-01-01 12:00:00' from realm;