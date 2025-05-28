package com.oopsw.clario.service;

import com.oopsw.clario.dto.MyBankDto;
import com.oopsw.clario.dto.MyCardDto;
import com.oopsw.clario.repository.MyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyDataService {
    @Autowired
    private MyDataRepository myDataRepository;

    public List<MyBankDto> getMyBankConnection(String memberId) {
        return myDataRepository.getMyBankConnection(memberId);
    }

    public List<MyCardDto> getMyCardConnection(String memberId) {
        return myDataRepository.getMyCardConnection(memberId);
    }

    public List<MyBankDto> getMyBankList(String memberId) {
        return myDataRepository.getMyBankList(memberId);
    }

    public List<MyCardDto> getMyCardList(String memberId) {
        return myDataRepository.getMyCardList(memberId);
    }
}
