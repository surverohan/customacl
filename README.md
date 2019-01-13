# Custom Principal Builder to SASL PLAINTEST with ACL Validation with out SSL

The principal builder will extract the CN property from the SASL PLAIN and generate the principal "User:XXXX" instead of the default "**User:CN=consumer.test.confluent.io,OU=TEST,O=CONFLUENT,L=PaloAlto,ST=Ca,C=US**" which is fetched from certificate when using TLS/SSL.

## Build the project
```
mvn package
```
This creates the file "target/customacl-0.0.1-SNAPSHOT-jar-with-dependencies.jar" which needs to be added to the JVM classpath.

## Prerequisites
- Configure Kafka brokers with below server.properties
- Create a topic "acltest"
- Create a directory "/Users/yyyyy/Documents/confluent-5.0.0/bin/path" and place the generated the jar (customacl-0.0.1-SNAPSHOT-jar-with-dependencies.jar") into it

### Set JVM classpath
    export CLASSPATH=/Users/yyyyy/Documents/confluent-5.0.0/bin/path/*

### Kafka server.properties
authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer
security.inter.broker.protocol= SASL_PLAINTEXT
sasl.mechanism.inter.broker.protocol=PLAIN
sasl.enabled.mechanisms=PLAIN
allow.everyone.if.no.acl.found=true
super.users=User:kafka
principal.builder.class=com.app.customacl.CustomPrincipalBuilder

## Configure Kafka for kafka_server_jaas.conf
export KAFKA_OPTS="-Djava.security.auth.login.config=../etc/kafka/kafka_server_jaas.conf"

KafkaServer {
   org.apache.kafka.common.security.plain.PlainLoginModule required
   username="kafka"
   password="kafka-secret"
   user_kafka="kafka-secret"
   user_test="test-secret"
   user_acltest="test-secret";
};

## Start Broker
./kafka-server-start ../etc/kafka/server.properties

## Create Topic
./kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic acltest

## Add ACL to Topic
./kafka-acls --authorizer kafka.security.auth.SimpleAclAuthorizer --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:test --operation All --group test-consumer-group --topic acltest --cluster

## Verify ACL
./kafka-acls --authorizer kafka.security.auth.SimpleAclAuthorizer --authorizer-properties zookeeper.connect=localhost:2181 --list

## Test
Test Authentication and Authorized process using KakfaProducerUsingSaslAclWithNoSSL from Project kafkaAuthenticationAndAuthorized