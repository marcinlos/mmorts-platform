package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 */
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
