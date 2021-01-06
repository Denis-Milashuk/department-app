DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS department;

CREATE TABLE IF NOT EXISTS department
(
    id SERIAL PRIMARY KEY,
    tittle VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS employee
(
    id SERIAL PRIMARY KEY,
    department_id INTEGER NOT NULL ,
    first_name VARCHAR(60) NOT NULL,
    last_name  VARCHAR(60) NOT NULL,
    birth_date DATE,
    salary NUMERIC(100,3),
    CONSTRAINT Unique_Names UNIQUE (first_name,last_name),
    CONSTRAINT FK_employees FOREIGN KEY (department_id) REFERENCES department(id)
);
