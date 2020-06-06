package com.cboehmit.configurator

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class MailingServiceTest (
        @Autowired var mailingService: MailingService
){
    val map = hashMapOf<String, Any>().apply {
        put("firstName", "Testwert 123")
        put("lastName", "Test 2")
    }

    @Test
    fun sendMail() {
        mailingService.sendMail("test.ftl", "christoph.boehm.ext@ogilvy.com", "Test", map)
    }

    @Test
    fun generateEmail() {
        val email:String
        email = mailingService.generateEmail("test.ftl", map)

        assertTrue(email.startsWith("<!DOCTYPE"))
        assertTrue(email.contains("Testwert 123"))
    }
}
