package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam","anna","jack","jenny");

    @Test
    public void fluxUsingIterable(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                .log();

        StepVerifier.create(namesFlux)
            .expectNext("adam","anna","jack","jenny")
            .verifyComplete();
    }

    @Test
    public void FluxUsingArray(){

        String[] names = new String[]{"adam","anna","jack","jenny"};

        Flux<String> namesFlux = Flux.fromArray(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }

    @Test
    public void FluxUsingStream(){

        Flux<String> namesFlux = Flux.fromStream(names.stream())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){

        Mono<String> mono = Mono.justOrEmpty(null);

        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){

        Supplier<String> name = () -> "adam";
        Mono<String> nameMono = Mono.fromSupplier(name)
                .log();

        System.out.println(name.get());

        StepVerifier.create(nameMono)
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> integerFlux = Flux.range(1,5).log();

        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

}
