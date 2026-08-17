package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * AP_CUST_PNT_INF (고객포인트현황) VO.
 */
@Getter
@Setter
public class PEAPntInfVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;                  // 통합고객번호
    private Integer avlbPnt;                 // 가용포인트
    private Integer tsaleAcmtAmt;             // 총적립누적포인트
    private Integer acmPnt;                  // 적립포인트
    private Integer acmCnt;                  // 적립건수
    private Integer acmCnclPnt;              // 적립취소포인트
    private Integer acmCnclCnt;              // 적립취소건수
    private Integer campPnt;                 // 캠페인포인트
    private Integer campCnt;                 // 캠페인건수
    private Integer campCnclPnt;             // 캠페인취소포인트
    private Integer campCnclCnt;             // 캠페인취소건수
    private Integer clubAcmPnt;              // 클럽적립포인트
    private Integer clubAcmCnt;              // 클럽적립건수
    private Integer clubAcmCnclPnt;          // 클럽적립취소포인트
    private Integer clubAcmCnclCnt;          // 클럽적립취소건수
    private Integer usePnt;                  // 사용포인트
    private Integer useCnt;                  // 사용건수
    private Integer useCnclPnt;              // 사용취소포인트
    private Integer useCnclCnt;              // 사용취소건수
    private Integer extnPnt;                 // 소멸포인트
    private Integer extnCnt;                 // 소멸건수
    private Integer trfPnt;                  // 양도포인트
    private Integer trfCnt;                  // 양도건수
    private Integer takePnt;                 // 양수포인트
    private Integer takeCnt;                 // 양수건수
    private Integer trfStbyPnt;              // 양도대기포인트
    private Integer pntExchRcvPnt;           // 포인트교환수신포인트
    private Integer pntExchRcvPntCnt;        // 포인트교환수신포인트건수
    private Integer pntExchSendPnt;          // 포인트교환전송포인트
    private Integer pntExchSendPntCnt;       // 포인트교환전송포인트건수
    private String frstJoinDt;               // 최초가입일자
    private String frstAprvlDt;              // 최초승인일자
    private String lastAprvlDt;              // 최종승인일자
    private String frstAprvlPtcoId;          // 최초승인참여사ID
    private String frstAprvlFrcsId;          // 최초승인가맹점ID
    private String lastAprvlPtcoId;          // 최종승인참여사ID
    private String lastAprvlFrcsId;          // 최종승인가맹점ID
    private String lastApledgPtcno;          // 최종승인원장상세번호
    private String mophOccpCertNo;           // 휴대전화점유인증번호
    private String mophOccpCertNoSendDtm;    // 휴대전화점유인증번호전송일시
    private String appCrdFrstAcmDt;          // 애플리케이션카드최초적립일자
    private String appFrstAprvlLedgNo;       // 애플리케이션최초승인원장번호
    private String rgstId;                   // 등록자ID
    private String rgstIp;                   // 등록자IP
    private String chgpId;                   // 변경자ID
    private String chgpIp;                   // 변경자IP
    private String pntTypeGbcd;              // 포인트유형구분코드
}
