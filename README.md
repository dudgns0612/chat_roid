# ChatRoid(Chatterbot)
  심심이프로그램을 모태로 만든 학습을 통한 컴퓨터와 사용자간의 채팅프로그램
  
## 개발언어
JAVA
  
##개발도구
  Java SE Development Kit 8, Eclipse, JavaFX Scene Builder 2.0, cnuma형태소분석기
  >###Download
    -cnuma형태소분석기 교수제공
    -JavaFX Scene Builder
      http://www.oracle.com/technetwork/java/javafxscenebuilder-1x-archive-2199384.html

## 시스템구성
### 기본설정
     tools.Statics.java : UI(fmxl)파일의 경로 및 필요한 Static자원으로 구성
     Files.FileStatics.java : file을 전송하기위한 소켓통신에 필요한 정보로 구성

### 통신
     tools.NetworkProtocols.java : 모든 Network통신을 위한 프로토콜과 서버소켓 연결정보로 구성 

### 서버
    FXServerControllers.ServerMainUicontroller.java : 클라이언트와 약속된 프로토콜을 이용하며 원하는 답을 전달함
    Files.FileServer.java : 클라이언트 학습 요청 시 연결되어 요청 받은 일을 수행함.

### 도구 
    tools.Worddivide.java : 학습을 위하여 클라이언트로부터 받은 문장을 알맞게 나눔.
    tools.Batchhandler.java : 형태소분석을 위한 배치생성 및 실행함.

### 학습   
    Study.MachineStudy.java : 서버로부터 전달받은 자원을 형태에 알맞게 정리하여 학습함.
    Study.Discriminatar.java : 학습시 욕설이나 비정상적인 단어들을 파악하여 알림.
  
### 커뮤니케이션
    Communication.WeightCalculation.java : tf-idf방식을 이용하여 각 단어의 가중치를 구하여 클라이언트가 원하는 가장 유사한 답을 구함.
    Communication.SelectWord.java : 학습된 자원을 점수를 계산하여 사용자가 원하는 대답을 유추하여 서버로 보내줌

### UI
    tools.Scenecontroll.java : 실행하는 동안에 필요에 따라 화면전환, 컨트롤러 정보 전달등 UI설정을 수행함
    tools.CustomDialog.java : 실행하는 동안에 전달,입력,경고등 필요에 따라 사용 하는 다이얼로그
    resoures, Ui : 각 ui에 필요한 이미지 / 아이콘 / fxml파일들을 담고있는 것

## 실행방법
   1. Server.ServerStart.java실행 On버튼 시작후 FileServer On버튼 실행
   2. Client.Start.java실행 후 서버 호스트주소 입력
   3. 정확한 서버정보 입력 후 Start버튼 실행 Chatroid와 대화
   4. 원하는 대답이 안나올 경우 되돌아가기 버튼을 실행 후 문장이나 파일로 원하는 대화학습
   5. 텍스트파일로 클라이언트가 학습할 시 서버에서 알맞은 파일인지 확인 후 학습
   5. Chatroid의 대화
