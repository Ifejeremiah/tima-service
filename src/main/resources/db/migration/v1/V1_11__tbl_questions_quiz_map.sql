IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_questions_quiz_map')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_questions_quiz_map ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_questions_quiz_map
    (
        id          INT IDENTITY (1,1) PRIMARY KEY,
        question_id INT          NOT NULL REFERENCES tbl_questions (id),
        quiz_id     INT          NOT NULL REFERENCES tbl_quiz (id),
        answer      VARCHAR(225) NOT NULL
    )
GO