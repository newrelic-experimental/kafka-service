@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.newrelic.monitoringeventemitter.BatchingEventProducerService
import com.newrelic.monitoringeventemitter.monitors.JvmMonitorService

data class __CLASS__Configuration @JsonCreator constructor(
    val __CAMEL__Config: String,
    // Make sure to set WRITE_ONLY access on secrets that should not be exposed via the /status/config endpoint
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val exampleSecret: String,
    val jvmMonitorConfig: JvmMonitorService.Config,
    val monitoringAccountId: Long,
    val batchingEventProducerServiceConfig: BatchingEventProducerService.Config
) : io.dropwizard.Configuration()