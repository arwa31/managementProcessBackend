package com.sid.manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;


    //----------------------------------sendNotif---------------------------------------------
    public boolean sendNotifEmail(String email, String memberName) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Vérification du compte du nouveau membre requise");

            String emailContent = String.format("""
            <div style="font-family: Arial, sans-serif; color: #333;">
                <p style="color: #2c3e50;">Bonjour Admin,</p>
                <p style="color: #2c3e50;">Un nouveau membre s'est inscrit et nécessite une vérification de compte</p>
                <p style="color: #2c3e50;">Veuillez cliquer sur le lien ci-dessous pour vérifier le compte</p>
                <p><a href="http://localhost:8080/verify-account?email=%s" target="_blank">Vérifier le compte</a></p>
                <p style="color: #2c3e50;">Merci,</p>
                <p style="color: #2c3e50;">L'équipe de votre application</p>
            </div>
        """, email);

            mimeMessageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);

            System.out.println("Email envoyé avec succès à " + email);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Échec de l'envoi de l'email à " + email + ": " + e.getMessage());
            return false;
        }
    }


    //----------------------------------OTP---------------------------------------------
    public String sendOptEmail(String email,String opt) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OPT");
        mimeMessageHelper.setText("""
             <div>
                <a href="http://localhost:4200/reset-password" target="_blank">Account Verifies ! click the link to Reset Password</a>
             </div>
                """.formatted(email,opt),true);

        javaMailSender.send(mimeMessage);

        return "Email sent Successfully !";
    }
    //---------------------------- Forget Password----------------------------------
    public String sendSetPassword(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText(generateEmailContent(email),true);

        javaMailSender.send(mimeMessage);

        return "Email sent Successfully !";
    }


    private String generateEmailContent(String email) {
        return """
                <!doctype html>
                <html lang="en-US">
                           
                <head>
                    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
                    <title>Reset Password Email Template</title>
                    <meta name="description" content="Reset Password Email Template.">
                    <style type="text/css">
                        a:hover {text-decoration: underline !important;}
                    </style>
                </head>
                           
                <body marginheight="0" topmargin="0" marginwidth="0" style="margin: 0px; background-color: #f2f3f8;" leftmargin="0">
                    <!--100% body table-->
                    <table cellspacing="0" border="0" cellpadding="0" width="100%" bgcolor="#f2f3f8"
                        style="@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;">
                        <tr>
                            <td>
                                <table style="background-color: #f2f3f8; max-width:670px;  margin:0 auto;" width="100%" border="0"
                                    align="center" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td style="height:80px;">&nbsp;</td>
                                    </tr>
                           
                                    <tr>
                                        <td style="height:20px;">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0"
                                                style="max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);">
                                                <tr>
                                                    <td style="height:40px;">&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:0 35px;">
                                                        <h1 style="color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;">You have
                                                            requested to reset your password</h1>
                                                        <span
                                                            style="display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;"></span>
                                                        <p style="color:#455056; font-size:15px;line-height:24px; margin:0;">
                                                            We cannot simply send you your old password. A unique link to reset your
                                                            password has been generated for you. To reset your password, click the
                                                            following link and follow the instructions.
                                                        </p>
                                                        <a href="http://localhost:4200/reset-password"
                                                            style="background:#3B62A0;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;">Reset
                                                            Password</a>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="height:40px;">&nbsp;</td>
                                                </tr>
                                            </table>
                                        </td>
                                    <tr>
                                        <td style="height:20px;">&nbsp;</td>
                                    </tr>
                           
                                    <tr>
                                        <td style="height:80px;">&nbsp;</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <!--/100% body table-->
                </body>
                           
                </html>
           """;}
}
