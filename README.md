# demo-kafka-service

(Replace this section with a description of the demo-kafka-service project.)


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
