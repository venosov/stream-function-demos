# stream-function-demos
Event Driven with Spring: based on https://www.youtube.com/watch?v=oTTfaynD1Xc&amp;t=2295s

$ docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

---

$ bin/zookeeper-server-start.sh config/zookeeper.properties

$ bin/kafka-server-start.sh config/server.properties

$ bin/kafka-topics.sh --list --zookeeper localhost:2181

```__consumer_offsets
uppercaseecho-in-0
uppercaseecho-out-0
```
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic uppercaseecho-in-0






