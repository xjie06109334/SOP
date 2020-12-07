package com.gitee.sop.gatewaycommon.bean;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * LRU缓存
 * LinkedHashMap 本身内部有一个触发条件则自动执行的方法：删除最老元素（最近最少使用的元素）
 * 由于最近最少使用元素是 LinkedHashMap 内部处理
 * 故我们不再需要维护 最近访问元素放在链尾，get 时直接访问/ put 时直接存储
 * created by Ethan-Walker on 2019/2/16
 */
public class LRUCache<K, V> {
    private final Map<K, V> map;

    public LRUCache(int capacity) {
        map = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                // 容量大于capacity 时就删除
                return size() > capacity;
            }
        };
    }
    public V get(K key) {
        return map.get(key);
    }

    public V computeIfAbsent(K key,
                                Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    public V put(K key, V value) {
        return map.put(key, value);
    }

    public Collection<V> values() {
        return map.values();
    }
}