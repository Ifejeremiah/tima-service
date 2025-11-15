-- CREATE TICKET CHAT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_ticket_chat')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_ticket_chat AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_ticket_chat](
    @idx BIGINT = 0 OUTPUT,
    @ticket_id INT,
    @user_id INT,
    @name VARCHAR(70),
    @email VARCHAR(70),
    @text VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_ticket_chats(ticket_id,
                             user_id,
                             name,
                             email,
                             text,
                             created_on)
VALUES (@ticket_id,
        @user_id,
        @name,
        @email,
        @text,
        GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FIND ALL TICKET CHAT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_ticket_chats')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_ticket_chats AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_ticket_chats(
    @page INT,
    @page_size INT,
    @ticket_id INT
)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT *
FROM tbl_ticket_chats
WHERE ticket_id = @ticket_id
ORDER BY id
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_ticket_chats
WHERE ticket_id = @ticket_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO
