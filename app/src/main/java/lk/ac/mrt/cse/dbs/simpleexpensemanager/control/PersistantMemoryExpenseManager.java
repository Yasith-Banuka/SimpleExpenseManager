package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantMemoryTransactionDAO;


public class PersistantMemoryExpenseManager  extends ExpenseManager{
    private Context context;

    public PersistantMemoryExpenseManager(Context context) {
        this.context=context;

    }

    public void setup() {
        AccountDAO persistantAccountDAO =new PersistantMemoryAccountDAO(context);
        setAccountsDAO(persistantAccountDAO);
        TransactionDAO persistantTransactionDAO=new PersistantMemoryTransactionDAO(context);
        setTransactionsDAO(persistantTransactionDAO);
    }
}
