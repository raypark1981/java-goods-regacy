package hmfrnt.ap.web;

import hmfrnt.ap.service.APGoodsService;
import hmfrnt.ap.vo.APGoodsVO;
import hmfrnt.web.ApiResponseVO;
import hmfrnt.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 상품 Controller.
 * 운영 URL 패턴 예시: /goods/list.nhd, /goods/detail.nhd
 */
@Tag(name = "상품", description = "상품 목록 및 상세 조회 API")
@Controller
@RequestMapping("/goods")
public class APGoodsController extends BaseController {

    private final APGoodsService apGoodsService;

    public APGoodsController(APGoodsService apGoodsService) {
        this.apGoodsService = apGoodsService;
    }

    @Operation(summary = "상품 목록 조회")
    @RequestMapping("/list.nhd")
    @ResponseBody
    public ApiResponseVO<List<APGoodsVO>> goodsList() {
        return ApiResponseVO.success(apGoodsService.getGoodsList());
    }

    @Operation(summary = "상품 상세 조회")
    @RequestMapping("/detail.nhd")
    @ResponseBody
    public ApiResponseVO<APGoodsVO> goodsDetail(
            @Parameter(description = "상품코드", example = "2735991")
            @RequestParam("gdCd") String gdCd) {
        return ApiResponseVO.success(apGoodsService.getGoodsDetail(gdCd));
    }
}
