## 기술 스택

<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&style=for-the-badge&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA">
  <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/Oracle Cloud-F80000?style=for-the-badge&logo=oracle&logoColor=white" alt="Oracle Cloud">
</p>

<br>

## 시스템 아키텍처

<img src="https://github.com/user-attachments/assets/4f2c021e-107e-414b-b52b-6f18a61df527" width="700" alt="System Architecture">

<br><br>

## ERD

<img src="https://github.com/user-attachments/assets/7d871a5f-bb2b-42b9-84da-8bd5a6dfe445" width="700" alt="ERD">

<br><br>

## 모듈 구조

```
subport/
├── api/
├── batch/
├── admin/
├── domain/
├── common/
```

- `api`
    - 실서비스 API 제공
- `batch`
    - 스케줄링 작업 관리 (구독 결제일 업데이트 및 소비 내역 생성, 결제일 알림 이메일 발송)
- `admin`
    - 대시보드 및 운영 데이터 관리 API 제공
- `domain`
    - 엔티티 클래스 관리 (+ @EnableJpaAuditing 적용)
- `common`
    - 공통 예외, 에러 코드, DTO, 상수 관리