///
/// If you launch with `Java CallSuper.java` in the terminal, the class SuperClass in the same folder is also found and compiled
/// 
public class CallSuper extends SuperClass {

    public CallSuper(String str) {
        // You can now have statements before you need to call super, but you must call super before you can use this or return from the constructor
        if (str == null) {
            throw new IllegalArgumentException("NPE str is null");
        }
        super(str);
    }

    public CallSuper(String str, boolean trim) {
        // You can now have statements before you need to call another constructor (e.g. this(...)), but you must call this(...) before you can use this or return from the constructor
        if (trim && str != null) {
            str = str.trim();
        }
        this(str);
    }

    static void main() {
        new CallSuper(" Hello, World! ", true).print();
    }
}
