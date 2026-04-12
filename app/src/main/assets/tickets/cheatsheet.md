---
number: -1
title: Шпаргалка команд.
---

## 🗒️ Шпаргалка PostgreSQL
**Пользователи и роли:**
```sql
CREATE USER name                          -- создать пользователя (с LOGIN)

CREATE ROLE name                          -- создать роль (без LOGIN)

CREATE USER name WITH PASSWORD 'pass'     -- с паролем

CREATE ROLE name WITH LOGIN               -- роль с возможностью логина

DROP USER name                            -- удалить пользователя
```

**Роли как группы:**
```sql
CREATE ROLE groupname                     -- создать группу

GRANT groupname TO user1, user2           -- добавить юзеров в группу

REVOKE groupname FROM user1               -- убрать юзера из группы
```

**Базы данных:**
```sql
CREATE DATABASE name                      -- создать БД

CREATE DATABASE name TEMPLATE template0   -- создать из чистого шаблона
```

**Схемы:**
```sql
CREATE SCHEMA name                        -- создать схему

CREATE SCHEMA name AUTHORIZATION user     -- схема с владельцем
```

**Таблицы:**
```sql
CREATE TABLE schema.name (col type, ...)  -- создать таблицу в схеме
```

**Grant:**
```sql
GRANT USAGE ON SCHEMA sch TO user             -- входить в схему

GRANT SELECT ON schema.table TO user          -- читать таблицу

GRANT INSERT, UPDATE, DELETE ON table TO user -- изменять таблицу

GRANT ALL ON schema.table TO user             -- все права на таблицу

GRANT ALL ON ALL TABLES IN SCHEMA sch TO user -- все права на все таблицы

GRANT rolename TO user                        -- выдать роль пользователю

REVOKE ALL ON schema.table FROM user          -- забрать все права
```

**PSQL**
- `\dn` - список схем
- `\dt schema.*` - таблицы внутри схемы
- `\d table_name`  - структура таблицы
- `\du` - список пользователей
- `\dp studs.*` - права пользователей к схме.таблице
- `\conninfo`  - информация о текущем юзере