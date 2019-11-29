package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {

        Flux<String> coldFlux = Flux.just("A","B","C","D","E","F")
                .delayElements(Duration.ofSeconds(1));

        coldFlux.subscribe((element) -> System.out.println("Subscriber 1: "+element));

        Thread.sleep(2000);

        coldFlux.subscribe((element) -> System.out.println("Subscriber 2: "+element));

        Thread.sleep(6000);
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {

        Flux<String> hotFlux = Flux.just("A","B","C","D","E","F")
                .delayElements(Duration.ofSeconds(1));


        ConnectableFlux<String> connectableFlux = hotFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(element -> System.out.println("Subscriber 1: "+element));
        Thread.sleep(3000);

        connectableFlux.subscribe(element -> System.out.println("Subscriber 2: "+element));
        Thread.sleep(4000);

    }

}
