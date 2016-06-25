package com.github.ericytsang.lib.cache

import org.junit.Test

/**
 * Created by Eric on 3/20/2016.
 */
class CacheTest
{
    val cache = Cache<Int,String>(
        evictionPolicy = LruEvictionPolicy<Int>(),
        maximumCapacity = 5)

    @Test
    fun cacheHitTest()
    {
        cache[0] = "hello"
        cache[1] = "hello"
        cache[2] = "hello"
        cache[0] = "hello"
        cache[3] = "hello"
        cache[4] = "hello"
        assert(cache.size == 5,{"assert(cache.size == 5)"})
        assert(cache[0] != null,{"assert(cache[0] != null)"})
        assert(cache[1] != null,{"assert(cache[1] != null)"})
        assert(cache[2] != null,{"assert(cache[2] != null)"})
        assert(cache[3] != null,{"assert(cache[3] != null)"})
        assert(cache[4] != null,{"assert(cache[4] != null)"})
    }

    @Test
    fun cacheEvictionTest()
    {
        cache[0] = "hello"
        cache[1] = "hello"
        cache[2] = "hello"
        cache[0] = "hello"
        cache[3] = "hello"
        cache[4] = "hello"
        cache[5] = "hello"
        assert(cache.size == 5,{"assert(cache.size == 5)"})
        assert(cache[0] != null,{"assert(cache[0] != null)"})
        assert(cache[1] == null,{"assert(cache[1] == null)"})
        assert(cache[2] != null,{"assert(cache[2] != null)"})
        assert(cache[3] != null,{"assert(cache[3] != null)"})
        assert(cache[4] != null,{"assert(cache[4] != null)"})
        assert(cache[5] != null,{"assert(cache[5] != null)"})
    }
}
