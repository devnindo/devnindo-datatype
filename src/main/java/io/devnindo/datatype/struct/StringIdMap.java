package io.devnindo.datatype.struct;

import java.util.IdentityHashMap;
import java.util.Map;

public class StringIdMap<V> extends IdentityHashMap<String, V>
{

    public StringIdMap(int expectedMaxSize) {
        super(expectedMaxSize);
    }

    @Override
    public V put(String key, V value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m)
    {
        m.entrySet().forEach(entry -> put(entry.getKey().intern(), entry.getValue()));
    }

    @Override
    public V get(Object key)
    {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException();
        }
        return super.get(((String) key).intern());
    }

    //implement the rest of the methods in the same way
}
