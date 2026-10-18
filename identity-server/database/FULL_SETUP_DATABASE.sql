-- Bảng app được phép dùng SSO
CREATE TABLE CLIENT_APP (
                            APP_ID        VARCHAR2(64)   NOT NULL,
                            NAME          VARCHAR2(200)  NOT NULL,
                            JWT_SECRET    VARCHAR2(512)  NOT NULL,
                            TOKEN_TTL_MS  NUMBER(19)     NOT NULL,
                            ENABLED       NUMBER(1)      DEFAULT 1 NOT NULL,
                            CONSTRAINT PK_CLIENT_APP PRIMARY KEY (APP_ID)
);

-- Redirect URI cho phép của từng app (so khớp chính xác)
CREATE TABLE CLIENT_APP_REDIRECT_URI (
                                         APP_ID        VARCHAR2(64)    NOT NULL,
                                         REDIRECT_URI  VARCHAR2(1000)  NOT NULL,
                                         CONSTRAINT FK_REDIRECT_APP FOREIGN KEY (APP_ID) REFERENCES CLIENT_APP (APP_ID) ON DELETE CASCADE
);
CREATE INDEX IX_REDIRECT_APP ON CLIENT_APP_REDIRECT_URI (APP_ID);

-- Mã dùng một lần đổi lấy JWT
CREATE TABLE AUTH_CODE (
                           CODE          VARCHAR2(128)   NOT NULL,
                           APP_ID        VARCHAR2(64)    NOT NULL,
                           USERNAME      VARCHAR2(200)   NOT NULL,
                           REDIRECT_URI  VARCHAR2(1000)  NOT NULL,
                           EXPIRES_AT    TIMESTAMP       NOT NULL,
                           USED          NUMBER(1)       DEFAULT 0 NOT NULL,
                           CREATED_AT    TIMESTAMP       NOT NULL,
                           CONSTRAINT PK_AUTH_CODE PRIMARY KEY (CODE),
                           CONSTRAINT FK_AUTH_CODE_APP FOREIGN KEY (APP_ID) REFERENCES CLIENT_APP (APP_ID)
);
CREATE INDEX IX_AUTH_CODE_EXPIRES ON AUTH_CODE (EXPIRES_AT);