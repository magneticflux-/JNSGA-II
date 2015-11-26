package org.skaggs.ec.properties;

import org.skaggs.ec.exceptions.NoValueSetException;
import org.skaggs.ec.exceptions.ObjectLockedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class Properties {
    private static final Map<Key, Object> defaultValues;

    static {
        Map<Key, Object> v = new HashMap<>();

        v.put(Key.IntKey.INT_POPULATION, null);

        v.put(Key.BooleanKey.BOOLEAN_THREADED, true);

        v.put(Key.DoubleKey.DOUBLE_ELITE_FRACTION, 0);

        defaultValues = Collections.unmodifiableMap(v);
    }

    private final HashMap<Key, Object> values;
    private boolean locked;

    public Properties() {
        values = new HashMap<>();
        locked = false;
    }

    public Properties lock() {
        this.locked = true;
        return this;
    }

    public boolean locked() {
        return this.locked;
    }

    private Properties setValue(Key key, Object object) {
        if (locked)
            throw new ObjectLockedException("This Properties object is already locked!");
        values.put(key, object);
        return this;
    }

    public int getInt(Key.IntKey key) {
        return (int) getValue(key);
    }

    public boolean getBoolean(Key.BooleanKey key) {
        return (boolean) getValue(key);
    }

    public double getDouble(Key.DoubleKey key) {
        return (double) getValue(key);
    }

    private Object getValue(Key key) {
        Object value = values.get(key);
        if (value == null) {
            value = defaultValues.get(key);
        }
        if (value == null) {
            throw new NoValueSetException("There is no default value set for the given key, and a value was not provided!");
        }
        return value;
    }

    public Properties setInt(Key.IntKey key, int value) {
        return setValue(key, value);
    }

    public Properties setBoolean(Key.BooleanKey key, boolean value) {
        return setValue(key, value);
    }

    public Properties setDouble(Key.DoubleKey key, double value) {
        return setValue(key, value);
    }

    public String toString() {
        return "Set values: " + this.values.toString();
    }
}
