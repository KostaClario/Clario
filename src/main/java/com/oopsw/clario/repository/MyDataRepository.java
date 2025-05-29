package com.oopsw.clario.repository;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyDataRepository {
    public List<MyBankDTO> getMyBankConnection(String memberId);
    public List<MyCardDTO> getMyCardConnection(String memberId);
    public List<MyBankDTO> getMyBankList(String memberId);
    public List<MyCardDTO> getMyCardList(String memberId);
}
