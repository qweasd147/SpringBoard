CREATE SMALLFILE TABLESPACE TEST_SPACE 
    DATAFILE 
        '/u01/app/oracle/oradata/XE/test_space.dbf' SIZE 104857600 AUTOEXTEND ON NEXT 10485760 MAXSIZE 11811160064 
    BLOCKSIZE 8192 
    DEFAULT NOCOMPRESS 
    ONLINE 
    SEGMENT SPACE MANAGEMENT AUTO 
    EXTENT MANAGEMENT LOCAL AUTOALLOCATE;

CREATE USER joohyung IDENTIFIED BY wkwj14 DEFAULT TABLESPACE TEST_SPACE PROFILE DEFAULT QUOTA UNLIMITED ON TEST_SPACE;


GRANT  
 CREATE SESSION
,CREATE TABLE
,CREATE SEQUENCE   
,CREATE VIEW
TO joohyung;
