# __REPO__

(Replace this section with a description of the __REPO__ project.)

## Using this Template

This repository contains a NOS template for an [Idiomancer service](https://source.datanerd.us/commune/idiomancer) written in Kotlin with Dagger 2. Go [here](https://source.datanerd.us/tools/nos)
for more information on how to get started with NOS.

### Template Environment Variables

This template has the following environment variables that you can set in the NOS configuration file (__REPO___nos.conf) before you publish.

* MONITORING_ACCOUNT_ID - This controls the account ID where NodeStatus events are sent, by default it is set to account 1.

NOTE: If you change the MONITORING_ACCOUNT_ID you need to do the following:

1. Get an Insights API key for the account you changed to, you can do that [here](https://staging-insights.newrelic.com/accounts/@MONITORING_ACCOUNT_ID@/manage/api_keys).
2. Write that Insights API key as a secret in vault. [Read here for directions on the vault path to use](https://pages.datanerd.us/site-engineering/nr-platform-docs/grand_central/configuration.html#environmentsecret_env_vars)
3. Update the secret_env_vars entry for the INSIGHTS_API_KEY in your grandcentral.yml to point to the new path.

## JVM Monitoring

This template has support for event based JVM monitoring using the [monitoring-event-emitter](https://nerdlife.datanerd.us/new-relic/improve-your-java-service-monitoring-with-monitoring-event-emitter).
More details on setting up the JVM monitoring are in [this post](https://nerdlife.datanerd.us/new-relic/improve-your-java-service-monitoring-with-monitoring-event-emitter).

The appName attribute for your events is configured by your `NEW_RELIC_APP_NAME` env var which is [auto injected by Grand Central](https://pages.datanerd.us/site-engineering/nr-platform-docs/grand_central/configuration.html#auto-injected-environment-variables).

Why do we use the insights API to insert these events? Because now that we are multi-region it is not trivial to dump your events straight into a topic headed to NRDB. Our customers
don't have the ability to do this and we shouldn't abuse it. For more details read [this post](https://nerdlife.datanerd.us/new-relic/posting-internal-monitoring-data-via-kafka-is-cheating-and-needs-to-stop).

## Developer Setup

Initial setup:

    ./gradlew clean idea

To build the service, run the following command from the project root:

    ./gradlew build

The build task will also run the tests. To skip the tests, use the `-x test` switch.

    ./gradlew build -x test

### Building Locally

To build the docker container:

    ./gradlew dockerBuild

To run the docker container:

    ./gradlew dockerRun

## Build and Deploy

The build & deploy pipeline of this service is managed by [Grand Central](https://grand-central.nr-ops.net).
