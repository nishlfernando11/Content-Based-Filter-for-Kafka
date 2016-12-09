@echo off

REM javac test\java\com\filter\ConsoleContentConsumerTest.java

REM java test.java.com.filter.ConsoleContentConsumerTest %1 %2 %3 %4 %5

java -cp FilterWithMVEL-1.0-SNAPSHOT-jar-with-dependencies.jar com.filter.clients.FilterProducer %1 %2 

pause