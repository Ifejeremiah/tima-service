-- CREATE TICKET --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_ticket')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_ticket AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_ticket](
    @idx BIGINT = 0 OUTPUT,
    @title VARCHAR(100),
    @status VARCHAR(12),
    @priority VARCHAR(6),
    @category VARCHAR(30),
    @created_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_tickets(title,
                        status,
                        priority,
                        category,
                        created_by,
                        created_on)
VALUES (@title,
        @status,
        @priority,
        @category,
        @created_by,
        GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FIND ALL TICKETS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_tickets')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_tickets AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_tickets(
    @page INT,
    @page_size INT,
    @search_query VARCHAR(40),
    @status VARCHAR(12),
    @priority VARCHAR(6),
    @category VARCHAR(30)
)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT *
FROM tbl_tickets
WHERE (id LIKE '%' + @search_query + '%')
  AND (status LIKE '%' + @status + '%')
  AND (priority LIKE '%' + @priority + '%')
  AND (category LIKE '%' + @category + '%')
ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_tickets
WHERE (id LIKE '%' + @search_query + '%')
  AND (status LIKE '%' + @status + '%')
  AND (priority LIKE '%' + @priority + '%')
  AND (category LIKE '%' + @category + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH TICKETS BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_ticket_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_ticket_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_ticket_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_tickets
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