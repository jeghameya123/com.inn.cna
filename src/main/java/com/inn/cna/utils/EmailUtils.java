package com.inn.cna.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender emailSender;
    public void sendSimpleMessage(String to,String Subject,String text,List<String>list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amitkumarx31@gmail.com");
        message.setTo(to);
        message.setSubject(Subject);
        message.setText(text);
        if (list != null && list.size() > 0) {
            message.setCc(getCcArray(list));
        }


        return cc;




    }
    private String[] getCcArray(List<String>ccList) {
        String[] cc = new String[ccList.size()];
        for(int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }
}
