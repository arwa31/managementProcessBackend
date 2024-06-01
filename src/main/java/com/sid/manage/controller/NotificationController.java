package com.sid.manage.controller;
import com.sid.manage.model.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;


@RestController
public class NotificationController {
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/send-email")
    public String sendEmail(@ModelAttribute EmailModel emailModel) throws MessagingException, IOException {
        /* Mail Simple (only text) using @Requestbody
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailEntity.getReceiver());
        simpleMailMessage.setSubject(emailEntity.getSubject());
        simpleMailMessage.setText(emailEntity.getText());*/

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(emailModel.getReceiver());
        mimeMessageHelper.setSubject(emailModel.getSubject());
        mimeMessageHelper.setText(emailModel.getText(),true);
       mimeMessageHelper.addAttachment(emailModel.getAttachment().getOriginalFilename(),convertMultipartToFile(emailModel.getAttachment(),emailModel.getAttachment().getOriginalFilename()));
        javaMailSender.send(mimeMessage);

        return "Email sent Successfully !";
    }
    //fichier converti sera stocké dans le répertoire temporaire (C:\Users\ASUS\AppData\Local\Temp\)de système d'exploitation avec le nom spécifié.
    private static File convertMultipartToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/"+ fileName);
        multipartFile.transferTo(convertFile);
        return convertFile;
    }



}
