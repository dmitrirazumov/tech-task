TESH-TASK
==============
A simple Java application that uses JDBC to work with PostgreSQL. The MVC pattern is used, application 
can be extended to servlet layer. (Java, PostgreSQL, JDBC, H2, JUnit 5, Mockito, Lombok)

Разработка изначально велась с использованием базы postgres. Далее в зависимостях была подключена база H2 и все тесты выполнялись на ней при условии, что она пуста.
В application.properties можно указать как H2 (jdbc:h2:~/test), так и postgres БД.
Таким образом, была предпринята попытка эмуляции работы с реальным приложением, когда тесты со слоем DAO могут быть чреваты последствиями.
Все тесты выполняются, скриншот приложен на гитхабе.
