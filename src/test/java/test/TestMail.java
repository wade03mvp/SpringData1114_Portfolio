package test;

import com.spring.mvc.portfolio.mail.SendEmail;
import org.junit.Test;

public class TestMail {
    
    @Test
    public void t1() throws Exception {
        String personal = "Hello"; // 發送者姓名
        String to = "vincenttuan@gmail.com"; // 發送給誰 ? 若有多筆", "號隔開
        String title = "測試信件"; // 信件 title
        // 信件內容 
        String html = "測試<p><h1 style:color: red>測試</h1><p>測試";
        
        SendEmail sendEmail = new SendEmail();
        sendEmail.submit(personal, to, title, html);
    }
}
