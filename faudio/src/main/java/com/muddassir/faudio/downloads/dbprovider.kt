package com.muddassir.faudio.downloads

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

private interface DependencyProviderInterface

private var _dependencyProvider: DependencyProvider? = null

internal class DependencyProvider(context: Context): DependencyProviderInterface {
    val databaseProvider = ExoDatabaseProvider(context)
    val downloadCache = SimpleCache(
        context.cacheDir, NoOpCacheEvictor(), databaseProvider)
    val dataSourceFactory = DefaultHttpDataSource.Factory();
    val downloadExecutor = Runnable::run
    val cacheDataSourceFactory = CacheDataSource.Factory().setCache(downloadCache)
        .setUpstreamDataSourceFactory(dataSourceFactory)
        .setCacheWriteDataSinkFactory(null); // Disable writing.
}

internal fun dependencyProvider(context: Context): DependencyProvider {
    if(_dependencyProvider == null) {
        _dependencyProvider = DependencyProvider(context)
    }

    return (_dependencyProvider as DependencyProvider)
}

