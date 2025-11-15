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
        'OPEN',
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

-- UPDATE TICKET --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_tickets')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_tickets AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_tickets](
    @id INT,
    @status VARCHAR(12),
    @assigned_to INT,
    @last_updated_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_tickets
SET status          = @status,
    assigned_to     = @assigned_to,
    last_updated_by = @last_updated_by,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH TICKET SUMMARY --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_ticket_summary')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_ticket_summary AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_ticket_summary]
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT COUNT(id)                                               AS total_tickets,
       SUM(CASE WHEN status = 'OPEN' THEN 1 ELSE 0 END)        AS open_tickets,
       SUM(CASE WHEN status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS in_progress_tickets,
       SUM(CASE WHEN status = 'RESOLVED' THEN 1 ELSE 0 END)    AS resolved_tickets
FROM tbl_tickets
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO