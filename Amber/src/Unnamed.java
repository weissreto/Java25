import java.util.List;

public class Unnamed {

    void main() {
        Object str = "Hello, World!";
        switch(str) {
            case String s -> System.out.println("Matched String: " + s);
            /// Unused pattern variables can now be declared with _
            case Object _ -> System.out.println("Matched Object");
        }

        int total = 0;
        /// Unused variables can now be declared with _
        for (var  _ : List.of(1,2,3,4,5)) {
            total++;
        }
        IO.println(total);
    }
}
