IF EXISTS (SELECT 1
           FROM sys.objects
           WHERE object_id = object_id(N'tbl_questions')
             AND type IN (N'U'))
    PRINT 'TABLE tbl_questions ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_questions
    (
        id               INT IDENTITY (1,1) PRIMARY KEY,
        subject          VARCHAR(100) NOT NULL,
        topic            VARCHAR(100) NOT NULL,
        difficulty_level VARCHAR(7)   NOT NULL,
        title            VARCHAR(MAX) NOT NULL,
        option_a         VARCHAR(225) NOT NULL,
        option_b         VARCHAR(225) NOT NULL,
        option_c         VARCHAR(225) NOT NULL,
        option_d         VARCHAR(225) NOT NULL,
        answer           VARCHAR(225) NOT NULL,
        created_by       VARCHAR(70)  NOT NULL,
        created_on       DATETIME     NOT NULL,
        last_updated_by  VARCHAR(70),
        last_updated_on  DATETIME
    )
GO