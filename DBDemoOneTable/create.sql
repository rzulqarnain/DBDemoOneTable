create table Course
(
	ID char(6) not null,
	name varchar(50) not null,
	credits int not null,
	primary key (ID)
);
insert into Course values ('CP1360', 'Introduction to Programming for Non-Programmers',3);
insert into Course values ('CP1450', 'Operating Systems',4);
insert into Course values ('CP1810', 'Fundamental Programming Constructs',5);
insert into Course values ('CP1931', 'Systems Analysis',6);
insert into Course values ('CP1990', 'Computer Hardware',3);
insert into Course values ('CP2060', 'Operating Systems Fundamentals',4);
insert into Course values ('CP3210', 'Object Oriented and Event Driven Programming',5);
insert into Course values ('CP3300', 'Data Structures',6);
insert into Course values ('CP3450', 'Database Design and Implementation',3);
insert into Course values ('CR1101', 'Network Foundations',4);
insert into Course values ('CR1250', 'Information Systems Security',5);
insert into Course values ('CR2700', 'Network Operating System Administration',6);
insert into Course values ('MA1900', 'Problem Solving for Information Technology',3);
insert into Course values ('MA1910', 'Introduction to Numerical Problem Solving',4);
insert into Course values ('MC1800', 'Software Applications I',5);
insert into Course values ('MC1801', 'Software Applications II',6);