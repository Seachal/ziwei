package com.laka.live.cache;

/**
 * Created by luwies on 16/5/30.
 */

import java.util.Map;

/**
 * An interface for a cache keyed by a String with a byte array as data.
 */
public interface PostCache {
    /**
     * Retrieves an entry from the cache.
     *
     * @param key PostCache key
     * @return An {@link Entry} or null in the event of a cache miss
     */
    Entry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     *
     * @param key   PostCache key
     * @param entry Data to store and metadata for cache coherency, TTL, etc.
     */
    void put(String key, Entry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     */
    void initialize();

    /**
     * Removes an entry from the cache.
     *
     * @param key PostCache key
     */
    void remove(String key);

    /**
     * Empties the cache.
     */
    void clear();

    /**
     * Data and metadata for an entry returned by the cache.
     */
    class Entry {

        public String key;

        public int size;

        public int method;

        public String requestUrl;

        public Map<String, String> postData;

    }

}
