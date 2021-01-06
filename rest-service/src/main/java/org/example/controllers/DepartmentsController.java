package org.example.controllers;
import org.example.dao.DepartmentDao;
import org.example.models.Department;
import org.example.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/departments")
public class DepartmentsController {

    private final Logger logger = LoggerFactory.getLogger(DepartmentsController.class);

    private final DepartmentDao departmentDAO;

    @Autowired
    public DepartmentsController(DepartmentDao departmentDao) {
        this.departmentDAO = departmentDao;
    }

    @GetMapping("/averageSalary")
    public ResponseEntity<Map<Department,Double>> getAverageSalary(){
        logger.debug("Method \"getAverageSalary()\" was called on request mapping /departments/averageSalary");
        Map<Department,Double> responseMap = departmentDAO.findAllAverageSalariesByDepartment();
        return responseMap != null && !responseMap.isEmpty()
                ? new ResponseEntity<>(responseMap, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{department_id}/employees")
    public ResponseEntity<List<Employee>> getAllEmployeesByDepartmentId(@PathVariable("department_id") long departmentId,
                                                                        @RequestParam(name = "Date1",required = false) Date date1,
                                                                        @RequestParam(name = "Date2",required = false) Date date2){
        List<Employee> resultList;
        if(date1 != null && date2 != null) {
            resultList = departmentDAO.findEmployeesByDepartmentId(departmentId, date1, date2);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + departmentId +"/employees?Date1=" + date1 + "&Date2=" + date2);
        } else if(date1 != null) {
            resultList = departmentDAO.findEmployeesByDepartmentId(departmentId,date1);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + departmentId +"/employees?Date1=" + date1);
        } else {
            resultList = departmentDAO.findEmployeesByDepartmentId(departmentId);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + departmentId +"/employees");
        }
        return resultList != null && !resultList.isEmpty()
                ? new ResponseEntity<>(resultList,HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department department){
        logger.debug("Method \"createDepartment\" was called on request mapping /departments");
        final boolean created = departmentDAO.addDepartment(department);
        return created
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{department_id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("department_id") long departmentId){
        logger.debug("Method \"deleteDepartmentById\" was called on request mapping /departments/" + departmentId);
        final boolean deleted = departmentDAO.removeDepartment(departmentId);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{department_id}")
    public ResponseEntity<?> updateDepartmentById(@PathVariable("department_id") long departmentId,
                                                  @RequestBody Department department){
        logger.debug("Method \"updateDepartmentById\" was called on request mapping /departments/" + departmentId);
        final boolean updated = departmentDAO.updateDepartment(departmentId,department);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/{department_id}/employees")
    public ResponseEntity<?> createEmployeeByDepartmentId(@PathVariable("department_id")long departmentId,
                                                          @RequestBody Employee employee){
        logger.debug("Method \"createEmployeeByDepartmentId\" was called on request mapping /departments/" + departmentId + "/employees");
        final boolean created = departmentDAO.addEmployee(departmentId, employee);
        return created
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("\"/{department_id}/employees/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("department_id") long department_id,
                                                     @PathVariable("id") long id){
        logger.debug("Method \"deleteEmployeeById\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        Employee employee = departmentDAO.findEmployeeById(id);
        return employee != null
                ? new ResponseEntity<>(employee,HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{department_id}/employees/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("department_id") long department_id,
                                                @PathVariable("id") long id){
        logger.debug("Method \"deleteEmployeeById\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        final boolean deleted = departmentDAO.removeEmployee(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{department_id}/employees/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable("department_id") long department_id,
                                                @PathVariable("id") long id,
                                                @RequestBody Employee employee){
        logger.debug("Method \"updateEmployeeById\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        final boolean updated = departmentDAO.updateEmployee(id,employee);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
