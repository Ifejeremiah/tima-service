IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_question_options_map')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_question_options_map ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_question_options_map
    (
        question_id INT          NOT NULL REFERENCES tbl_questions (id),
        options      VARCHAR(225) NOT NULL
    )
GO