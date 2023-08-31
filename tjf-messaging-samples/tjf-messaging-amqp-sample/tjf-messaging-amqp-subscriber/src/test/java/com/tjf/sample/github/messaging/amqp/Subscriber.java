package com.tjf.sample.github.messaging.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class Subscriber {
    public String fooIn = "";

    public String barIn = "";

    @RabbitListener(queues = "foo")
    public void foo(String in) {
        this.fooIn += "foo:" + in;
    }

    @RabbitListener(queues = "bar")
    public void bar(String in) {
        this.barIn += "bar:" + in;
    }

    @RabbitListener(queues = "baz")
    public String baz(String in) {
        return "baz:" + in;
    }
}
