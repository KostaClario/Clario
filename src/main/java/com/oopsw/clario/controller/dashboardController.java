package com.oopsw.clario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class dashboardController {
    @GetMapping("statistics")
    public String statisticsView() {
        return "statistics/statisticsView";
    }

    @GetMapping("card")
    public String cardsView() {
        return "card/cardView";
    }

}
