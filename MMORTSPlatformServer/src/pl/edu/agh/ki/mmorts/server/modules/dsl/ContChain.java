package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ContChain implements Cont {

    private List<Cont> contList = new ArrayList<Cont>();

    public ContChain(Cont... conts) {
        this(Arrays.asList(conts));
    }

    public ContChain(Iterable<Cont> conts) {
        for (Cont cont : conts) {
            contList.add(cont);
        }
    }

    class NextCont implements Cont {

        Iterator<Cont> it;

        NextCont(Iterator<Cont> it) {
            this.it = it;
        }

        @Override
        public void execute(Control c) {
            if (it.hasNext()) {
                Cont cont = it.next();
                c.continueWith(new NextCont(it));
                cont.execute(c);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Control c) {
        c.continueWith(new NextCont(contList.iterator()));
    }

}
