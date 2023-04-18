package com.refaldi.testptcin.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.refaldi.testptcin.interfaces.PenggajianServiceInterface;
import com.refaldi.testptcin.pojos.EmployeePojo;

public class PenggajianService implements PenggajianServiceInterface{

    //global variable
    private BigDecimal totalMoney = new BigDecimal("0.0"); // in Years
    private BigDecimal totalMoneyLayer50 = new BigDecimal("0.0");
    private BigDecimal totalTaxPerYear = new BigDecimal("0.0");
    private BigDecimal monthlyTax = new BigDecimal("0.0");
    private EmployeePojo employee;
    private String maritalStatus = "";
    private String country = "";
    private int countChild = 0;

    //init variable
    public PenggajianService(String payload){
        //JSON Parse
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
        
        //shorten usable employee variable
        this.employee = gson.fromJson(payload, EmployeePojo.class); 
        this.maritalStatus = employee.getEmployee().getMaritalStatus();
        this.country = employee.getEmployee().getCountry();
        this.countChild = employee.getEmployee().getChilds();
    }

    //can be void / String (for public view)
    public String getTotalWagePerMonth(){
        // if you want filter something later on, for now, no need because test didn't specified
        // Object[][] abc = new Object[employee.getKomponenGaji().size()][3];
        //
        // for (int i = 0; i < employee.getKomponenGaji().size(); i++) {
        //     abc[i][0] = employee.getKomponenGaji().get(i).getName();
        //     abc[i][1] = employee.getKomponenGaji().get(i).getType();
        //     abc[i][2] = employee.getKomponenGaji().get(i).getAmount();
        // }

        // instead combine all amount into one, use decimal for better precision
        // Note : per-month value
        for (int i = 0; i < employee.getKomponenGaji().size(); i++) {
            BigDecimal gajiAmount = new BigDecimal(employee.getKomponenGaji().get(i).getAmount());
            totalMoney = totalMoney.add(gajiAmount);
        }

        //convert it to per-year
        totalMoney = totalMoney.multiply(BigDecimal.valueOf(12));

        return totalMoney.toString();
    }

    //can be void / String (for public view)
    //NOTE : country is if else based, if it get too long/complited, please make new function
    public BigDecimal getSalaryAfterPtkp(){
        BigDecimal ptkpCut = new BigDecimal("0.0");

        //currently static
        int assuranceMonth = 1;
        
        BigDecimal totalAssurance = new BigDecimal("12000000");
        totalAssurance = totalAssurance.multiply(BigDecimal.valueOf(assuranceMonth));
        
        if(country.equals("indonesia")){
            if(maritalStatus.equals("false")){
                ptkpCut = new BigDecimal("25000000");
            } else if(maritalStatus.equals("true") && (countChild == 0 || countChild < 1)){
                ptkpCut = new BigDecimal("50000000");
            } else if(maritalStatus.equals("true") && countChild > 0){
                ptkpCut = new BigDecimal("75000000");
            }

            totalMoney = totalMoney.subtract(ptkpCut);
        } 
        else {
            if(maritalStatus.equals("false")){
                ptkpCut = new BigDecimal("15000000");
            } else if(maritalStatus.equals("true")){
                ptkpCut = new BigDecimal("30000000");
            }

            totalMoney = totalMoney.subtract(totalAssurance);
            totalMoney = totalMoney.subtract(ptkpCut);
        }

        return totalMoney;
    }

    //NOTE : plugin based
    private void calculateLayerIndonesia(){
        BigDecimal layer50 = new BigDecimal("50000000");
        BigDecimal layer250 = new BigDecimal("250000000");
        BigDecimal percentage = new BigDecimal("0.0");
        
        //compare money
        int compareResultLayer50 = totalMoney.compareTo(layer50);
        int compareResultLayer250 = totalMoney.compareTo(layer250);

        //calculate as always layer 50
        percentage = new BigDecimal("0.05"); //5%
        totalMoneyLayer50 = layer50.multiply(percentage);

        //totalMoney > 50 && totalMoney <= 250
        if(compareResultLayer50 > 0 && compareResultLayer250 <= 0){
            percentage = new BigDecimal("0.10"); //10%
            totalMoney = totalMoney.subtract(layer50);
            totalMoney = totalMoney.multiply(percentage);
        }

        //totalMoney > 250
        if(compareResultLayer250 > 0){
            percentage = new BigDecimal("0.15"); //15%
            totalMoney = totalMoney.subtract(layer50);
            totalMoney = totalMoney.multiply(percentage);
        }
    }

    //NOTE : plugin based
    private void calculateLayerVietnam(){
        BigDecimal layer50 = new BigDecimal("50000000");
        BigDecimal percentage = new BigDecimal("0.0");
        
        //compare money
        int compareResultLayer50 = totalMoney.compareTo(layer50);

        //calculate as always layer 50
        percentage = new BigDecimal("0.025"); //2.5%
        totalMoneyLayer50 = layer50.multiply(percentage);

        //totalMoney > 50
        if(compareResultLayer50 > 0){
            percentage = new BigDecimal("0.075"); //10%
            totalMoney = totalMoney.subtract(layer50);
            totalMoney = totalMoney.multiply(percentage);
        }
    }

    //NOTE : plugin based
    private void calculateTax(){
        totalTaxPerYear = totalTaxPerYear.add(totalMoneyLayer50);
        totalTaxPerYear = totalTaxPerYear.add(totalMoney);
        int monthPerYear = 12;

        monthlyTax = totalTaxPerYear.divide(BigDecimal.valueOf(monthPerYear), 2, RoundingMode.HALF_UP);
    }

    //can be void / String (for public view)
    public String getTaxResult(){
        
        //execute one by one
        //i call this plugin based, for convenient / easy develop
        getTotalWagePerMonth();
        getSalaryAfterPtkp();

        if(country.equals("indonesia")){
            calculateLayerIndonesia();
        } else {
            calculateLayerVietnam();
        }

        calculateTax();

        //can also develop with nested class which result is like this :
        //getTotalWagePerMonth().getSalaryAfterPtkp().calculateLayerIndonesia(param).calculateTax();

        return monthlyTax.toString();
    }
    
}
