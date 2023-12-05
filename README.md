# MBTI matching social app / M친톡 - MBTI를 이용한 친구 찾기 톡!

## 프로젝트 소개
MBTI 친구 톡의 줄임말인 M친톡은 자신의 MBTI를 검사하고 본인 MBTI와 상성이 잘 맞는 순으로 친구들을 보여주고 그 친구들과 채팅도하고 게시물을 통해 다양한 사람들과 의사소통도 할 수 있는 앱입니다. 

## 프로젝트 상세 

### 개발기간
23.10.10 ~ 23.11.17

### 시연 영상
[영상 보기](https://drive.google.com/file/d/1Kwgsahq4zV414lMSXAA0sujLGkYtqP3u/view)

### 와이어 프레임
![Untitled (1)](https://github.com/limduh/nb_01_introduce_myself/assets/111894739/9001005d-2ce3-464c-8d32-2290286ac69a)
![Untitled (2)](https://github.com/limduh/nb_01_introduce_myself/assets/111894739/22a4a67a-7e2d-4571-8535-8f23e7e3a042)


*[Figma] (https://www.figma.com/file/QXM0DdeFpzehGclrY0fI9p/1?type=design&node-id=0-1&mode=design)

## 주요 기능
### LoginActivity
* google login 기능과 회원가입, 이메일을 통한 비밀번호 찾기 기능

### BottomActivity, UI 디자인
* 전반적인 레이아웃 디자인과 BottomBar를 통한 Fragment 전환과 디자인을 담당
### Friend Find/List Fragment, DetailActivity
* 리스트를 누르면 DetailActivity로 이동, 사용자의 정보와 친구추가, 채팅, 차단기능 표시
* Friend Find 에서 MBTI 알고리즘을 이용해 상성에 따른 하트 갯수를 분배 및 리스트 위치조정
* 친구 추가시 Friend list에 표시
* 친구 차단시 Friend list에서 없어지고 차단 목록에 표시
* 필터 기능을 통한 원하는 친구를 필터링
* 필터가 되지않으면 검색 결과가 없음 이미지 출력
* 친구 리스트에 친구가 없으면 친구 없음 이미지 출력
### PostFragment
* 검색기능을 통해 원하는 게시물 검색 가능
* 게시물 수정시 게시물 내용과 이미지 수정 가능
* 필터 기능을 통해 좋아요한 게시물과 나의 게시물, 모든 게시물을 필터링
* 게시물 프로필을 누르면 상세 페이지로 이동
### ChatFragment
* 채팅방을 만들어서 1대1 채팅 가능
* 실시간 채팅 기능, 시간 표시
* 마지막으로 채팅한 기록을 리스트에 표시
### MyproflieFragment
* 내 사용자 정보 출력
* MBTI 정보 수정/검사 기능
* 차단한 사용자 목록 관리
* 회원탈퇴, 로그아웃 기능 

## 
![Service-2](https://github.com/6pleasant-MBTITalk/MBTI_Talk/assets/139088072/542b1455-bd8f-4994-896a-9b25621db252)

