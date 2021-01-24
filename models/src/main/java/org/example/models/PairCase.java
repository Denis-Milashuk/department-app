package org.example.models;

public class PairCase {
    private Department department;
    private Double aDouble;

    public PairCase(){}

    public PairCase(Department department, Double aDouble) {
        this.department = department;
        this.aDouble = aDouble;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Double getDouble() {
        return aDouble;
    }

    public void setDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    @Override
    public String toString() {
        return "PairCase{" +
                "department=" + department +
                ", aDouble=" + aDouble +
                '}';
    }
}
