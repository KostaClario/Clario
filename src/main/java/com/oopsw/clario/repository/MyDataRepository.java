package com.oopsw.clario.repository;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyDataRepository {
    public List<MyBankDTO> getMyBankConnection(int memberId);
    public List<MyCardDTO> getMyCardConnection(int memberId);
    public List<MyBankDTO> getMyBankList(int memberId);
    public List<MyCardDTO> getMyCardList(int memberId);
}
