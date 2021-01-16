package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.entities.Watch;
import com.spring.mvc.portfolio.service.EmailService;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        // 建立 Investor
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
    
    // 查詢單筆(根據 id)
    @GetMapping(value = {"/{id}"})
    public Investor get(@PathVariable("id") Optional<Integer> id) {
        return service.getInvestorRepository().findOne(id.get());
    }
    
    // 單筆修改(根據 id)
    @PutMapping(value = {"/{id}"})
    @Transactional
    public Boolean update(@PathVariable("id") Optional<Integer> id, 
                          @RequestBody Map<String, String> jsonMap) {
        // 是否有 id
        if(!id.isPresent()) {
            return false;
        }
        // 該筆資料是否存在 ?
        if(get(id) == null) {
            return false;
        }
        // 修改資料
        service.getInvestorRepository().update(
                id.get(), 
                jsonMap.get("username"), 
                jsonMap.get("password"), 
                jsonMap.get("email"), 
                Integer.parseInt(jsonMap.get("balance")));
        return true;
    }
    
    // 單筆刪除(根據 id)
    @DeleteMapping(value = {"/{id}"})
    @Transactional
    public Boolean update(@PathVariable("id") Optional<Integer> id) {
        // 是否有 id
        if(!id.isPresent()) {
            return false;
        }
        // 該筆資料是否存在 ?
        if(get(id) == null) {
            return false;
        }
        // 刪除資料
        service.getInvestorRepository().delete(id.get());
        return true;
    }
    
    @GetMapping("/duplicate/{username}")
    public Boolean isDuplicateUsername(@PathVariable("username") Optional<String> username) {
        if(username.isPresent()) {
           Investor investor = service.getInvestorRepository().getInvestor(username.get());
           return investor == null ? false : true;
        }
        return false;
    }
}
