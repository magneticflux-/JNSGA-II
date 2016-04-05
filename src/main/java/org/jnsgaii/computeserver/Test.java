package org.jnsgaii.computeserver;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Created by skaggsm on 4/1/16.
 */
public class Test implements Remote, Runnable, Serializable {

    public final static long serialVersionUID = 1L;

    @Override
    public void run() {
        System.out.println("Ran");
    }
}
