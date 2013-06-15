package pl.edu.agh.ki.mmorts.server.modules.dsl;

public class While implements Cont {
    
    private Condition cond;
    private Cont body;

    public While(Condition cond, Cont body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public void execute(Control c) {
        if (cond.holds()) {
            c.continueWith(this);
            c.continueWith(body);
        }
    }

}
