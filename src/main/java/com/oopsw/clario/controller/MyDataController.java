package com.oopsw.clario.controller;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import com.oopsw.clario.service.MyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/myData")
public class MyDataController {

    @Autowired
    private MyDataService myDataService;

    @GetMapping("/mydataconnection/{memberid}")
    public String myDataConnection(@PathVariable String memberid, Model model) {
        List<MyBankDTO> banks = myDataService.getMyBankConnection(memberid);
        List<MyCardDTO> cards = myDataService.getMyCardConnection(memberid);

        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);

        return "myData/myDataConnection";
    }

    @GetMapping("/mybankandcardlist/{memberId}")
    public String myBankAndCardList(@PathVariable String memberId, Model model) {
        List<MyBankDTO> banks = myDataService.getMyBankList(memberId);
        List<MyCardDTO> cards = myDataService.getMyCardList(memberId);
        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);
        return "myData/mybankandcardlist";
    }
}
