package pl.edu.agh.ki.mmorts.server.modules.dsl;

public class And implements Condition {
    
    private Condition[] conds;

    public And(Condition... conds) {
        this.conds = conds;
    }

    @Override
    public boolean holds() {
        for (Condition c: conds) {
            if (! c.holds()) {
                return false;
            }
        }
        return true;
    }
    
}
