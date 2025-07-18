-- CREATE JOB --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_job')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_job AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_job](
    @idx BIGINT = 0 OUTPUT,
    @status VARCHAR(20),
    @original_file_name VARCHAR(155),
    @created_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_jobs (status,
                      original_file_name,
                      created_by,
                      created_on)
VALUES (@status,
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
    @status VARCHAR(20),
    @status_message VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_jobs
SET status          = @status,
    status_message  = @status_message,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- FIND JOBS FOR PROCESSING --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_find_jobs_for_processing')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_find_jobs_for_processing AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_find_jobs_for_processing](
    @fetch_count INT,
    @amnesty_time INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE TOP (@fetch_count) tbl_jobs
SET status          = 'PROCESSING',
    last_updated_on = GETDATE()
OUTPUT inserted.*
WHERE status = 'NEW'
   OR (status = 'PROCESSING' AND DATEDIFF(ss, last_updated_on, GETDATE()) > @amnesty_time)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- FIND JOB BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_find_job_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_find_job_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_find_job_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_jobs
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO
