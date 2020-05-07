package com.learnreactivespring.fluxandmonoplayground;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sun.security.x509.OtherName;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling(){

        Flux<String> fluxString = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.out.println("Exception occurred" + e);
                    return Flux.just("default", "default1");
                        }
                )
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                .expectNext("default","default1")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandling_onErrorReturn(){

        Flux<String> fluxString = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default")
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandling_onErrorMap(){

        Flux<String> fluxString = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetry(){

        Flux<String> fluxString = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(1)
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetryBackoff(){

        Flux<String> fluxString = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Runtime Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retryBackoff(1, Duration.ofSeconds(5))
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(IllegalStateException.class)
                .verify();

    }

    @Test
    public void monoErrorHandling_onErrorMap_withThen(){

        Mono<String> monoString = Mono.just("A").log()
                .then(Mono.error(new RuntimeException("Runtime Exception Occurred")))
                .then(Mono.just("B").log())
                .onErrorMap((e) ->
                        new CustomException(e)
                );

        StepVerifier.create(monoString.log())
                .expectError(CustomException.class)
                .verify();

    }

}
