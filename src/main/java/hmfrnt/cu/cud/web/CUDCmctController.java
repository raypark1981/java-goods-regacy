package hmfrnt.cu.cud.web;

import hmfrnt.common.HdgmMap;
import hmfrnt.common.HdgmUtil;
import hmfrnt.cu.cud.service.CUDCmctService;
import hmfrnt.cu.cud.vo.AncmVO;
import hmfrnt.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 고객센터 공지사항 Controller.
 * 운영 URL 패턴: /ancmDtlView.nhd, /ancmDtlViewGlob.nhd
 */
@Controller
public class CUDCmctController extends BaseController {

    private final CUDCmctService cudCmctService;

    public CUDCmctController(CUDCmctService cudCmctService) {
        this.cudCmctService = cudCmctService;
    }

    /**
     * 공지사항 목록 화면.
     * 운영: @RequestMapping("/ancmDtlList.nhd")
     */
    @RequestMapping("/ancmDtlList.nhd")
    public String selectAncmDtlList(@RequestParam Map<String, String> paramMap,
                                    Model model) throws Exception {

        HdgmMap hdgmParam = new HdgmMap();
        hdgmParam.putAll(paramMap);

        HdgmMap responseMap = newResponseMap();
        HdgmMap resultMap   = newResponseMap();
        resultMap.putAll(hdgmParam);

        HdgmMap ancmMap = cudCmctService.getAncmDtlList(hdgmParam);
        if (ancmMap != null) {
            resultMap.putAll(ancmMap);
        }

        model.addAttribute("resultMap", resultMap);
        return "cu/cud/AncmDtlList";
    }

    /**
     * 공지사항 상세 화면.
     * 운영: @RequestMapping({"/ancmDtlView.nhd", "/ancmDtlViewGlob.nhd"})
     */
    @RequestMapping({"/ancmDtlView.nhd", "/ancmDtlViewGlob.nhd"})
    public String selectAncmDtlView(@RequestParam Map<String, String> paramMap,
                                    Model model) throws Exception {

        HdgmMap hdgmParam = new HdgmMap();
        hdgmParam.putAll(paramMap);

        // 파라미터 필수값 검증
        if (hdgmParam.get("bbcId") == null || hdgmParam.getString("bbcId").isEmpty()) {
            return redirect("/ancmDtlList.nhd");
        }

        // 글로벌 언어 처리
        String globLang = getSessionGlobLang();
        hdgmParam.put("globLang", globLang);

        HdgmMap responseMap = newResponseMap();
        HdgmMap resultMap   = newResponseMap();
        resultMap.putAll(hdgmParam);

        AncmVO ancmMap = cudCmctService.getAncmDtlView(hdgmParam);
        if (ancmMap != null) {
            // 운영 패턴: HdgmUtil.convertAsciiToWcml 처리 후 Map에 담음
            resultMap.put("bbcTitl", HdgmUtil.convertAsciiToWcml(ancmMap.getBbcTitl()));
            resultMap.put("bbcCntn", HdgmUtil.convertAsciiToWcml(ancmMap.getBbcCntn()));
        }

        model.addAttribute("resultMap", resultMap);
        return "cu/cud/AncmDtlView";
    }
}
