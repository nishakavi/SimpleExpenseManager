package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

/**
 * Created by Nishanthini on 11/20/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    public void setup() {

        SQLiteDatabase myDatabase = context.openOrCreateDatabase("140302P", context.MODE_PRIVATE, null);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                            "AccountNum VARCHAR PRIMARY KEY," +
                            "Bank VARCHAR," +
                            "Holder VARCHAR," +
                            "InitialAmount REAL" + " );");

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                            "TransactionId INTEGER PRIMARY KEY," +
                            "AccountNum VARCHAR," +
                            "Type INT," +
                            "Amount REAL," +
                            "LogDate DATE," +
                            "FOREIGN KEY (AccountNum) REFERENCES Account(AccountNum)" + ");");

        setAccountsDAO(new PersistentAccountDAO(myDatabase));
        setTransactionsDAO(new PersistentTransactionDAO(myDatabase));
    }
}


