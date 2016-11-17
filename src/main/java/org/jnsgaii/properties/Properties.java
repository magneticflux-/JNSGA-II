package org.jnsgaii.properties;

import org.jnsgaii.exceptions.NoValueSetException;
import org.jnsgaii.exceptions.ObjectLockedException;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell Skaggs on 11/25/2015.
 */
public class Properties {

    private static final Map<Key, Object> defaultValues;

    static { //Default value initialization
        Map<Key, Object> v = new HashMap<>();

        v.put(Key.BooleanKey.DefaultBooleanKey.THREADED, true);
        v.put(Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM, 1);

        defaultValues = Collections.unmodifiableMap(v);
    }

    private final AbstractMap<Key, Object> values;
    private boolean locked;

    public Properties() {
        this.values = new HashMap<>();
        this.locked = false;
    }

    public Properties lock() {
        this.locked = true;
        return this;
    }

    public void testKey(Key key) {
        this.getValue(key);
    }

    public Object getValue(Key key) {
        Object value = this.values.get(key);
        if (value == null) {
            value = defaultValues.get(key);
        }
        if (value == null) {
            throw new NoValueSetException("There is no default value set for the given key, and a value was not provided!");
        }
        return value;
    }

    public boolean locked() {
        return this.locked;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public int getInt(Key.IntKey key) {
        return (int) this.getValue(key);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public boolean getBoolean(Key.BooleanKey key) {
        return (boolean) this.getValue(key);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public double getDouble(Key.DoubleKey key) {
        return (double) this.getValue(key);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public Properties setInt(Key.IntKey key, int value) {
        return this.setValue(key, value);
    }

    public Properties setValue(Key key, Object object) {
        if (this.locked)
            throw new ObjectLockedException("This Properties object is already locked!");
        this.values.put(key, object);
        return this;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public Properties setBoolean(Key.BooleanKey key, boolean value) {
        return this.setValue(key, value);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public Properties setDouble(Key.DoubleKey key, double value) {
        return this.setValue(key, value);
    }

    public String toString() {
        return "Set values: " + this.values;
    }
}
