package com.company.cache.service;

import com.company.cache.model.Cache;
import com.company.cache.model.CacheEntry;
import com.company.cache.config.CacheConfiguration;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This is the interface used to talk to cache.
 * Read and write operation should be done here. It will expose API's to read and write.
 * The configuration of cache is defined here.
 */
public class DataService {

    private int layerCapacity;
    private Cache cache = new Cache();
    private int readTime;
    private int writeTime;

    public DataService(CacheConfiguration config) {

        for(int i = 1; i <= config.number_of_levels; i++) {
            cache.cacheLevels.add(i);
            cache.cacheData.put(i, new LinkedList<>());
        }
        readTime = config.read_time;
        writeTime = config.write_time;
        layerCapacity = config.capacity_each_layer;
    }

    public int read(String key) {
        int totalTime = 0;
        int i = 0;
        CacheEntry entry = null;
        for( ; i < cache.cacheLevels.size(); i++) {
            Integer cacheLevel = cache.cacheLevels.get(i);
            entry = searchInLevel(cacheLevel, key);
            totalTime += readTime;
            if(entry != null) {
                break;
            }
        }
        if(entry != null) {
            // write to all previous levels
            for(int j = i-1 ; j >= 0 ; j--) {
                writeToLevel(entry, j);
                totalTime += writeTime;
            }
        }

        return totalTime;
    }

    private void writeToLevel(CacheEntry entry, int j) {
        Deque<CacheEntry> cacheEntries = cache.cacheData.get(j);
        if(cacheEntries.size() == layerCapacity) {
            // we will remove least used entry
            cacheEntries.removeLast();
        }
        cacheEntries.addFirst(entry);
    }

    private CacheEntry searchInLevel(Integer cacheLevel, String key) {
        CacheEntry entry = null;
        Deque<CacheEntry> cacheEntries = cache.cacheData.get(cacheLevel);
        for (CacheEntry cacheEntry : cacheEntries) {
            if(cacheEntry.key.equals(key)) {
                cacheEntries.remove(cacheEntry);
                cacheEntries.addFirst(cacheEntry);
                entry = cacheEntry;
                break;
            }
        }
        return entry;
    }

    public int write(String key, String value) {

        int totalTime = 0;
        for(int i = 0 ; i < cache.cacheLevels.size(); i++) {
            Integer cacheLevel = cache.cacheLevels.get(i);
            Deque<CacheEntry> cacheEntries = cache.cacheData.get(cacheLevel);
            boolean found = false;
            for (CacheEntry cacheEntry : cacheEntries) {
                if(cacheEntry.key.equals(key)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                writeToLevel(new CacheEntry(key, value), cacheLevel);
                totalTime += writeTime;
            } else {
                totalTime += readTime;
            }
        }
        return totalTime;
    }
}
