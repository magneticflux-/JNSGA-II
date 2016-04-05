package org.jnsgaii.computeserver;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

/**
 * Created by skaggsm on 3/31/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Naming.rebind("rmi://localhost/Test", new Test());
            Runnable r = (Runnable) Naming.lookup("rmi://localhost/Test");
            r.run();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
