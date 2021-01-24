package org.example.controllers;
import org.example.dao.DepartmentDao;
import org.example.models.Department;
import org.example.models.Employee;
import org.example.models.PairCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;



@RestController
@RequestMapping("/departments")
public class DepartmentsRestController {

    private final Logger logger = LoggerFactory.getLogger(DepartmentsRestController.class);

    private final DepartmentDao departmentDAO;

    @Autowired
    public DepartmentsRestController(DepartmentDao departmentDao) {
        this.departmentDAO = departmentDao;
    }

    @GetMapping("/averageSalary")
    public ResponseEntity<List<PairCase>> getDepartmentsWithAverageSalaries(){
        logger.debug("Method \"getAverageSalary()\" was called on request mapping /departments/averageSalary");
        List<PairCase> responseList = departmentDAO.findAllAverageSalariesByDepartment();
        return responseList != null && !responseList.isEmpty()
                ? new ResponseEntity<>(responseList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{department_id}/employees")
    public ResponseEntity<List<Employee>> getAllEmployeesByDepartmentId(@PathVariable("department_id") long department_Id,
                                                                        @RequestParam(name = "Date1",required = false) Date date1,
                                                                        @RequestParam(name = "Date2",required = false) Date date2){
        List<Employee> resultList;
        if(date1 != null && date2 != null) {
            resultList = departmentDAO.findEmployeesByDepartmentId(department_Id, date1, date2);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + department_Id +"/employees?Date1=" + date1 + "&Date2=" + date2);
        } else if(date1 != null) {
            resultList = departmentDAO.findEmployeesByDepartmentId(department_Id,date1);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + department_Id +"/employees?Date1=" + date1);
        } else {
            resultList = departmentDAO.findEmployeesByDepartmentId(department_Id);
            logger.debug("Method \"getAllEmployeesByDepartmentId()\" was called on request mapping /departments/" + department_Id +"/employees");
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

    @GetMapping("/{department_id}")
    public ResponseEntity<Department> findDepartmentById(@PathVariable ("department_id") long id){
        logger.debug("Method \"findDepartmentById\" was called on request mapping /departments/" + id);
        Department department = departmentDAO.findDepartment(id);
        return department != null
                ? new ResponseEntity<>(department,HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{department_id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("department_id") long department_Id){
        logger.debug("Method \"deleteDepartmentById\" was called on request mapping /departments/" + department_Id);
        final boolean deleted = departmentDAO.removeDepartment(department_Id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{department_id}")
    public ResponseEntity<?> updateDepartmentById(@PathVariable("department_id") long department_Id,
                                                  @RequestBody Department department){
        logger.debug("Method \"updateDepartmentById\" was called on request mapping /departments/" + department_Id);
        final boolean updated = departmentDAO.updateDepartment(department_Id,department);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/{department_id}/employees")
    public ResponseEntity<?> createEmployeeByDepartmentId(@PathVariable("department_id")long department_Id,
                                                          @RequestBody Employee employee){
        logger.debug("Method \"createEmployeeByDepartmentId\" was called on request mapping /departments/" + department_Id + "/employees");
        final boolean created = departmentDAO.addEmployee(department_Id, employee);
        return created
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/{department_id}/employees/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("department_id") long department_id,
                                                     @PathVariable("id") long id){
        logger.debug("Method \"findEmployeeById\" was called on request mapping /departments/" + department_id + "/employees/" + id);
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
        employee.setDepartmentId(department_id);
        final boolean updated = departmentDAO.updateEmployee(id,employee);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
