package org.example.dao;

import org.example.models.Department;
import org.example.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcDepartmentDAO implements DepartmentDao, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JdbcDepartmentDAO.class);

    private NamedParameterJdbcTemplate template;

    private static final String SQL_ADD_DEPARTMENT = "INSERT INTO department (tittle) VALUES (:new_tittle)";

    private static final String SQL_DELETE_EMPLOYEES_FROM_DELETED_DEPARTMENT = "DELETE FROM employee WHERE department_id = :id";

    private static final String SQL_DELETE_DEPARTMENT = "DELETE FROM department WHERE id = :id";

    private static final String SQL_UPDATE_DEPARTMENT = "UPDATE department SET tittle = :tittle WHERE id = :id";

    private static final String SQL_FIND_EMPLOYEE_BY_ID = "SELECT * FROM employee WHERE id = :id";

    private static final String SQL_ADD_EMPLOYEE = "INSERT INTO employee (department_id, first_name, last_name, birth_date, salary)" +
                                                        "VALUES (:department_id, :first_name, :last_name, :birth_date, :salary)";

    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE employee SET " +
                                                    "department_id = :departmentId, " +
                                                    "first_name = :first_name, " +
                                                    "last_name = :last_name, " +
                                                    "birth_date = :birth_date, " +
                                                    "salary = :salary " +
                                                    "WHERE id = :id";

    private static final String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = :id";

    private static final String FIND_ALL_AVERAGE_SALARIES_BY_DEPARTMENT = "SELECT d.id, d.tittle, AVG(e.salary) " +
                                                                        "FROM department d LEFT JOIN employee e ON d.id = e.department_id " +
                                                                        "GROUP BY d.tittle,d.id ORDER BY d.id";

    private static final String FIND_EMPLOYEES_BY_DEPARTMENT_ID = "SELECT * FROM Employee WHERE department_id =:department_Id";

    private static final String FIND_EMPLOYEES_BY_DEPARTMENT_ID_AND_BIRTHDATE = "SELECT * FROM employee WHERE department_id = :department_Id AND birth_date = :birth_date";

    private static final String FIND_EMPLOYEES_BY_DEPARTMENT_ID_AND_BEHIND_TWO_BIRTHDATE = "SELECT * FROM employee WHERE department_id =:department_Id AND birth_date BETWEEN :date1 AND :date2";

    @Autowired
    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
        logger.debug("NamedParameterJdbcTemplate was injected");
    }

    @Override
    public void afterPropertiesSet() {
        if (template == null){
            logger.error("NamedParameterJdbcTemplate was not injected");
            throw new BeanCreationException("NamedParameterJdbcTemplate is null on JdbcDepartmentDAO");}
    }

    @Override
    public boolean addDepartment(Department department) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("new_tittle",department.getTittle());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(SQL_ADD_DEPARTMENT, parameterSource, keyHolder);
            logger.debug("Added Department with id = " + keyHolder.toString() + ".");
        }catch (DataAccessException e){
            logger.error("Department was not added", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeDepartment(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
        template.update(SQL_DELETE_EMPLOYEES_FROM_DELETED_DEPARTMENT,parameterSource);
        try {
            template.update(SQL_DELETE_DEPARTMENT,parameterSource);
        }catch (DataAccessException e){
            logger.error("Department was not deleted", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDepartment(long id,Department department) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("tittle",department.getTittle());
        parameterSource.addValue("id",id);
        try {
            template.update(SQL_UPDATE_DEPARTMENT,parameterSource);
        }catch (DataAccessException e){
            logger.error("Department was not updated", e);
            return false;
        }
        return true;
    }

    @Override
    public Employee findEmployeeById(long id){
        SqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
        return template.queryForObject(SQL_FIND_EMPLOYEE_BY_ID,parameterSource,new EmployeesMapper());
    }

    @Override
    public boolean addEmployee(long departmentId, Employee employee) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("department_id",departmentId);
        parameterSource.addValue("first_name",employee.getFirstName());
        parameterSource.addValue("last_name",employee.getLastName());
        parameterSource.addValue("birth_date",employee.getBirthDate());
        parameterSource.addValue("salary",employee.getSalary());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(SQL_ADD_EMPLOYEE,parameterSource,keyHolder);
            logger.debug("Added Employee with id = " + keyHolder.getKey() + ".");
        }catch (DataAccessException e){
            logger.error("Employee was not added", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeEmployee(long id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
        try {
            template.update(DELETE_EMPLOYEE,parameterSource);
        }catch (DataAccessException e){
            logger.error("Employee was not deleted", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEmployee(long id,Employee employee) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("departmentId",employee.getDepartmentId());
        parameterSource.addValue("first_name",employee.getFirstName());
        parameterSource.addValue("last_name",employee.getLastName());
        parameterSource.addValue("birth_date",employee.getBirthDate());
        parameterSource.addValue("salary",employee.getSalary());
        parameterSource.addValue("id",id);
        try {
            template.update(SQL_UPDATE_EMPLOYEE,parameterSource);
        }catch (DataAccessException e){
            logger.error("Employee was not updated", e);
            return false;
        }
        return true;
    }

    @Override
    public Map<Department, Double> findAllAverageSalariesByDepartment() {
        Map<Department,Double> resultMap = new LinkedHashMap<>();
        template.query(FIND_ALL_AVERAGE_SALARIES_BY_DEPARTMENT,new FindAllAverageSalariesByDepartmentMapper())
                .forEach(x ->resultMap.put(x.getKey(),x.getValue()));
        return resultMap;
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("department_Id",departmentId);
        return template.query(FIND_EMPLOYEES_BY_DEPARTMENT_ID,paramMap,new EmployeesMapper());
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId, Date birthDate) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("department_Id", departmentId);
        paramMap.put("birth_date", birthDate);
        return template.query(FIND_EMPLOYEES_BY_DEPARTMENT_ID_AND_BIRTHDATE,paramMap,new EmployeesMapper());
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId, Date date1, Date date2) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("department_Id",departmentId);
        paramMap.put("date1", date1);
        paramMap.put("date2", date2);
        return template.query(FIND_EMPLOYEES_BY_DEPARTMENT_ID_AND_BEHIND_TWO_BIRTHDATE,paramMap,new EmployeesMapper());
    }

    private static class FindAllAverageSalariesByDepartmentMapper implements RowMapper<AbstractMap.SimpleEntry<Department, Double>>{
        @Override
        public AbstractMap.SimpleEntry<Department, Double> mapRow(ResultSet resultSet, int i) throws SQLException {
                Department department = new Department();
                department.setId(resultSet.getLong("id"));
                department.setTittle(resultSet.getString("tittle"));
                department.setEmployees(new ArrayList<>());
                Double averageSalary = resultSet.getDouble(3);
            return new AbstractMap.SimpleEntry<>(department,averageSalary);
        }
    }

    private static class EmployeesMapper implements RowMapper<Employee>{
        @Override
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee resultEmployee = new Employee();
            resultEmployee.setId(resultSet.getLong("id"));
            resultEmployee.setDepartmentId(resultSet.getLong("department_id"));
            resultEmployee.setFirstName(resultSet.getString("first_name"));
            resultEmployee.setLastName(resultSet.getString("last_name"));
            resultEmployee.setBirthDate(resultSet.getDate("birth_date"));
            resultEmployee.setSalary(resultSet.getDouble("salary"));
            return resultEmployee;
        }
    }
}
