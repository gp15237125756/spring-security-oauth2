# spring-security-oauth2

the demo shows the usage for spring-security-oauth2.While I wrote these codes,it's 2020/7/1,the  Spring Security OAuth project is deprecated.
The latest OAuth 2.0 support is provided by Spring Security. See the OAuth 2.0 Migration Guide for further details.
The demo demonstrates how spring-security-oauth2 works,it will definitely help you out whatsoever,especially you are going to put Spring Security OAuth
into your project.

# clarify some concepts

oauth2 generally has four modes.In the demo,either client credentials mode or password mode works well.Further more,that two modes are most popular in
real world.Also,the JWTTokenStore and RedisTokenStore are both supported here.

# test steps

1. get access_token(password mode)

curl -X  -d '{"username": "admin", "password":"123", "grant_type":"password", "client_id":"app", "scope":"user_info", "client_secret":"1234$#@!"}' \
POST http://localhost:8080//oauth/token

response :

{
"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTM1OTE1NjgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiJdLCJqdGkiOiI3M2VhNjIzNy1iNmJkLTQyNWUtYTM2OS1kZjU0MTA0ZjdmZmMiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJ1c2VyX2luZm8iXX0.Rdk8388lahyGuqs0V13NDWJqMKeNpW-pC3dXpS7vRh8",
"token_type":"bearer",
"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInVzZXJfaW5mbyJdLCJhdGkiOiI3M2VhNjIzNy1iNmJkLTQyNWUtYTM2OS1kZjU0MTA0ZjdmZmMiLCJleHAiOjE1OTM4MzEyNjgsImF1dGhvcml0aWVzIjpbIlJPTEVfYWRtaW4iXSwianRpIjoiY2ZlZjE2ZmEtNTc0Mi00MGI1LWI1MGYtMDRkNTBlZjE2YTZkIiwiY2xpZW50X2lkIjoiYXBwIn0.18pDWASzodsTsC3UNeNWwPqhreEBI48ZPtE9c2FkTJU",
"expires_in":299,
"scope":"user_info",
"jti":"73ea6237-b6bd-425e-a369-df54104f7ffc"
}

2. access api(password mode)

open the browser,then input http://localhost:8080/admin/hello/1?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTM1OTE1NjgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiJdLCJqdGkiOiI3M2VhNjIzNy1iNmJkLTQyNWUtYTM2OS1kZjU0MTA0ZjdmZmMiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJ1c2VyX2luZm8iXX0.Rdk8388lahyGuqs0V13NDWJqMKeNpW-pC3dXpS7vRh8
you can also pass the bearer access_token in headers (Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTM1OTE1NjgsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiJdLCJqdGkiOiI3M2VhNjIzNy1iNmJkLTQyNWUtYTM2OS1kZjU0MTA0ZjdmZmMiLCJjbGllbnRfaWQiOiJhcHAiLCJzY29wZSI6WyJ1c2VyX2luZm8iXX0.Rdk8388lahyGuqs0V13NDWJqMKeNpW-pC3dXpS7vRh8)

if you choose client credentials mode,only grant_type、 scope、clientId、secret are required.
