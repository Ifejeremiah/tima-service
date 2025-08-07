-- START QUIZ --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_start_quiz')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_start_quiz AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_start_quiz](
    @quizId INT = 0 OUTPUT,
    @student_id INT,
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7),
    @number_of_questions INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

DECLARE @count INT;
SELECT @count = COUNT(*)
FROM tbl_questions
WHERE subject = @subject
  AND topic = @topic
  AND difficulty_level = @difficulty_level

SELECT TOP (@number_of_questions) *
FROM tbl_questions
WHERE subject = @subject
  AND topic = @topic
  AND difficulty_level = @difficulty_level
ORDER BY NEWID()

INSERT INTO tbl_quiz(student_id, subject, topic, difficulty_level, number_of_questions, created_on)
VALUES (@student_id, @subject, @topic, @difficulty_level, @count, GETDATE())
SELECT @quizId = @@IDENTITY
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

GO

-- FIND ALL QUIZZES --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_quizzes')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_quizzes AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_quizzes(
    @page INT,
    @page_size INT)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT *
FROM tbl_quiz

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_quiz
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ALL QUIZZES BY STUDENT ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_quizzes_by_student_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_quizzes_by_student_id AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_quizzes_by_student_id(
    @page INT,
    @page_size INT,
    @student_id INT)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT *
FROM tbl_quiz
WHERE student_id = @student_id

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_quiz
WHERE student_id = @student_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH QUIZ BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_quiz_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_quiz_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_quiz_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_quiz
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- SUBMIT QUIZ --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_submit_quiz')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_submit_quiz AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_submit_quiz](
    @id INT,
    @score INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_quiz
SET score               = @score,
    last_updated_on     = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO
