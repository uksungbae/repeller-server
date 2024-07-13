# 조수퇴치 서버

# 로그인 인증,인가 

Spring Security기반으로 개발되었습니다 

JWT 를 활용한 인증, 인가를 진행합니다 

토큰은 총 두종류로 구성되어 있습니다
uri 별 인증, 인가를 위한  AccessToken과 
만료기간이 짧은 accessToken을 재발급 받기 위한 RefreshToken이 존재합니다 

컨트롤러 접근전 Filter 코드는 common/config/filter/JwtTokenFilter.java 에서 확인할 수 있습니다

# CI/CD 

Github Actions를 활용하여 CI/CD를 진행합니다
-> .github/workflows/backend-ci.yml

현재 서버 컴퓨터에 SSH로 접근이 안되어서
ssh로 접근하여 배포하는 방식은 주석되어있는 상태입니다. 

docker hub에 push를 하여 서버에서 shell script 로 실행되는 로직을 최종적으로 생각하고 있습니다. 

-> 현재 서버 컴퓨터는 parsec을 이용하여 접근하여서 직접 shell script를 실행하는 방식으로 업데이트하고 있습니다.

# DB

현재 DB는 H2로 일단 실행을 시키고있습니다 추후에 Mysql 또는 PostgreSql로 변경할 예정입니다

# API 명세서 

http://222.116.135.206:8080/docs/index.html 로 접속하면 API 명세서를 확인할 수 있습니다

# 작업 필요한 부분 
- SSE를 활용하여 서버에서 -> 게이트웨이로 데이터를 전송하여 기기별 퇴치 메소드를 실행시키는 코드를 추가할 예정입니다
- 현재 퇴치 소리관련 api가 작성되지 않는 상태입니다. 퇴치소리를 저장하고 학습을 시켜도 서버에서 퇴치 기기로 소리를 전달하는 부분이 아직 확정되지 않아서 설계 완료후 진행할 예정입니다.
