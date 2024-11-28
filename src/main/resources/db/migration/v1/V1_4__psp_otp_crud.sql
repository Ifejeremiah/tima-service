-- CREATE OTP --

IF
    NOT EXISTS(SELECT *
               FROM sys.objects
               WHERE object_id = OBJECT_ID(N'psp_create_otp')
                 AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_otp AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_otp](
    @id BIGINT = 0 OUTPUT,
    @email VARCHAR(70),
    @otp CHAR(7),
    @expires_at DATETIME)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_otp(email, otp, expires_at, created_on)
VALUES (@email, @otp, @expires_at, GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @id = @@IDENTITY
GO

-- FETCH OTP BY EMAIL AND OTP --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_otp_by_email_and_otp')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_otp_by_email_and_otp AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_otp_by_email_and_otp](
    @email VARCHAR(70),
    @otp CHAR(7))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_otp
WHERE email = @email
  AND otp = @otp
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- DELETE OTP --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'[psp_delete_otp]')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_otp AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_delete_otp] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

DELETE
FROM tbl_otp
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO


