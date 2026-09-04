package com.gamezone.model;

public class Seller  extends Person{
    
    private String employeeCode;
    private String shift;

    public Seller(String name, String id, String phone, String employeeCode, String shift){
        super(name, id, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
        
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getShift() {
        return shift;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
    
    @Override
    public String getRoleDescription() {
        return "Vendedor - Código: " + employeeCode + " - Turno: " + shift;
    }
    
}
