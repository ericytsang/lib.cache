package com.github.ericytsang.lib.cache

import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by Eric on 3/20/2016.
 */
class LruEvictionPolicy<E>():Cache.EvictionPolicy<E>
{
    val elements = LinkedHashSet<E>()
    val lruQueue = LinkedBlockingQueue<E>()

    override fun access(element:E)
    {
        // remove the element from the queue if present so it looks like it
        // was simply moved to the head of the queue
        if (elements.contains(element))
        {
            lruQueue.remove(element)
        }

        // add the element to the head of the queue
        elements.add(element)
        lruQueue.add(element)
    }
    override fun remove(element:E)
    {
        if (elements.remove(element))
        {
            lruQueue.remove(element)
        }
    }
    override fun clear()
    {
        if (elements.isNotEmpty())
        {
            elements.clear()
            lruQueue.clear()
        }
    }
    override fun evict():E
    {
        val removed = lruQueue.remove()
        elements.remove(removed)
        return removed
    }
}
