-- CREATE JOB --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_job')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_job AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_job](
    @idx BIGINT = 0 OUTPUT,
    @job_status VARCHAR(20),
    @request_id VARCHAR(100),
    @original_file_name VARCHAR(155),
    @created_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_jobs (status,
                      request_id,
                      original_file_name,
                      created_by,
                      created_on)
VALUES (@job_status,
        @request_id,
        @original_file_name,
        @created_by,
        GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- UPDATE JOB STATUS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_job_status')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_job_status AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_job_status](
    @id INT,
    @job_status VARCHAR(20),
    @status_message VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_jobs
SET status          = @job_status,
    status_message  = @status_message,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- UPDATE JOB STATUS AND RECORD COUNT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_job_status_and_record_count')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_job_status_and_record_count AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_job_status_and_record_count](
    @id INT,
    @job_status VARCHAR(20),
    @record_count VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_jobs
SET status          = @job_status,
    record_count    = @record_count,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- FIND ALL JOBS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_jobs')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_jobs AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_jobs(
    @page INT,
    @page_size INT,
    @status VARCHAR(100)
)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT id,
       request_id,
       record_count,
       status as job_status,
       original_file_name,
       created_by,
       created_on
FROM tbl_jobs
WHERE status LIKE '%' + @status + '%'

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_jobs
WHERE status LIKE '%' + @status + '%'
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND JOB BY REQUEST ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_find_job_by_request_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_find_job_by_request_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_find_job_by_request_id] @request_id VARCHAR(100)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT id,
       request_id,
       record_count,
       status as job_status,
       status_message,
       original_file_name,
       created_by,
       created_on,
       last_updated_on
FROM tbl_jobs
WHERE request_id = @request_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO

-- EXECUTE QUERY --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_execute_query')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_execute_query AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE psp_execute_query(
    @query VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION
    exec (@query)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO
