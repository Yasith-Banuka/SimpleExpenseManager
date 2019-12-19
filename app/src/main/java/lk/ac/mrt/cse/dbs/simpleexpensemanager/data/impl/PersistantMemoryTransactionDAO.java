package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.FeedReaderContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.FeedReaderDbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantMemoryTransactionDAO implements TransactionDAO {
    SQLiteDatabase db;
    SimpleDateFormat dateFormat;

    public PersistantMemoryTransactionDAO(Context context) {
        FeedReaderDbHelper dbHelper=new FeedReaderDbHelper(context);
        db=dbHelper.getWritableDatabase();
        dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues content= new ContentValues();
        content.put(FeedReaderContract.FeedTransaction.ACCOUNT_NO,accountNo);
        content.put(FeedReaderContract.FeedTransaction.DATE,dateFormat.format(date));
        content.put(FeedReaderContract.FeedTransaction.AMOUNT,amount);
        content.put(FeedReaderContract.FeedTransaction.EXPENSE_TYPE,expenseType.toString());

        db.insert(FeedReaderContract.FeedTransaction.TABLE_NAME,null,content);
    }

    public List<Transaction> getAllTransactionLogs() {
        Cursor cursor=db.query(FeedReaderContract.FeedTransaction.TABLE_NAME,null,null,null,null,null,null);
        List<Transaction> transactionList= new ArrayList<>();
        while(cursor.moveToNext()) {
            String acctNum=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.ACCOUNT_NO));
            Date date= null;
            try {
                date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double amount= Double.parseDouble(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.AMOUNT)));
            ExpenseType expenseType=ExpenseType.INCOME;
            if((cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.EXPENSE_TYPE))).equals(ExpenseType.EXPENSE.toString())){

                    expenseType=ExpenseType.EXPENSE;
            }
            Transaction transaction =new Transaction(date,acctNum,expenseType,amount);
            transactionList.add(transaction);
        }
        return transactionList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor cursor=db.query(FeedReaderContract.FeedTransaction.TABLE_NAME,null,null,null,null,null,null);
        List<Transaction> transactionList= new ArrayList<>();
        cursor.moveToLast();
        int count=0;
        while(count<limit) {
            String acctNum=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.ACCOUNT_NO));
            Date date= null;
            try {
                date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double amount= Double.parseDouble(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.AMOUNT)));
            ExpenseType expenseType=ExpenseType.INCOME;
            if((cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedTransaction.EXPENSE_TYPE))).equals(ExpenseType.EXPENSE.toString())){

                expenseType=ExpenseType.EXPENSE;
            }
            Transaction transaction =new Transaction(date,acctNum,expenseType,amount);
            transactionList.add(transaction);
            count+=1;
            cursor.moveToPrevious();
        }
        return transactionList;
    }

}
