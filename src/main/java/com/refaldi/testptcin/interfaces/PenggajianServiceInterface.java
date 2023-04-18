package com.refaldi.testptcin.interfaces;

import java.math.BigDecimal;

//define public api
public interface PenggajianServiceInterface {
    public String getTotalWagePerMonth();
    public BigDecimal getSalaryAfterPtkp();
    public String getTaxResult();
}
