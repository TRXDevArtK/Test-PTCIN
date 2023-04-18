package com.refaldi.testptcin.pojos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

//define employee object
public class EmployeePojo {
    private Employee employee;

    @SerializedName("komponengaji")
    private List<KomponenGaji> komponenGaji;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<KomponenGaji> getKomponenGaji() {
        return komponenGaji;
    }

    public void setKomponenGaji(List<KomponenGaji> komponenGaji) {
        this.komponenGaji = komponenGaji;
    }

    public static class Employee {
        private String name;
        private String sex;

        @SerializedName("marital status")
        private String maritalStatus;
        
        private int childs;
        private String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public int getChilds() {
            return childs;
        }

        public void setChilds(int childs) {
            this.childs = childs;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class KomponenGaji {
        private String name;
        private String type;
        private String amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}

