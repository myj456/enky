# JWT 토큰 기반 로그인 및 회원가입 기능 구현
Spring Boot + MySQL + Spring Sequrity + Jwt 기반 회원 관리 시스템입니다.

<br>

## 개요
- 회원가입 및 로그인
- JWT 토큰을 활용한 인증/인가
- 사용자 권한 관리
- Security 보안 적용

<br>
<br>

## API 설계 및 명세
| Method | Endpotin | Content |
|----|-----|-----|
| POST | /join | 회원가입 |
| POST | /login | 로그인 |
| GET | /admin | 테스트용 |

### 회원가입 <br>
설명: 새 사용자를 추가합니다.

- 요청 예시:
````
{
    "username": "아이디",
    "password": "비밀번호",
    "nickname": "닉네임"
}
````

<br>

- 응답 예시:
````
"회원가입이 완료되었습니다."
````

<br>
<br>

### 로그인 <br>
설명: 사용자 인증 후 JWT 토큰을 발급합니다.

- 요청 예시:
````
{
    "username": "아이디",
    "password": "비밀번호",
}
````

<br>

- 응답 헤더 예시:
````
Authorization: Bearer {access_token}
````

<br>
<br>

### 테스트 <br>
설명: 사용자 인증 테스트입니다. <br>
권한: ADMIN

- 헤더:
````
Authorization: Bearer {access_token}
````

<br>

- 응답 예시:
````
Main Controller username: {username}, role: {role}
````

<br>
<br>
  
## 개발하면서 발생한 문제 및 해결
### 1. jwt 만료일 설정
- **문제점**
    1. 서버의 시간대가 UTC로 설정이 되어 있어 만료기간이 9시간 차이가 나는 문제가 발생했음.
    2. jwt 토큰 생성 시 만료일이 60*60*10L(36초) 후로 설정되어 있었음.
- **해결**
    1. application.properties에서 timezone을 Asia/Seoul로 설정을 함.
    2. 60*60*10L에서 60*60*10*1000L(10시간)으로 번경함. -> 추후엔 1시간으로 변경할 예정.

### 2. Security 권한 인증 문제
- **문제점**
    1. Security에서 hasAnyRole() 메소드 사용했지만, role값에 앞에 'ROLE_'을 붙이지 않아 권한 인증이 안됐었음.
- **해결**
    1. role값 앞에 'ROLE_'붙인 후 유저 값을 저장하도록 수정함.
 

<br>
<br>
