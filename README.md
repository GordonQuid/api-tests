# Практика: написание авто-теста с нуля

## Требования для запуска:
- Java 17
- Maven
- Google Chrome

## Запуск

Из корневой директории проекта выполнить команду:
```
mvn clean test
```

По умолчанию тесты запускаются на `https://fakerestapi.azurewebsites.net`.
Чтобы указать другой URL:
```
mvn clean test -Dbase.url=https://dev.fakerestapi.azurewebsites.net
```