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
```
{
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

## Product Category API
1. Просмотр продуктовых категорий (GET)<br>
   http://localhost:8080/product-categories/all
2. Просмотр одной категории (GET)<br>
   http://localhost:8080/product-categories/get?id=<id>
3. Создание новой категории (POST)<br>
   Должен быть админом<br>
   http://localhost:8080/product-categories/create?name=<name>
4. Обновление названия категории (POST)<br>
   Должен быть админом<br>
   http://localhost:8080/product-categories/update?name=<name>&id=<id>
5. Удаление категории (DELETE)<br>
   Должен быть админом<br>
   http://localhost:8080/product-categories/delete?id=<id>

## Product API
1. Просмотр товаров<br>
http://localhost:8080/products (GET)<br>
Опционально можно передать параметры pageNo perPage<br> http://localhost:8080/products?pageNo=<номер_страницы>&perPage=<кол-во_элемнтов_на_странице> (GET)
2. Просмотр одного товара<br>
   http://localhost:8080/products/one?id=<id_товара> (GET)<br>
3. Поиск товаров<br>
   http://localhost:8080/products/search?searchName=<name> (GET)
4. Фильтр по категории<br>
   http://localhost:8080/products/category?categoryId=<номер_категории> (GET)<br>
   Опционально можно передать параметры pageNo perPage <br>http://localhost:8080/products/category?categoryId=<номер_категории>&pageNo=<номер_страницы>&perPage=<кол-во_элемнтов_на_странице> (GET)
5. Создание товара<br>
http://localhost:8080/products/create (POST)<br>Должен быть админом<br>
```
{
    "sku": "123123",
    "shortName": "test",
    "description": "test",
    "unitPrice": 123500,
    "imageUrl": "tets.jpg",
    "active": true,
    "unitsInActiveStock": 5,
    "unitsInReserve": 0,
    "category": 1
}
```
4. Удаление товара<br>http://localhost:8080/products/delete?id=<id_товара> (DELETE)<br>Должен быть админом<br>
5. Обновлнение мастер-данных товара<br>
   http://localhost:8080/products/update-data (POST)<br>Должен быть админом<br>
```
{   
    "id": <id_товара>,
    "sku": "testSKUUpd",
    "shortName": "testUpd",
    "description": "testUpd",
    "imageUrl": "tets_.jpg"
}
```
6. Обновление цены<br>
   http://localhost:8080/products/update-price?id=<id_товара>&price=<новая_цена> (POST)<br>Должен быть админом<br>

## API корзины
1. Добавление товара в корзину<br>
http://localhost:8080/orders/product/add
```
{
    "id":<id_товара>,
    "price": <цена_товара>,
    "quantity": <кол-во>
}
```
В этот момет произойдет несколько вещей:
- Будет создан Cookie cartId, который будет использован наравне с логином (если он есть) при создании или поиске заказа
- Будет создан или найден заказ по Cookie cartId или имени пользоваля
- В случае, если пользователь создал корзину под своим логином, затем зашел с другого устройства, не залогинился и создал новую корзину, то после логина, старый заказ передет в статус CANCELLED
- Кол-во товаров, которые были добавлены в корзину будут перемещены в резерв таблицы с товарами (и уйдут из доступности для заказа)
2. Просмотр товаров в своей корзине<br>http://localhost:8080/orders/my-cart
3. Уменьшение кол-ва товара<br>http://localhost:8080/orders/product/decrease?productId=<id_товара>&decreaseAmount=<кол-во_товара> <br>
При этом, кол-во decreaseAmount уменьшит резерв товара
4. Удаление товара из корзины кол-ва товара<br>http://localhost:8080/orders/product/delete?productId=<id_товара> <br>
   При этом, отмененное кол-во уменьшит резерв и увеличит активный сток
5. Перевод корзины в статус CANCELLED<br>http://localhost:8080/orders/delete?order=<номер_заказа> <br>Должен быть админом<br>

