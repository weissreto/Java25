import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import javax.management.RuntimeErrorException;

///
/// Execute this example using the command `Java --enable-preview StructuredConcurrency.java` in the terminal.
/// 
public class StructuredConcurrency {
    private static final Random RANDOM = new Random();

    void main() {
        IO.println(all());
        IO.println(any());
    }

    record Result(String result1, String result2) {};

    private Result all() {
        try (var scope = StructuredTaskScope.open()) {
            var subTask1 = scope.fork(this::task1);
            var subTask2 = scope.fork(this::task2);
            scope.join(); // Wait for all tasks to complete
            return new Result(subTask1.get(), subTask2.get());
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String any() {
        try (var scope = StructuredTaskScope.open(Joiner.anySuccessfulResultOrThrow())) {
            var subTask1 = scope.fork(this::task1);
            var subTask2 = scope.fork(this::task2);
            scope.join(); // Wait for any task to complete
            if (subTask1.state() == State.SUCCESS){
                return subTask1.get();
            }
            return subTask2.get();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String task1() throws InterruptedException {
        Thread.sleep(RANDOM.nextInt(5000));
        return "Hello";
    }

    private String task2() throws InterruptedException {
        Thread.sleep(RANDOM.nextInt(5000));
        return "World";
    }
}   
