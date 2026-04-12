---
number: -2
title: ЗАДАНИЕ.
---
## Вариант 0

- `student1` и `student2` могут изменять таблицу `studs.Exams`;
- `student2` может изменять таблицу `studs.Groups` (у `student1` такой возможности нет);
- Подключение по TCP/IP с аутентификацией по имени пользователя (без пароля).

**1. Создание пользователей и ролей**

```sql
CREATE USER student1;
CREATE USER student2;
CREATE USER teacher WITH PASSWORD 'teacher_password';

GRANT USAGE ON SCHEMA studs TO student1, student2, teacher;
GRANT USAGE ON SCHEMA test TO teacher;

CREATE ROLE exams_access;
CREATE ROLE groups_access;

GRANT INSERT, UPDATE, DELETE ON studs.Exams TO exams_access;
GRANT INSERT, UPDATE, DELETE ON studs.Groups TO groups_access;

GRANT exams_access TO student1, student2;
GRANT groups_access TO student2;

GRANT INSERT ON studs.Groups, studs.Exams, test.Exams TO teacher;
```

**2. Настройка аутентификации**

Добавим в файл `pg_hba.conf`:

![](./images/pg_hba.png)

Перезагрузим PostgreSQL:
    pg_ctl reload -D $PGDATA


## Задание
Предполагается, что в БД Education (в PostgreSQL) есть пользователи – `student1`, `student2`, `teacher`. Также в БД Education созданы таблицы `Groups` (в схеме `studs`) и `Exams` (в схемах `studs` и `test`). Изначально у `student1`, `student2` нет прав на изменение каких-либо таблиц (в том числе `Groups`, `Exams`), `teacher` – имеет возможность **добавлять** данные в эти таблицы.

Напишите код для создания всех указанных пользователей (с описанными возможностями) и опишите необходимые действия (и конфигурационные файлы), чтобы пользователи могли подключиться к БД и:

- **Вариант 0)** при этом пользователи `student1`, `student2` могли изменять таблицу `studs.Exams`, пользователь `student2` должен иметь возможность изменять таблицу `studs.Groups` (y `student1` нет такой возможности). Параметры подключения для пользователей – ТСP/IР, по имени пользователя.
- **Вариант 1)** при этом пользователь `student2` мог изменять таблицу `studs.Exams`; пользователь `student1` должен иметь возможность изменять таблицу `studs.Groups` (y `student2` нет такой возможности). Параметры подключения для пользователей – ТСР/IР, по паролю.

При выполнении задания: **нельзя** добавлять напрямую привилегии на изменение каких-либо таблиц пользователям – `student1`, `student2`.
