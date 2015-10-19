package com.prj.util;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * ClassName CdiMailSender
 * Description CDI 邮件发送器
 * Author Alex (Jinghao) Yan
 */
public class MailSender implements  Runnable {

    private String from;
    private String to;
    private String subject;
    private String content;

    public MailSender(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param wait 是否同步等待
     * @return .
     */
    public static boolean sendMail(String from, String to, String subject, String content, boolean wait) {

        boolean flag = true;
        if (wait) {
            flag = doSendMail(from, to, subject, content);
        } else {
            MailSender sender = new MailSender(from, to, subject, content);
            new Thread(sender, "New thread to send email~").start();
        }
        return flag;

    }

    private static boolean doSendMail(String from, String to, String subject, String content) {
        boolean ret = true;
        JavaMailSender mailSender = (JavaMailSender)ApplicationContextUtils.getBean("mailSender");
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            //设置utf-8或GBK编码，否则邮件会有乱码
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");
            messageHelper.setTo(to);//接受者
            messageHelper.setFrom(from);//发送者
            messageHelper.setSubject(subject);//主题
            //邮件内容，注意加参数true，表示启用html格式
            messageHelper.setText(content,true);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public void run() {
        MailSender.doSendMail(from, to, subject, content);
    }
}
