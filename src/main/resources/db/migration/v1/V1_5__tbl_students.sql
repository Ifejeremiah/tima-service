IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_students')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_students ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_students
    (
        id               INT IDENTITY (1,1) PRIMARY KEY,
        user_id          INT          NOT NULL REFERENCES tbl_user_login (id),
        name             VARCHAR(70)  NOT NULL,
        age_range        VARCHAR(10)  NOT NULL,
        guardian_email   VARCHAR(70)  NOT NULL,
        agree_to_terms   BIT DEFAULT 0,
        name_of_school   VARCHAR(100) NOT NULL,
        education_level  VARCHAR(60)  NOT NULL,
        grade            VARCHAR(20)  NOT NULL,
        exam_types       VARCHAR(225) NOT NULL,
        goal_description VARCHAR(225),
        learning_goals   VARCHAR(225),
        learning_time    VARCHAR(10),
        can_notify       BIT DEFAULT 1,
        created_on       DATETIME     NOT NULL,
        last_updated_on  DATETIME
    )
GO