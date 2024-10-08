package com.newrelic.demokafkaservice

import com.google.common.collect.ImmutableMap
import com.newrelic.autoservices.AutoService
import com.newrelic.autoservices.AutoServices
import com.newrelic.idiomancer.dagger2.GrandCentralModule
import com.newrelic.idiomancer.dagger2.IdiomancerModule
import com.newrelic.monitoringeventemitter.AbstractSendStrategy
import com.newrelic.monitoringeventemitter.BatchingEventProducerService
import com.newrelic.monitoringeventemitter.SingleEventProducer
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
    fun autoServiceMembersNotDirectlyRequiredByOtherPartsOfTheObjectGraph(jvmMonitorService: JvmMonitorService): Set<AutoService> {
        return setOf<AutoService>(jvmMonitorService)
    }

    @Provides
    @Singleton
    fun getDemoKafkaServiceConfiguration(demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration): Configuration {
        return demoKafkaServiceConfiguration
    }

    @Provides
    @Singleton
    fun getSingleEventProducer(
        demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration,
        autoServices: AutoServices
    ): SingleEventProducer {
        val batchingEventProducerConfig = demoKafkaServiceConfiguration.batchingEventProducerServiceConfig
        val accountId = demoKafkaServiceConfiguration.monitoringAccountId
        val okHttpClient = OkHttpClient()
        val sendStrategy = AbstractSendStrategy.builder()
            .withInsightsApi(okHttpClient, batchingEventProducerConfig.insightsApiUri, batchingEventProducerConfig.insightsApiKey)
            .build()
        val rawAttributes = ImmutableMap.of<String, Any>()
        return BatchingEventProducerService(autoServices, sendStrategy, accountId, batchingEventProducerConfig, rawAttributes)
    }

    @Provides
    @Singleton
    fun getJvmMonitorService(
        demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration,
        singleEventProducer: SingleEventProducer,
        autoServices: AutoServices
    ): JvmMonitorService {
        val jvmMonitorConfig = demoKafkaServiceConfiguration.jvmMonitorConfig
        return JvmMonitorService(autoServices, jvmMonitorConfig, singleEventProducer)
    }
}