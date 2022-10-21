### requirements:

+ jdk 11 (or higher)
+ maven 3.8.6 (or higher)

How to create database schema and run a local app, run following commands:

* mvn clean compile -U -Pdev
* mvn -Pdev jpa-schema:generate
* mvn -Pdev sql:execute
* mvn spring-boot:run

How to create database schema, import data in all tables and run a local app, run following commands:

* mvn clean compile -U -Pdev
* mvn -Pdev jpa-schema:generate
* mvn -Pdev sql:execute@import-data
* mvn spring-boot:run

How to (target folder):

* build - mvn clean package -Pdev
* run local - java -jar <jarname>.jar

Api Documentation (Swagger)

* http://localhost:8080/api/swagger-ui/index.html (local host)
* http://host/context-path/api/swagger-ui/index.html

Change db parameters

* open pom.xml
* change following properties for maven plugin

```xml
<jdbcUrl>jdbc:postgresql://localhost:5432/postgres</jdbcUrl>
<jdbcUser>postgres</jdbcUser>
<jdbcPassword></jdbcPassword>
```

* rebuild and run an app

Hosts (src/main/resources/application.properties)

```clojure
server.port=8080
backend.host=http://localhost:${server.port}
frontend.host=http://localhost:3000
```

Change backend server port

* open file src/main/resources/application.properties
* change following property

```clojure
server.port=8080
```

* rebuild and run an app

For changing database driver open pom.xml and change following properties

```xml
<driverGroupId>org.postgresql</driverGroupId>
<driverArtifactId>postgresql</driverArtifactId>
<driverVersion>42.4.0</driverVersion>
```

* go to https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client/3.0.6

```xml
<driverGroupId>org.mariadb.jdbc</driverGroupId>
<driverArtifactId>mariadb-java-client</driverArtifactId>
<driverVersion>3.0.6</driverVersion>
```

* set org.mariadb.jdbc in driverGroupId tag
* mariadb-java-client in driverArtifactId tag
* 3.0.6 in driverVersion tag
* change db parameters
* change following parameters in pom.xml for maven plugins as in for src/main/resources/application.properties

for postgresql
```xml
<jdbcDriver>org.postgresql.Driver</jdbcDriver>
<jdbcUrl>jdbc:postgresql://localhost:5432/postgres</jdbcUrl>
<jdbcUser>postgres</jdbcUser>
<jdbcPassword>postgres</jdbcPassword>
<hibernateDialect>org.hibernate.dialect.PostgreSQL10Dialect</hibernateDialect>
```
for mariadb
```xml
<jdbcDriver>org.mariadb.jdbc.Driver/jdbcDriver>
<jdbcUrl>jdbc:mariadb://localhost:3306/DB</jdbcUrl>
<jdbcUser>root</jdbcUser>
<jdbcPassword>root</jdbcPassword>
<hibernateDialect>org.hibernate.dialect.MariaDB106Dialect</hibernateDialect>
```

* rebuild and run an app

Emails parameters (src/main/resources/application.properties)

```clojure
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=support@flatlogic.com
spring.mail.password=
spring.mail.properties.mail.smtp.starttls.auto=true
spring.mail.properties.mail.smtp.starttls.enable=true
email.from=support@flatlogic.com
```
