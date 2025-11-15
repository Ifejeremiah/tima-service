IF
    EXISTS (SELECT 1
            FROM sys.objects
            WHERE object_id = object_id(N'tbl_ticket_chats')
              AND type IN (N'U'))
    PRINT 'TABLE tbl_ticket_chats ALREADY EXISTS... CREATE TABLE ABORTED'
ELSE
    CREATE TABLE tbl_ticket_chats
    (
        id         INT IDENTITY (1,1) PRIMARY KEY,
        ticket_id  INT REFERENCES tbl_tickets (id),
        user_id    INT REFERENCES tbl_user_login (id),
        name       VARCHAR(70)  NOT NULL,
        email      VARCHAR(70)  NOT NULL,
        text       VARCHAR(MAX) NOT NULL,
        created_on DATETIME     NOT NULL
    )
GO
