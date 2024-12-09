-- CREATE QUESTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_question')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_question AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_question](
    @idx BIGINT = 0 OUTPUT,
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7),
    @title VARCHAR(MAX),
    @option_a VARCHAR(225),
    @option_b VARCHAR(225),
    @option_c VARCHAR(225),
    @option_d VARCHAR(225),
    @answer VARCHAR(225),
    @created_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_questions(subject,
                          topic,
                          difficulty_level,
                          title,
                          option_a,
                          option_b,
                          option_c,
                          option_d,
                          answer,
                          created_by,
                          created_on)
VALUES (@subject,
        @topic,
        @difficulty_level,
        @title,
        @option_a,
        @option_b,
        @option_c,
        @option_d,
        @answer,
        @created_by,
        GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FIND ALL QUESTIONS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_questions')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_questions AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_questions(
    @page INT,
    @page_size INT,
    @search_query VARCHAR(40))
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT id,
       subject,
       topic,
       difficulty_level
FROM tbl_questions
WHERE (subject LIKE '%' + @search_query + '%')
   OR (topic LIKE '%' + @search_query + '%')
   OR (difficulty_level LIKE '%' + @search_query + '%')

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_questions
WHERE (subject LIKE '%' + @search_query + '%')
   OR (topic LIKE '%' + @search_query + '%')
   OR (difficulty_level LIKE '%' + @search_query + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ALL QUESTION'S SUBJECTS --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_question_subjects')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_question_subjects AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_question_subjects
AS

    BEGIN TRANSACTION
SELECT subject
FROM tbl_questions
ORDER BY subject
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ALL TOPICS BY QUESTION'S SUBJECT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_topics_by_question_subject ')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_topics_by_question_subject  AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_topics_by_question_subject @subject VARCHAR(100)
AS

    BEGIN TRANSACTION
SELECT topic
FROM tbl_questions
WHERE subject = @subject
ORDER BY topic
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FIND ALL QUESTIONS FOR QUIZ --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_questions_for_quiz')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_questions_for_quiz AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_questions_for_quiz(
    @page_size INT,
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7))
AS
    BEGIN TRANSACTION
SELECT TOP (@page_size) id,
                        title,
                        option_a,
                        option_b,
                        option_c,
                        option_d
FROM tbl_questions
WHERE subject = @subject
  AND topic = @topic
  AND difficulty_level = @difficulty_level
ORDER BY NEWID()
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH QUESTION BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_question_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_question_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_question_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_questions
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- UPDATE QUESTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_question')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_question AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_question](
    @id INT,
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7),
    @title VARCHAR(MAX),
    @option_a VARCHAR(225),
    @option_b VARCHAR(225),
    @option_c VARCHAR(225),
    @option_d VARCHAR(225),
    @answer VARCHAR(225),
    @last_updated_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_questions
SET subject          = @subject,
    topic            = @topic,
    difficulty_level = @difficulty_level,
    title            = @title,
    option_a         = @option_a,
    option_b         = @option_b,
    option_c         = @option_c,
    option_d         = @option_d,
    answer           = @answer,
    last_updated_by  = @last_updated_by,
    last_updated_on  = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- DELETE QUESTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_delete_question')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_delete_question AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_delete_question](
    @id INT)
AS
    BEGIN TRANSACTION

DELETE
FROM tbl_questions
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO