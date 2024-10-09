package com.newrelic.demokafkaservice

import com.fasterxml.jackson.annotation.JsonCreator
import com.newrelic.demokafkaservice.config.ProcessorConfig
import com.newrelic.monitoringeventemitter.BatchingEventProducerService
import com.newrelic.monitoringeventemitter.monitors.JvmMonitorService

data class DemoKafkaServiceConfiguration @JsonCreator constructor(
    val jvmMonitorConfig: JvmMonitorService.Config,
    val monitoringAccountId: Long,
    val batchingEventProducerServiceConfig: BatchingEventProducerService.Config,
    val processorConfig: ProcessorConfig
) : io.dropwizard.Configuration()
