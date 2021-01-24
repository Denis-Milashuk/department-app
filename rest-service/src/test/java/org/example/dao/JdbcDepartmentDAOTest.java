package org.example.dao;

import org.example.config.TestConfig;
import org.example.models.Department;
import org.example.models.Employee;
import org.example.models.PairCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@SpringJUnitConfig(TestConfig.class)
@DisplayName ("Start of DAO testing")
public class JdbcDepartmentDAOTest {

    @Autowired
    private JdbcDepartmentDAO departmentDAO;

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final String SQL_GET_COUNT_OF_DEPARTMENTS = "SELECT count(*) FROM department";
    private static final String SQL_GET_TITTLE_FROM_DEPARTMENT_BY_ID = "SELECT tittle FROM department WHERE id = :id";
    private static final String SQL_GET_COUNT_OF_EMPLOYEES = "SELECT count(*) FROM employee";

    @Test
    @DisplayName("The addDepartment() method should add a department")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void addDepartment(){
        Department department = new Department();
        department.setTittle("ORP department");
        departmentDAO.addDepartment(department);
        int countOfDepartments = template.getJdbcTemplate().queryForObject(SQL_GET_COUNT_OF_DEPARTMENTS, Integer.class);
        Assertions.assertEquals(4, countOfDepartments);
        SqlParameterSource parameterSource = new MapSqlParameterSource("id",4);
        String tittle = template.queryForObject(SQL_GET_TITTLE_FROM_DEPARTMENT_BY_ID, parameterSource, String.class);
        Assertions.assertEquals("ORP department", tittle);
    }

    @Test
    @DisplayName("The findDepartment() method should find a department")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findDepartment(){
        Assertions.assertEquals("Human Resources Department",departmentDAO.findDepartment(2).getTittle());
    }

    @Test
    @DisplayName("The removeDepartment() method should remove a department")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
            config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void removeDepartment(){
        departmentDAO.removeDepartment(3);
        int countOfDepartments = template.getJdbcTemplate().queryForObject(SQL_GET_COUNT_OF_DEPARTMENTS, Integer.class);
        Assertions.assertEquals(2,countOfDepartments);
    }

    @Test
    @DisplayName("The updateDepartment() method should update a department")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void updateDepartment(){
        Department department = new Department();
        department.setTittle("ORP department");
        departmentDAO.updateDepartment(3,department);
        int countOfDepartments = template.getJdbcTemplate().queryForObject(SQL_GET_COUNT_OF_DEPARTMENTS, Integer.class);
        Assertions.assertEquals(3,countOfDepartments);
        SqlParameterSource parameterSource = new MapSqlParameterSource("id",3);
        String updatedTittle = template.queryForObject(SQL_GET_TITTLE_FROM_DEPARTMENT_BY_ID,parameterSource,String.class);
        Assertions.assertEquals("ORP department",updatedTittle);
    }

    @Test
    @DisplayName("The findEmployeeById() method should find an employee")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findEmployeeById(){
        Employee employee = departmentDAO.findEmployeeById(1);
        Assertions.assertEquals(1,employee.getDepartmentId());
        Assertions.assertEquals(employee.getFirstName(), "John");
        Assertions.assertEquals(employee.getLastName(), "Wick");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        Assertions.assertEquals(dateFormat.format(employee.getBirthDate()), dateFormat.format(new GregorianCalendar(1975, Calendar.MARCH,30).getTime()));
        Assertions.assertEquals(employee.getSalary(), 3000);
    }

    @Test
    @DisplayName("The addEmployee() method should add an employee")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void addEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Bobby");
        employee.setLastName("Fischer");
        employee.setBirthDate(new Date(new GregorianCalendar(1943,Calendar.MARCH,9).getTime().getTime()));
        employee.setSalary(100_000);
        departmentDAO.addEmployee(1,employee);
        int countOfEmployees = template.getJdbcTemplate().queryForObject(SQL_GET_COUNT_OF_EMPLOYEES,Integer.class);
        Assertions.assertEquals(9,countOfEmployees);
        Employee addedEmployee = departmentDAO.findEmployeeById(9);
        employee.setId(addedEmployee.getId());
        employee.setDepartmentId(1);
        Assertions.assertEquals(employee,addedEmployee);
    }

    @Test
    @DisplayName("The removeEmployee() method should remove an employee")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void removeEmployee(){
        departmentDAO.removeEmployee(5);
        int countOfEmployees = template.getJdbcTemplate().queryForObject(SQL_GET_COUNT_OF_EMPLOYEES,Integer.class);
        Assertions.assertEquals(7,countOfEmployees);
        Assertions.assertThrows(Throwable.class,() -> departmentDAO.findEmployeeById(5));
    }

    @Test
    @DisplayName("The updateEmployee() method should update an employee")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void updateEmployee(){
        Employee employee = new Employee();
        employee.setFirstName("Bobby");
        employee.setLastName("Fischer");
        employee.setBirthDate(new Date(new GregorianCalendar(1943,Calendar.MARCH,9).getTime().getTime()));
        employee.setSalary(100_000);
        employee.setDepartmentId(2);
        departmentDAO.updateEmployee(1,employee);
        employee.setId(1);
        Assertions.assertEquals(employee, departmentDAO.findEmployeeById(1));
    }

    @Test
    @DisplayName("The findAllAverageSalariesByDepartment() method should find all departments with their average salaries")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findAllAverageSalariesByDepartment(){
        List<PairCase> resultList = departmentDAO.findAllAverageSalariesByDepartment();
        Assertions.assertEquals(3, resultList.size());
        Double averageSalaryForDevelopers = resultList
                .stream()
                .filter(a -> "Developer department".equals(a.getDepartment().getTittle()))
                .findFirst()
                .orElseThrow()
                .getDouble();
        Assertions.assertEquals(averageSalaryForDevelopers, 3000d);
    }

    @Test
    @DisplayName("The findEmployeesByDepartmentId() method should find all employees by department_id")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findEmployeesByDepartmentId(){
        List<Employee> resultList = departmentDAO.findEmployeesByDepartmentId(2);
        Assertions.assertEquals(3,resultList.size());
    }

    @Test
    @DisplayName("The findEmployeesByDepartmentIdAndBirthDate() method should find all employees by department_id and birthdate")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findEmployeesByDepartmentIdAndBirthDate(){
        List<Employee> resultList = departmentDAO.findEmployeesByDepartmentId(2,new Date(new GregorianCalendar(1986,Calendar.JANUARY,16).getTime().getTime()));
        Assertions.assertEquals(2,resultList.size());
    }

    @Test
    @DisplayName("The findEmployeesByDepartmentIdAndTwoDates() method should find all employees by department_id and with birthdate between two dates")
    @SqlGroup({
            @Sql(value = "classpath:test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8",separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findEmployeesByDepartmentIdAndTwoDates(){
        List<Employee> resultList = departmentDAO.findEmployeesByDepartmentId(1,
                new Date(new GregorianCalendar(1960,Calendar.JANUARY,1).getTime().getTime()),
                new Date(new GregorianCalendar().getTime().getTime()));
        Assertions.assertEquals(2,resultList.size());
    }
}
