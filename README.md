# SPRING ADVANCED
# 🛠️ Spring Advanced Project

##프로젝트 개요
이 프로젝트는 Spring Boot 기반으로 **할 일 관리(Todo) API 및 뉴스 피드 댓글 기능**을 구현하는 프로젝트입니다.  
JPA와 MySQL을 활용하여 데이터를 저장하고, 테스트 코드를 작성하여 기능을 검증했습니다.

## 실행 방법
###프로젝트 클론
```bash
git clone https://github.com/wjdgus2319/spring-advanced.git
cd spring-advanced
git checkout feature/lv1-refactor
./gradlew bootRun
Java 17 이상 필요
MySQL 사용 (application.yml에서 설정 필요)
Lv1-1: Early Return 패턴을 적용하여 가독성 개선
Lv1-2: 불필요한 if-else 문을 제거하여 코드 최적화
Lv1-3: 비밀번호 검증 로직을 DTO에서 처리하도록 변경 및 테스트 코드 추가
fetch join을 사용하던 TodoRepository를 @EntityGraph 기반으로 변경하여 최적화
PasswordEncoderTest: 올바르게 동작하도록 테스트 코드 수정
ManagerServiceTest: InvalidRequestException 발생 시 테스트 검증 및 수정
CommentServiceTest: Todo가 존재하지 않는 경우 예외 처리가 정상적으로 동작하도록 테스트 코드 수정
TodoRepository에서 fetch join을 사용하여 Todo와 User를 한 번에 가져왔지만, 다른 쿼리에서도 개별적으로 데이터를 조회하여 N+1 문제가 발생
@EntityGraph(attributePaths = {"user"})를 적용하여 Todo 조회 시 자동으로 User까지 로드되도록 변경했다
비밀번호 검증 로직이 Service에서 처리되고 있어, DTO가 비즈니스 로직을 알지 못하는 구조를 유지해야 하는 원칙에 어긋났다
비밀번호 검증 로직을 DTO 내부에서 처리하도록 변경하여 Service의 역할을 단순화 하여 해결했다
todo의_user가_null인_경우_예외가_발생한다() 테스트가 NullPointerException으로 실패되서
ManagerService에서 todo.getUser()가 null인 경우 예외를 던지지 않아 발생했다
if (todo.getUser() == null) {
    throw new InvalidRequestException("해당 Todo의 User가 존재하지 않습니다.");
}
위와 같은 null 체크 로직을 추가하여 테스트가 정상적으로 통과하도록 수정해서 해결

