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
    @created_by VARCHAR(70),
    @created_on DATETIME)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_jobs (status,
                      created_by,
                      created_on)
VALUES (@status,
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
    @status VARCHAR(20))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_jobs
SET status = @status,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- UPDATE JOB STATUS MESSAGE --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_job_status_message')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_job_status_message AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_job_status_message](
    @id INT,
    @status_message  VARCHAR(MAX))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_jobs
SET status_message = @status_message
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- FIND ALL NEW JOBS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_find_all_new_jobs')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_find_all_new_jobs AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_find_all_new_jobs]
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT * FROM tbl_jobs WHERE status = 'NEW'

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

SELECT * FROM tbl_jobs WHERE id = @id

    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;
GO
