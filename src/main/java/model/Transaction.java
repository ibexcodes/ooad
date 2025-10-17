package model;

import java.util.Date;

public class Transaction {
    private final Date date;
    private final double amount;
    private final String type;

    public Transaction(Date date, double amount, String type) {
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Date getDate() { return date; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
}
