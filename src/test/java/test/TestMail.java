package test;

import com.spring.mvc.portfolio.mail.SendEmail;

public class TestMail {
    
    public static void main(String[] args) throws Exception {
        String personal = "Hello"; // 發送者姓名
        String to = "dwyanewade03mvp@gmail.com"; // 發送給誰 ? 若有多筆", "號隔開
        String title = "測試信件"; // 信件 title
        // 信件內容 
        String html = "測試<h1 style:color: red>測試</h1>測試";
        
        SendEmail sendEmail = new SendEmail();
        sendEmail.submit(personal, to, title, html);
    }
}
