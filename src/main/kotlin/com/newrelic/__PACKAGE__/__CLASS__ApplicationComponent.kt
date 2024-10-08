@file:Suppress("ktlint")

package com.newrelic.__PACKAGE__

import com.newrelic.idiomancer.dagger2.IdiomancerModule
import dagger.BindsInstance
import dagger.Component
import io.dropwizard.setup.Environment
import javax.inject.Singleton

@Component(modules = [__CLASS__ApplicationModule::class])
@Singleton
interface __CLASS__ApplicationComponent {
    fun wiring(): IdiomancerModule.Wiring

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun environment(environment: Environment): Builder

        @BindsInstance
        fun configuration(configuration: __CLASS__Configuration): Builder
        fun build(): __CLASS__ApplicationComponent
    }
}