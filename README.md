# e-commerce-backend
Java Spring Boot E-Commerce Backend for Learning Purposes

## Как запустить приложение?
Docker-compose создаст необходимые контейнеры<br>
Порт для jar — 8080

## User API
1. Flyway создаст стандартного админа. С ним доступен доступ в любое приложение
   1. login: admin@admin.ru
   2. password: secretPassword

2. Регистрация нового пользователя http://localhost:8080/registration/signup (POST)<br>
Передайте JSON:
```
{
   "firstName": "firstName",
   "lastName": "lastName",
   "email": "email@gmail.com",
   "password": "password"
}
```
Вам в ответ придет токен:
```
{
    "confirmationToken": "token"
}
```

3. Подтверждение нового пользователя
Скопируйте токен<br>
Подтвердите пользователя по ссылке http://localhost:8080/registration/user-confirm?token=<token> <br>
В ответ придет окен пользователя <email> подтвержден
4. Залогиниться<br>
http://localhost:8080/login
<br> Через x-www-form-urlencode передать:
```
username: email@gmail.com
password: password
```
В ответ придет JWT вида:
```
{
    "accessToken": "JWTaccessToken",
    "refreshToken": "JWTrefreshToken"
}
```
5. Авторизация<br>
Авторизация проходит через Header:
```Authorization: Bearer JWTaccessToken```
6. Просмотр информации о себе
   http://localhost:8080/users/self <br>
Ответ:
```{
    "firstName": "firstName",
    "lastName": "lastName",
    "userRole": "USER",
    "email": "email@gmail.com"
}
```
7. Изменение своего пароля<br>
   http://localhost:8080/users/self/change-password
   В POST надо передать JSON:<br>
```
{
"oldPassword": "password",
"newPassword": "password1"
}
```
8. Сделать пользователя админом

Для того, чтобы сделать пользователя админом, нужно быть админом
http://localhost:8080/users/make-admin?username=<почта пользователя>

9. Просмотр всех пользователей

Нужно быть админом<br>
http://localhost:8080/users/all

