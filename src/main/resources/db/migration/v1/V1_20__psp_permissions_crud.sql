-- CREATE PERMISSION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_permission](
    @idx BIGINT = 0 OUTPUT,
    @name VARCHAR(40),
    @code VARCHAR(40))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_permissions(name, code, created_on)
VALUES (@name, @code, GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FETCH PERMISSIONS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permissions')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permissions AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permissions] @search_query VARCHAR(40)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_permissions
WHERE (name LIKE '%' + @search_query + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- FETCH PERMISSION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permission] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_permissions
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- FETCH PERMISSION BY CODE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permission_by_code')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permission_by_code AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permission_by_code] @code VARCHAR(40)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_permissions
WHERE code = @code
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- UPDATE PERMISSION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_permission](
    @id INT,
    @name VARCHAR(40),
    @code VARCHAR(40))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_permissions
SET name            = @name,
    code            = @code,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

