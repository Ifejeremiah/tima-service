IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_quiz')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_quiz ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_quiz
    (
        id                  INT IDENTITY (1,1) PRIMARY KEY,
        student_id          INT      NOT NULL REFERENCES tbl_students (id),
        score               INT,
        number_of_questions INT      NOT NULL,
        created_on          DATETIME NOT NULL,
        last_updated_on     DATETIME
    )
GO