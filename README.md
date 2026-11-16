# SOA: первый рубеж

Материалы для совместной подготовки к первому рубежу по сервис-ориентированной архитектуре.

## Источники и статус

- [Исходный список вопросов и практических задач](docs/soa-rubezh-1-questions.txt).
- Справочник «Рубежка 1 - подготовка.pdf» хранится локально и не включён в репозиторий.
- 33 теоретических вопроса и 12 практических задач, всего 45 файлов билетов.
- Ответы 01–04 сохранены; ответы 26–33 заполнены. Вопросы 05–25 содержат план ответа, задачи 34–45 — исходное условие; эти ответы и решения ещё предстоит написать.

Список вопросов определяет состав подготовки. PDF используется как вспомогательный материал; его примеры и формулировки нужно проверять при написании ответов. Неоднозначные условия практики сохранены, их трактовку следует явно указывать в решении.

## Как устроены материалы

Билеты находятся в `app/src/main/assets/tickets/NN.md`. В начале каждого файла обязательны `number` и `title` между разделителями `---`; после них идёт Markdown ответа.

`app/src/main/assets/ticket-blocks.md` используется приложением. Последнее число в строке — номер тематической группы: 1–8 для теории, 9 для практики. `app/src/tickets.md` содержит такую же копию списка; при изменении состава обновлять оба файла. Группы соответствуют темам подготовки, а не вариантам рубежа.

## Билеты

### 1. Архитектура и основные принципы SOA

- [01. Подходы к проектированию приложений. Монолитная и сервис-ориентированная архитектура.](app/src/main/assets/tickets/01.md)
- [02. Понятие сервиса. Общие свойства и характеристики сервисов.](app/src/main/assets/tickets/02.md)
- [03. Основные принципы SOA. Подходы к реализации SOA, стандарты и протоколы.](app/src/main/assets/tickets/03.md)
- [04. Общие принципы построения и элементы сервис-ориентированных систем. Структура SOA-приложения.](app/src/main/assets/tickets/04.md)

### 2. Веб-сервисы и SOAP

- [05. Понятие веб-сервиса. Категоризация веб-сервисов.](app/src/main/assets/tickets/05.md)
- [06. SOAP: принципы работы, особенности, достоинства и недостатки.](app/src/main/assets/tickets/06.md)
- [07. Языки и стандарты описания веб-сервисов. WSDL и другие спецификации.](app/src/main/assets/tickets/07.md)
- [08. Интеграция веб-сервисов. Принципы взаимодействия и способы обмена данными.](app/src/main/assets/tickets/08.md)

### 3. RESTful веб-сервисы

- [09. RESTful веб-сервисы. Основные принципы и особенности архитектурного подхода.](app/src/main/assets/tickets/09.md)
- [10. Понятие ресурса в RESTful. URI, представление ресурса и полезная нагрузка (payload).](app/src/main/assets/tickets/10.md)
- [11. Виды RESTful-сервисов. Интерпретация методов HTTP в RESTful.](app/src/main/assets/tickets/11.md)
- [12. Правила именования и организации ресурсов в RESTful-сервисах.](app/src/main/assets/tickets/12.md)
- [13. Спецификация и документирование RESTful-сервисов. OpenAPI и Swagger.](app/src/main/assets/tickets/13.md)
- [14. Архитектурный принцип HATEOAS.](app/src/main/assets/tickets/14.md)

### 4. Разработка RESTful-сервисов на Java: JAX-RS

- [15. Технология JAX-RS. Назначение, архитектура и особенности.](app/src/main/assets/tickets/15.md)
- [16. Основные и вспомогательные аннотации JAX-RS.](app/src/main/assets/tickets/16.md)
- [17. Описание и реализация ресурсов в JAX-RS.](app/src/main/assets/tickets/17.md)

### 5. Разработка RESTful-сервисов на Spring Web MVC

- [18. Технология Spring Web MVC REST. Архитектура и особенности.](app/src/main/assets/tickets/18.md)
- [19. Архитектура Spring Web MVC. Основные компоненты и их взаимодействие.](app/src/main/assets/tickets/19.md)
- [20. Аннотации Spring Web MVC для разработки RESTful-сервисов.](app/src/main/assets/tickets/20.md)
- [21. Сериализация и десериализация данных в Spring Web MVC REST.](app/src/main/assets/tickets/21.md)

### 6. Spring Data REST

- [22. Технология Spring Data REST. Назначение, архитектура и особенности.](app/src/main/assets/tickets/22.md)
- [23. Spring Data REST: конфигурация и основные аннотации.](app/src/main/assets/tickets/23.md)
- [24. Spring Data REST: стратегии экспорта репозиториев.](app/src/main/assets/tickets/24.md)
- [25. Spring Data REST: виды ресурсов и автоматическое описание сервисов.](app/src/main/assets/tickets/25.md)

### 7. Основы криптографии и протоколы SSL/TLS

- [26. Основы криптографии в веб-приложениях.](app/src/main/assets/tickets/26.md)
- [27. Симметричное и асимметричное шифрование. Ключи и сертификаты.](app/src/main/assets/tickets/27.md)
- [28. Протоколы SSL и TLS. Особенности, сходства и различия.](app/src/main/assets/tickets/28.md)
- [29. Доверенные центры сертификации и иерархия сертификатов. Самоподписанные сертификаты.](app/src/main/assets/tickets/29.md)

### 8. Криптография и защищённые соединения в Java

- [30. Настройка защищённого соединения в Java.](app/src/main/assets/tickets/30.md)
- [31. Хранилища сертификатов и ключей в Java: KeyStore и TrustStore.](app/src/main/assets/tickets/31.md)
- [32. Утилита keytool. Назначение и основные команды.](app/src/main/assets/tickets/32.md)
- [33. Криптография в Java: стандарты, API и алгоритмы.](app/src/main/assets/tickets/33.md)

### 9. Практика

- [34. Корзина покупок на JAX-RS: XML](app/src/main/assets/tickets/34.md)
- [35. Spring Data REST: экспорт непубличных методов](app/src/main/assets/tickets/35.md)
- [36. Spring Data REST: экспорт только одного метода](app/src/main/assets/tickets/36.md)
- [37. Заказы резиновых уточек по 3D-модели на сервлетах](app/src/main/assets/tickets/37.md)
- [38. Спецификация REST API домофона](app/src/main/assets/tickets/38.md)
- [39. Spring Data REST: сервис на основе репозитория](app/src/main/assets/tickets/39.md)
- [40. Spring Data REST: экспорт публичных методов CrudRepository](app/src/main/assets/tickets/40.md)
- [41. Казино: слот-машина на JAX-RS](app/src/main/assets/tickets/41.md)
- [42. Управление банкоматом на сервлетах](app/src/main/assets/tickets/42.md)
- [43. Управление шлагбаумом на JAX-RS](app/src/main/assets/tickets/43.md)
- [44. Спецификация REST API записи студентов на курсы](app/src/main/assets/tickets/44.md)
- [45. Управление номеронабирателем на сервлетах](app/src/main/assets/tickets/45.md)
