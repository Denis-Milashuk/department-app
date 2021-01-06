ALTER TABLE employee DROP CONSTRAINT Unique_Names;
ALTER TABLE employee DROP CONSTRAINT FK_Employees;

TRUNCATE TABLE department;
TRUNCATE TABLE employee;

ALTER TABLE employee ADD CONSTRAINT Unique_Names UNIQUE (first_name,last_name);
ALTER TABLE employee ADD CONSTRAINT FK_Employees FOREIGN KEY (department_id) REFERENCES department(id);
ALTER TABLE department ALTER COLUMN id RESTART WITH 1;
ALTER TABLE employee ALTER COLUMN id RESTART WITH 1;