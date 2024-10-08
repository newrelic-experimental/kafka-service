package com.newrelic.demokafkaservice

import com.newrelic.idiomancer.dagger2.IdiomancerModule
import dagger.BindsInstance
import dagger.Component
import io.dropwizard.setup.Environment
import javax.inject.Singleton

@Component(modules = [DemoKafkaServiceApplicationModule::class])
@Singleton
interface DemoKafkaServiceApplicationComponent {
    fun wiring(): IdiomancerModule.Wiring

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun environment(environment: Environment): Builder

        @BindsInstance
        fun configuration(configuration: DemoKafkaServiceConfiguration): Builder
        fun build(): DemoKafkaServiceApplicationComponent
    }
}