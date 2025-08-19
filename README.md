# 🛒 자영업자모으자 자자자

## 프로젝트 소개

- 자영업자모으자는 자영업자를 위한 **팀구매형 이커머스 서비스**입니다.
- 자영업자들이 영업에 필요한 상품만을 합리적인 가격으로 물건을 구매할 수 있습니다.
- 리스크가 적은 공동구매가 가능하며, 개별 구매도 가능합니다.
- 업종별 카테고리를 통한 맞춤형 상품 큐레이션을 제공합니다.


## 기술 스택

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security (OAuth2)
- Spring Data JPA
- QueryDSL
- MySQL 8.0
- Redis

### Infrastructure & DevOps
- AWS S3 (이미지 저장)
- Toss Payments (결제 시스템)

### 주요 라이브러리
- JWT 토큰 인증
- Swagger (API 문서화)
- SSE (실시간 알림)


## 프로젝트 구조

```
src/main/java/com/jajaja/
├── domain/
│   ├── auth/          # 인증/인가
│   ├── cart/          # 장바구니
│   ├── coupon/        # 쿠폰
│   ├── delivery/      # 배송지
│   ├── member/        # 회원
│   ├── notification/  # 알림
│   ├── order/         # 주문
│   ├── point/         # 포인트
│   ├── product/       # 상품
│   ├── review/        # 리뷰
│   ├── search/        # 검색
│   └── team/          # 팀구매
├── global/
│   ├── config/        # 설정
│   ├── security/      # 보안
│   ├── scheduler/     # 스케줄러
│   └── S3/           # S3 설정
└── Application.java
```


## 시스템 아키텍처
<img width="1095" height="528" alt="architecture" src="https://github.com/user-attachments/assets/bae2a6a9-f726-4683-bec8-b3d1346f01ec" />


## 브랜치 전략

### Git Flow 전략
- `main`: 운영 배포 브랜치
- `dev`: 개발 통합 브랜치
- `feat/#이슈번호-기능명`: 기능 개발 브랜치
- `fix/#이슈번호-수정사항`: 긴급 수정 브랜치
- `refactor/#이슈번호-수정사항`: 긴급 수정 브랜치

### 커밋 컨벤션
```
✨ feat: 새로운 기능 추가
🐛 fix: 버그 수정
📚 docs: 문서 추가, 수정, 삭제
🧪 test: 테스트 코드, 리팩토링 테스트 코드 추가
💄 style: 코드 포맷팅, 세미콜론 누락, 코드 변경 없음
♻️ refactor: 코드 리팩토링
⚙️ setting: 개발 환경 설정 관련 변경
🚀 deploy: 배포 관련 변경사항
🧹 chore: 빌드 업무 수정, 패키지 매니저 수정
```



## 팀원 구성

| 이름                                                                                                                                               | 역할      | 담당 기능                          |
|--------------------------------------------------------------------------------------------------------------------------------------------------|-------------|------------------------------------|
| [<img src="https://avatars.githubusercontent.com/u/96182623?v=4" height=130 width=130> <br/> @tl1l1l1s](https://github.com/tl1l1l1s) **신윤지**     | 백엔드 팀장  | Cart, Coupon, Delivery               |
| [<img src="https://avatars.githubusercontent.com/u/51525934?v=4" height=130 width=130> <br/> @floreo1242](https://github.com/floreo1242) **윤진수** | 백엔드 팀원  | Auth, Order, Point                       |
| [<img src="https://avatars.githubusercontent.com/u/129031670?v=4" height=130 width=130> <br/> @munjji](https://github.com/munjji) **이지희**        | 백엔드 팀원  | Product(Detail), Review, Team, S3, CI/CD |
| [<img src="https://avatars.githubusercontent.com/u/126665882?v=4" height=130 width=130> <br/> @Yunji-Yun](https://github.com/Yunji-Yun) **윤윤지**  | 백엔드 팀원   | Product(Home), Notification, Search      |
| [<img src="https://avatars.githubusercontent.com/u/145523888?v=4" height=130 width=130> <br/> @soooong7](https://github.com/soooong7) **이송미**    | 백엔드 팀원   | Product(Category), Review, Team          |

