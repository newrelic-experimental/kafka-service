package com.newrelic.demokafkaservice

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class DemoKafkaServiceApplication : io.dropwizard.Application<DemoKafkaServiceConfiguration>() {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            DemoKafkaServiceApplication().run(*args)
        }
    }

    override fun run(demoKafkaServiceConfiguration: DemoKafkaServiceConfiguration, environment: Environment) {
        val component = DaggerDemoKafkaServiceApplicationComponent.builder()
            .configuration(demoKafkaServiceConfiguration)
            .environment(environment)
            .build()
        component.wiring()
    }

    override fun initialize(bootstrap: Bootstrap<DemoKafkaServiceConfiguration>) {
        bootstrap.objectMapper.registerModule(KotlinModule.Builder().build())
        // DemoKafkaServiceConfiguration should merge in values from Environment Variables
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(bootstrap.configurationSourceProvider, EnvironmentVariableSubstitutor(false))
    }
}