-- CREATE USER LOGIN --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_user_login')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_user_login AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_user_login](
    @idx BIGINT = 0 OUTPUT,
    @email VARCHAR(70),
    @password VARCHAR(225))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_user_login(email, password, status, created_on)
VALUES (@email, @password, 'INACTIVE', GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- SET LAST LOGIN TIME --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_set_last_login_time')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_set_last_login_time AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_set_last_login_time] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_user_login
SET last_login_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH USER LOGIN BY EMAIL --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_user_login_by_email')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_user_login_by_email AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_user_login_by_email] @email VARCHAR(70)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_user_login
WHERE email = @email
  AND status <> 'DELETED'
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH USER LOGIN BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_user_login_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_user_login_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_user_login_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_user_login
WHERE id = @id
  AND status <> 'DELETED'
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- UPDATE USER LOGIN --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_user_login')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_user_login AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_user_login](
    @id INT,
    @password VARCHAR(225),
    @status VARCHAR(15),
    @email_confirmed BIT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_user_login
SET password        = @password,
    status          = @status,
    email_confirmed = @email_confirmed,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- DELETE USER LOGIN --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'[psp_delete_user_login]')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_user_login AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_delete_user_login] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_user_login
SET status          = 'DELETED',
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error

GO


