package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.repository.InvestorRepository;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portfolio/verify")
public class VerifyController {
    @Autowired
    private PortfolioService service;
    
    // 驗證 : /3/6281597b
    @GetMapping("/{id}/{code}")
    @Transactional
    public String verify(HttpSession session,
                        @PathVariable("id") Optional<Integer> id,
                        @PathVariable("code") Optional<String> code) {
        String message = "ERROR";
        Investor investor = service.getInvestorRepository().findOne(id.get());
        if(investor != null && investor.getCode().equals(code.get())) {
            message = "SUCCESS";
            service.getInvestorRepository().updatePass(id.get(), Boolean.TRUE);
        }
        session.setAttribute("message", message);
        return "redirect:/portfolio/verify.jsp";
    }
}