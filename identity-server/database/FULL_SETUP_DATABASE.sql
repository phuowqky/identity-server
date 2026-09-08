-- ================================================================
-- BUOC : DROP SEQ
-- ================================================================

BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE USERS_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE ROLES_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- ================================================================
-- BUOC :  DROP TABLE USER
-- ================================================================
BEGIN EXECUTE IMMEDIATE 'DROP TABLE users CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

BEGIN EXECUTE IMMEDIATE 'DROP TABLE ROLES CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

BEGIN EXECUTE IMMEDIATE 'DROP TABLE USER_ROLES CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/


-- ================================================================
-- BUOC : CREATE SEQUENCES
-- ================================================================
CREATE SEQUENCE USERS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

CREATE SEQUENCE ROLES_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/
-- ================================================================
-- BUOC : CREATE TABLE
-- ================================================================

-- ---------------- USER ----------------
CREATE TABLE users (
                       id          NUMBER(19,0) PRIMARY KEY,
                       username    VARCHAR2(50)  NOT NULL UNIQUE,
                       email       VARCHAR2(100) NOT NULL UNIQUE,
                       password    VARCHAR2(255) NOT NULL,
                       full_name   VARCHAR2(100),
                       age         NUMBER(10,0),
                       enabled     NUMBER(1,0) DEFAULT 1 NOT NULL,
                       don_vi_id   NUMBER(19,0),
                       created_at  TIMESTAMP,
                       updated_at  TIMESTAMP
);
/

-- ---------------- ROLES ----------------
CREATE TABLE ROLES (
                       id          NUMBER(19,0) PRIMARY KEY,
                       name        VARCHAR2(50) NOT NULL UNIQUE,
                       description VARCHAR2(255),
                       created_at  TIMESTAMP,
                       updated_at  TIMESTAMP
);
/

-- ---------------- USER_ROLES ----------------
CREATE TABLE USER_ROLES (
                            user_id NUMBER(19,0) NOT NULL,
                            role_id NUMBER(19,0) NOT NULL,
                            CONSTRAINT PK_USER_ROLES PRIMARY KEY (user_id, role_id),
                            CONSTRAINT FK_USER_ROLES_USER FOREIGN KEY (user_id) REFERENCES USERS(id),
                            CONSTRAINT FK_USER_ROLES_ROLE FOREIGN KEY (role_id) REFERENCES ROLES(id)
);