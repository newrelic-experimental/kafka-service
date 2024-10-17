package com.newrelic.demokafkaservice

import com.newrelic.autoservices.AbstractScheduledAutoService
import com.newrelic.autoservices.AutoServices
import com.newrelic.demokafkaservice.config.ProcessorConfig
import com.newrelic.demokafkaservice.kafka.ErrorProducer
import com.newrelic.monitoringeventemitter.MonitoringRecorder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

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

        // TODO: Build out kafka publishing logic

        monitoringRecorder.incrementCounter(
            "demo-kafka-service.iterations.completed",
            1
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
