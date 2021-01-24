package org.example.dao;

import org.example.models.Department;
import org.example.models.Employee;
import org.example.models.PairCase;

import java.sql.Date;
import java.util.List;

public interface DepartmentDao {
    Department findDepartment (long id);
    boolean addDepartment(Department department);
    boolean removeDepartment (long id);
    boolean updateDepartment (long id, Department department);
    Employee findEmployeeById(long id);
    boolean addEmployee(long departmentId,Employee employee);
    boolean removeEmployee (long id);
    boolean updateEmployee (long id, Employee employee);
    List<PairCase> findAllAverageSalariesByDepartment();
    List<Employee> findEmployeesByDepartmentId(long departmentId);
    List<Employee> findEmployeesByDepartmentId(long departmentId,Date birthDate);
    List<Employee> findEmployeesByDepartmentId(long departmentId,Date date1,Date date2);
}
