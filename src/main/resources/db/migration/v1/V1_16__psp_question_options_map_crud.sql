-- CREATE QUESTION OPTIONS MAP --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_question_options_map')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_question_options_map AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_question_options_map](
    @idx BIGINT = 0 OUTPUT,
    @question_id VARCHAR(MAX),
    @options VARCHAR(225))
AS
    BEGIN TRANSACTION

INSERT INTO tbl_question_options_map(question_id, options)
VALUES (@question_id, @options)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = 1
GO

-- FIND ALL QUESTION OPTIONS MAP BY QUESTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_question_options_map_by_question')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_question_options_map_by_question AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_question_options_map_by_question(
    @question_id INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION
SELECT *
FROM tbl_question_options_map
WHERE question_id = @question_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- DELETE QUESTION OPTIONS MAP --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_delete_question_options_map')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_question_options_map AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_delete_question_options_map](
    @question_id INT)
AS
    BEGIN TRANSACTION

DELETE
FROM tbl_question_options_map
WHERE question_id = @question_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO