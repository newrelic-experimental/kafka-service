package com.newrelic.demokafkaservice.status

import com.codahale.metrics.health.HealthCheck
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoKafkaServiceHealthCheck @Inject
constructor() : HealthCheck() {

    @Throws(Exception::class)
    override fun check(): Result {
        return Result.healthy()
    }
}
