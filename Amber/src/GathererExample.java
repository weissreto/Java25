import static java.util.stream.Gatherers.windowFixed;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;

public class GathererExample {
    void main() {
        IO.println("Using standard gatherer");

        List.of(1,2,3,4,5,6,7)
            .stream()
            // Like collect and Collectors for Stream terminal operations
            // gather and Gatherers are for Stream intermediate operations
            .gather(windowFixed(2))
            .forEach(IO::println); 

        IO.println("Using own gatherer");

        List.of(1,2,3,4,5,6,7)
            .stream()
            .gather(mygather())
            .forEach(IO::println); 
    
        IO.println("Using gatherer with state:");

        List.of(1,2,3,4,5,6,7)
            .stream()
            .gather(mygather2())
            .forEach(IO::println); 
        }   

    Gatherer<Integer, Void, Integer> mygather() {
        return Gatherer.of((_, element, downstream) -> {
            if ((int)element % 2 == 0) {
                return downstream.push(element * 2);
            }
            return true;
        });
    }

    Gatherer<Integer, AtomicInteger, Integer> mygather2() {
        return Gatherer.of(
            () -> new AtomicInteger(1), 
            (state, element, downstream) -> {
                int value = state.get() + element;
                state.set(value);
                return downstream.push(value);
            },
            Gatherer.defaultCombiner(), 
            Gatherer.defaultFinisher());
    }

}
