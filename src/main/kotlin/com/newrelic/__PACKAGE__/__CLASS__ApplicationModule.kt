@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__

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
class __CLASS__ApplicationModule {
    @Provides
    @Singleton
    fun autoServiceMembersNotDirectlyRequiredByOtherPartsOfTheObjectGraph(jvmMonitorService: JvmMonitorService): Set<AutoService> {
        return setOf<AutoService>(jvmMonitorService)
    }

    @Provides
    @Singleton
    fun get__CLASS__Configuration(__CAMEL__Configuration: __CLASS__Configuration): Configuration {
        return __CAMEL__Configuration
    }

    @Provides
    @Singleton
    fun getSingleEventProducer(
        __CAMEL__Configuration: __CLASS__Configuration,
        autoServices: AutoServices
    ): SingleEventProducer {
        val batchingEventProducerConfig = __CAMEL__Configuration.batchingEventProducerServiceConfig
        val accountId = __CAMEL__Configuration.monitoringAccountId
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
        __CAMEL__Configuration: __CLASS__Configuration,
        singleEventProducer: SingleEventProducer,
        autoServices: AutoServices
    ): JvmMonitorService {
        val jvmMonitorConfig = __CAMEL__Configuration.jvmMonitorConfig
        return JvmMonitorService(autoServices, jvmMonitorConfig, singleEventProducer)
    }
}