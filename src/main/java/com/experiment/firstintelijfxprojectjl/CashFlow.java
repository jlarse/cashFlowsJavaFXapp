/**
 * CashFlow.java         Eclipse IDE

 * @author Justin Larsen
 * 7/23/2024
 *
 * Description: Implements a cash flow. Represents a cash flow either a possitive value(money coming in) or negative value(money going out).
 * Contains Expense and Income categories to describe the nature of the flow. Also a date the cash flow took place.
 */

package com.experiment.firstintelijfxprojectjl;

import java.io.Serializable;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class CashFlow implements Serializable {

    public enum Category
    {
        //For expenses
        HOUSING,
        UTILITIES,
        TRANSPORTATION,
        FOOD,
        HEALTHCARE,
        EDUCATION,
        PERSONAL_CARE,
        ENTERTAINMENT,
        TRAVEL,
        MISC,

        //For income
        SALARY,
        INVESTMENTS,
        BUISNESS,
        OTHER
    }

    private double amount;
    private LocalDate date;
    private Category category;
    private String description;

    public CashFlow()
    {

    }

    public CashFlow(Category category, LocalDate date, double amount ) throws InvalidCashFlowException {
        if(amount == 0)
            throw new InvalidCashFlowException("Amount must be greater than zero.");

        LocalDate now = LocalDate.now();
        if(date.isAfter(LocalDate.now()))
            throw new InvalidCashFlowException("May not use a future date.");
        else if(date.isBefore(now.minusYears(100)))
            throw new InvalidCashFlowException("May not add a cash flow longer than 100 years ago.");

        if(amount < 0)
        {
            switch(category){
                case SALARY,INVESTMENTS,BUISNESS,OTHER :
                    throw new InvalidCashFlowException(category.name()  + " is an invalid category chosen for an expense cash flow.");
            }

        } else
        {
            switch(category){
                case HOUSING,UTILITIES,TRANSPORTATION,FOOD,HEALTHCARE,EDUCATION,PERSONAL_CARE,ENTERTAINMENT,TRAVEL,MISC :
                    throw new InvalidCashFlowException(category.name()  + " is an invalid category chosen for an income cash flow.");
            }
        }
        double temp = amount;
        BigDecimal bd = new BigDecimal(temp);
        bd = bd.setScale(2, RoundingMode.DOWN); // Set scale to 2 and round down
        this.amount = bd.doubleValue();
        this.category = category;
        this.date = date;
        this.description = "";
    }

    public double getAmount()
    {
        return amount;
    }

    public LocalDate getLocalDate()
    {
        return date;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public static String[] getExpenseCategories()
    {
        String[] expenseCategories = new String[10];
        expenseCategories[0] = "Housing";
        expenseCategories[1] = "Utilities";
        expenseCategories[2] = "Transportation";
        expenseCategories[3] = "Food";
        expenseCategories[4] = "Healthcare";
        expenseCategories[5] = "Education";
        expenseCategories[6] = "Personal Care";
        expenseCategories[7] = "Entertainment";
        expenseCategories[8] = "Travel";
        expenseCategories[9] = "Misc";
        return expenseCategories;
    }

    public static String[] getIncomeCategories()
    {
        String[] incomeCategories = new String[4];
        incomeCategories[0] = "Salary";
        incomeCategories[1] = "Investments";
        incomeCategories[2] = "Buisness";
        incomeCategories[3] = "Other";
        return incomeCategories;
    }

    //if string value source matches any category return the category enum
    public static Category getCategoryFromString(String source)
    {
        switch(source)
        {
            case "Housing":
                return Category.HOUSING;
            case "Utilities":
                return Category.UTILITIES;
            case "Transportation":
                return Category.TRANSPORTATION;
            case "Food":
                return Category.FOOD;
            case "Healthcare":
                return Category.HEALTHCARE;
            case "Education":
                return Category.EDUCATION;
            case "Personal Care":
                return Category.PERSONAL_CARE;
            case "Entertainment":
                return Category.ENTERTAINMENT;
            case "Travel":
                return Category.TRAVEL;
            case "Misc":
                return Category.MISC;

            case "Salary":
                return Category.SALARY;
            case "Investments":
                return Category.INVESTMENTS;
            case "Buisness":
                return Category.BUISNESS;
            case "Other":
                return Category.OTHER;
            default:
                return null;
        }
    }

    public Category getCatCategory()
    {
        return this.category;
    }
    public String getCategory()
    {
        switch(category)
        {
            case Category.HOUSING -> {

                return "Housing";
            }
            case Category.UTILITIES -> {
                return "Utilities";
            }

            case Category.TRANSPORTATION -> {
                return "Transportation";
            }
            case Category.FOOD -> {
                return "Food";
            }
            case Category.HEALTHCARE -> {
                return "Healthcare";
            }
            case Category.EDUCATION -> {
                return "Education";
            }
            case Category.PERSONAL_CARE ->{
                return "Personal Care";
            }
            case Category.ENTERTAINMENT -> {
                return "Entertainment";
            }
            case Category.TRAVEL -> {
                return "Travel";
            }
            case Category.MISC -> {
                return "Misc";
            }
            case Category.SALARY ->{
                return "Salary";
            }
            case Category.INVESTMENTS -> {
                return "Investments";
            }
            case Category.BUISNESS -> {
                return "Buisness";
            }
            case Category.OTHER -> {
                return "Other";
            }
            default -> {
                return "No Category Assigned";
            }
        }
    }

    //returns string representation of cash flow
    @Override
    public String toString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter) + " " + category + " "  + amount;
    }

    //Exception thrown when a cash flow is being assigned improper values
    static public class InvalidCashFlowException extends Exception {
        public InvalidCashFlowException(String message) {
            super(message);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        // Check if the object is compared with itself
        if (this == obj) {
            return true;
        }

        // Check if obj is an instance of CashFlow
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Typecast obj to Person so that we can compare data members
        CashFlow cashFlow = (CashFlow) obj;

        // Compare the data members and return accordingly
        return date.equals(cashFlow.date) && (amount == cashFlow.amount) && (category == cashFlow.category) && (description.equals(cashFlow.description));
    }

}

