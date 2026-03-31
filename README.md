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

## 기술 및 아키텍처 선택 이유

### Java 17

- Record를 활용해 보일러플레이트 코드를 줄이고, 텍스트 블록과 개선된 switch 문을 활용해 코드 가독성을 높이기 위해 Java 17을 선택했습니다.
- 추후 batch 모듈처럼 I/O 바운드 작업이 많은 영역에서는 필요에 따라 Java 21로 전환해 가상 스레드 도입을 고려하고 있습니다.

### Docker

- 서버에 실행 환경을 직접 설치·관리하는 번거로움을 없애고, 어디서든 동일한 환경에서 실행할 수 있도록 Docker를 선택했습니다.
- 컨테이너 단위로 격리되어 있어 특정 모듈에 문제가 생겨도 다른 모듈에 영향을 주지 않으며 해당 컨테이너만 재시작할 수 있습니다.
- `api`·`batch`·`admin` 각 모듈을 별도 컨테이너로 운영하며 멀티 모듈 구조와 맞물려 모듈 단위 독립 배포를 가능하게 합니다.

### OCI (Oracle Cloud Infrastructure)

- AWS 프리티어 정책 변경 대안으로 Oracle Cloud Free Tier를 선택했습니다. (상시 무료 클라우드 서비스)
- 컴퓨팅 인스턴스, MySQL HeatWave, 오브젝트 스토리지, 이메일 딜리버리 등 실서비스 운영에 필요한 대부분의 인프라를 별도 비용 없이 구성할 수 있습니다.

### 멀티 모듈 독립 배포 구조

- 실서비스·배치·어드민의 독립 배포가 필요했고, 프로젝트 규모와 비용 문제를 고려하여 멀티 모듈 기반 독립 배포 방식을 선택했습니다.
- 코드베이스를 공유하면서 `api`·`batch`·`admin` 각 모듈을 독립적인 이미지로 빌드해 별도 컨테이너로 배포할 수 있도록 구성하여 실서비스·배치·어드민 재배포 시 서로 영향을 주지 않는 구조를
  달성했습니다.

### 클린 아키텍처 (실용적 적용)

- 각 클래스의 역할을 명확히 하고 변경 영향을 최소화하며, 추후 팀원 합류 시 작업 충돌을 줄이기 위해 클린 아키텍처 적용을 선택했습니다.
- 외부 의존성(영속화, 외부 API 호출 등)과의 경계는 유지하되, 도메인 객체와 JPA 엔티티는 분리하지 않고 엔티티를 도메인으로 함께 사용했습니다.
    - 초기에는 완전한 분리를 시도했으나 소규모 프로젝트에서 작업 비용이 과도하다고 판단해 조정했습니다.
- **현재 타협점**
    - 조회 로직은 유스케이스를 세분화하지 않고 단일 유스케이스로 처리
    - admin 모듈은 유스케이스 없이 Command/Query 구조로 단순화
    - 현재 상황에 맞는 수준을 지속적으로 고민 중 (개발 속도, 유지 보수성 등의 균형을 고려)

<br>

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
    - 스케줄링 작업 관리
        - 구독 결제일 업데이트 및 소비 내역 생성
        - 결제일 알림 이메일 발송
        - 리프레시 토큰 및 게스트 정보 정리
- `admin`
    - 대시보드 및 운영 데이터 관리 API 제공
- `domain`
    - 엔티티 클래스 관리 (+ @EnableJpaAuditing 적용)
- `common`
    - 공통 예외, 에러 코드, DTO, 상수 관리

<br>

## ERD

<img src="https://github.com/user-attachments/assets/7d871a5f-bb2b-42b9-84da-8bd5a6dfe445" width="700" alt="ERD">