-- ASSIGN ROLE TO ADMIN USER  --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_user_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_user_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_create_user_role(@user_id INT,
                                     @role_id INT)
AS
    BEGIN TRANSACTION
    SET NOCOUNT ON

INSERT INTO tbl_users_roles(user_id,
                            role_id)
VALUES (@user_id,
        @role_id)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- REMOVE ROLE ON ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_delete_user_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_user_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_delete_user_role(@user_id INT,
                                     @role_id INT)
AS
    BEGIN TRANSACTION
    SET NOCOUNT ON

DELETE
FROM tbl_users_roles
WHERE user_id = @user_id
  AND role_id = @role_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ADMIN USER ROLE  --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_user_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_user_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_user_role](@user_id INT,
                                      @role_id INT)
AS
    BEGIN TRANSACTION
    SET NOCOUNT OFF

SELECT b.*

FROM tbl_users_roles a
         INNER JOIN tbl_roles b
                    ON a.role_id = b.id

WHERE a.user_id = @user_id
  AND b.id = @role_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ROLES ON ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_roles_on_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_roles_on_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_roles_on_user](
    @user_id INT)
AS
    BEGIN TRANSACTION

SELECT b.*

FROM tbl_users_roles a
         INNER JOIN tbl_roles b
                    ON a.role_id = b.id

WHERE a.user_id = @user_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND PERMISSIONS ON ADMIN USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permissions_on_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permissions_on_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permissions_on_user](
    @user_id INT)
AS
    BEGIN TRANSACTION

SELECT d.*

FROM tbl_users_roles a
         INNER JOIN tbl_roles b
                    ON a.role_id = b.id
         INNER JOIN tbl_roles_permissions c
                    ON b.id = c.role_id
         INNER JOIN tbl_permissions d
                    ON c.permission_id = d.id

WHERE a.user_id = @user_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ROLES ON CURRENT USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_roles_on_current_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_roles_on_current_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_roles_on_current_user](
    @current_user_id INT)
AS
    BEGIN TRANSACTION

SELECT b.*

FROM tbl_users_roles a
         INNER JOIN tbl_roles b
                    ON a.role_id = b.id
         INNER JOIN tbl_admin_users c
                    ON a.user_id = c.id
         INNER JOIN tbl_user_login d
                    ON c.user_login_id = d.id

WHERE d.id = @current_user_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND PERMISSIONS ON CURRENT USER --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permissions_on_current_user')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permissions_on_current_user AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permissions_on_current_user](
    @current_user_id INT)
AS
    BEGIN TRANSACTION

SELECT d.*

FROM tbl_users_roles a
         INNER JOIN tbl_roles b
                    ON a.role_id = b.id
         INNER JOIN tbl_roles_permissions c
                    ON b.id = c.role_id
         INNER JOIN tbl_permissions d
                    ON c.permission_id = d.id
         INNER JOIN tbl_admin_users e
                    ON a.user_id = e.id
         INNER JOIN tbl_user_login f
                    ON e.user_login_id = f.id

WHERE f.id = @current_user_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO