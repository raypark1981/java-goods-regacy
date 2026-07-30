package com.hyundaicard.mmall.controller;

import com.hyundaicard.mmall.model.GoodsVO;
import com.hyundaicard.mmall.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 상품 상세 페이지
     * URL: /goods/goodsDetail.do?gdCd=2735991
     */
    @GetMapping("/goodsDetail.do")
    public String goodsDetail(@RequestParam("gdCd") String gdCd, Model model) {
        GoodsVO goods = goodsService.getGoodsDetail(gdCd);
        model.addAttribute("goods", goods);
        // dispatcher-servlet.xml ViewResolver 에 의해
        // → /WEB-INF/views/goods/goodsDetail.jsp 로 연결
        return "goods/goodsDetail";
    }

    /**
     * 상품 목록 페이지
     * URL: /goods/goodsList.do
     */
    @GetMapping("/goodsList.do")
    public String goodsList(Model model) {
        model.addAttribute("goodsList", goodsService.getGoodsList());
        return "goods/goodsList";
    }

}
