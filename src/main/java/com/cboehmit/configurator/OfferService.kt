package com.cboehmit.configurator

import com.cboehmit.configurator.data.OfferDTO
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class OfferService (
        @Autowired var mailingService: MailingService,
        @Value("\${config.offer.folder}") val offerFolder: String,
        @Value("\${config.mail.smtpUsername}") val companyEmail: String,
        @Value("\${config.template.customerOfferTemplate}") val customerOfferEmailTemplate: String,
        @Value("\${config.template.customerOfferSubject}") val customerOfferEmailSubject: String,
        @Value("\${config.template.companyOfferTemplate}") val companyOfferEmailTemplate: String,
        @Value("\${config.template.companyOfferSubject}") val companyOfferEmailSubject: String
){
    private val mapper = ObjectMapper().registerModule(KotlinModule())

    fun getOffer(offerId: String): OfferDTO {
        return mapper.readValue(File(File(offerFolder, offerId), "offer.json"), object : TypeReference<OfferDTO>() {})
    }

    fun createNewOffer(offer: OfferDTO): OfferDTO {
        // Generating new Id
        offer.id = UUID.randomUUID().toString()

        // Saving file on filesystem
        val folder = File(offerFolder, offer.id)
        val file = File(folder, "offer.json")
        if(!folder.exists()) folder.mkdirs()
        file.writeText(mapper.writeValueAsString(offer))

        // Sending out emails as result
        mailingService.sendMail(companyOfferEmailTemplate, companyEmail, companyOfferEmailSubject, offer)
        mailingService.sendMail(customerOfferEmailTemplate, offer.email, customerOfferEmailSubject, offer)

        return offer
    }
}
