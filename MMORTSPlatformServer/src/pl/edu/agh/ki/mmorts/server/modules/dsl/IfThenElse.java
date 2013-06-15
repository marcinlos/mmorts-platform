package pl.edu.agh.ki.mmorts.server.modules.dsl;

public class IfThenElse implements Cont {
    
    private Condition cond;
    private Cont contIf;
    private Cont contElse;

    public IfThenElse(Condition cond, Cont contIf, Cont contElse) {
        this.cond = cond;
        this.contIf = contIf;
        this.contElse = contElse;
    }

    @Override
    public void execute(Control c) {
        if (cond.holds()) {
            c.continueWith(contIf);
        } else {
            c.continueWith(contElse);
        }
    }

}
