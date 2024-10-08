@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class __CLASS__Application : io.dropwizard.Application<__CLASS__Configuration>() {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            __CLASS__Application().run(*args)
        }
    }

    override fun run(__CAMEL__Configuration: __CLASS__Configuration, environment: Environment) {
        val component = Dagger__CLASS__ApplicationComponent.builder()
            .configuration(__CAMEL__Configuration)
            .environment(environment)
            .build()
        component.wiring()
    }

    override fun initialize(bootstrap: Bootstrap<__CLASS__Configuration>) {
        bootstrap.objectMapper.registerModule(KotlinModule.Builder().build())
        // __CLASS__Configuration should merge in values from Environment Variables
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(bootstrap.configurationSourceProvider, EnvironmentVariableSubstitutor(false))
    }
}