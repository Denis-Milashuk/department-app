package org.example.controllers;

import org.example.models.Department;
import org.example.models.Employee;
import org.example.models.PairCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentsUIController {

    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(DepartmentsUIController.class);

    @Value("${url.getDepartmentsWithAverageSalaries}")
    private String URL_INDEX;

    @Value("${url.findDepartments}")
    private String URL_FIND_DEPARTMENTS;

    @Value("${url.findDepartment}")
    private String URL_FIND_DEPARTMENT;

    @Value("${url.findEmployees}")
    private String URL_FIND_EMPLOYEES;

    @Value("${url.findEmployee}")
    private String URL_FIND_EMPLOYEE;

    @Autowired
    DepartmentsUIController (RestTemplate restTemplate){this.restTemplate = restTemplate;}

    @GetMapping
    public String index(Model model){
        logger.debug("Method \"index\" was called on request mapping /departments");
        ParameterizedTypeReference<List<PairCase>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<PairCase>> entity = restTemplate.exchange(URL_INDEX, HttpMethod.GET,null,typeRef);
        model.addAttribute("departments",entity.getBody());
        return "departments/index";
    }

    @GetMapping("/new")
    public String newDepartment(Model model){
        logger.debug("Method \"newDepartment\" was called on request mapping /departments/new");
        model.addAttribute(new Department());
        return "departments/new";
    }

    @PostMapping
    public String createDepartment(@ModelAttribute Department department){
        logger.debug("Method \"createDepartment\" was called on request mapping /departments");
        restTemplate.postForObject(URL_FIND_DEPARTMENTS, department, Department.class);
        return "redirect:/departments";
    }

    @GetMapping("/{department_id}/edit")
    public String editDepartment(@PathVariable("department_id") int department_id,
                                 Model model){
        logger.debug("Method \"editDepartment\" was called on request mapping /departments/" + department_id + "/edit");
        ResponseEntity<Department> entity = restTemplate.getForEntity(URL_FIND_DEPARTMENT,Department.class,department_id);
        model.addAttribute("department",entity.getBody());
        return "departments/edit";
    }

    @PutMapping("/{department_id}")
    public String updateDepartment(@PathVariable("department_id") long department_id,
                                   @ModelAttribute Department department){
        logger.debug("Method \"updateDepartment\" was called on request mapping /departments/" + department_id);
        restTemplate.put(URL_FIND_DEPARTMENT, department, department_id);
        return "redirect:/departments";
    }

    @DeleteMapping("/{department_id}")
    public String deleteDepartment(@PathVariable("department_id") long department_id){
        logger.debug("Method \"deleteDepartment\" was called on request mapping /departments/" + department_id);
        restTemplate.delete(URL_FIND_DEPARTMENT,department_id);
        return "redirect:/departments";
    }

    @GetMapping("/{department_id}/employees")
    public String findEmployees(@PathVariable ("department_id") long department_id,
                                Model model){
        logger.debug("Method \"findEmployees\" was called on request mapping /departments/" + department_id + "/employee");
        ParameterizedTypeReference<List<Employee>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Employee>> entityEmployees = restTemplate.exchange(URL_FIND_EMPLOYEES, HttpMethod.GET,null, typeRef,department_id);
        model.addAttribute("employees",entityEmployees.getBody());
        ResponseEntity<Department> entityDepartment = restTemplate.getForEntity(URL_FIND_DEPARTMENT,Department.class,department_id);
        model.addAttribute("department",entityDepartment.getBody());
        return "departments/employees/showAll";
    }

    @GetMapping("/{department_id}/employees/new")
    public String newEmployee(@PathVariable("department_id") long department_id,
                              Model model){
        logger.debug("Method \"newEmployee\" was called on request mapping /departments/" + department_id + "/employee/new");
        model.addAttribute("department_id",department_id);
        model.addAttribute("employee",new Employee());
        return "departments/employees/new";
    }

    @PostMapping("/{department_id}/employees")
    public String createEmployee(@ModelAttribute Employee employee,
                                 @PathVariable("department_id") long department_id){
        logger.debug("Method \"createEmployee\" was called on request mapping /departments/" + department_id + "/employees");
        restTemplate.postForObject(URL_FIND_EMPLOYEES, employee, Employee.class, department_id);
        String mapping = "/departments/" + department_id + "/employees";
        return "redirect:" + mapping;
    }

    @GetMapping("/{department_id}/employees/{id}/edit")
    public String editEmployee(@PathVariable("department_id") long department_id,
                               @PathVariable("id") long id,
                               Model model){
        logger.debug("Method \"editEmployee\" was called on request mapping /departments/" + department_id + "/employees/" + id + "/edit");
        ResponseEntity<Employee> entity = restTemplate.getForEntity(URL_FIND_EMPLOYEE,Employee.class, department_id, id);
        model.addAttribute("employee",entity.getBody());
        return "departments/employees/edit";
    }

    @PutMapping("/{department_id}/employees/{id}")
    public String updateEmployee(@PathVariable("department_id") long department_id,
                                 @PathVariable("id") long id,
                                 @ModelAttribute Employee employee){
        logger.debug("Method \"updateEmployee\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        restTemplate.put(URL_FIND_EMPLOYEE, employee, department_id, id);
        String mapping = "/departments/" + department_id + "/employees/" + id;
        return "redirect:" + mapping;
    }

    @GetMapping("/{department_id}/employees/{id}")
    public String findEmployee(@PathVariable("department_id") long department_id,
                               @PathVariable("id") long id,
                               Model model){
        logger.debug("Method \"findEmployee\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        ResponseEntity<Employee> entityEmployee = restTemplate.getForEntity(URL_FIND_EMPLOYEE, Employee.class, department_id, id);
        model.addAttribute("employee",entityEmployee.getBody());
        return "departments/employees/show";
    }

    @DeleteMapping("/{department_id}/employees/{id}")
    public String deleteEmployee(@PathVariable("department_id") long department_id,
                                 @PathVariable("id") long id){
        logger.debug("Method \"deleteEmployee\" was called on request mapping /departments/" + department_id + "/employees/" + id);
        restTemplate.delete(URL_FIND_EMPLOYEE, department_id, id);
        String mapping = "/departments/" + department_id + "/employees";
        return "redirect:" + mapping;
    }

}
