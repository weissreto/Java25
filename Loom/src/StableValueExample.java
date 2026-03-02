import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

///
/// Execute this example using the command `Java --enable-preview StableValueExample.java` in the terminal.
/// 
public class StableValueExample {
    private static final StableValue<String> STABLE_VALUE = StableValue.of();
    private static final Supplier<String> STABLE_VALUE_2 = StableValue.supplier(() -> "Hello World");
    private static final Map<String, Integer> STABLE_MAP = StableValue.map(Set.of("Hello", "World"), String::length);
    private static final List<String> STABLE_LIST = StableValue.list(5, i -> "Value " + i);
    
    void main() {
        // JIT compiler will optimize the code and constant fold the value.
        IO.println(getValue());  // JIT Compiles to IO.println("Hello, World!");

        IO.println(STABLE_VALUE_2.get());  // JIT Compiles to IO.println("Hello, World!");

        IO.println(STABLE_MAP.get("Hello"));  // JIT Compiles to IO.println(5);
        
        IO.println(STABLE_LIST.get(2)); // JIT Compiles to IO.println("Value 2");
    }

    private String getValue() {
        // Multithreading safe. The supplier will only be called once, and the value will be cached for subsequent calls.
        // API will change in future
        return STABLE_VALUE.orElseSet(() -> "Hello, World!");
    }
}
