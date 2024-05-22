package com.mizore.easybuy.service.business;

import com.mizore.easybuy.config.JavaMailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailConfig javaMailConfig;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(Integer orderId, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 邮件标题
        String subject = "New user complaint message";
        // 邮件正文
        String text =
                "Dear EasyBuy admin:\n" +
                        "You have received a new complaint.\n" +
                        "OrderId: " + orderId + ", reason: " +reason+"\n" +
                        "Please handle the complaint as soon as possible.";
        // 发件人
        message.setFrom(javaMailConfig.getFrom());
        // 收件人
        message.setTo(javaMailConfig.getTo());
        // 标题
        message.setSubject(subject);
        // 正文
        message.setText(text);
        // 发送邮件
        javaMailSender.send(message);
    }

}
