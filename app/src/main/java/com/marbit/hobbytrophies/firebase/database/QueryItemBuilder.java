package com.marbit.hobbytrophies.firebase.database;


public class QueryItemBuilder {
    private boolean digital;
    private boolean barter;
    private int category;

    public QueryItem build(){
        return new QueryItem(this);
    }

    public QueryItemBuilder digital(boolean digital){
        this.digital = digital;
        return this;
    }

    public QueryItemBuilder barter(boolean barter){
        this.barter = barter;
        return this;
    }

    public QueryItemBuilder category(int category){
        this.category = category;
        return this;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public boolean isBarter() {
        return barter;
    }

    public void setBarter(boolean barter) {
        this.barter = barter;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
