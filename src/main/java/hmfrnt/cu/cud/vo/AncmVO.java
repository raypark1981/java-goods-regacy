package hmfrnt.cu.cud.vo;

import hmfrnt.web.BaseObject;

/**
 * 고객센터 공지사항 상세 조회 VO.
 * DB 조회 결과를 담는 객체 (읽기 전용).
 */
public class AncmVO extends BaseObject {

    private static final long serialVersionUID = 1L;

    private String bbcId;    // 게시판 ID
    private String bbcTitl;  // 제목
    private String bbcCntn;  // 내용
    private String globLang; // 글로벌 언어 코드
    private String stlvFlg;  // 스타일 플래그

    public String getBbcId()    { return bbcId; }
    public void   setBbcId(String bbcId)       { this.bbcId = bbcId; }

    public String getBbcTitl()  { return bbcTitl; }
    public void   setBbcTitl(String bbcTitl)   { this.bbcTitl = bbcTitl; }

    public String getBbcCntn()  { return bbcCntn; }
    public void   setBbcCntn(String bbcCntn)   { this.bbcCntn = bbcCntn; }

    public String getGlobLang() { return globLang; }
    public void   setGlobLang(String globLang) { this.globLang = globLang; }

    public String getStlvFlg()  { return stlvFlg; }
    public void   setStlvFlg(String stlvFlg)   { this.stlvFlg = stlvFlg; }
}
