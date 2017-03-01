# scissors-02-2017
#### RWS - многопользовательский шутер

#### Команда:
* Байрамуков Ян
* Злобина Светлана
* Кучаева Карина

#### API:
 1. регистрация 
 
 /api/signup POST 
 
 body: {login, email, password}
 
 2. данные пользователя
 
 /api/user/{login} GET 

 3. изменение данных пользователя
 
 /api/user/{login} POST
 
 body: {email, password}

 4. логин пользователя
 
 /api/session POST
 
 body: {login, password}

 5. логаут
 
 /api/session DELETE 

 6. пользователь текущей сессии
 
 /api/session GET 
