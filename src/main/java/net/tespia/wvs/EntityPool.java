/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityPool<T> {
    private Map<Integer, T> pool = new ConcurrentHashMap<>();


    public void insert(Integer id, T t) {
        this.pool.put(id, t);
    }

    public Collection<T> getValues() {
        return pool.values();
    }

    public void remove(Integer id) {
        this.pool.remove(id);
    }

    public T get(Integer id) {
        return pool.get(id);
    }

}
