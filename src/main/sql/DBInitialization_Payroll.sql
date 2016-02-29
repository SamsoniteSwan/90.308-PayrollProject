CREATE SCHEMA IF NOT EXISTS payroll;

/* Clean database tables */
DROP TABLE IF EXISTS payroll.tblPayPeriods CASCADE;
DROP TABLE IF EXISTS payroll.tblWorkLog CASCADE;
DROP TABLE IF EXISTS payroll.tblEmployees CASCADE;
DROP TABLE IF EXISTS payroll.tblRecords CASCADE;


GRANT ALL ON *.* TO 'monty'@'localhost' IDENTIFIED BY 'some_pass';

CREATE TABLE payroll.tblEmployees(
   employeeId VARCHAR(10) NOT NULL,
   firstName VARCHAR(100) NOT NULL,
   lastName VARCHAR(100) NOT NULL,
   dob DATETIME,
   status VARCHAR(20) NOT NULL DEFAULT 'active',
   PRIMARY KEY (employeeId)
);

CREATE TABLE payroll.tblWorkLog(
   id INT NOT NULL AUTO_INCREMENT,
   workday DATETIME NOT NULL,
   employee VARCHAR(10) NOT NULL,
   hours DECIMAL(4, 2) NOT NULL,
   vacationhrs DECIMAL NOT NULL,
   FOREIGN KEY (employee) REFERENCES tblEmployees(employeeId),
   PRIMARY KEY (id)
);

CREATE TABLE payroll.tblPayPeriods(
   id INT NOT NULL AUTO_INCREMENT,
   wage DECIMAL(5, 2) NOT NULL,
   startDate DATETIME NOT NULL,
   endDate DATETIME NOT NULL,
   employee VARCHAR(10) NOT NULL,
   FOREIGN KEY (employee) REFERENCES tblEmployees(employeeId),
   PRIMARY KEY (id)
);

CREATE TABLE payroll.tblRecords(
   recordId INT NOT NULL AUTO_INCREMENT,
   employeeId VARCHAR(10) NOT NULL,
   employeeFN VARCHAR(100),
   employeeLN VARCHAR(100),
   period VARCHAR(50) NOT NULL,
   PRIMARY KEY (recordId)
);

INSERT INTO payroll.tblEmployees (employeeId, lastName, firstName, dob) VALUES ("0000", "Smith", "Joe", '1999/10/10');
INSERT INTO payroll.tblEmployees (employeeId, lastName, firstName, dob) VALUES ("1111", "Stevensen", "Steve", '1986/4/18');

INSERT INTO payroll.tblWorkLog (employee, workday, hours, vacationhrs) VALUES ("0000", '2015/10/10', '5.5', '3.0');
INSERT INTO payroll.tblWorkLog (employee, workday, hours, vacationhrs) VALUES ("0000", '2015/10/11', '8.0', '0.0');
INSERT INTO payroll.tblWorkLog (employee, workday, hours, vacationhrs) VALUES ("1111", '2015/10/10', '7.5', '0.0');

INSERT INTO payroll.tblPayPeriods (employee, startDate, endDate, wage) VALUES ("0000", '2015/10/10', '2015/10/17', '35.20');
INSERT INTO payroll.tblPayPeriods (employee, startDate, endDate, wage) VALUES ("0000", '2015/10/18', '2015/10/25', '37.65');

INSERT INTO payroll.tblRecords (employeeId, employeeFN, employeeLN, period) VALUES ('0000', 'Joe', 'Schmoe', '1/15/2015-1/29/2015');
INSERT INTO payroll.tblRecords (employeeId, employeeFN, employeeLN, period) VALUES ('1111', 'Thelma', 'Louise', '1/30/2015-2/13/2015');


