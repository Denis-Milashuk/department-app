package org.example.controllers;

import org.example.dao.JdbcDepartmentDAO;
import org.example.models.Department;
import org.example.models.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.*;

@DisplayName("Start of DepartmentController testing")
public class DepartmentControllerTest {

    private static final Department DEPARTMENT = new Department();
    private static final Employee EMPLOYEE = new Employee();
    private static final ResponseEntity<List<Employee>> RESPONSE_ENTITY_OK = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<List<Employee>> RESPONSE_ENTITY_CREATED = new ResponseEntity<>(HttpStatus.CREATED);
    private static final ResponseEntity<List<Employee>> RESPONSE_ENTITY_NOT_FOUND = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    private static final ResponseEntity<List<Employee>> RESPONSE_ENTITY_NOT_MODIFIED = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    private static final JdbcDepartmentDAO DEPARTMENT_DAO = Mockito.mock(JdbcDepartmentDAO.class);
    private static final DepartmentsController CONTROLLER = new DepartmentsController(DEPARTMENT_DAO);

    @Test
    @DisplayName("The getAverageSalary() method should return a correct ResponseEntity object")
    public void getAverageSalary(){
        Map<Department,Double> responseMap = new HashMap<>();
        Department department1 = new Department();
        department1.setId(1);
        department1.setTittle("First department");
        Department department2 = new Department();
        department2.setId(2);
        department2.setTittle("Second department");
        responseMap.put(department1,3000d);
        responseMap.put(department2,5000d);

        ResponseEntity<Map<Department,Double>> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        Mockito.when(DEPARTMENT_DAO.findAllAverageSalariesByDepartment()).thenReturn(responseMap);
        Assertions.assertEquals(responseEntity,CONTROLLER.getAverageSalary());

        Mockito.when(DEPARTMENT_DAO.findAllAverageSalariesByDepartment()).thenReturn(null);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAverageSalary());
        Mockito.when(DEPARTMENT_DAO.findAllAverageSalariesByDepartment()).thenReturn(new HashMap<>());
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAverageSalary());
    }

    @Test
    @DisplayName("The getAllEmployeesByDepartmentId() method should return a correct ResponseEntity object")
    public void getAllEmployeesByDepartmentId(){
        List<Employee> responseList1 = new ArrayList<>();
        List<Employee> responseList2 = new ArrayList<>();
        List<Employee> responseList3 = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setFirstName("Denis");;
        Employee employee2 = new Employee();
        employee1.setId(2);
        employee1.setFirstName("Anna");
        Employee employee3 = new Employee();
        employee1.setId(3);
        employee1.setFirstName("Barbara");
        responseList1.add(employee1);
        responseList1.add(employee2);
        responseList1.add(employee3);
        responseList2.add(employee1);
        responseList2.add(employee2);
        responseList3.add(employee3);

        ResponseEntity<List<Employee>> responseEntity1 = new ResponseEntity<>(responseList1,HttpStatus.OK);
        ResponseEntity<List<Employee>> responseEntity2 = new ResponseEntity<>(responseList2,HttpStatus.OK);
        ResponseEntity<List<Employee>> responseEntity3 = new ResponseEntity<>(responseList3,HttpStatus.OK);
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1)).thenReturn(responseList1);
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123456))).thenReturn(responseList2);
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123),new Date(123456))).thenReturn(responseList3);

        Assertions.assertEquals(responseEntity1,CONTROLLER.getAllEmployeesByDepartmentId(1,null,null));
        Assertions.assertEquals(responseEntity2,CONTROLLER.getAllEmployeesByDepartmentId(1, new Date(123456),null));
        Assertions.assertEquals(responseEntity3,CONTROLLER.getAllEmployeesByDepartmentId(1,new Date(123),new Date(123456)));

        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1)).thenReturn(null);
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123456))).thenReturn(null);
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123),new Date(123456))).thenReturn(null);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1,null,null));
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1, new Date(123456),null));
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1,new Date(123),new Date(123456)));

        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1)).thenReturn(new ArrayList<>());
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123456))).thenReturn(new ArrayList<>());
        Mockito.when(DEPARTMENT_DAO.findEmployeesByDepartmentId(1,new Date(123),new Date(123456))).thenReturn(new ArrayList<>());
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1,null,null));
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1, new Date(123456),null));
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_FOUND,CONTROLLER.getAllEmployeesByDepartmentId(1,new Date(123),new Date(123456)));
    }

    @Test
    @DisplayName("The createDepartment() method should return a correct ResponseEntity object")
    public void createDepartment(){
        Mockito.when(DEPARTMENT_DAO.addDepartment(DEPARTMENT)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_CREATED,CONTROLLER.createDepartment(DEPARTMENT));
        Mockito.when(DEPARTMENT_DAO.addDepartment(DEPARTMENT)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.createDepartment(DEPARTMENT));
    }

    @Test
    @DisplayName("The deleteDepartmentById() method should return a correct ResponseEntity object")
    public void deleteDepartmentById(){
        Mockito.when(DEPARTMENT_DAO.removeDepartment(1)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_OK,CONTROLLER.deleteDepartmentById(1));
        Mockito.when(DEPARTMENT_DAO.removeDepartment(0)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.deleteDepartmentById(0));
    }

    @Test
    @DisplayName("The updateDepartmentById() method should return a correct ResponseEntity object")
    public void updateDepartmentById(){
        Mockito.when(DEPARTMENT_DAO.updateDepartment(1, DEPARTMENT)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_OK,CONTROLLER.updateDepartmentById(1, DEPARTMENT));
        Mockito.when(DEPARTMENT_DAO.updateDepartment(2, DEPARTMENT)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.updateDepartmentById(2, DEPARTMENT));
    }

    @Test
    @DisplayName("The createEmployeeByDepartmentId() method should return a correct ResponseEntity object")
    public void createEmployeeByDepartmentId(){
        Mockito.when(DEPARTMENT_DAO.addEmployee(1, EMPLOYEE)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_CREATED,CONTROLLER.createEmployeeByDepartmentId(1, EMPLOYEE));
        Mockito.when(DEPARTMENT_DAO.addEmployee(2, EMPLOYEE)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.createEmployeeByDepartmentId(2, EMPLOYEE));
    }

    @Test
    @DisplayName("The findEmployeeById() method should return a correct ResponseEntity object")
    public void findEmployeeById(){
        Mockito.when(DEPARTMENT_DAO.findEmployeeById(1)).thenReturn(EMPLOYEE);
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(EMPLOYEE,HttpStatus.OK);
        Assertions.assertEquals(responseEntity,CONTROLLER.findEmployeeById(1,1));
        Mockito.when(DEPARTMENT_DAO.findEmployeeById(2)).thenReturn(null);
        Assertions.assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND),CONTROLLER.findEmployeeById(1,2));
    }

    @Test
    @DisplayName("The deleteEmployeeById() method should return a correct ResponseEntity object")
    public void deleteEmployeeById(){
        Mockito.when(DEPARTMENT_DAO.removeEmployee(1)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_OK,CONTROLLER.deleteEmployeeById(1,1));
        Mockito.when(DEPARTMENT_DAO.removeEmployee(2)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.deleteEmployeeById(1,2));
    }

    @Test
    @DisplayName("The updateEmployeeById() method should return a correct ResponseEntity object")
    public void updateEmployeeById(){
        Mockito.when(DEPARTMENT_DAO.updateEmployee(1,EMPLOYEE)).thenReturn(true);
        Assertions.assertEquals(RESPONSE_ENTITY_OK,CONTROLLER.updateEmployeeById(1,1,EMPLOYEE));
        Mockito.when(DEPARTMENT_DAO.updateEmployee(2,EMPLOYEE)).thenReturn(false);
        Assertions.assertEquals(RESPONSE_ENTITY_NOT_MODIFIED,CONTROLLER.updateEmployeeById(1,2,EMPLOYEE));
    }
}
