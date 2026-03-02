import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

///
/// Execute `Java --enable-native-access=ALL-UNNAMED FFM.java` in the terminal.
/// 
public class FFM {
    void main() throws Throwable {
        // Lookup the native function "strlen" from the C standard library
        Linker linker = Linker.nativeLinker();
        MemorySegment symbol = linker.defaultLookup().find("strlen").orElseThrow();

        // Bind a method handle to the strlen function, specifying the function descriptor (return type and parameter types)
        FunctionDescriptor functionDescr = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
        MethodHandle strlen = linker.downcallHandle(symbol, functionDescr);

        // Open an Arena to manage native memory
        try (var arena = Arena.ofConfined()) {
            // Allocate a native memory segment and copy the string "Hello" into it
            MemorySegment str = arena.allocateFrom("Hello");
            // Call the native strlen function using the method handle
            long len = (long) strlen.invoke(str);
            IO.println("Length: " + len);
        }
    }
}