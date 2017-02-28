package qunar;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送邮件的测试程序
 *
 * @author lwq
 */
public class MailUtil {

    private static final Properties props = new Properties();
    // 构建授权信息，用于进行SMTP进行身份验证
    private static Authenticator authenticator;
    // 使用环境属性和授权信息，创建邮件会话
    private static Session mailSession;
    private static InternetAddress form;

    static {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.user", "yq_service@163.com");
        props.put("mail.password", "8R80znN52017");
        props.put("mail.smtp.localhost", "localhost");
        authenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        mailSession = Session.getInstance(props, authenticator);
        try {
            form = new InternetAddress(props.getProperty("mail.user"));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void send(String sendTo, String subject, String content) throws MessagingException {
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        message.setFrom(form);
        // 设置收件人
        String[] sends = sendTo.split(",");
        InternetAddress[] addresses = new InternetAddress[sends.length];
        for (int i = 0; i < sends.length; i++) {
            addresses[i] = new InternetAddress(sends[i]);
        }
        message.setRecipients(RecipientType.TO, addresses);
        // 设置邮件标题
        message.setSubject(subject);
        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");
        // 发送邮件
        Transport.send(message);
    }
}