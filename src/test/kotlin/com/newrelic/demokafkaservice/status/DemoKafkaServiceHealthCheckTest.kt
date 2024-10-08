package com.newrelic.demokafkaservice.status

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DemoKafkaServiceHealthCheckTest {

    @Test
    fun healthCheckIsHealthy() {
        val healthCheck = DemoKafkaServiceHealthCheck()
        val result = healthCheck.execute()
        assertThat(result.isHealthy).isTrue()
    }
}
