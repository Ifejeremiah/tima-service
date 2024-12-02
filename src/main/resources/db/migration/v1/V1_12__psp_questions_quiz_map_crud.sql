-- CREATE QUESTION QUIZ MAP --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_question_quiz_map')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_question_quiz_map AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_question_quiz_map](
    @idx BIGINT = 0 OUTPUT,
    @question_id INT,
    @quiz_id INT,
    @answer VARCHAR(225))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_questions_quiz_map (question_id, quiz_id, answer)
VALUES (@question_id, @quiz_id, @answer)
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO