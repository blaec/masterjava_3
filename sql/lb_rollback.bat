set LB_HOME="C:\Program Files\liquibase-3.6.2"
call %LB_HOME%\liquibase.bat --driver=org.postgresql.Driver ^
--classpath=%LB_HOME%\lib ^
--changeLogFile=databaseChangeLog.sql ^
--url="jdbc:postgresql://localhost:5432/masterjava" ^
--username=user ^
--password=password ^
rollback rollbackToDate 2019-03-26 22:50:51.673475