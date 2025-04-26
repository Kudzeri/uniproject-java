# UniProject - Система управления университетом

## Описание проекта
UniProject - это веб-приложение для управления университетом, построенное на Spring Boot. Проект включает в себя систему аутентификации с ролями, управление новостями, событиями, курсами и их студентами, а также функционал отправки email-уведомлений.

## Технологии
- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT для аутентификации
- Swagger/OpenAPI для документации API
- Spring Mail для отправки email
- Lombok для уменьшения boilerplate кода
- Docker и Docker Compose для контейнеризации

## Структура проекта
```
src/main/java/com/example/movies/
├── config/           # Конфигурационные классы
├── controller/       # REST контроллеры
├── dto/             # Data Transfer Objects
├── exception/       # Обработчики исключений
├── mapper/          # Мапперы для преобразования объектов
├── model/           # Сущности базы данных
├── repository/      # JPA репозитории
├── security/        # Конфигурация безопасности
├── service/         # Бизнес-логика
├── util/            # Утилитарные классы
└── MoviesApplication.java
```

## Требования
- Java 21
- Maven
- Docker и Docker Compose
- PostgreSQL

## Установка и запуск

### Локальная разработка
1. Клонируйте репозиторий:
```bash
git clone [url-репозитория]
```

2. Создайте файл `.env` на основе `.env.example`:
```bash
cp .env.example .env
```

3. Заполните `.env` файл необходимыми значениями

4. Запустите приложение:
```bash
mvn spring-boot:run
```

### Запуск через Docker
1. Соберите и запустите контейнеры:
```bash
docker-compose up --build
```

Приложение будет доступно по адресу: http://localhost:8080

## API Документация
После запуска приложения, документация Swagger доступна по адресу:
- http://localhost:8080/swagger-ui.html

## Безопасность
- Аутентификация через JWT
- Защита от CSRF
- Конфигурация CORS
- Безопасное хранение паролей

## Логирование
Логи приложения сохраняются в директории `logs/uniproject.log`

## Разработка
1. Создайте новую ветку для ваших изменений
2. Внесите изменения
3. Создайте pull request

# MIT License

Copyright (c) 2025 Kudzeri

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
