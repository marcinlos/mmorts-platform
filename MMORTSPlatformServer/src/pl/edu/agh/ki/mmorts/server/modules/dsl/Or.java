package pl.edu.agh.ki.mmorts.server.modules.dsl;

public class Or implements Condition {

    private Condition[] conds;

    public Or(Condition... conds) {
        this.conds = conds;
    }

    @Override
    public boolean holds() {
        for (Condition c: conds) {
            if (c.holds()) {
                return true;
            }
        }
        return false;
    }

}
