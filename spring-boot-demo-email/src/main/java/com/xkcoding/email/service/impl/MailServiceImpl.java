package com.xkcoding.email.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.xkcoding.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>
 * 邮件接口
 * </p>
 *
 * @package: com.xkcoding.email.service.impl
 * @description: 邮件接口
 * @author: yangkai.shen
 * @date: Created in 2018/11/21 13:49
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) throws MailException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        if (ArrayUtil.isNotEmpty(cc)) {
            message.setCc(cc);
        }
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content, String... cc) throws MessagingException {
        MimeMessageHelper helper = this.getMimeMessageHelperInstance(to, subject, content,cc);
        mailSender.send(helper.getMimeMessage());

    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件地址
     * @param cc       抄送地址
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc) throws MessagingException {

        if (!new File(filePath).exists()) {
            throw new MessagingException("当前文件不存在。");
        }
        if (Files.isDirectory(Paths.get(filePath)) ) {
            throw new MessagingException("上传的文件不能为目录。");
        }
        MimeMessageHelper helper = this.getMimeMessageHelperInstance(to, subject, content,cc);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf("/"));
        helper.addAttachment(fileName, file);
        mailSender.send(helper.getMimeMessage());
    }

    /**
     * 发送正文中有静态资源的邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param rscPath 静态资源地址
     * @param rscId   静态资源id
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     */
    @Override
    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = this.getMimeMessageHelperInstance(to, subject, content,cc);
        helper.setText(content, true);
        if (ArrayUtil.isNotEmpty(cc)) {
            helper.setCc(cc);
        }
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);

        mailSender.send(message);
    }

    /**
     * 创建 MimeMessageHelper 实例
     *
     * @param to        收件人地址
     * @param subject   邮件主题
     * @param content   邮件内容
     * @param cc        抄送地址
     * @return          MimeMessageHelper 实例
     * @throws MessagingException 邮件发送异常
     */
    private MimeMessageHelper getMimeMessageHelperInstance(String to, String subject,String content,String...  cc) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        if (ArrayUtil.isNotEmpty(cc))  helper.setCc(cc);
        return helper;
    }
}
