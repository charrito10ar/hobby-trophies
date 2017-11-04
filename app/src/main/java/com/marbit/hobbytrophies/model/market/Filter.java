package com.marbit.hobbytrophies.model.market;

public class Filter {
    private String text = "";
    private double minPrice;
    private double maxPrice;
    private boolean barter;
    private int itemCategory;
    private boolean digital;
    private int orderBy;

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isBarter() {
        return barter;
    }

    public void setBarter(boolean barter) {
        this.barter = barter;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
