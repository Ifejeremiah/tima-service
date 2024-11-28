IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_otp')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_otp ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_otp
    (
        id         INT IDENTITY (1,1) PRIMARY KEY,
        email      VARCHAR(70) NOT NULL,
        otp        CHAR(7)     NOT NULL,
        expires_at DATETIME    NOT NULL,
        created_on DATETIME    NOT NULL,
    )
GO
