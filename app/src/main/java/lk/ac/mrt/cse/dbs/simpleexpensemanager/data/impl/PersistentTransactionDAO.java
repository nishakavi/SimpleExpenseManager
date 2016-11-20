package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Nishanthini on 11/20/2016.
 */


public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase database;
    // Constructor
    public PersistentTransactionDAO(SQLiteDatabase database){
        this.database = database;

    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteStatement b = database.compileStatement("INSERT INTO TransactionLog (AccountNum, Type, Amount, LogDate) VALUES(?,?,?,?)");
        b.bindString(1,accountNo);
        long t;
        if(expenseType== ExpenseType.EXPENSE){ t= 0;}
        else {t= 1;}
        b.bindLong(2,t);
        b.bindDouble(3,amount);
        b.bindLong(1,date.getTime());

        b.executeInsert();


    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        Cursor selectData = database.rawQuery("SELECT * from TransactionLog", null);

        selectData.moveToFirst();

        List<Transaction> transactionList = new ArrayList<Transaction>();

        while(selectData.moveToNext()){
            Transaction transaction = new Transaction (new Date(selectData.getLong(selectData.getColumnIndex("LogDate"))),
                                                       selectData.getString(selectData.getColumnIndex("AccountNum")),
                                                       (selectData.getInt(selectData.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                                                       selectData.getDouble(selectData.getColumnIndex("Amount")));
            transactionList.add(transaction);
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit){
        Cursor selectData = database.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit, null);


        selectData.moveToFirst();

        List<Transaction> transactionList = new ArrayList<Transaction>();

        while (selectData.moveToNext()) {
            Transaction transaction = new Transaction(new Date(selectData.getLong(selectData.getColumnIndex("LogDate"))),
                                                      selectData.getString(selectData.getColumnIndex("AccountNum")),
                                                      (selectData.getInt(selectData.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                                                      selectData.getDouble(selectData.getColumnIndex("Amount")));
            transactionList.add(transaction);
        }
        return transactionList;
    }
}
