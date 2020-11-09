package org.example.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department implements Serializable {
    long id;
    String tittle;
    List<Employee> employees;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public boolean addEmployee(Employee employee){
        if(employees == null){
            employees = new ArrayList<>();
        }else if (employees.contains(employee)){
            return false;
        }
        employees.add(employee);
        return true;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", tittle='" + tittle + '\'' +
                ", employees=" + employees +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id &&
                tittle.equals(that.tittle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tittle);
    }
}
