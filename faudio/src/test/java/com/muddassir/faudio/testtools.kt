package com.muddassir.faudio

import android.net.Uri
import org.mockito.Matchers
import org.powermock.api.mockito.PowerMockito

fun makeUri(): Uri {
    PowerMockito.mockStatic(Uri::class.java)
    val uri: Uri = PowerMockito.mock(Uri::class.java)

    PowerMockito.`when`<Any>(Uri::class.java, "parse", Matchers.anyString())
        .thenReturn(uri)

    return uri
}