package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;
import java.util.ArrayList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Nishanthini on 11/19/2016.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;
    
    public PersistentAccountDAO(SQLiteDatabase database){
        this.database = database;
    }
    @Override
    public List<String> getAccountNumbersList(){
        Cursor selectData = database.rawQuery("SELECT AccountNum FROM Account", null);
        selectData.moveToFirst(); // Cursor is pointed to first record
        List<String> account = new ArrayList<String>();  // List to store the data
        
        while (selectData.moveToNext()) {  //Looping
            account.add(selectData.getString(selectData.getColumnIndex("AccountNum")));
        }
        return account;
            
    }
        

    
    
    public List<Account> getAccountsList(){
        Cursor selectData = database.rawQuery("SELECT * FROM Account",null);
        selectData.moveToFirst();
        List<Account> accountList = new ArrayList<Account>();
        Account account;
        while(selectData.moveToNext()){
            account = new Account(selectData.getString(selectData.getColumnIndex("AccountNum")),
                                          selectData.getString(selectData.getColumnIndex("Bank")),
                                          selectData.getString(selectData.getColumnIndex("Holder")),
                                          selectData.getDouble(selectData.getColumnIndex("InitialAmount")));
            accountList.add(account);
        }

        return accountList;
        
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor selectData = database.rawQuery("SELECT * FROM Account WHERE AccountNum = " + accountNo,null);
        Account account= null;
        selectData.moveToFirst();




        while(selectData.moveToNext()){
            account = new Account(selectData.getString(selectData.getColumnIndex("AccountNum")),
                                  selectData.getString(selectData.getColumnIndex("Bank")),
                                  selectData.getString(selectData.getColumnIndex("Holder")),
                                  selectData.getDouble(selectData.getColumnIndex("InitialAmount")));
        }

        return account;
    }
    public void addAccount(Account account) {

        SQLiteStatement a = database.compileStatement("INSERT INTO Account (AccountNum,Bank,Holder,InitialAmount) VALUES (?,?,?,?)");

        a.bindString(1, account.getAccountNo());
        a.bindString(2, account.getBankName());
        a.bindString(3, account.getAccountHolderName());
        a.bindDouble(4, account.getBalance());


        a.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteStatement a = database.compileStatement("DELETE FROM Account WHERE AccountNum = ?");

        a.bindString(1,accountNo);

        a.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteStatement a = database.compileStatement("UPDATE Account SET InitialAmount = InitialAmount + ?");


        if(expenseType == ExpenseType.EXPENSE){  // to decide whether to add the amount or deduct the amount acc to the type of transaction
            a.bindDouble(1,-amount);
        }else{
            a.bindDouble(1,amount);
        }
        a.executeUpdateDelete();
    }
}
