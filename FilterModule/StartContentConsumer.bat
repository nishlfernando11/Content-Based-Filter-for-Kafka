@echo off

REM javac test\java\com\filter\ConsoleContentConsumerTest.java

REM java test.java.com.filter.ConsoleContentConsumerTest %1 %2 %3 %4 %5

java -cp FilterModule-1.0-jar-with-dependencies.jar com.filter.clients.ContentConsumer %1 %2 %3 %4 %5

pause