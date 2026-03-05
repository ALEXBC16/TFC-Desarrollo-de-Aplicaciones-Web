package edu.lopezalejandro._aMarcha.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String apiKey;

    public void enviarCorreoConfirmacion(String to, String nombreUsuario) {

        String contenido = """
            <div style="font-family: Arial, sans-serif; background-color:#f5f5f5; padding:20px;">
                
                <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:10px;">
                    
                    <h2 style="color:#2c3e50;">Hola %s 👋</h2>
                    
                    <p>Tu cuenta en <b>1ª Marcha</b> ha sido creada correctamente.</p>
                    
                    <p>
                        Ya puedes iniciar sesión en la plataforma y comenzar a realizar 
                        <b>tests de preparación para el carnet de conducir</b>.
                    </p>
                    
                    <div style="margin-top:20px; padding:15px; background:#ecf0f1; border-radius:5px;">
                        🚗 ¡Mucho éxito en tu preparación!
                    </div>
                    
                    <p style="margin-top:30px; font-size:14px; color:#777;">
                        Este es un correo automático, por favor no respondas a este mensaje.
                    </p>
                    
                    <hr>
                    
                    <p style="font-size:12px; color:#999;">
                        © 2026 1ª Marcha - Plataforma de tests de conducción
                    </p>
                    
                </div>
            
            </div>
            """.formatted(nombreUsuario);


        Email from = new Email("alexlpl04@gmail.com"); 
        String subject = "Bienvenido a 1ª Marcha 🚗";
        Email toEmail = new Email(to);
        Content content = new Content("text/html", contenido);

        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);

        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }
}