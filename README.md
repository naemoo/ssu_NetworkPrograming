# Server 기능 
### Login 기능 (RMI 사용 - Serializable을 이용)
### Bolcking 방지를 위한 Multi-Thread 구조와 Multi-Socket 사용(클라이언트 당 Socket 1개)
### 로그인 기능 성공 시 소켓 연결 (SSL Socket 사용)
### 게임 방 만들기, 쪽지보내기, 방 참가하기(Application protocol을 사용)
### 게임 방마다 데이터 처리 및 진행 (RMI + SSL Socket이용) 
####  방마다 PK를 가지는데 PK맞게 RMI 포트주소 생성

# Client 기능
### Login UI (Server에 등록된 아이디가 아니면 접속 불가능 - Server/Client 에서 확인 할 수 있습니다.)
![image](https://user-images.githubusercontent.com/50524321/84813604-29e30100-b04b-11ea-8857-c594658b89c4.png)
### Lobby UI (현재 접속한 User 목록, User목록에서 User 선택 후 쪽지 기능, 방 만들기, 방 참가기능
![image](https://user-images.githubusercontent.com/50524321/84814275-32880700-b04c-11ea-8937-2473626ebe36.png)
### GameRoom UI (채팅 기능, 정답 처리 및 문제 난이도 선택)
##### 1. 기본적인 채팅기능
![image](https://user-images.githubusercontent.com/50524321/84815097-7b8c8b00-b04d-11ea-99e1-fbdf8c510e68.png)
##### 2. 정답 시
![image](https://user-images.githubusercontent.com/50524321/84815250-b8f11880-b04d-11ea-8051-06edc1a000ea.png)
##### 3. 문제 난이도 선택 시 (문제 변함)
![image](https://user-images.githubusercontent.com/50524321/84815514-1b4a1900-b04e-11ea-8f65-34a9d5bb5b82.png)

# 실행 순서
### 1. Server/bin 파일에서 cmd를 킨다.
### 2. start rmiregistry를 실행한다. (RMI 실행을 위해)
### 3. Server/Server 실행
### 4. Client/ProblemClient 실행한다.
