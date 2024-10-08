package com.newrelic.demokafkaservice

import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.ConfigOverride
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.jersey.client.ClientProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import javax.ws.rs.client.Client

const val EXAMPLE_SECRET = "psssssst"
const val EXAMPLE_CONFIG = "exposedValue"
const val clientReadTimeoutMilliseconds = 30000 // things can be slow when running tests locally in Docker for Mac

@ExtendWith(DropwizardExtensionsSupport::class)
class DemoKafkaServiceApplicationTest {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DemoKafkaServiceApplicationTest::class.java)
        private val appRule = DropwizardAppExtension<DemoKafkaServiceConfiguration>(
            DemoKafkaServiceApplication::class.java,
            ResourceHelpers.resourceFilePath("server.yml"),
            ConfigOverride.config("demoKafkaServiceConfig", EXAMPLE_CONFIG),
            ConfigOverride.config("exampleSecret", EXAMPLE_SECRET)
        )
    }

    private var client: Client? = null

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        client = getClient(testInfo)
    }

    private fun getClient(testInfo: TestInfo): Client {
        val client = JerseyClientBuilder(appRule.environment)
            .build(this.javaClass.simpleName + "#" + testInfo.testMethod.map { it.name }.orElseThrow())
        client.property(ClientProperties.READ_TIMEOUT, clientReadTimeoutMilliseconds)
        return client
    }

    @Test
    fun main_ChecksConfiguration_NoExceptionThrown() {
        DemoKafkaServiceApplication.main(arrayOf("check", ResourceHelpers.resourceFilePath("server.yml")))
    }

    @Test
    fun exposesStatusCheckEndpoint() {
        val response = client!!.target(String.format("http://localhost:%d/status/check", appRule.localPort))
            .request().get()

        LOGGER.info(response.readEntity(String::class.java))
        assertThat(response.status).isEqualTo(200)
    }

    @Test
    fun exposesHistoEndpoint() {
        val response = client!!.target(String.format("http://localhost:%d/status/histo", appRule.localPort))
            .request().get()

        assertThat(response.status).isEqualTo(200)
        val s = response.readEntity(String::class.java)
        LOGGER.info(s)
        assertThat(s).isNotEmpty()
    }

    @Test
    fun exposesHelpEndpoint() {
        val response = client!!.target(String.format("http://localhost:%d/status/help", appRule.localPort))
            .request().get()

        assertThat(response.status).isEqualTo(200)
        val s = response.readEntity(String::class.java)
        LOGGER.info(s)
        assertThat(s).isNotEmpty()
    }

    @Test
    fun exposesServicesEndpoint() {
        val response = client!!.target(String.format("http://localhost:%d/status/services", appRule.localPort))
            .request().get()

        assertThat(response.status).isEqualTo(200)
        val s = response.readEntity(String::class.java)
        LOGGER.info(s)
        assertThat(s).isNotEmpty()
        assertThat(s).contains("isHealthy\":true")
    }

    @Test
    fun exposesConfigEndpoint() {
        val response = client!!.target(String.format("http://localhost:%d/status/config", appRule.localPort))
            .request().get()

        assertThat(response.status).isEqualTo(200)
        val s = response.readEntity(String::class.java)
        LOGGER.info(s)
        assertThat(s).isNotEmpty()
        assertThat(s).contains(EXAMPLE_CONFIG)
    }

    @Test
    fun configEndpointDoesNotExposeSecret() {
        val response = client!!.target(String.format("http://localhost:%d/status/config", appRule.localPort))
            .request().get()

        assertThat(response.status).isEqualTo(200)
        val s = response.readEntity(String::class.java)
        LOGGER.info(s)
        assertThat(s).isNotEmpty()
        assertThat(s).doesNotContain(EXAMPLE_SECRET)
        assertThat(appRule.configuration.exampleSecret).isEqualTo(EXAMPLE_SECRET)
    }
}
