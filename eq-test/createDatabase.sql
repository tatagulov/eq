create table person_type (
  person_type_id SMALLINT NOT NULL PRIMARY KEY,
  person_type_name VARCHAR(255) NOT NULL
);

create table person (
  person_id INTEGER not null PRIMARY KEY,
  person_type_id SMALLINT NOT NULL REFERENCES person_type(person_type_id),
  first_name VARCHAR (255) not null,
  last_name VARCHAR(255),
  birth_date date
);

CREATE table dep (
  dep_id SMALLINT NOT NULL PRIMARY KEY,
  dep_name VARCHAR(255) NOT NULL
);

create table person_move (
  person_move_id INTEGER NOT NULL PRIMARY KEY,
  person_id INTEGER NOT NULL REFERENCES person(person_id),
  from_dep_id SMALLINT NOT NULL REFERENCES dep(dep_id),
  to_dep_id SMALLINT NOT NULL REFERENCES dep(dep_id),
  move_date date
);