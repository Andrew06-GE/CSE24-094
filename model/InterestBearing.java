package main.com.banking.model;

import java.math.BigDecimal;

public interface InterestBearing {
    void applyMonthlyInterest();
    BigDecimal getMonthlyInterestRate();
}
