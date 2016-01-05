create table book (
  book_id int not null primary key,
  title varchar(255) not null,
  author varchar(255)
);

create table subject_type (
  subject_type_id smallint not null primary key,
  subject_type_name varchar(255) not null
);

create table subject (
  subject_id int not null primary key,
  subject_type_id smallint not null references subject_type(subject_type_id)

);

create table department (
  department_id int not null,
  subject_type_id smallint not null,
  name varchar(255),
  primary key (department_id,subject_type_id),
  foreign key (department_id,subject_type_id) references subject(subject_id, subject_type_id),
  check (subject_type_id=1)
);

create table reader (
  reader_id int not null primary key,
  subject_type_id smallint not null,
  first_name varchar (255) not null,
  last_name varchar (255) not null,
  middle_name varchar(255),
  phone char(10),
  address varchar(512),
  foreign key (reader_id,subject_type_id) references subject(subject_id, subject_type_id),
  check (subject_type_id=2)
);

create table copy(
  copy_id int not null primary key,
  book_id int not null references book(book_id),
  book_barcode varchar(12) not null
);

create table transfer (
  transfer_id int not null primary key,
  from_subject_id int not null,
  from_subject_type_id smallint not null,
  to_subject_id int not null,
  to_subject_type_id int not null,
  transfer_date date not null,
  transfer_time time not null,
  foreign key (from_subject_id,from_subject_type_id) references subject(subject_id, subject_type_id),
  foreign key (to_subject_id,to_subject_type_id) references subject(subject_id, subject_type_id),
  check (from_subject_id<>to_subject_id)
);

create table transfer_invoice(
  transfer_id int not null references transfer(transfer_id),
  copy_id int not null references copy(copy_id),
  constraint transfer_invoice_pk primary key(transfer_id,copy_id)
);

create sequence book_seq;
create sequence owner_seq;
create sequence copy_seq;
create sequence transfer_seq;