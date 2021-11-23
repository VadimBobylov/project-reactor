import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;


public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        Mono<Integer> mono = Mono.just(333);
        Flux<Integer> flux = Flux.just(10, 33, 1, 49);

        Flux<Integer> fluxFromMono = mono.flux();
//        System.out.println(fluxFromMono.blockFirst());

        Mono<Boolean> monoAny = flux.any(s -> s.equals(41));
//        System.out.println(monoAny.block());

        Mono<Integer> integerMono = flux.elementAt(0);
//        System.out.println(integerMono.block());

        Flux<Integer> range1 = Flux.range(1, 5);
//        System.out.println(range1.blockFirst());
//        System.out.println(range1.elementAt(0).block());
//        System.out.println(range1.blockLast());

        Flux.range(1, 10)
                .subscribe(s -> {
//                        System.out.println("value: " + s);
                });

        Flux.fromIterable(List.of("one", "two", "three", "a", "b"))
                .subscribe(s -> {
//                    System.out.println("value: " + s);
                });


        //example 1 Flux:
        Flux
                .generate(sink -> {
                    sink.next("hello");
                })
                //уходит в параллельный поток
                .delayElements(Duration.ofMillis(1000))
                .take(3)
                .subscribe(s -> {
//                    System.out.println("value: " + s.toString());
                });

        System.out.println("===============");
        //example 1 Java core:
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(1000);
                    var simulateSinkValue = "hello";
//                    System.out.println("simulate value: " + simulateSinkValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //нужно подождать поток "delayElements(Duration.ofMillis(500))"
        //а так же поток new Thread(()->{
//        Thread.sleep(4000);
        //finish example one


        Flux
                .generate(
                        () -> 1234,
                        (state, sink) -> {
                            if (state > 1250) {
                                sink.complete();
                            } else {
                                sink.next("step:" + state);
                            }
                            return state + 3;
                        }
                ).subscribe(x -> {
            System.out.println("value:" + x);
        });
        //https://youtu.be/77-wOZs2nPE?t=893


    }
}
