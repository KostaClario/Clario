package com.oopsw.clario.controller;

import com.oopsw.clario.dto.MyBankDto;
import com.oopsw.clario.dto.MyCardDto;
import com.oopsw.clario.service.MyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MyDataController {

    @Autowired
    private MyDataService myDataService;

    @GetMapping("/mydataconnection/{memberid}")
    public String myDataConnection(@PathVariable String memberid, Model model) {
        List<MyBankDto> banks = myDataService.getMyBankList(memberid);
        List<MyCardDto> cards = myDataService.getMyCardList(memberid);

        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);

        return "myData/myDataConnection";
    }

    @GetMapping("/mybankandcardlist/{memberid}")
    public String myBankAndCardList(@PathVariable String memberid, Model model) {
        List<MyBankDto> banks = myDataService.getMyBankList(memberid);
        List<MyCardDto> cards = myDataService.getMyCardList(memberid);
        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);
        return "myData/myBankAndCardList";
    }
}
