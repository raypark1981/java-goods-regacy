package com.hyundaicard.mmall.service;

import com.hyundaicard.mmall.dao.GoodsMapper;
import com.hyundaicard.mmall.model.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsService(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    public GoodsVO getGoodsDetail(String gdCd) {
        return goodsMapper.selectGoodsDetail(gdCd);
    }

    public List<GoodsVO> getGoodsList() {
        return goodsMapper.selectGoodsList();
    }

}
