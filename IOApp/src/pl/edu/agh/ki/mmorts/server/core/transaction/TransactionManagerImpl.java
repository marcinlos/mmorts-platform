package pl.edu.agh.ki.mmorts.server.core.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Concrete implementation of {@linkplain TransactionManager}, using
 * {@linkplain SequentialTM} as per-thread local managers.
 */
public class TransactionManagerImpl implements TransactionManager {


    /**
     * List of global listeners. Should provide fast reads, otherwise it may be
     * a bottleneck. It is initialized with a {@linkplain CopyOnWriteArrayList}.
     */
    private List<TransactionsBeginListener> listeners;

    /** Per-thread local transaction manager */
/*    private static final ThreadLocal<SequentialTM> localManager = new ThreadLocal<SequentialTM>() {
        @Override
        protected SequentialTM initialValue() {
            return new SequentialTM();
        }
    };*/
    // now it's just one local manager
    private SequentialTM localManager = new SequentialTM();

    ///** Per-thread current transaction */
    //private static final ThreadLocal<Transaction> transaction = new ThreadLocal<Transaction>();
    
    // and one transaction
    private Transaction transaction;

    /**
     * Creates the transaction manager instance.
     */
    public TransactionManagerImpl() {
        listeners = new ArrayList<TransactionsBeginListener>();
    }
    
    /**
     * @return Local transaction manager
     */
    private SequentialTM getManager() {
        return localManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(TransactionsBeginListener listener) {
        listeners.add(listener);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(TransactionsBeginListener listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction getCurrent() {
        return transaction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction begin() {
        if (getCurrent() == null) {
            Transaction t = getManager().createTx();
            transaction = t;
            // Notify all listeners
            for (TransactionsBeginListener listener : listeners) {
                listener.begin(t);
            }
            return t;
        } else {
            throw new TransactionStateException("Cannot nest transactions");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        if (getCurrent() != null) {
            SequentialTM tm = getManager();
            tm.commit();
            transaction = null;
        } else {
            throw new TransactionStateException("No transaction to commit");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() {
        if (getCurrent() != null) {
            SequentialTM tm = getManager();
            tm.rollback();
            transaction = null;
        } else {
            throw new TransactionStateException("No transaction to rollback");
        }
    }

}