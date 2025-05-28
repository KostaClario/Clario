package com.oopsw.clario.repository;

import com.oopsw.clario.dto.MyBankDto;
import com.oopsw.clario.dto.MyCardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyDataRepository {
    public List<MyBankDto> getMyBankConnection(String memberId);
    public List<MyCardDto> getMyCardConnection(String memberId);
    public List<MyBankDto> getMyBankList(String memberId);
    public List<MyCardDto> getMyCardList(String memberId);
}
