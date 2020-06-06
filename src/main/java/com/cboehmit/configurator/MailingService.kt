package com.cboehmit.configurator

import freemarker.template.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.StringWriter
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class MailingService (
        @Value("\${config.mail.debug}") private val debug: Boolean,
        @Value("\${config.mail.smtpServer}") private val smtpServer: String,
        @Value("\${config.mail.smtpPort}") private val smtpPort: String,
        @Value("\${config.mail.smtpUsername}") private val smtpUsername: String,
        @Value("\${config.mail.smtpPassword}") private val smtpPassword: String,
        @Value("\${config.template.templateFolder}") private val templateFolder: String
) {
    private var session: Session = Session.getInstance(
            Properties().apply {
                put("mail.smtp.auth", true)
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", smtpServer)
                put("mail.smtp.port", smtpPort)
                put("mail.smtp.ssl.trust", smtpServer)
            }
    )

    private var templateConfiguration: Configuration = Configuration(Configuration.VERSION_2_3_30).apply {
        defaultEncoding = "UTF-8"
        setDirectoryForTemplateLoading(File(templateFolder))
    }

    fun sendMail(template: String, email: String, subject: String, data: Any) {
        val msgText = generateEmail(template, data)
        val msg = MimeMessage(session).apply {
            setFrom(InternetAddress(smtpUsername))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
            setRecipients(Message.RecipientType.BCC, InternetAddress.parse(smtpUsername))
            setSubject(subject)
            setContent(msgText, "text/html")
        }

        val trans: Transport = session.getTransport("smtp")

        if(!debug) {
            try {
                trans.connect(smtpServer, smtpUsername, smtpPassword)
                trans.sendMessage(msg, msg.allRecipients)
            } finally {
                trans.close()
            }
        }
    }

    fun generateEmail(template: String, data: Any): String {
        val writer = StringWriter()
        val tmpl = templateConfiguration.getTemplate(template)
        tmpl.process(data, writer)

        return writer.toString()
    }
}
