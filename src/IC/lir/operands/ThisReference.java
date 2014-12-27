package IC.lir.operands;

public class ThisReference extends Memory {

    public ThisReference() {
        super(null);
    }

    @Override
    public String toString() {
        return "this";
    }
}
