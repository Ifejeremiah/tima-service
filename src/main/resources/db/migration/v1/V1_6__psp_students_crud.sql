-- CREATE STUDENT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_create_student')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_create_student AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_create_student](
    @idx BIGINT = 0 OUTPUT,
    @user_id INT,
    @name VARCHAR(70),
    @age_range VARCHAR(10),
    @guardian_email VARCHAR(70),
    @agree_to_terms BIT,
    @name_of_school VARCHAR(100),
    @education_level VARCHAR(60),
    @grade VARCHAR(20),
    @exam_types VARCHAR(225),
    @goal_description VARCHAR(225),
    @learning_goals VARCHAR(225),
    @learning_time VARCHAR(10),
    @can_notify BIT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

INSERT INTO tbl_students(user_id,
                         name,
                         age_range,
                         guardian_email,
                         agree_to_terms,
                         name_of_school,
                         education_level,
                         grade,
                         exam_types,
                         goal_description,
                         learning_goals,
                         learning_time,
                         can_notify,
                         created_on)
VALUES (@user_id,
        @name,
        @age_range,
        @guardian_email,
        @agree_to_terms,
        @name_of_school,
        @education_level,
        @grade,
        @exam_types,
        @goal_description,
        @learning_goals,
        @learning_time,
        @can_notify,
        GETDATE())
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

SELECT @idx = @@IDENTITY
GO

-- FIND ALL STUDENTS  --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_students')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_students AS BEGIN SET NOCOUNT ON; END')
GO
ALTER PROCEDURE psp_fetch_students(
    @page INT,
    @page_size INT,
    @search_query VARCHAR(40))
AS
DECLARE @offset INT
    SET @offset = (@page - 1) * @page_size

    BEGIN TRANSACTION
SELECT id,
       user_id,
       name,
       age_range,
       name_of_school,
       education_level,
       grade
FROM tbl_students
WHERE ((name LIKE '%' + @search_query + '%')
    OR (age_range LIKE '%' + @search_query + '%'))
   OR (name_of_school LIKE '%' + @search_query + '%')

ORDER BY id DESC
OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY

SELECT COUNT(*) AS count
FROM tbl_students
WHERE ((name LIKE '%' + @search_query + '%')
    OR (age_range LIKE '%' + @search_query + '%'))
   OR (name_of_school LIKE '%' + @search_query + '%')
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION
GO

-- FETCH STUDENT BY ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_student_by_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_student_by_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_student_by_id] @id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_students
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- FETCH STUDENT BY USER ID --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_fetch_student_by_user_id')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_fetch_student_by_user_id AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_fetch_student_by_user_id] @user_id INT
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

SELECT *
FROM tbl_students
WHERE user_id = @user_id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

-- UPDATE STUDENT --

IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'psp_update_student')
                AND type IN (N'P', N'PC'))
    EXEC ('CREATE PROCEDURE psp_update_student AS BEGIN SET NOCOUNT ON; END')
GO

ALTER PROCEDURE [psp_update_student](
    @id INT,
    @name VARCHAR(70),
    @age_range VARCHAR(10),
    @guardian_email VARCHAR(70),
    @agree_to_terms BIT,
    @name_of_school VARCHAR(100),
    @education_level VARCHAR(60),
    @grade VARCHAR(20),
    @exam_types VARCHAR(225),
    @goal_description VARCHAR(225),
    @learning_goals VARCHAR(225),
    @learning_time VARCHAR(10),
    @can_notify BIT)
AS
    SET NOCOUNT ON
    BEGIN TRANSACTION

UPDATE tbl_students
SET name             = @name,
    age_range        = @age_range,
    guardian_email   = @guardian_email,
    agree_to_terms   = @agree_to_terms,
    name_of_school   = @name_of_school,
    education_level  = @education_level,
    grade            = @grade,
    exam_types       = @exam_types,
    goal_description = @goal_description,
    learning_goals   = @learning_goals,
    learning_time    = @learning_time,
    can_notify       = @can_notify,
    last_updated_on  = GETDATE()
WHERE id = @id
    IF @@ERROR <> 0
        ROLLBACK TRANSACTION;
    ELSE
        COMMIT TRANSACTION;

    RETURN @@Error
GO

