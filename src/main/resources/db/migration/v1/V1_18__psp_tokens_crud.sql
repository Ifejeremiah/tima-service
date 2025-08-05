-- CREATE TOKEN --

IF
    NOT EXISTS(SELECT *
               FROM sys.objects
               WHERE object_id = OBJECT_ID(N'psp_create_token')
                 AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_token AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_token](
    @idx BIGINT = 0 OUTPUT,
    @email VARCHAR(70),
    @refresh_token VARCHAR(100),
    @expires_at DATETIME)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

DECLARE @exiting_refresh_token VARCHAR(100)

SELECT @exiting_refresh_token = refresh_token
FROM tbl_tokens
WHERE email = @email
    IF @exiting_refresh_token IS NOT NULL
        BEGIN
            UPDATE tbl_tokens
            SET refresh_token = @refresh_token,
                expires_at    = @expires_at,
                created_on    = GETDATE()
            WHERE email = @email
            SELECT @idx = 0
        END
    ELSE
        BEGIN
            INSERT INTO tbl_tokens(email, refresh_token, expires_at, created_on)
            VALUES (@email, @refresh_token, @expires_at, GETDATE())
            SELECT @idx = @@IDENTITY
        END
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- FETCH TOKEN --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_token_by_email_and_refresh_token')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_token_by_email_and_refresh_token AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_token_by_email_and_refresh_token](
    @email VARCHAR(70),
    @refresh_token VARCHAR(100))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_tokens
WHERE email = @email
  AND refresh_token = @refresh_token
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO
