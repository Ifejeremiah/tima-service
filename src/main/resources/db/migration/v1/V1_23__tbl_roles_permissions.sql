IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_roles_permissions')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_roles_permissions ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_roles_permissions
    (
        role_id       INT NOT NULL REFERENCES tbl_roles (id),
        permission_id INT NOT NULL REFERENCES tbl_permissions (id)
    )
GO

IF EXISTS(SELECT *
          FROM sys.indexes
          WHERE name = 'idx_roles_permissions'
            AND object_id = OBJECT_ID(N'tbl_roles_permissions'))
    PRINT 'INDEX idx_roles_permissions ALREADY EXISTS... CREATE INDEX ABORTED'
ELSE
    CREATE UNIQUE INDEX idx_roles_permissions
        on tbl_roles_permissions (role_id, permission_id)
