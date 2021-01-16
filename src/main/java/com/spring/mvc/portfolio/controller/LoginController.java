package com.spring.mvc.portfolio.controller;


import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/portfolio")
public class LoginController {
    @Autowired
    private PortfolioService service;
    
    @PostMapping("/login")
    public String login(@RequestParam("username") Optional<String> username,
                        @RequestParam("password") Optional<String> password,
                        HttpSession session) {
        Investor investor = service.getInvestorRepository().getInvestor(username.get());
        if (investor != null && investor.getPassword().equals(password.get())) {
            session.setAttribute("investor", investor);
            session.setAttribute("watch_id", investor.getWatchs().iterator().next().getId());
            return "redirect:/portfolio/index.jsp";
        }
        return logout(session);
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/portfolio/login.jsp";
    }
}
