package com.cboehmit.configurator.data

data class OfferDTO(
        var id: String? = null,
        var firstName: String,
        var lastName: String,
        var company: String? = null,
        var phone: String? = null,
        var email: String
)
