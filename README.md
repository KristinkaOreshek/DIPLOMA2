# Дипломный проект профессии «Тестировщик»
## Запуск SUT, авто-тестов и генерация репорта
Предусловие : Установлено ПО: Git Bash, IntelliJ IDEA и Docker Desktop, Java 11.

1.  Клонировать репозиторий коммандой Git clone
2.  Запустить Docker Desktop
3.  Открыть проект в IntelliJ IDEA
4.  В терминале в корне проекта запустить контейнеры: docker-compose up --build
5.  Запустить сервис с указанием пути к БД
   * для mysql
java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
* для postgresql
java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

5.  Запустить тесты:
   * для mysql
./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"

* для postgresql ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
6.  Создать отчёт Allure и открыть в браузере: .\gradlew allureServe

7.  Завершить работу:
* остановить работу приложения командой Ctrl+C или закрыть окно терминала, в котором оно было запущено;
* остановить контейнер командой docker-compose down или командой Ctrl+C в окне терминала, в котором он был запущен.
