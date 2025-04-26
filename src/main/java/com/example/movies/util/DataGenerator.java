package com.example.movies.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {
        "Александр", "Анна", "Дмитрий", "Екатерина", "Михаил", "Мария", "Иван", "Елена",
        "Сергей", "Ольга", "Андрей", "Наталья", "Алексей", "Татьяна", "Владимир", "Юлия",
        "Павел", "Ирина", "Николай", "Светлана", "Артем", "Анастасия", "Максим", "Виктория"
    };
    
    private static final String[] LAST_NAMES = {
        "Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов", "Васильев", "Попов", "Соколов",
        "Михайлов", "Новиков", "Федоров", "Морозов", "Волков", "Алексеев", "Лебедев", "Семенов",
        "Егоров", "Павлов", "Козлов", "Степанов", "Николаев", "Орлов", "Андреев", "Макаров"
    };
    
    private static final String[] COURSE_TITLES = {
        "Основы программирования", "Веб-разработка", "Мобильная разработка", "Базы данных",
        "Алгоритмы и структуры данных", "Машинное обучение", "Кибербезопасность", "DevOps",
        "Тестирование ПО", "UI/UX дизайн", "Cloud Computing", "Blockchain технологии",
        "Искусственный интеллект", "Big Data", "Game Development", "Computer Networks",
        "Software Architecture", "Agile методологии", "Data Science", "Computer Vision"
    };
    
    private static final String[] COURSE_DESCRIPTIONS = {
        "Изучение фундаментальных принципов программирования и разработки ПО",
        "Создание современных веб-приложений с использованием популярных фреймворков",
        "Разработка мобильных приложений для iOS и Android",
        "Проектирование и оптимизация реляционных и NoSQL баз данных",
        "Изучение эффективных алгоритмов и структур данных для решения сложных задач",
        "Основы машинного обучения и нейронных сетей",
        "Защита информации и систем от киберугроз",
        "Автоматизация процессов разработки и развертывания",
        "Методологии и инструменты тестирования программного обеспечения",
        "Создание удобных и привлекательных пользовательских интерфейсов"
    };
    
    private static final String[] NEWS_TITLES = {
        "Новые технологии в образовании", "Старт нового учебного года", "Итоги хакатона",
        "Встреча с экспертами индустрии", "Новые курсы в программе", "Успехи наших студентов",
        "Международное сотрудничество", "Обновление учебных программ", "Новые преподаватели",
        "Студенческие проекты", "Научные исследования", "Карьерные возможности"
    };
    
    private static final String[] NEWS_CONTENTS = {
        "Мы рады представить новые образовательные технологии, которые помогут студентам в обучении",
        "Традиционное начало учебного года прошло с большим успехом",
        "Наши студенты показали отличные результаты на международном хакатоне",
        "Встреча с ведущими экспертами индустрии открыла новые перспективы для студентов",
        "Расширяем программу обучения новыми актуальными курсами",
        "Наши выпускники успешно трудоустраиваются в ведущие IT-компании",
        "Заключены новые партнерские соглашения с зарубежными университетами",
        "Учебные программы обновлены с учетом последних тенденций в IT",
        "К нам присоединились новые опытные преподаватели",
        "Студенческие проекты получают признание на международных конкурсах"
    };
    
    private static final String[] EVENT_TITLES = {
        "День открытых дверей", "Хакатон", "Конференция разработчиков", "Воркшоп",
        "Мастер-класс", "Семинар", "Круглый стол", "Презентация проектов",
        "Встреча с работодателями", "Карьерный форум", "Технический митап",
        "Студенческая конференция", "IT-фестиваль", "Курсы повышения квалификации"
    };
    
    private static final String[] EVENT_DESCRIPTIONS = {
        "Приглашаем всех желающих познакомиться с нашей образовательной программой",
        "24-часовой марафон программирования для студентов и профессионалов",
        "Ежегодная конференция, посвященная последним тенденциям в разработке",
        "Практический воркшоп по современным технологиям разработки",
        "Экспертный мастер-класс от ведущих специалистов индустрии",
        "Образовательный семинар по актуальным темам IT",
        "Обсуждение важных вопросов развития IT-образования",
        "Презентация лучших студенческих проектов",
        "Встреча с представителями ведущих IT-компаний",
        "Форум карьерных возможностей для студентов и выпускников"
    };

    public static String generateName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + " " + 
               LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public static String generateEmail(String name) {
        String[] parts = name.toLowerCase().split(" ");
        return parts[0] + "." + parts[1] + random.nextInt(1000) + "@example.com";
    }

    public static String generateCourseTitle() {
        return COURSE_TITLES[random.nextInt(COURSE_TITLES.length)];
    }

    public static String generateCourseDescription() {
        return COURSE_DESCRIPTIONS[random.nextInt(COURSE_DESCRIPTIONS.length)];
    }

    public static String generateNewsTitle() {
        return NEWS_TITLES[random.nextInt(NEWS_TITLES.length)];
    }

    public static String generateNewsContent() {
        return NEWS_CONTENTS[random.nextInt(NEWS_CONTENTS.length)];
    }

    public static String generateEventTitle() {
        return EVENT_TITLES[random.nextInt(EVENT_TITLES.length)];
    }

    public static String generateEventDescription() {
        return EVENT_DESCRIPTIONS[random.nextInt(EVENT_DESCRIPTIONS.length)];
    }

    public static LocalDateTime generateRandomDateTime() {
        LocalDate date = LocalDate.now().plusDays(random.nextInt(365));
        LocalTime time = LocalTime.of(random.nextInt(24), random.nextInt(60));
        return LocalDateTime.of(date, time);
    }

    public static String generateRandomLocation() {
        String[] locations = {
            "Аудитория 101", "Аудитория 201", "Аудитория 301", "Конференц-зал",
            "Актовый зал", "Компьютерный класс 1", "Компьютерный класс 2",
            "Лекционный зал", "Коворкинг", "Библиотека"
        };
        return locations[random.nextInt(locations.length)];
    }
} 