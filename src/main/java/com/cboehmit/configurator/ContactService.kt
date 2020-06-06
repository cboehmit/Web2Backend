package com.cboehmit.configurator

import com.cboehmit.configurator.data.ContactDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ContactService (
        @Autowired val mailingService: MailingService,
        @Value("\${config.mail.smtpUsername}") val companyEmail: String,
        @Value("\${config.template.customerContactTemplate}") val customerContactEmailTemplate: String,
        @Value("\${config.template.customerContactSubject}") val customerContactEmailSubject: String,
        @Value("\${config.template.companyContactTemplate}") val companyContactEmailTemplate: String,
        @Value("\${config.template.companyContactSubject}") val companyContactEmailSubject: String
) {
    fun sendContact(contactDTO: ContactDTO) {
        mailingService.sendMail(companyContactEmailTemplate, companyEmail, companyContactEmailSubject, contactDTO)
        mailingService.sendMail(customerContactEmailTemplate, contactDTO.email, customerContactEmailSubject, contactDTO)
    }
}
