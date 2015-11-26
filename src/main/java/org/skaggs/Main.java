package org.skaggs;

import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

public class Main {

    public static void main(String[] args) {
        Properties p = new Properties().setInt(Key.IntKey.INT_POPULATION, 500).lock();
        System.out.println("Hello world!");
        System.out.println(p);
    }
}
