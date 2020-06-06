package com.cboehmit.configurator

import com.cboehmit.configurator.data.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.util.*

@RestController
class WebController (
        @Autowired var contactService: ContactService,
        @Autowired var offerService: OfferService
)
{
    val logger = LoggerFactory.getLogger(javaClass)
    val mapper = ObjectMapper().registerModule(KotlinModule())

    @PostMapping("/contact")
    fun contact(@RequestBody c: ContactDTO): ResponseEntity<String> {
        logger.info("New contact: \n ${mapper.writeValueAsString(c)}")

        try {
            contactService.sendContact(c)
            return ResponseEntity.ok("OK")
        } catch (ex: Exception) {
            return ResponseEntity.badRequest().body("ERROR")
        }
    }

    @GetMapping("/offer/{offerId}")
    fun getOffer(@PathVariable offerId: String): ResponseEntity<OfferDTO> {
        return ResponseEntity.ok(offerService.getOffer(offerId))
    }

    @PostMapping("/offer")
    fun createOffer(@RequestBody o: OfferDTO): ResponseEntity<OfferDTO> {
        var o2 = offerService.createNewOffer(o)
        return ResponseEntity.ok(o2)
    }
}
