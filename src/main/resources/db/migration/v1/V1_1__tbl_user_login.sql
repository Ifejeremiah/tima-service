IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_user_login')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_user_login ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_user_login
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        email           VARCHAR(70) UNIQUE NOT NULL,
        password        VARCHAR(225)       NOT NULL,
        status          VARCHAR(15)        NOT NULL,
        email_confirmed BIT DEFAULT 0,
        last_login_on   DATETIME,
        created_on      DATETIME           NOT NULL,
        last_updated_on DATETIME
    )
GO