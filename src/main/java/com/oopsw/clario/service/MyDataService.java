package com.oopsw.clario.service;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import com.oopsw.clario.repository.MyDataRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyDataService {
    @Autowired
    private MyDataRepository myDataRepository;

    public List<MyBankDTO> getMyBankConnection(@Param("memberId") String memberId) {
        return myDataRepository.getMyBankConnection(memberId);
    }

    public List<MyCardDTO> getMyCardConnection(String memberId) {
        return myDataRepository.getMyCardConnection(memberId);
    }

    public List<MyBankDTO> getMyBankList(String memberId) {
        return myDataRepository.getMyBankList(memberId);
    }

    public List<MyCardDTO> getMyCardList(String memberId) {
        return myDataRepository.getMyCardList(memberId);
    }
}
