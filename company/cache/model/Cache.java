package com.company.cache.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
 * This will be model for cache. The data used by cache is defined here.
 */
public class Cache {

    public List<Integer> cacheLevels = new ArrayList<>();

    public HashMap<Integer, Deque<CacheEntry>> cacheData = new HashMap<>();

}
