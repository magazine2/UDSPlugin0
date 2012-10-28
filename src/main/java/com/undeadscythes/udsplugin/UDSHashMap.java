package com.undeadscythes.udsplugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UDSHashMap<Object> extends HashMap<String, Object> {
    public final Object get(final String key) {
        return super.get(key.toLowerCase(Locale.ENGLISH));
    }

    public final boolean containsKey(final String key) {
        return super.containsKey(key.toLowerCase(Locale.ENGLISH));
    }

    public final boolean containsPartialKey(final String partialKey) {
        for(Map.Entry<String, Object> i : super.entrySet()) {
            if(i.getKey().contains(partialKey.toLowerCase(Locale.ENGLISH))) {
                return true;
            }
        }
        return false;
    }

    public final Object remove(final String key) {
        return super.remove(key.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public final Object put(final String key, final Object value) {
        return super.put(key.toLowerCase(Locale.ENGLISH), value);
    }
}
