# Краткое описание проекта

Этот HTTP-сервис предоставляет API для сокращения ссылок, редиректа по коротким ссылкам и сбора
статистики по ним.
Проект реализован с использованием Spring Boot, база данных — H2 (in-memory mode), сборщик — Apache
Maven, язык — Java 17.

## Функциональность включает:

Генерацию короткой ссылки (`POST /generate`)
Редирект по короткой ссылке (`GET /l/{hash}`)
Получение статистики по конкретной ссылке (`GET /stats/{hash}`)
Рейтинг популярных ссылок с постраничным выводом (`GET /stats`)

## Перед запуском убедитесь, что установлены:

JDK 17+
Apache Maven 3+
Git

## Для запуска приложения:

1. Клонировать данный репозиторий
2. Заполнить конфигурационный файл application.properties согласно примеру
   src/main/resources/application.properties.example
   для запуска БД и подключения сервиса Sentry:
   spring.datasource.url - местонахождение базы данных (=jdbc:h2:file:{путь к каталогу})
   spring.datasource.username - пользовательский логин для входа в БД
   spring.datasource.password - пользовательский пароль для входа в БД
   sentry.dsn - ключ для подключения к сервису Sentry.io (получить по адресу сервиса)
3. Собрать приложение командой в терминале:
   mvn clean package
   После успешной сборки в папке target/ появится исполняемый JAR-файл.
4. Запустить приложение командой в терминале:
   java -jar target/{имя собранного пакета}.jar
   По умолчанию приложение запустится на порту 8080
5. Интерфейс базы H2 находится по адресу
   http://localhost:8080/h2-console
6. Для закрытия приложения можно использовать комбинацию CTRL+C

## Использование эндпоинтов:

1. `POST /generate`
   Тело запроса должно содержать оригинальную ссылку по примеру
   {
   "original": "https://example.com/long-url"
   }
2. `GET /l/{hash}`
   Запрос должен содержать имеющуюся в базе короткую ссылку по примеру
   GET /l/b
3. `GET /stats/{hash}`
   Запрос должен содержать имеющуюся в базе короткую ссылку по примеру