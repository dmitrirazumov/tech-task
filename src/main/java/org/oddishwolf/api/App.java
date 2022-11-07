package org.oddishwolf.api;

import org.oddishwolf.api.dto.UpdateUserDto;
import org.oddishwolf.api.entity.User;
import org.oddishwolf.api.service.UserService;

import java.util.List;
import java.util.Optional;

public class App {

    public static void main(String[] args) {

        /*
        TASK 3:  ЗАГРУЗКА SQL СКРИПТА С ТЕСТОВЫМ НАБОРОМ ДАННЫХ

        На уровне сервисов есть 3 метода для инициализации данных:
        init() - создает тестовые таблицы с добавлением данных для существующей базы данных;
        initTables() - создает тестовые таблицы без добавления данных;
        initData() - добавляет данные, если таблицы были созданы методом initTables().

        Чтение скриптов происходит из src/main/resources, путь можно поменять в application.properties.
        */
        UserService userService = UserService.buildUserService();
        userService.init();

        /*
        TASK 1:  ПОИСК В РЕПОЗИТОРИИ АККАУНТА ПОЛЬЗОВАТЕЛЯ ПО ИМЕНИ

        На уровне сервисов реализован метод: get(String username).
        */
        Optional<User> user1 = userService.get("username1");
        Optional<User> user2 = userService.get("username2");
        Optional<User> user3 = userService.get("username3");
        System.out.println("[" + user1 + "], " + "[" + user2 + "], " + "[" + user3 + "]");

        /*
        TASK 2:  ИЗМЕНЕНИЕ У УЧЕТНОЙ ЗАПИСИ ЛЮБОГО АТРИБУТА

        На уровне сервисов реализован метод: update(UpdateUserDto userDto)
        Объект проходит:
            1. валидацию: проверки username на null и пробелы/табуляции; на существование пользователя в БД; на отсутствие остальных полей при введенем пользователе;
                          на существование пользователя, на чье имя хотим изменить текущее; на правильный формат (dd-MM-yyyy) даты; на соответсвия коду гендера (1 - MALE, 2 - FEMALE).
                          (если валидация не проходит, то выбрасывается соответсвующей исключение ValidationException)
            2. маппинг: так как данные могут приходить с прикладного уровня, то, скорее всего, это будут строки. Для этого осуществляется маппинг на entity, с которой работает слой DAO;
            3. вызов соответсвующего метода из слоя DAO.
        */
        userService.update(UpdateUserDto.builder()
                .username("username1")
                .firstName("someRandomName1")
                .birthday("24-11-1998")
                .gender("2")
                .build());
        userService.update(UpdateUserDto.builder()
                .username("username2")
                .newUsername("newNameForUsername2")
                .build());
        Optional<User> updatedUser1 = userService.get("username1");
        Optional<User> updatedUser2 = userService.get("username2");
        System.out.println("[" + updatedUser1 + "], " + "[" + updatedUser2 + "]");

        /*
        TASK 4:  ПОЛУЧИТЬ СПИСОК ВСЕХ ПОЛЬЗОВАТЕЛЕЙ, А ЗАТЕМ С ПОМОЩЬЮ Stream API НАЙТИ:
                    а) имена всех пользователей, которые младше 20 лет;
                    б) посчитать количество пользователей, у которых фамилия оканчивается на "ов".

        На уровне сервисов реализованы методы:
        getAll() - получение всех пользователей;
        filterByAge() - получение пользоваталей моложе 20 лет;
        filterByLastNamePostfix() - получение пользователей, чьи фамилии заканчиваются на "ов".
        */
        List<User> users = userService.getAll();
        List<User> usersFilteredByAge = userService.filterByAge();
        List<User> usersFilteredByLastNamePostfix = userService.filterByLastNamePostfix();
        System.out.println(users);
        System.out.println(usersFilteredByAge);
        System.out.println(usersFilteredByLastNamePostfix);
    }
}
