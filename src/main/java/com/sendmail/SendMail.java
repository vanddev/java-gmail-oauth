package com.sendmail;

import com.sun.mail.smtp.SMTPTransport;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail implements Serializable{

    private static final long serialVersionUID = 1265603938749854454L;
    private static final String FROM_EMAIL = "example@gmail.com";
    
    public void sendMail(String to_email, String subject, String message, String mimeType) {
		Properties props = new Properties();
                String oauthToken = "";
                OAuth2Authenticator.initialize();
                oauthToken = AccessTokenFromRefreshToken.getAccessToken();
		props.put("mail.smtp.starttls.enable","true"); 
		props.put("mail.smtp.host", "smtp.gmail.com"); //server SMTP do GMAIL
                props.put("mail.smtp.starttls.required", "true");
                props.put("mail.smtp.sasl.enable", "true");
                props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
                props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
                Session session = Session.getInstance(props);
                session.setDebug(true);
		Message msg = new MimeMessage(session);
		try {
			//Setando o destinatário
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
			//Setando a origem do email
			msg.setFrom(new InternetAddress(FROM_EMAIL));
			//Setando o assunto
			msg.setSubject(subject);
			//Setando o conteúdo/corpo do email
			msg.setContent(message,mimeType);
		} catch (Exception e) {
			System.out.println(">> Erro: Can't finish email content");
			e.printStackTrace();
		}
		//Objeto encarregado de enviar os dados para o email
//		Transport tr;
		try {
//                    Transport.send(msg);
                    SMTPTransport smtpTransport = OAuth2Authenticator.connectToSmtp("smtp.gmail.com", 587, FROM_EMAIL, oauthToken, true);
                    smtpTransport.sendMessage(msg, msg.getAllRecipients());
                    smtpTransport.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(">> Erro: Can't send email");
			e.printStackTrace();
		}
	}
}
