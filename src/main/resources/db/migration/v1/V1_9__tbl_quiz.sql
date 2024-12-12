IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_quiz')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_quiz ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_quiz
    (
        id                  INT IDENTITY (1,1) PRIMARY KEY,
        student_id          INT          NOT NULL REFERENCES tbl_students (id),
        subject             VARCHAR(100) NOT NULL,
        topic               VARCHAR(100) NOT NULL,
        difficulty_level    VARCHAR(7)   NOT NULL,
        score               INT,
        number_of_questions INT,
        created_on          DATETIME     NOT NULL,
        last_updated_on     DATETIME
    )
GO
