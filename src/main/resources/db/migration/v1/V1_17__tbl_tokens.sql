IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_tokens')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_tokens ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_tokens
    (
        id            INT IDENTITY (1,1) PRIMARY KEY,
        email         VARCHAR(70)  NOT NULL,
        refresh_token VARCHAR(100) NOT NULL,
        expires_at    DATETIME     NOT NULL,
        created_on    DATETIME     NOT NULL,
    )
GO
