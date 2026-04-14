---
number: 34
title: Конфигурация Spring Boot с профилями dev/prod
keywords: Конфигурация Spring Boot с профилями dev/prod — профиль dev с БД на сервере aqua/helios и heap 2GB, профиль prod с БД на боевом сервере и heap 16GB
---

### Спам-бот Telegram — REST контроллер отправки сообщений конкретному пользователю, случайному пулу пользователей и всему сообществу


#### application.yaml
```yaml
spring:
  application:
    name: demo-app
  profiles:
    active: dev
```
Или задать через env-переменную `SPRING_PROFILES_ACTIVE=prod`

#### application-dev.yaml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://aqua-helios-db:5432/dev_db
    username: dev_user
    password: dev_pass
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080

env:
  heap: 2g
```
#### application-prod.yaml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://prod-db-server:5432/prod_db
    username: prod_user
    password: prod_pass
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

server:
  port: 80

env:
  heap: 16g
```

При этом Heap не задаётся в Spring Boot YAML, а через JVM параметры:
```bash
java -Xms512m -Xmx2g -jar app.jar --spring.profiles.active=dev
```