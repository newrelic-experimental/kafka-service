@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__.status

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class __CLASS__HealthCheckTest {

    @Test
    fun healthCheckIsHealthy() {
        val healthCheck = __CLASS__HealthCheck()
        val result = healthCheck.execute()
        assertThat(result.isHealthy).isTrue()
    }
}