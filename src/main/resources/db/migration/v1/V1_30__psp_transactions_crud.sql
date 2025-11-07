-- CREATE TRANSACTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_transaction')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_transaction AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_transaction](
    @idx BIGINT = 0 OUTPUT,
    @transaction_ref VARCHAR(40),
    @customer_id INT,
    @status VARCHAR(20),
    @amount DECIMAL(10, 2),
    @payment_plan VARCHAR(20),
    @payment_method VARCHAR(20),
    @next_payment_on DATETIME)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_transactions(transaction_ref,
                             customer_id,
                             status,
                             amount,
                             payment_plan,
                             payment_method,
                             created_on,
                             next_payment_on)
VALUES (@transaction_ref,
        @customer_id,
        @status,
        @amount,
        @payment_plan,
        @payment_method,
        GETDATE(),
        @next_payment_on)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FIND ALL TRANSACTIONS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_transactions')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_transactions AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_transactions(
    @page INT,
    @page_size INT,
    @search_query VARCHAR(40)
)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT *
FROM tbl_transactions
WHERE (transaction_ref LIKE '%' + @search_query + '%')
   OR (status LIKE '%' + @search_query + '%')
ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_transactions
WHERE (transaction_ref LIKE '%' + @search_query + '%')
   OR (status LIKE '%' + @search_query + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH TRANSACTION BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_transaction_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_transaction_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_transaction_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_transactions
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH TRANSACTION BY REF --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_transaction_by_ref')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_transaction_by_ref AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_transaction_by_ref] @transaction_ref VARCHAR(40)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_transactions
WHERE transaction_ref = @transaction_ref
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- UPDATE TRANSACTION STATUS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_transaction_status')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_transaction_status AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_transaction_status](
    @id INT,
    @status VARCHAR(20))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_transactions
SET status          = @status,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH TRANSACTION SUMMARY --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_transaction_summary')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_transaction_summary AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_transaction_summary]
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT SUM(amount)                                                  AS total,
       SUM(CASE WHEN status LIKE 'COMPLETE' THEN amount ELSE 0 END) AS complete,
       SUM(CASE WHEN status LIKE 'PENDING' THEN amount ELSE 0 END)  AS pending,
       SUM(CASE WHEN status LIKE 'FAILED' THEN amount ELSE 0 END)   AS failed,
       SUM(CASE WHEN status LIKE 'REVERSED' THEN amount ELSE 0 END) AS reversed
FROM tbl_transactions
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO