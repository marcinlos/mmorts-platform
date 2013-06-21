package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.edu.agh.ki.mmorts.server.core.transaction.Transaction;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionsBeginListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManagerImpl;

/**
 * Simple test and demonstraction of transactionality support. 
 * 
 * @author los
 */
public class TransactionTest {
    
    private ExecutorService exec = Executors.newFixedThreadPool(2);
    
    private TransactionManager tm = new TransactionManagerImpl();
    
    static class Task implements Runnable {
        
        int n;
        TransactionManager manager;
        
        Task(int n, TransactionManager manager) {
            this.manager = manager;
            this.n = n;
        }
        
        @Override
        public void run() {
            manager.begin();
            Transaction t = manager.getCurrent();
            t.addListener(new TransactionListener() {
                @Override
                public void rollback() {
                    System.out.printf("%2d Rollback\n", n);
                }
                
                @Override
                public void commit() {
                    System.out.printf("%2d Commit\n", n);
                }
            });
            t = manager.getCurrent();
            t.addListener(new TransactionListener() {
                @Override
                public void rollback() {
                    System.out.printf("%2d Rollback$$$\n", n);
                }
                
                @Override
                public void commit() {
                    System.out.printf("%2d Commit$$$\n", n);
                }
            });
            try {
                Thread.sleep(1000 + (int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (Math.random() > 0.5) {
                manager.commit();
            } else {
                manager.rollback();
            }
        }
    }
    
    public void run() {
        tm.addListener(new TransactionsBeginListener() {
            @Override
            public void begin(Transaction transaction) {
                System.out.println("Begin");
            }
        });
        for (int i = 0; i < 10; ++ i) {
            exec.execute(new Task(i, tm));
        }
    }

    public static void main(String[] args) {
        new TransactionTest().run();
    }

}
