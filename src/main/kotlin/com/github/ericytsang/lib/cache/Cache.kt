package com.github.ericytsang.lib.cache

import java.util.*

/**
 * Created by Eric on 3/20/2016.
 */
class Cache<K,V>(val evictionPolicy:EvictionPolicy<K>,val maximumCapacity:Int,loadFactor:Float = 0.75F):MutableMap<K,V>
{
    private val underlyingMap = HashMap<K,V>(maximumCapacity,loadFactor)

    override val size:Int get() = underlyingMap.size
    override fun containsKey(key:K):Boolean = underlyingMap.containsKey(key)
    override fun containsValue(value:V):Boolean = underlyingMap.containsValue(value)
    override fun get(key:K):V?
    {
        evictionPolicy.access(key)
        return underlyingMap[key]
    }
    override fun isEmpty():Boolean = underlyingMap.isEmpty()
    override val entries:MutableSet<MutableMap.MutableEntry<K,V>> get() = underlyingMap.entries
    override val keys:MutableSet<K> get() = underlyingMap.keys
    override val values:MutableCollection<V> get() = underlyingMap.values

    override fun clear()
    {
        evictionPolicy.clear()
        underlyingMap.clear()
    }

    override fun put(key:K,value:V):V?
    {
        // if the operation will violate capacity, evict an entry
        if (size+1 > maximumCapacity && !keys.contains(key))
        {
            val toEvict = evictionPolicy.evict()
            assert(underlyingMap.remove(toEvict) != null)
        }

        // perform the operation
        evictionPolicy.access(key)
        return underlyingMap.put(key,value)
    }

    override fun putAll(from:Map<out K,V>)
    {
        from.forEach {put(it.key,it.value)}
    }

    override fun remove(key:K):V?
    {
        evictionPolicy.remove(key)
        return underlyingMap.remove(key)
    }

    /**
     * cache eviction policy interface
     */

    interface EvictionPolicy<E>
    {
        /**
         * indicates that [element] is accessed. [element] can be an existing
         * element in the cache, or it could be new.
         *
         * may throw an [IllegalStateException] if the operation would violate
         * the cache policy.
         */
        fun access(element:E)

        /**
         * removes [element] from collection.
         */
        fun remove(element:E)

        /**
         * removes all elements from the collection.
         */
        fun clear()

        /**
         * returns the next element element to evict.
         */
        fun evict():E
    }
}
