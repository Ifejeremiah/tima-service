IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_jobs')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_jobs ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_jobs
    (
        id                 INT IDENTITY (1,1) PRIMARY KEY,
        status             VARCHAR(20)  NOT NULL,
        request_id         VARCHAR(100) NOT NULL,
        record_count       INT          NOT NULL,
        status_message     VARCHAR(MAX),
        original_file_name VARCHAR(155) NOT NULL,
        created_by         VARCHAR(70)  NOT NULL,
        created_on         DATETIME     NOT NULL,
        last_updated_on    DATETIME,
    )
GO