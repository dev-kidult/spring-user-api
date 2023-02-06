# Spring boot kotlin, with Security, User API



## Getting started

- Local redis is needed. if you have other redis server ,set up the
  properties ``application.yaml`` -> ``spring.redis.host``, ``spring.redis.port``

- And then run the application ``./gradle bootJar``

- Users are saved when run the application. Refer ``devkidult.git.springuserapi.component``

## APIs

example for example.http
- ``verify-code`` API must be run before ``/api/auth/sign-up`` and ``/api/auth/new-password`` API 
