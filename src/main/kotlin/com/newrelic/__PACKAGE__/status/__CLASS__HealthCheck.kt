@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__.status

import com.codahale.metrics.health.HealthCheck
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class __CLASS__HealthCheck @Inject
constructor() : HealthCheck() {

    @Throws(Exception::class)
    override fun check(): Result {
        return Result.healthy()
    }
}