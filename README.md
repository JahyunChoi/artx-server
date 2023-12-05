## 프로젝트
누구나 쉽게 미술품을 사고 팔 수 있는 커머스 프로젝트입니다.

## 프로젝트 구성
![Group 119](https://github.com/dlworhd/artx-server/assets/102597172/c0c13363-938e-465b-a016-e84d1c771f9b)

## ERD 설계
![market](https://github.com/m-artx/artx-server/assets/102597172/8edabaa3-9afb-4d33-9ae0-adcb5f150eab)

## 요구사항 명세서

1. Spring WAS Server(Java/Spring, JPA Hibernetes, Redis DB)

### 회원 도메인
  
  1)   [x] 회원가입/회원탈퇴/로그인/로그아웃할 수 있습니다.
       - [x] 회원가입 시 이메일, 아이디 중복 및 유저 중복 재차 검증할 수 있습니다.
       - [x] 모든 패스워드는 Spring Security Password Encoder에 의해 암호화가 됩니다.
       - [x] 로그인 화면에서 아이디 찾기, 비밀번호 초기화할 수 있습니다.
       - [x] 로그인 시 JWT Access Token 및 Refresh Token(HTTP Only Cookie)을 제공하여 Stateless 상태를 유지합니다.
       - [x] JWT Token 유효기간 만료시 Reissue API를 통해 재발급할 수 있습니다.
  2)   [x] 마이페이지를 통해 정보 수정할 수 있습니다.
       - [x] 프로필 이미지 등록 및 변경
       - [x] 패스워드, 주소지 등록, 변경 및 기본 주소지 변경
  3)   [x] 모든 유저는 권한을 부여받습니다.
       - [x] 모든 API는 Spring Seuciry 설정과 함께 인가 및 권한을 거칩니다.
       - [x] 권한별로 회원을 조회할 수 있습니다.(USER, ARTIST, ADMIN)
       - [x] USER와 ARTIST간 권한 신청 및 조회할 수 있습니다.(ARTIST로 권한 신청 시 작품 이미지 2개 필수 첨부)
  4)   문의 버튼을 눈에 잘 띄는 곳에 배치한 후 고객과 상담원의 적극적인 커뮤니케이션을 유도할 수 있도록 하였습니다

 ### 작품 도메인

  1)   [x] 모든 작가는 작품을 등록/수정/삭제할 수 있습니다.
       - [x] 작품 삭제는 Soft Delete 방식(isDeleted)을 사용하며 실제로 삭제하지 않습니다.
       - [x] 작품 이미지는 개별 등록을 하며 서버에 최초 저장 시 Prefix("Temp\_")를 적용하여 저장이 되고 작품 등록 시Prefix("Product_")를 변경하여 별도의 디렉토리에 저장하게 됩니다.
  2)   [x] 모든 회원은 작품을 조회할 수 있습니다.
       - [x] 작품 조회는 카테고리/유저 및 작품명으로 페이지 조회(Pagination)할 수 있습니다.
       - [x] 신규 작가 및 인기 작가의 작품 조회할 수 있습니다.
       - [x] 작품 조회시 작품 조회수 카운트가 Redis에 누적이 되며 Scheduling을 통해 일괄적으로 적용시킵니다.

 ### 주문 도메인

  1)   [x] 모든 사용자는 작품을 주문할 수 있습니다.
       - [x] 재고 상태 변경의 동시성 문제를 해결하기 위해 Optimisstic Lock을 적용합니다.(버전 관리를 통해 충돌을 감지하며 예외를 발생시키고 정합성을 보장합니다.)
  2)   [x] 주문 상태를 관리합니다.(ORDER_READY, ORDER_SUCCESS, ORDER_APPROVAL, ORDER_FAILURE, ORDER_CANCEL)
  3)   [x] 최근 주문 날짜를 기준으로 조회를 할 수 있습니다. (총 주문액, 주문명, 개별 상품 정보 등 반환)
  4)   [x] 주문 상태, 배송 상태에 따라 취소 가능 여부가 결정됩니다
       - [x] 주문은 ORDER_READY, ORDER_SUCCESS의 경우에만 취소가 가능합니다.
       - [x] 배송은 DELIVERY_READY의 경우에만 취소가 가능합니다.
  5.   [x] 모든 작가는 주문된 상품들을 조회할 수 있습니다.

 ### 결제 도메인

  1)   [x] 현재 모든 결제는 카카오페이를 통해 이루어집니다.
  2)   [x] Krampolin IDE 환경 제약으로 인해 프록시 서버를 통하여 결제를 요청합니다.
  3)   [x] 결제 상태를 관리합니다.(PAYMENT_READY, PAYMENT_SUCCESS, PAYMENT_FAILURE, PAYMENT_CANCELD)
  4)   [x] 결제 취소는 주문, 배송 상태에 따라 부분 취소 및 전체 취소가 가능합니다.
       - [x] 주문은 ORDER_READY, ORDER_SUCCESS의 경우에만 취소 가능
       - [x] 배송은 DELIVERY_READY의 경우에만 취소 가능

 ### 관리자 도메인

  1) [x] 통계 정보를 확인할 수 있습니다.
       - [x] 일간, 월간, 연간 주문 총 카운트 및 증감 카운트
       - [x] 새로운 유저 및 신규 전환 작가
  2) [x] 배너를 등록할 수 있습니다.
  3) [x] 권한 요청을 조회하고 승인 및 거절할 수 있습니다.
  4) [x] 카테고리를 생성할 수 있습니다.
  5) [x] 공지사항 게시글을 생성할 수 있습니다.

### 이메일 도메인
  1) [x] Krampolin IDE 환경 제약으로 인해 Spring WAS 서버에서 요청을 받아 처리합니다.  
  2) [x] Google에서 제공하는 SMTP 프로토콜 서비스를 이용하여 이메일을 전송할 수 있습니다.
  3) [x] 인증을 위한 이메일을 전송할 수 있습니다.
  4) [x] 패스워드 초기화 시 새로운 패스워드를 만들어 이메일을 전송할 수 있습니다.

