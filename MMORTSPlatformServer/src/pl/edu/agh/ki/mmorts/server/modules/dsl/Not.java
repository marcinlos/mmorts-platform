package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 */
public class Not implements Condition {

    private Condition cond;
    
    public Not(Condition cond) {
        this.cond = cond;
    }

    @Override
    public boolean holds() {
        return ! cond.holds();
    }

}
