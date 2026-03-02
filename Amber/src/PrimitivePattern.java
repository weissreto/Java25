///
/// Execute `java --enable-preview PrimitivePattern.java`
/// 
public class PrimitivePattern {

    void main() {
        long value = 127; // 128, 32768, 2147483648L
        if (value instanceof byte b) {
            IO.println("value is a byte: " + b);
        } else if (value instanceof short s) {
            IO.println("value is a short: " + s);
        } else if (value instanceof int i) {
            IO.println("value is an int: " + i);
        } else if (value instanceof long l) {
            IO.println("value is a long: " + l);
        }

        switch(value) {
            case byte b -> IO.println("value is a byte: " + b);
            case short s -> IO.println("value is a short: " + s);
            case int i -> IO.println("value is an int: "+ i);
            case long l -> IO.println("value is a long: " + l);
        }
    }        
}
