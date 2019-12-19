package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.FeedReaderContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.FeedReaderDbHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import java.util.ArrayList;
import java.util.List;

public class PersistantMemoryAccountDAO implements AccountDAO{
    SQLiteDatabase db;

    public PersistantMemoryAccountDAO(Context context) {
        FeedReaderDbHelper dbHelper=new FeedReaderDbHelper(context);
        db=dbHelper.getWritableDatabase();
    }

    public List<String> getAccountNumbersList() {
        String[] projection = {FeedReaderContract.FeedAccount.ACCOUNT_NO};

        Cursor cursor = db.query(FeedReaderContract.FeedAccount.TABLE_NAME,projection,
                null,null,null,null,null);
        List<String> accountNumbersList= new ArrayList<>();
        while(cursor.moveToNext()) {
            String accountNumber=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_NO));
            accountNumbersList.add(accountNumber);
        }
        return accountNumbersList;
    }

    public List<Account> getAccountsList() {

        Cursor cursor = db.query(FeedReaderContract.FeedAccount.TABLE_NAME,null,
                null,null,null,null,null);
        List<Account> accountList= new ArrayList<>();
        while(cursor.moveToNext()) {
            String acctNum=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_NO));
            String bankName=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BANK_NAME));
            String acctHolder=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME));
            double balance= Double.parseDouble(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BALANCE)));
            Account account= new Account(acctNum,bankName,acctHolder,balance);
            accountList.add(account);
        }
        return accountList;
    }

    public Account getAccount(String accountNo) throws InvalidAccountException {
        String selection= FeedReaderContract.FeedAccount.ACCOUNT_NO+" = ";
        String[] selectionArg={accountNo};
        Cursor cursor = db.query(FeedReaderContract.FeedAccount.TABLE_NAME,null,
                selection,selectionArg,null,null,null);
        Account account;
        if(cursor.moveToFirst()) {
            String acctNum=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_NO));
            String bankName=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BANK_NAME));
            String acctHolder=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME));
            double balance= Double.parseDouble(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BALANCE)));
            account= new Account(acctNum,bankName,acctHolder,balance);
        }
        else {
            throw new InvalidAccountException("Invalid Account Number");
        }
        return account;
    }

    public void addAccount(Account account) {
        ContentValues content= new ContentValues();
        content.put(FeedReaderContract.FeedAccount.ACCOUNT_NO,account.getAccountNo());
        content.put(FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        content.put(FeedReaderContract.FeedAccount.BANK_NAME,account.getBankName());
        content.put(FeedReaderContract.FeedAccount.BALANCE,account.getBalance());

        db.insert(FeedReaderContract.FeedAccount.TABLE_NAME,null,content);
    }

    public void removeAccount(String accountNo) throws InvalidAccountException{
        String selection= FeedReaderContract.FeedAccount.ACCOUNT_NO+" = ";
        String[] selectionArg={accountNo};
        Cursor cursor = db.query(FeedReaderContract.FeedAccount.TABLE_NAME,null,
                selection,selectionArg,null,null,null);
        String[] delArg ={accountNo};
        if(cursor.moveToFirst()) {
            db.delete(FeedReaderContract.FeedAccount.TABLE_NAME, FeedReaderContract.FeedAccount.ACCOUNT_NO + "=" , delArg);
        }
        else {
            throw new InvalidAccountException("Invalid AccountNumber");
        }
    }
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String selection= FeedReaderContract.FeedAccount.ACCOUNT_NO+" = ";
        String[] selectionArg={accountNo};
        Cursor cursor = db.query(FeedReaderContract.FeedAccount.TABLE_NAME,null,
                selection,selectionArg,null,null,null);
        Account account;
        if(cursor.moveToFirst()) {
            String acctNum=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_NO));
            String bankName=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BANK_NAME));
            String acctHolder=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME));
            double balance= Double.parseDouble(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedAccount.BALANCE)));
            ContentValues content= new ContentValues();
            content.put(FeedReaderContract.FeedAccount.ACCOUNT_NO,acctNum);
            content.put(FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME,acctHolder);
            content.put(FeedReaderContract.FeedAccount.BANK_NAME,bankName);
            switch (expenseType) {
                case EXPENSE:
                    content.put(FeedReaderContract.FeedAccount.BALANCE,balance-amount);
                    break;
                case INCOME:
                    content.put(FeedReaderContract.FeedAccount.BALANCE,balance+amount);
                    break;
            }
            db.update(FeedReaderContract.FeedAccount.TABLE_NAME,content,selection,selectionArg);
        }
        else {
            throw new InvalidAccountException("Invalid Account Number");
        }


    }



}
