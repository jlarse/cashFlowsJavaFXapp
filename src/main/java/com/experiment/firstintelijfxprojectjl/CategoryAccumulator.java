package com.experiment.firstintelijfxprojectjl;

import javafx.scene.paint.Color;

public class CategoryAccumulator {
    private CashFlow.Category category;
    private double totalAccumulation;
    private Color color;

    public CategoryAccumulator(CashFlow.Category category, Color color)
    {
        this.category = category;
        this.color = color;
        this.totalAccumulation = 0;
    }

    public Color getColor()
    {
        return color;
    }

    //returns true if this and category are same category
    public boolean sameCategory(CashFlow.Category category)
    {
        return this.category == category;
    }

    public void accumulate(double amount)
    {
        if(amount < 0)
            totalAccumulation += amount * -1;
        else
            totalAccumulation += amount;
    }

    public void setToZero()
    {
        totalAccumulation = 0;
    }

    public CashFlow.Category getCategory()
    {
        return category;
    }

    public double getAccumulation()
    {
        return totalAccumulation;
    }
}
