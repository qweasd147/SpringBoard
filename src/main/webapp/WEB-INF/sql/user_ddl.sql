--------------------------------------------------------
--  파일이 생성됨 - 화요일-9월-04-2018
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table TB_USER
--------------------------------------------------------

  CREATE TABLE "TB_USER"
   (	"IDX" NUMBER,
	"SERVICE_NAME" VARCHAR2(50),
	"ID" VARCHAR2(100),
	"NAME" VARCHAR2(150),
	"NICK_NAME" VARCHAR2(150),
	"EMAIL" VARCHAR2(150),
	"STATE" NUMBER
   )
--------------------------------------------------------
--  DDL for Index TB_USER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TB_USER_PK" ON "TB_USER" ("IDX")
--------------------------------------------------------
--  DDL for Index UNQ_SERVICE_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "UNQ_SERVICE_ID" ON "TB_USER" ("SERVICE_NAME", "ID")
--------------------------------------------------------
--  Constraints for Table TB_USER
--------------------------------------------------------

  ALTER TABLE "TB_USER" MODIFY ("IDX" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("SERVICE_NAME" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("ID" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("NAME" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("EMAIL" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" MODIFY ("STATE" NOT NULL ENABLE)

  ALTER TABLE "TB_USER" ADD CONSTRAINT "TB_USER_PK" PRIMARY KEY ("IDX") ENABLE

  ALTER TABLE "TB_USER" ADD CONSTRAINT "UNQ_SERVICE_ID" UNIQUE ("SERVICE_NAME", "ID") ENABLE

--------------------------------------------------------
--  파일이 생성됨 - 월요일-7월-02-2018
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence SQ_USER
--------------------------------------------------------

   CREATE SEQUENCE  "SQ_USER"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE