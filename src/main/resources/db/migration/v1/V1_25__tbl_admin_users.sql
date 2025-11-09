IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_admin_users')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_admin_users ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_admin_users
    (
        id              INT IDENTITY (1,1) PRIMARY KEY,
        user_login_id   INT         NOT NULL REFERENCES tbl_user_login (id),
        first_name      VARCHAR(70) NOT NULL,
        last_name       VARCHAR(70) NOT NULL,
        job_title       VARCHAR(50) NOT NULL,
        mobile_phone    VARCHAR(20) NOT NULL,
        address         VARCHAR(50) NOT NULL,
        city            VARCHAR(20) NOT NULL,
        state           VARCHAR(20) NOT NULL,
        country         VARCHAR(20) NOT NULL,
        created_on      DATETIME    NOT NULL,
        created_by      VARCHAR(70) NOT NULL,
        last_updated_on DATETIME,
        last_updated_by VARCHAR(70)
    )
GO
