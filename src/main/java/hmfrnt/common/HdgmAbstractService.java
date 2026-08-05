package hmfrnt.common;

/**
 * 전체 ServiceImpl 의 공통 베이스 추상 클래스.
 * 운영에서는 트랜잭션 처리, 공통 로깅 등 AOP 연동 기반을 제공한다.
 * 각 ServiceImpl은 이 클래스를 상속하고 Service 인터페이스를 implements 한다.
 */
public abstract class HdgmAbstractService {

    /**
     * 공통 예외 처리 래퍼.
     * 운영 프레임워크에서는 Exception 발생 시 공통 에러 코드로 변환한다.
     */
    protected void handleException(Exception e) throws Exception {
        throw e;
    }
}
