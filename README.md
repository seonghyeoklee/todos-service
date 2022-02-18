## 백엔드 개발 과제 - TODO

## 프로젝트 구성
- Project `Gradle Project`
- Language `Java`
- `Spring boot 2.6.3`
- Packaging `Jar`
- `Java 11`
- Dependencies
    - `Spring boot`, `Spring security`, `Spring Data Jpa`, `H2`, `lombok`, `validation`

## 기능 요구사항

| method | endpoint         | 기능       | 권한   |
|--------|------------------|----------|------|
| GET    | /todos           | 일정 전체 조회 | USER |
| GET    | /todos/{todosId} | 일정 조회    | USER |
| POST   | /todos           | 일정 생성    | USER |
| PUT    | /todos/{todosId} | 일정 수정    | USER |
| DELETE | /todos/{todosId} | 일정 삭제    | USER |
| POST   | /signup          | 회원가입     | 없음   |
| POST   | /login           | 로그인      | 없음   |

Todos RestApi 모두 구현되어 있습니다. 추가적으로 작업되어 있는 것은 Spring Security 를 이용한 JWT 인증방식입니다.
우선 사용자 권한을 나누어 일정관리에 필요한 권한을 설정했고, 로그인된 사용자만 일정관리 기능을 사용할 수 있습니다.
그리고 h2 데이터베이스를 사용하여 쉽게 테스트 가능하도록 구성하였습니다.

읽기 쉽고 이해하기 쉽게 코드를 작성하려고 노력했습니다. 잘못 작성된 부분이 있다면 피드백 주셔도 좋고, 코드에 대한 리뷰도 너무 좋을 것 같습니다.

감사합니다.