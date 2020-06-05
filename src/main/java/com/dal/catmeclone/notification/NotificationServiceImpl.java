/**
 * 
 */
package com.dal.catmeclone.notification;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dal.catmeclone.model.Course;
import com.dal.catmeclone.model.User;



/**
 * @author Mayank
 *
 */

@Service
public class NotificationServiceImpl implements NotificationService {
	
	final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	@Value("${from.email}")
	private String fromgmail;
	
	@Value("${from.password}")
	private String fromPassword;
	
	
	@Value("${account.subject}")
	private String subject;
	
	@Value("${login.url}")
	private String loginurl;
	


	@Override
	public void sendNotificationToNewuser(User user,String password, Course course) {
		// TODO Auto-generated method stub
	
		
		
		
		String body ="<h2 >Hi "+user.getFirstName()+",</h2>"+
		"<p>Your Account has been created successfully and you have also been enrolled to subject: "+course.getCourseID()+"</p>"
		+ "<p>Please find below your login credential:&nbsp;</p>"
		+ "<p ><strong>Username: "+user.getBannerId()+"</strong></p>"
		+ "<p ><strong>Password: "+password+"</strong></p>"
		+ "<p style='text-align: center;'><strong> Please click on the button to log in</strong></p>"
		+ "<p style='text-align: center;'><a href="+loginurl+" target='_blank'><button style='background-color: #a0e9ed;'>Log In</button></a></p>"
		+ "<p style='text-align: left;'>&nbsp;</p>"
		+ "<p style='text-align: left;'>You are most welcomed to be a part of this organisation.</p>"
		+ "<p >Best Regards,</p>"
		+ "<p>CSCI5708-Grp12</p>"
		+ "<p>&nbsp;</p>";
		
		send(fromgmail,fromPassword,user.getEmail(),subject,body);  

	}

	public void sendNotificationForPassword(String BannerId,String password,String sendto) {
		
	        String body ="<h2 >Hi "+BannerId+",</h2>"+
                    "<p>New Password for your account is geenrated successfully </p>"
                    + "<p>Please find below your login credential:&nbsp;</p>"
                    + "<p ><strong>Username: "+BannerId+"</strong></p>"
                    + "<p ><strong>Password: "+password+"</strong></p>"
                    + "<p style='text-align: center;'><strong> Please click on the button to log in</strong></p>"
                    + "<p style='text-align: center;'><a href="+loginurl+" target='_blank'><button style='background-color: #a0e9ed;'>Log In</button></a></p>"
                    + "<p style='text-align: left;'>&nbsp;</p>"
                    + "<p Note: for security reason, </p>"
                    + "<p> you must change your password after logging in.</p>"
                    + "<p >Best Regards,</p>"
                    + "<p>CSCI5708-Grp12</p>"
                    + "<p>&nbsp;</p>";

			String subject = "Forgot password";
			send(fromgmail,fromPassword,sendto,subject,body);
	}

	
	private void send(String from,String password,String to,String sub,String msg){  
	        
			//Get properties object    		
	        Properties props = new Properties();    
	        props.put("mail.smtp.host", "smtp.gmail.com");    
	        props.put("mail.smtp.socketFactory.port", "465");    
	        props.put("mail.smtp.socketFactory.class",    
	                  "javax.net.ssl.SSLSocketFactory");    
	        props.put("mail.smtp.auth", "true");    
	        props.put("mail.smtp.port", "465");    
	        props.put("mail.smtp.starttls.enable","true");
	        
	        //get Session   
	        Session session = Session.getInstance(props,    
	         new javax.mail.Authenticator() {    
	         protected PasswordAuthentication getPasswordAuthentication() {    
	         return new PasswordAuthentication(from,password);  
	         }    
	        });    
        
	        //compose message 
	           
	         MimeMessage message = new MimeMessage(session);    
	         try {
				message.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));
			  

				message.setSubject(sub);   
		
				message.setText(msg,"UTF-8", "html");    

				//send message  
				Transport.send(message);  
	         } catch (MessagingException e) {
					// TODO Auto-generated catch block
					logger.error("Error in sending email");
			 }  
	}

	        

}
