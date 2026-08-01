package com.hyundaicard.mmall.controller;

import com.hyundaicard.mmall.model.GoodsVO;
import com.hyundaicard.mmall.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 상품 목록 조회 API
    // GET /goods/list.do
    @RequestMapping("/list.do")
    @ResponseBody
    public List<GoodsVO> goodsList() {
        return goodsService.getGoodsList();
    }

    // 상품 상세 조회 API
    // GET /goods/detail.do?gdCd=GOODS001
    @RequestMapping("/detail.do")
    @ResponseBody
    public GoodsVO goodsDetail(@RequestParam("gdCd") String gdCd) {
        return goodsService.getGoodsDetail(gdCd);
    }

}
