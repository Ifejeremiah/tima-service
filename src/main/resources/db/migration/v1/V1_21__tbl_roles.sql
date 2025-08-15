IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_roles')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_roles ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_roles
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        name            VARCHAR(40)  NOT NULL,
        description     VARCHAR(100) NOT NULL,
        created_on      DATETIME     NOT NULL,
        last_updated_on DATETIME
    )
GO