-- CREATE QUIZ --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_quiz')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_quiz AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_quiz](
    @idx BIGINT = 0 OUTPUT,
    @student_id INT,
    @number_of_questions INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_quiz(student_id, number_of_questions, created_on)
VALUES (@student_id, @number_of_questions, GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
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

-- UPDATE QUIZ --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_quiz')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_quiz AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_quiz](
    @id INT,
    @score INT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_quiz
SET score           = @score,
    last_updated_on = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO