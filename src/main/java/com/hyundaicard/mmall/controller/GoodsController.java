package com.hyundaicard.mmall.controller;

import com.hyundaicard.mmall.model.GoodsVO;
import com.hyundaicard.mmall.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Tag(name = "상품", description = "상품 목록 및 상세 조회 API")
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "상품 목록 조회", description = "전체 상품 목록을 반환합니다.")
    @RequestMapping("/list.do")
    @ResponseBody
    public List<GoodsVO> goodsList() {
        return goodsService.getGoodsList();
    }

    @Operation(summary = "상품 상세 조회", description = "상품코드로 단건 상품 정보를 반환합니다.")
    @RequestMapping("/detail.do")
    @ResponseBody
    public GoodsVO goodsDetail(
            @Parameter(description = "상품코드", example = "2735991")
            @RequestParam("gdCd") String gdCd) {
        return goodsService.getGoodsDetail(gdCd);
    }

}
