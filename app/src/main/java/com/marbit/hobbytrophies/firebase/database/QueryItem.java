package com.marbit.hobbytrophies.firebase.database;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

public class QueryItem  {
    private boolean digital;
    private boolean barter;
    private int category;
    private Query itemFilterQuery ;

    public QueryItem(){
        itemFilterQuery = FirebaseDatabase.getInstance().getReference()
                .child(DataBaseConstants.COLUMN_ITEMS)
                .orderByChild(DataBaseConstants.CHILD_PRICE)
                .limitToLast(10);
    }

    public QueryItem(QueryItemBuilder queryItemBuilder) {
        this.digital = queryItemBuilder.isDigital();
        this.barter = queryItemBuilder.isBarter();
        this.category = queryItemBuilder.getCategory();
    }
}
