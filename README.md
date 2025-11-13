Excel2Word - Spring Boot sample
================================

This project reads an uploaded .xlsx file and creates a .doc file for each row (STT, Ngay TL).

How to run
----------
Requirements: JDK 17+, Maven

1. Build:
   mvn clean package

2. Run:
   java -jar target/excel2word-0.0.1-SNAPSHOT.jar

3. Open in browser:
   http://localhost:8080

Notes about creating .exe
-------------------------
- You can wrap the generated jar using Launch4j (https://launch4j.sourceforge.net/)
- Or create a native installer including a runtime using jpackage (JDK 14+)
