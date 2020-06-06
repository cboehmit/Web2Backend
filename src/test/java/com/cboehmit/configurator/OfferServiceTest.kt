package com.cboehmit.configurator

import com.cboehmit.configurator.data.OfferDTO
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class OfferServiceTest (
        @Autowired var offerService: OfferService
){
    @Test
    fun createNewOffer() {
        val offer = OfferDTO(
                firstName = "TestFirstName",
                lastName = "TestLastName",
                email = "test@boehm-it.com"
        )

        val offerId = offerService.createNewOffer(offer).id!!
        val savedOffer = offerService.getOffer(offerId)

        assertEquals(offer.firstName, savedOffer.firstName)
        assertEquals(offer.email, savedOffer.email)
    }
}
