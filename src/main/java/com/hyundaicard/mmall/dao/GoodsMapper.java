package com.hyundaicard.mmall.dao;

import com.hyundaicard.mmall.model.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    GoodsVO selectGoodsDetail(@Param("gdCd") String gdCd);

    List<GoodsVO> selectGoodsList();

}
