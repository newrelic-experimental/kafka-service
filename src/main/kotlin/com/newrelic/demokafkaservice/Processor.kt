package com.newrelic.demokafkaservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.newrelic.api.agent.NewRelic
import com.newrelic.autoservices.AbstractScheduledAutoService
import com.newrelic.autoservices.AutoServices
import com.newrelic.demokafkaservice.config.ProcessorConfig
import com.newrelic.demokafkaservice.kafka.ErrorProducer
import com.newrelic.monitoringeventemitter.MonitoringRecorder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class Processor
@Inject constructor(
    autoServices: AutoServices,
    private val config: ProcessorConfig,
    private val kafkaProducer: ErrorProducer,
    private val monitoringRecorder: MonitoringRecorder
) :
    AbstractScheduledAutoService(autoServices) {
    companion object {
        private val logger = LoggerFactory.getLogger(Processor::class.java.name)
        private val messagesSent = mutableListOf<Map<String, Any>>()
    }

    override fun runOneIteration() {
        monitoringRecorder.incrementCounter("demo-kafka-service.iterations.ran", 1)

        val messagesToSend = Random.nextInt(1000, 10000)
        val messagePayloads = generatePayloads(messagesToSend)

        logger.info("Sending $messagesToSend events to kafka")
        messagePayloads.forEach { payload ->
            try {
                if (config.explodeLogCount) logger.info("sending event with attributes: ${payload.keys}")

                val serializedPayload = jacksonObjectMapper().writeValueAsBytes(payload)
                kafkaProducer.sendEvent(serializedPayload)
            } catch (ex: Exception) {
                if (config.noticeError) NewRelic.noticeError(ex)

                if (config.logError) logger.error("Failed to send kafka event: ${ex.message}", ex)
            }
        }

        if (config.explodeMemory) messagesSent.addAll(messagePayloads)

        monitoringRecorder.incrementCounter(
            "demo-kafka-service.iterations.completed",
            1,
            mapOf("messagesSent" to messagesToSend)
        )
        logger.info("Finished iteration")
    }

    override fun scheduler(): Scheduler {
        return Scheduler.newFixedRateSchedule(180, 60, TimeUnit.SECONDS)
    }

    private fun generatePayloads(messagesToSend: Int): List<Map<String, Any>> {
        val payloads = mutableListOf<Map<String, Any>>()
        for (i in 1..messagesToSend) {
            payloads.add(
                mapOf(
                    "attribute_1" to i,
                    "attribute_2" to "foo"
                )
            )
        }
        return payloads
    }
}
