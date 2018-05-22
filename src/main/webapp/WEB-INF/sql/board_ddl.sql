--------------------------------------------------------
--  파일이 생성됨 - 금요일-5월-18-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table TB_BOARD
--------------------------------------------------------

  CREATE TABLE "TB_BOARD" 
   (	"IDX" NUMBER, 
	"SUBJECT" VARCHAR2(300), 
	"CONTENTS" CLOB, 
	"HITS" NUMBER DEFAULT 0, 
	"STATE" NUMBER DEFAULT 1, 
	"WRITER" VARCHAR2(100), 
	"REG_DATE" DATE
   )
--------------------------------------------------------
--  DDL for Table TB_BOARD_FILE
--------------------------------------------------------

  CREATE TABLE "TB_BOARD_FILE" 
   (	"IDX" NUMBER, 
	"CONTENT_TYPE" VARCHAR2(30), 
	"FILE_PATH" VARCHAR2(300), 
	"ORIGIN_FILE_NAME" VARCHAR2(300), 
	"SAVE_FILE_NAME" VARCHAR2(300), 
	"FILE_SIZE" NUMBER, 
	"WRITER" VARCHAR2(300), 
	"REG_DATE" DATE, 
	"STATE" NUMBER DEFAULT 1
   )
--------------------------------------------------------
--  DDL for Table TB_BOARD_FILE_MAP
--------------------------------------------------------

  CREATE TABLE "TB_BOARD_FILE_MAP" 
   (	"BOARD_IDX" NUMBER, 
	"FILE_IDX" NUMBER
   )
--------------------------------------------------------
--  DDL for Index TB_BOARD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TB_BOARD_PK" ON "TB_BOARD" ("IDX")
--------------------------------------------------------
--  DDL for Index TB_BOARD_FILE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TB_BOARD_FILE_PK" ON "TB_BOARD_FILE" ("IDX")
--------------------------------------------------------
--  DDL for Index TB_BOARD_FILE_MAP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TB_BOARD_FILE_MAP_PK" ON "TB_BOARD_FILE_MAP" ("BOARD_IDX", "FILE_IDX")
--------------------------------------------------------
--  Constraints for Table TB_BOARD
--------------------------------------------------------

  ALTER TABLE "TB_BOARD" MODIFY ("IDX" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("SUBJECT" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("CONTENTS" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("HITS" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("STATE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("WRITER" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" MODIFY ("REG_DATE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD" ADD CONSTRAINT "TB_BOARD_PK" PRIMARY KEY ("IDX") ENABLE
--------------------------------------------------------
--  Constraints for Table TB_BOARD_FILE
--------------------------------------------------------

  ALTER TABLE "TB_BOARD_FILE" MODIFY ("IDX" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("CONTENT_TYPE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("FILE_PATH" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("ORIGIN_FILE_NAME" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("SAVE_FILE_NAME" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("FILE_SIZE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("WRITER" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("REG_DATE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" MODIFY ("STATE" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE" ADD CONSTRAINT "TB_BOARD_FILE_PK" PRIMARY KEY ("IDX") ENABLE
--------------------------------------------------------
--  Constraints for Table TB_BOARD_FILE_MAP
--------------------------------------------------------

  ALTER TABLE "TB_BOARD_FILE_MAP" MODIFY ("BOARD_IDX" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE_MAP" MODIFY ("FILE_IDX" NOT NULL ENABLE)
 
  ALTER TABLE "TB_BOARD_FILE_MAP" ADD CONSTRAINT "TB_BOARD_FILE_MAP_PK" PRIMARY KEY ("BOARD_IDX", "FILE_IDX") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table TB_BOARD_FILE_MAP
--------------------------------------------------------

  ALTER TABLE "TB_BOARD_FILE_MAP" ADD CONSTRAINT "FK_BOARD_FILE" FOREIGN KEY ("FILE_IDX")
	  REFERENCES "TB_BOARD_FILE" ("IDX") ENABLE
 
  ALTER TABLE "TB_BOARD_FILE_MAP" ADD CONSTRAINT "FK_BOARD_IDX" FOREIGN KEY ("BOARD_IDX")
	  REFERENCES "TB_BOARD" ("IDX") ENABLE
