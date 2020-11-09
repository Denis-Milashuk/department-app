package org.example.dao;

import org.example.models.Department;
import org.example.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcDepartmentDAO implements DepartmentDao, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JdbcDepartmentDAO.class.getName());

    private NamedParameterJdbcTemplate template;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void afterPropertiesSet() {
        if (template == null)throw new BeanCreationException("NamedParameterJdbcTemplate is null on JdbcDepartmentDAO");
    }

    @Override
    public boolean addDepartment(Department department) {
        String sqlQuery = "INSERT INTO department (tittle) VALUES (:new_tittle)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("new_tittle",department.getTittle());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(sqlQuery,parameterSource,keyHolder);
            logger.info("Added Department with id = " + keyHolder.toString() + ".");
        }catch (DataAccessException e){
            logger.error("Department was not added", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeDepartment(long id) {
        String sqlQueryDeleteEmployeesFromDeletedDepartment = "DELETE FROM employee WHERE department_id = :id";
        String sqlQueryDeleteDepartment = "DELETE FROM department WHERE id = :id;";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
        template.update(sqlQueryDeleteEmployeesFromDeletedDepartment,parameterSource);
        try {
            template.update(sqlQueryDeleteDepartment,parameterSource);
        }catch (DataAccessException e){
            logger.error("Department was not deleted", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDepartment(long id,Department department) {
        String sqlQuery = "UPDATE department SET tittle = :tittle WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("tittle",department.getTittle());
        parameterSource.addValue("id",id);
        try {
            template.update(sqlQuery,parameterSource);
        }catch (DataAccessException e){
            logger.error("Department was not updated", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean addEmployee(long departmentId, Employee employee) {
        String sqlAddEmployee = "INSERT INTO employee (department_id, first_name, last_name, birth_date, salary)" +
                                    "VALUES (:department_id, :first_name, :last_name, :birth_date, :salary)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("department_id",departmentId);
        parameterSource.addValue("first_name",employee.getFirstName());
        parameterSource.addValue("last_name",employee.getLastName());
        parameterSource.addValue("birth_date",employee.getBirthDate());
        parameterSource.addValue("salary",employee.getSalary());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(sqlAddEmployee,parameterSource,keyHolder);
            logger.info("Added Employee with id = " + keyHolder.getKey() + ".");
        }catch (DataAccessException e){
            logger.error("Employee was not added", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeEmployee(long id) {
        String sqlQuery = "DELETE FROM employee WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
        try {
            template.update(sqlQuery,parameterSource);
        }catch (DataAccessException e){
            logger.error("Employee was not deleted", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEmployee(long id,Employee employee) {
        String sqlQuery = "UPDATE employee SET " +
                "department_id = :departmentId, " +
                "first_name = :first_name, " +
                "last_name = :last_name, " +
                "birth_date = :birth_date, " +
                "salary = :salary " +
                "WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("department_id",employee.getDepartmentId());
        parameterSource.addValue("first_name",employee.getFirstName());
        parameterSource.addValue("last_name",employee.getLastName());
        parameterSource.addValue("birth_date",employee.getBirthDate());
        parameterSource.addValue("salary",employee.getSalary());
        parameterSource.addValue("id",id);
        try {
            template.update(sqlQuery,parameterSource);
        }catch (DataAccessException e){
            logger.error("Employee was not updated", e);
            return false;
        }
        return true;
    }

    @Override
    public Map<Department, Double> findAllAverageSalariesByDepartment() {
        String sqlQuery = "SELECT d.id, d.tittle, AVG(e.salary)" +
                " FROM department d LEFT JOIN employee e ON d.id = e.department_id " +
                "GROUP BY d.tittle,d.id ORDER BY d.id";
        Map<Department,Double> resultMap = new LinkedHashMap<>();
        template.query(sqlQuery,new FindAllAverageSalariesByDepartmentMapper())
                .forEach(x ->resultMap.put(x.getKey(),x.getValue()));
        return resultMap;
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId) {
        String sqlQuery = "SELECT * FROM Employee WHERE department_id =:departmentId";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("departmentId",departmentId);
        return template.query(sqlQuery,paramMap,new EmployeesMapper());
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId, Date birthDate) {
        String sqlQuery = "SELECT * FROM employee WHERE department_id = :department_Id AND birth_date = :birth_date";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("department_Id",departmentId);
        paramMap.put("birth_date",birthDate.toString());
        return template.query(sqlQuery,paramMap,new EmployeesMapper());
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(long departmentId, Date date1, Date date2) {
        String sqlQuery = "SELECT * FROM employee WHERE department_id =:department_Id AND birth_date BETWEEN :date1 AND :date2";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("department_Id",departmentId);
        paramMap.put("date1", date1);
        paramMap.put("date2", date2);
        return template.query(sqlQuery,paramMap,new EmployeesMapper());
    }

    private static class FindAllAverageSalariesByDepartmentMapper implements RowMapper<AbstractMap.SimpleEntry<Department, Double>>{
        @Override
        public AbstractMap.SimpleEntry<Department, Double> mapRow(ResultSet resultSet, int i) throws SQLException {
                Department department = new Department();
                department.setId(resultSet.getLong("d.id"));
                department.setTittle(resultSet.getString("d.tittle"));
                department.setEmployees(new ArrayList<>());
                Double averageSalary = resultSet.getDouble("e.salary");
            return new AbstractMap.SimpleEntry<>(department,averageSalary);
        }
    }

    private static class EmployeesMapper implements RowMapper<Employee>{
        @Override
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee resultEmployee = new Employee();
            resultEmployee.setId(resultSet.getLong("id"));
            resultEmployee.setDepartmentsId(resultSet.getLong("department_id"));
            resultEmployee.setFirstName(resultSet.getNString("first_name"));
            resultEmployee.setLastName(resultSet.getString("last_name"));
            resultEmployee.setBirthDate(resultSet.getDate("birth_date"));
            resultEmployee.setSalary(resultSet.getDouble("salary"));
            return resultEmployee;
        }
    }
}
