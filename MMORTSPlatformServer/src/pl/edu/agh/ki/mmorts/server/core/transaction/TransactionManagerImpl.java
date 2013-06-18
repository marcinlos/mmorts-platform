package pl.edu.agh.ki.mmorts.server.core.transaction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

/**
 * Concrete implementation of {@linkplain TransactionManager}, using
 * {@linkplain SequentialTM} as per-thread local managers.
 * 
 * @author los
 */
public class TransactionManagerImpl implements TransactionManager {

    private static final Logger logger = Logger
            .getLogger(TransactionManagerImpl.class);

    /**
     * List of global listeners. Should provide fast reads, otherwise it may be
     * a bottleneck. It is initialized with a {@linkplain CopyOnWriteArrayList}.
     */
    private List<TransactionsBeginListener> listeners;

    /** Per-thread local transaction manager */
    private static final ThreadLocal<SequentialTM> localManager = new ThreadLocal<SequentialTM>() {
        @Override
        protected SequentialTM initialValue() {
            return new SequentialTM();
        }
    };

    /** Per-thread current transaction */
    private static final ThreadLocal<Transaction> transaction = new ThreadLocal<Transaction>();

    /**
     * Creates the transaction manager instance.
     */
    public TransactionManagerImpl() {
        logger.debug("Initializing");
        listeners = new CopyOnWriteArrayList<TransactionsBeginListener>();
    }
    
    /**
     * @return Local transaction manager
     */
    private SequentialTM getManager() {
        return localManager.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(TransactionsBeginListener listener) {
        logger.debug("Adding listener");
        listeners.add(listener);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(TransactionsBeginListener listener) {
        logger.debug("Removing listener");
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction getCurrent() {
        return transaction.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction begin() {
        if (getCurrent() == null) {
            Transaction t = getManager().createTx();
            transaction.set(t);
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
            transaction.remove();
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
            transaction.remove();
        } else {
            throw new TransactionStateException("No transaction to rollback");
        }
    }

}
