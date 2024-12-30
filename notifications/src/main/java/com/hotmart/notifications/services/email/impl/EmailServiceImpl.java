package com.hotmart.notifications.services.email.impl;

import com.hotmart.notifications.dto.event.AccessDataEventDTO;
import com.hotmart.notifications.dto.event.EventDTO;
import com.hotmart.notifications.dto.event.RecoverPasswordEventDTO;
import com.hotmart.notifications.dto.response.NotificationsResponseDTO;
import com.hotmart.notifications.enums.SentStatus;
import com.hotmart.notifications.enums.TemplateType;
import com.hotmart.notifications.services.email.EmailService;
import com.hotmart.notifications.services.notifications.NotificationsService;
import com.hotmart.notifications.utils.JsonUtil;
import com.hotmart.notifications.utils.NotificationsUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service("myEmailService")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JsonUtil jsonUtil;
    private final NotificationsUtils notificationsUtils;
    private final NotificationsService service;

    @Override
    public void execute(EventDTO event, String payload) {
        NotificationsResponseDTO notification = service.save(event);

        try {
            TemplateType template = event.getTemplate();
            switch (template) {
                case CHANGE_PASSWORD -> sendEmailRecoverPassword(payload);
                case DATA_ACCESS -> sendEmailDataAccess(payload);
            }

            service.viewedOrSent(notification.getId(), SentStatus.SENT);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação via e-mail: ", e);
            service.viewedOrSent(notification.getId(), SentStatus.FAILED);
        }
    }

    private void sendEmailRecoverPassword(String payload) {
        RecoverPasswordEventDTO event = jsonUtil.toRecoverPasswordEvent(payload);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper email = notificationsUtils.createConfigEmail(mimeMessage, event.getEmail(), "Altere sua" +
                    " senha");

            Context context = new Context(LocaleContextHolder.getLocale());
            context.setVariable("pswdrst", event.getPswdrst());
            context.setVariable("uuid", event.getUuid());

            final String html = templateEngine.process(event.getTemplate().getPage(), context);

            email.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmailDataAccess(String payload) {
        AccessDataEventDTO event = jsonUtil.toAccessDataEvent(payload);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper email = notificationsUtils.createConfigEmail(mimeMessage, event.getEmail(), "[Hotmart] " +
                    "Acesso aos Dados");

            Context context = new Context(LocaleContextHolder.getLocale());
            context.setVariable("name", event.getName());
            context.setVariable("uuid", event.getUuidAccessData());

            final String html = templateEngine.process(event.getTemplate().getPage(), context);

            email.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
