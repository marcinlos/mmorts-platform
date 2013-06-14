package com.example;

import pl.edu.agh.ki.mmorts.server.core.Init;

public class DispatcherTest {

    public static void main(final String[] args) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                new Init(args);
            }
        });
    }


}
