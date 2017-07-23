package com.marbit.hobbytrophies.model.market;

public class Filter {
    private double minPrice;
    private double maxPrice;
    private boolean barter;
    private int itemType;
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

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
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
}
