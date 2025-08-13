IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_permissions')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_permissions ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_permissions
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        name            VARCHAR(40) NOT NULL,
        code            VARCHAR(40) NOT NULL,
        created_on      DATETIME    NOT NULL,
        last_updated_on DATETIME
    )
GO