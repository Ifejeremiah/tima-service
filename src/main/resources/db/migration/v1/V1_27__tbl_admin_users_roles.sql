IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_users_roles')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_users_roles ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_users_roles
    (
        user_id INT NOT NULL REFERENCES tbl_admin_users (id),
        role_id INT NOT NULL REFERENCES tbl_roles (id)
    )
GO
