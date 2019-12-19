package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

public final class FeedReaderContract {
    private FeedReaderContract() {}

    public static class FeedAccount {
        public static final String TABLE_NAME = "Account";
        public static final String ACCOUNT_NO="accountNo";
        public static final String BALANCE = "balance";
        public static final String BANK_NAME = "bankName";
        public static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    }

    public static class FeedTransaction {
        public static final String TABLE_NAME = "Transaction";
        public static final String DATE="date";
        public static final String AMOUNT = "amount";
        public static final String EXPENSE_TYPE = "expenseType";
        public static final String ACCOUNT_NO = "accountNo";
    }

}
