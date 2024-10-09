package com.newrelic.demokafkaservice

import com.google.common.collect.ImmutableMap
import com.newrelic.api.agent.NewRelic
import com.newrelic.autoservices.AutoService
import com.newrelic.autoservices.AutoServices
import com.newrelic.demokafkaservice.config.ProcessorConfig
import com.newrelic.demokafkaservice.kafka.ErrorProducer
import com.newrelic.idiomancer.dagger2.GrandCentralModule
import com.newrelic.idiomancer.dagger2.IdiomancerModule
import com.newrelic.monitoringeventemitter.AbstractSendStrategy
import com.newrelic.monitoringeventemitter.MonitoringApi
import com.newrelic.monitoringeventemitter.MonitoringRecorder
import com.newrelic.monitoringeventemitter.monitors.JvmMonitorService
import dagger.Module
import dagger.Provides
import io.dropwizard.Configuration
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [IdiomancerModule::class, GrandCentralModule::class])
class DemoKafkaServiceApplicationModule {
    @Provides
    @Singleton
    fun autoServiceMembersNotDirectlyRequiredByOtherPartsOfTheObjectGraph(
        jvmMonitorService: JvmMonitorService,
        processor: Processor
    ): Set<AutoService> {
        return setOf<AutoService>(jvmMonitorService, processor)
    }

    @Provides
    @Singleton
    fun getDemoKafkaServiceConfiguration(demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration): Configuration {
        return demoKafkaServiceConfiguration
    }

    @Provides
    @Singleton
    fun getProcessorConfiguration(demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration): ProcessorConfig {
        return demoKafkaServiceConfiguration.processorConfig
    }

    @Provides
    @Singleton
    fun getMonitoringRecorder(
        config: DemoKafkaServiceConfiguration,
        autoServices: AutoServices
    ): MonitoringRecorder {
        val batchingEventProducerConfig = config.batchingEventProducerServiceConfig
        val accountId = config.monitoringAccountId
        val okHttpClient = OkHttpClient()
        val sendStrategy = AbstractSendStrategy.builder()
            .withConfig(batchingEventProducerConfig, okHttpClient)
            .withExplicitClientClose(true)
            .useNoticeError(true)
            .withErrorHandler { e, attr -> NewRelic.noticeError(e, attr, true) }
            .build()

        val rawAttributes = ImmutableMap.of<String, Any>()
        return MonitoringApi(
            autoServices,
            sendStrategy,
            accountId,
            batchingEventProducerConfig,
            rawAttributes
        )
    }

    @Provides
    @Singleton
    fun getJvmMonitorService(
        demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration,
        monitoringRecorder: MonitoringRecorder,
        autoServices: AutoServices
    ): JvmMonitorService {
        val jvmMonitorConfig = demoKafkaServiceConfiguration.jvmMonitorConfig
        return JvmMonitorService(autoServices, jvmMonitorConfig, monitoringRecorder)
    }

    @Provides
    @Singleton
    fun getProcessor(
        autoServices: AutoServices,
        processorConfig: ProcessorConfig,
        kafkaProducer: ErrorProducer,
        monitoringRecorder: MonitoringRecorder
    ): Processor {
        return Processor(autoServices, processorConfig, kafkaProducer, monitoringRecorder)
    }
}
