public class ScopedValueExample {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
    private static final ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();

    void main() {
        try {  
            THREAD_LOCAL.set("Hello, World!");
            local();
        } finally {
            THREAD_LOCAL.remove();
        }

       ScopedValue.where(SCOPED_VALUE, "Hello, World!").run(this::value);
    }

    private void local() {
        IO.println("Local: " + THREAD_LOCAL.get());
        THREAD_LOCAL.set("Another Value");
        local2();
        IO.println("Local: " + THREAD_LOCAL.get());
    }

    private void local2() {
        IO.println("Local: " + THREAD_LOCAL.get());
    }

    private void value() {
        IO.println("Value: " + SCOPED_VALUE.get());
        ScopedValue.where(SCOPED_VALUE, "Another Value").run(this::value2);
        IO.println("Value: " + SCOPED_VALUE.get());
    }

    private void value2() {
        IO.println("Value: " + SCOPED_VALUE.get());
    }
}