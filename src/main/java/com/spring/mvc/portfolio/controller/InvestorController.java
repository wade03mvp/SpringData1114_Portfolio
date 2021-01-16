package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.entities.Watch;
import com.spring.mvc.portfolio.service.EmailService;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller + @ResponseBody
@RequestMapping("/portfolio/investor")
public class InvestorController {
    @Autowired
    private PortfolioService service;
    
    @Autowired
    private EmailService emailService;
    // 新增
    @PostMapping(value = {"/", "/add"})
    public Investor add(@RequestBody Map<String, String> jsonMap) {
        // jsonMap 就是前端傳來的 json 字串所轉換後的集合資料
        // {"username":"admin","password":"1234","email":"vincentjava@yahoo.com.tw","balance":"200"}
        Investor investor = new Investor();
        investor.setUsername(jsonMap.get("username"));
        investor.setPassword(jsonMap.get("password"));
        investor.setEmail(jsonMap.get("email"));
        investor.setBalance(Integer.parseInt(jsonMap.get("balance")));
        investor.setPass(Boolean.FALSE);
        // 設定 email 認證碼
        investor.setCode(Integer.toHexString(investor.hashCode()));
        // 建立 watch
        Watch watch = new Watch();
        watch.setInvestor(investor);
        watch.setName(investor.getUsername() + "的投資組合");
        // 存檔 Investor
        service.getInvestorRepository().save(investor);
        // 存檔 Watch
        service.getWatchRepository().save(watch);
        
        // 發送認證信件
        emailService.send(investor);
        
        return investor;
    }
    
    // 查詢全部
    @GetMapping(value = {"/", "/query"})
    public List<Investor> queryAll() {
        return service.getInvestorRepository().findAll();
    }
}
