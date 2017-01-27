package br.com.restfulSmartFier.factory;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.restfulSmartFier.model.Estacao;

public class Email {

	public void enviarEmail(Estacao estacao) {
		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
						// TODO Auto-generated method stub
						return new javax.mail.PasswordAuthentication(
								"allanwborges@gmail.com", "WEb@master007");

					}
				});

		session.setDebug(true);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("allanwborges@gmail.com"));
			Address[] toUser = InternetAddress.parse(estacao.getFuncionario()
					.getEmail());
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Alerta de email ESTA��O OFFLINE");
			message.setText("Ol�! Tudo bem, senhor "
					+ estacao.getFuncionario().getNome()
					+ "? Voc� est� recebendo esse email porque est� cadastrado"
					+ "como respons�vel t�cnico pela esta��o "
					+ estacao.getNome_estacao() + " que se encontra offline."
					+ "Abaixo segue as informa��es dispon�veis." + ""
					+ "Dados da esta��o OFFLINE:" + "Nome: "
					+ estacao.getNome_estacao() + " Endere�o "
					+ estacao.getEndereco() + "IP: " + estacao.getIp()+" Modelo: "+estacao.getModelo());
			
			Transport.send(message);
			
		} catch (MessagingException e) {
			throw new RuntimeException(e);

		}

	}

	public static void main(String[] args) {
		// new Email().enviarEmail();
	}

}
