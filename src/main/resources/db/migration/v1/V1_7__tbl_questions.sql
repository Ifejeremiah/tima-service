IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_questions')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_questions ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_questions
    (
        id               INT IDENTITY (1,1) PRIMARY KEY,
        question         VARCHAR(MAX) NOT NULL,
        answer           VARCHAR(225) NOT NULL,
        subject          VARCHAR(100) NOT NULL,
        topic            VARCHAR(100) NOT NULL,
        difficulty_level VARCHAR(10)  NOT NULL,
        mode             VARCHAR(20)  NOT NULL,
        status           VARCHAR(10)  NOT NULL,
        exam_type        VARCHAR(20),
        exam_year        VARCHAR(5),
        created_by       VARCHAR(70)  NOT NULL,
        created_on       DATETIME     NOT NULL,
        last_updated_by  VARCHAR(70),
        last_updated_on  DATETIME
    )
GO