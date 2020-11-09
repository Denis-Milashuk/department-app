package org.example.controllers;
import org.example.dao.DepartmentDao;
import org.example.models.Department;
import org.example.models.Employee;
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
public class DepartmentController {

    private final DepartmentDao departmentDao;

    @Autowired
    public DepartmentController(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @GetMapping("/averageSalary")
    public ResponseEntity<Map<Department,Double>> getAverageSalary(){
        Map<Department,Double> responseMap = departmentDao.findAllAverageSalariesByDepartment();
        return responseMap != null && !responseMap.isEmpty()
                ? new ResponseEntity<>(responseMap, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{department_id}/employees")
    public ResponseEntity<List<Employee>> getAllEmployeesByDepartmentId(@PathVariable("department_id") long departmentId,
                                                                        @RequestParam(name = "Date1",required = false) Date date1,
                                                                        @RequestParam(name = "Date2",required = false) Date date2){
        List<Employee> resultList;
        if(date1 != null && date2 != null)
            resultList = departmentDao.findEmployeesByDepartmentId(departmentId, date1, date2);
        else if(date1 != null)
            resultList = departmentDao.findEmployeesByDepartmentId(departmentId,date1);
        else
            resultList = departmentDao.findEmployeesByDepartmentId(departmentId);
        return resultList != null && !resultList.isEmpty()
                ? new ResponseEntity<>(resultList,HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department department){
        final boolean created = departmentDao.addDepartment(department);
        return created
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{department_id}")
    public ResponseEntity<?> deleteDepartmentById(@PathVariable("department_id") long departmentId){
        final boolean deleted = departmentDao.removeDepartment(departmentId);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{department_id}")
    public ResponseEntity<?> updateDepartmentById(@PathVariable("department_id") long departmentId,
                                                  @RequestBody Department department){
        final boolean updated = departmentDao.updateDepartment(departmentId,department);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/{department_id}/employees")
    public ResponseEntity<?> createEmployeeByDepartmentId(@PathVariable("department_id")long departmentId,
                                                          @RequestBody Employee employee){
        final boolean created = departmentDao.addEmployee(departmentId, employee);
        return created
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{department_id}/employees/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") long id){
        final boolean deleted = departmentDao.removeEmployee(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{department_id}/employees/{id}")
    public ResponseEntity<?> updateEmployeeById(@PathVariable("id") long id,
                                                @RequestBody Employee employee){
        final boolean updated = departmentDao.updateEmployee(id,employee);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
