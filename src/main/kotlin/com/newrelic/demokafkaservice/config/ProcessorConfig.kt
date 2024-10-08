package com.newrelic.demokafkaservice.config

data class ProcessorConfig(
    val explodeLogCount: Boolean,
    val noticeError: Boolean,
    val logError: Boolean,
    val explodeMemory: Boolean
)
