TESH-TASK
==============
A simple Java application that uses JDBC to work with PostgreSQL. The MVC pattern is used, application 
can be extended to servlet layer. (Java, PostgreSQL, JDBC, H2, JUnit 5, Mockito, Lombok)

Разработка изначально велась с использованием базы postgres. Далее в зависимостях была подключена база H2 и все тесты выполнялись на ней при условии, что она пуста.</br>
В application.properties можно указать как H2 (jdbc:h2:~/test), так и postgres БД.</br>
Таким образом, была предпринята попытка эмуляции работы с реальным приложением, когда тесты со слоем DAO могут быть чреваты последствиями.</br>
Все тесты выполняются, скриншот приложен на гитхабе.

## TASK 3

**ЗАГРУЗКА SQL СКРИПТА С ТЕСТОВЫМ НАБОРОМ ДАННЫХ**

На уровне сервисов есть 3 метода для инициализации данных:

1. init() - создает тестовые таблицы с добавлением данных для существующей базы данных;
2. initTables() - создает тестовые таблицы без добавления данных;
3. initData() - добавляет данные, если таблицы были созданы методом initTables().

Чтение скриптов происходит из src/main/resources, путь можно поменять в application.properties.

```
UserService userService = UserService.buildUserService();
userService.init();
```
