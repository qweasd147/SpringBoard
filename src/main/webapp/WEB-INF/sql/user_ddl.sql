--------------------------------------------------------
--  파일이 생성됨 - 목요일-6월-28-2018
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table TB_USER
--------------------------------------------------------

  CREATE TABLE "TB_USER"
   (	"SERVICE_NAME" VARCHAR2(50),
	"ID" VARCHAR2(100),
	"NAME" VARCHAR2(150),
	"NICK_NAME" VARCHAR2(150),
	"EMAIL" VARCHAR2(100),
	"STATE" NUMBER DEFAULT 0
   )
--------------------------------------------------------
--  DDL for Index TB_USER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TB_USER_PK" ON "TB_USER" ("SERVICE_NAME", "ID")
--------------------------------------------------------
--  Constraints for Table TB_USER
--------------------------------------------------------

  ALTER TABLE "TB_USER" MODIFY ("SERVICE_NAME" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("ID" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" ADD CONSTRAINT "TB_USER_PK" PRIMARY KEY ("SERVICE_NAME", "ID") ENABLE
