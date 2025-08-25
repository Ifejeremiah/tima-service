-- ASSIGN PERMISSION TO ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_role_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_role_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_create_role_permission(
    @role_id INT,
    @permission_id INT)
AS
    BEGIN TRANSACTION
    SET NOCOUNT ON

INSERT INTO tbl_roles_permissions([role_id],
                                  [permission_id])
VALUES (@role_id,
        @permission_id)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- REMOVE PERMISSION ON ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_delete_role_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_role_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_delete_role_permission(
    @role_id INT,
    @permission_id INT)
AS
    BEGIN TRANSACTION
    SET NOCOUNT ON

DELETE
FROM tbl_roles_permissions
WHERE [role_id] = @role_id
  AND [permission_id] = @permission_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND PERMISSION ON ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_role_permission')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_role_permission AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_role_permission](
    @role_id INT,
    @permission_id INT
)
AS
    BEGIN TRANSACTION
    SET NOCOUNT OFF

SELECT role_id, permission_id, b.*

FROM tbl_roles_permissions a
         INNER JOIN tbl_permissions b
                    ON a.permission_id = b.id

WHERE role_id = @role_id
  AND permission_id = @permission_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND PERMISSIONS ON ROLE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_permissions_on_role')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_permissions_on_role AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_permissions_on_role](
    @role_id INT)
AS
    BEGIN TRANSACTION

SELECT a.id, a.name, a.code

FROM tbl_permissions a
         INNER JOIN tbl_roles_permissions b
                    ON a.id = b.permission_id

WHERE b.role_id = @role_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO
