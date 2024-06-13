### APIs

GET http://localhost:8080/api/v1/ => Home

GET http://localhost:8080/api/v1/login => Login

POST http://localhost:8080/api/v1/register => Register

* {
  "name": "Ana",
  "email": "test@example.com",
  "password": "12345abc"
}

GET http://localhost:8080/api/v1/confirm?token={token} => Token confirmed


