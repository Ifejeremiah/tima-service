-- CREATE ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_admin_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_admin_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_admin_user](
    @idx BIGINT = 0 OUTPUT,
    @first_name VARCHAR(70),
    @last_name VARCHAR(70),
    @email VARCHAR(70),
    @job_title VARCHAR(50),
    @mobile_phone VARCHAR(20),
    @address VARCHAR(50),
    @city VARCHAR(20),
    @state VARCHAR(20),
    @country VARCHAR(20),
    @created_by VARCHAR(70))
AS

    SET NOCOUNT ON
    BEGIN TRANSACTION

DECLARE @created_on DATETIME
DECLARE @user_login_id BIGINT
SELECT @created_on = GETDATE()

INSERT INTO tbl_user_login (email,
                            password,
                            status,
                            created_on)

VALUES (@email,
        '$2a$10$MhcTMClLDAA3JBH.9yJsj.cCg.KDuA2T8T0z5mz7oXaCt8E95W27e',
        'ACTIVE',
        @created_on)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION

    BEGIN TRANSACTION

SELECT @user_login_id = @@IDENTITY

INSERT INTO tbl_user_details(user_login_id,
                             first_name,
                             last_name,
                             job_title,
                             mobile_phone,
                             address,
                             city,
                             state,
                             country,
                             created_on,
                             created_by)
VALUES (@user_login_id,
        @first_name,
        @last_name,
        @job_title,
        @mobile_phone,
        @address,
        @city,
        @state,
        @country,
        @created_on,
        @created_by)

SELECT @idx = @@IDENTITY
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH ADMIN USERS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_admin_users')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_admin_users AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_fetch_admin_users(
    @page INT,
    @page_size INT,
    @search_query VARCHAR(40))
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size
    BEGIN TRANSACTION

SELECT a.id,
       a.first_name,
       a.last_name,
       b.email,
       a.job_title,
       a.country
FROM tbl_user_details a
         INNER JOIN tbl_user_login b ON a.user_login_id = b.id
WHERE b.status <> 'DELETED'
  AND ((a.first_name LIKE '%' + @search_query + '%')
    OR (a.last_name LIKE '%' + @search_query + '%')
    OR (b.email LIKE '%' + @search_query + '%'))
ORDER BY a.[id] DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_user_details a
         INNER JOIN tbl_user_login b ON a.user_login_id = b.id
WHERE b.status <> 'DELETED'
  AND ((a.first_name LIKE '%' + @search_query + '%')
    OR (a.last_name LIKE '%' + @search_query + '%')
    OR (b.email LIKE '%' + @search_query + '%'))
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_admin_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_admin_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_admin_user](
    @id INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT a.*,
       b.email
FROM tbl_user_details a
         INNER JOIN tbl_user_login b ON a.user_login_id = b.id
WHERE a.id = @id
  AND b.status <> 'DELETED'
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@ERROR
GO

-- UPDATE ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_admin_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_admin_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_admin_user](
    @id INT,
    @first_name VARCHAR(70),
    @last_name VARCHAR(70),
    @job_title VARCHAR(50),
    @mobile_phone VARCHAR(20),
    @address VARCHAR(50),
    @city VARCHAR(20),
    @state VARCHAR(20),
    @country VARCHAR(20),
    @last_updated_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_user_details
SET first_name      = @first_name,
    last_name       = @last_name,
    job_title       = @job_title,
    mobile_phone    = @mobile_phone,
    address         = @address,
    city            = @city,
    state           = @state,
    country         = @country,
    last_updated_by = @last_updated_by,
    last_updated_on = GETDATE()

WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO
