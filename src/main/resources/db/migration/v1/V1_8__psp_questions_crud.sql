-- CREATE QUESTION --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_question')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_question AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_question](
    @idx BIGINT = 0 OUTPUT,
    @question VARCHAR(MAX),
    @options VARCHAR(MAX),
    @answer VARCHAR(225),
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7),
    @mode VARCHAR(20),
    @status VARCHAR(10),
    @exam_type VARCHAR(20),
    @exam_year VARCHAR(5),
    @created_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_questions(question,
                          options,
                          answer,
                          subject,
                          topic,
                          difficulty_level,
                          mode,
                          status,
                          exam_type,
                          exam_year,
                          created_by,
                          created_on)
VALUES (@question,
        @options,
        @answer,
        @subject,
        @topic,
        @difficulty_level,
        @mode,
        @status,
        @exam_type,
        @exam_year,
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
    @subject VARCHAR(100),
    @mode VARCHAR(20),
    @difficulty_level VARCHAR(7),
    @exam_type VARCHAR(20),
    @search_query VARCHAR(40),
    @start_date DATE,
    @end_date DATE
)
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT id,
       question,
       subject,
       topic,
       difficulty_level,
       mode,
       exam_type,
       created_on
FROM tbl_questions
WHERE (subject LIKE '%' + @subject + '%')
  AND (mode LIKE '%' + @mode + '%')
  AND (difficulty_level LIKE '%' + @difficulty_level + '%')
  AND (exam_type LIKE '%' + @exam_type + '%' OR exam_type IS NULL)
  AND (question LIKE '%' + @search_query + '%')
  AND CAST(created_on AS DATE) BETWEEN (CASE WHEN @start_date IS NULL THEN '2001-08-23' ELSE @start_date END) AND (CASE WHEN @end_date IS NULL THEN GETDATE() ELSE @end_date END)

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_questions
WHERE (subject LIKE '%' + @subject + '%')
  AND (mode LIKE '%' + @mode + '%')
  AND (difficulty_level LIKE '%' + @difficulty_level + '%')
  AND (exam_type LIKE '%' + @exam_type + '%' OR exam_type IS NULL)
  AND (question LIKE '%' + @search_query + '%')
  AND CAST(created_on AS DATE) BETWEEN (CASE WHEN @start_date IS NULL THEN '2001-08-23' ELSE @start_date END) AND (CASE WHEN @end_date IS NULL THEN GETDATE() ELSE @end_date END)
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
SELECT DISTINCT subject
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
SELECT DISTINCT topic
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
                        question
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
    @question VARCHAR(MAX),
    @options VARCHAR(MAX),
    @answer VARCHAR(225),
    @subject VARCHAR(100),
    @topic VARCHAR(100),
    @difficulty_level VARCHAR(7),
    @mode VARCHAR(20),
    @status VARCHAR(10),
    @exam_type VARCHAR(20),
    @exam_year VARCHAR(5),
    @last_updated_by VARCHAR(70))
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_questions
SET question         = @question,
    options          = @options,
    answer           = @answer,
    subject          = @subject,
    topic            = @topic,
    difficulty_level = @difficulty_level,
    mode             = @mode,
    status           = @status,
    exam_type        = @exam_type,
    exam_year        = @exam_year,
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

-- FETCH QUESTION SUMMARY --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_question_summary')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_question_summary AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_question_summary]
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT COUNT(id)                                          AS total_questions,
       SUM(CASE WHEN mode = 'PRACTICE' THEN 1 ELSE 0 END) AS total_practice_questions,
       SUM(CASE WHEN mode = 'EXAM' THEN 1 ELSE 0 END)     AS total_exam_questions,
       SUM(CASE WHEN status = 'DRAFT' THEN 1 ELSE 0 END)  AS total_draft_questions
FROM tbl_questions
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO
