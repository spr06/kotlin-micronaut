package app

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.ThreadFactory

class IOThreadFactory : ThreadFactory {
    private val factory: ThreadFactory = ThreadFactoryBuilder()
        .setNameFormat("micronaut-fixed-io-thread-pool-%d")
        .setDaemon(false)
        .setPriority(Thread.NORM_PRIORITY)
        .build()

    override fun newThread(r: Runnable): Thread {
        return factory.newThread(r)
    }
}


