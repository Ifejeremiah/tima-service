IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_tickets')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_tickets ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_tickets
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        title           VARCHAR(100) NOT NULL,
        status          VARCHAR(12)  NOT NULL,
        priority        VARCHAR(6)   NOT NULL,
        category        VARCHAR(30)  NOT NULL,
        assigned_to     INT REFERENCES tbl_admin_users (id),
        created_by      VARCHAR(70)  NOT NULL,
        created_on      DATETIME     NOT NULL,
        last_updated_on DATETIME
    )
GO
