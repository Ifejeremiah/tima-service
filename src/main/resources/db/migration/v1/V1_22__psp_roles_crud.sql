-- CREATE ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_role](
    @idx BIGINT = 0 OUTPUT,
    @name VARCHAR(40),
    @description VARCHAR(100))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_roles(name, description, created_on)
VALUES (@name, @description, GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FETCH ROLES --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_roles')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_roles AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_roles](@page INT,
                                  @page_size INT,
                                  @search_query VARCHAR(40))
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size
    BEGIN TRANSACTION

SELECT id, name
FROM tbl_roles
WHERE (name LIKE '%' + @search_query + '%')

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_roles
WHERE (name LIKE '%' + @search_query + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- FETCH ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_role] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_roles
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- FETCH ROLE BY NAME --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_role_by_name')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_role_by_name AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_role_by_name] @name VARCHAR(40)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_roles
WHERE name = @name
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- UPDATE ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_role](
    @id INT,
    @name VARCHAR(40),
    @description VARCHAR(100))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_roles
SET name            = @name,
    description     = @description,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

