package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import 	android.content.Context;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "170645V";
    private static final String SQL_CREATE_ACCOUNT =
            "CREATE TABLE " + FeedReaderContract.FeedAccount.TABLE_NAME+" ("+
                    FeedReaderContract.FeedAccount.ACCOUNT_NO +" STRING PRIMARY KEY, " +
                    FeedReaderContract.FeedAccount.ACCOUNT_HOLDER_NAME+" TEXT, "+
                    FeedReaderContract.FeedAccount.BANK_NAME+" TEXT, "+
                    FeedReaderContract.FeedAccount.BALANCE+" DECIMAL(10,2))";

    private static final String SQL_CREATE_TRANSACTION =
            "CREATE TABLE " + FeedReaderContract.FeedTransaction.TABLE_NAME+" ( "+
                    FeedReaderContract.FeedTransaction.ACCOUNT_NO +" TEXT, " +
                    FeedReaderContract.FeedTransaction.DATE+" DATE, "+
                    FeedReaderContract.FeedTransaction.EXPENSE_TYPE+" TEXT, "+
                    FeedReaderContract.FeedTransaction.AMOUNT+" DECIMAL(10,2), "+
                    "FOREIGN KEY CONSTRAINT("+FeedReaderContract.FeedTransaction.ACCOUNT_NO+") REFERENCES "+
                    FeedReaderContract.FeedAccount.TABLE_NAME+"("+FeedReaderContract.FeedAccount.ACCOUNT_NO+"))";

    private static final String SQL_DELETE_ACCOUNT =
            "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedAccount.TABLE_NAME;

    private static final String SQL_DELETE_TRANSACTION =
            "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedTransaction.TABLE_NAME;
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACCOUNT);
        db.execSQL(SQL_CREATE_TRANSACTION);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ACCOUNT);
        db.execSQL(SQL_CREATE_TRANSACTION);
        onCreate(db);
    }

}
