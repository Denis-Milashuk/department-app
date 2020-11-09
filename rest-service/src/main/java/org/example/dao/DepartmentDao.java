package org.example.dao;

import org.example.models.Department;
import org.example.models.Employee;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface DepartmentDao {
    boolean addDepartment(Department department);
    boolean removeDepartment (long id);
    boolean updateDepartment (long id, Department department);
    boolean addEmployee(long departmentId,Employee employee);
    boolean removeEmployee (long id);
    boolean updateEmployee (long id, Employee employee);
    Map<Department,Double> findAllAverageSalariesByDepartment();
    List<Employee> findEmployeesByDepartmentId(long departmentId);
    List<Employee> findEmployeesByDepartmentId(long departmentId,Date birthDate);
    List<Employee> findEmployeesByDepartmentId(long departmentId,Date date1,Date date2);
}
