IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_transactions')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_transactions ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_transactions
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        transaction_ref VARCHAR(40) UNIQUE NOT NULL,
        customer_id     INT REFERENCES tbl_user_login (id),
        status          VARCHAR(20)        NOT NULL,
        amount          DECIMAL(10, 2)     NOT NULL,
        payment_plan    VARCHAR(20)        NOT NULL,
        payment_method  VARCHAR(20)        NOT NULL,
        created_on      DATETIME           NOT NULL,
        next_payment_on DATETIME           NOT NULL,
        last_updated_on DATETIME
    )
GO
