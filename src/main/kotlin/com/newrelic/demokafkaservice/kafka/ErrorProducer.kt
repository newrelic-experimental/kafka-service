package com.newrelic.demokafkaservice.kafka

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorProducer
@Inject constructor() {
    fun sendEvent(event: ByteArray) {
        return
    }
}
