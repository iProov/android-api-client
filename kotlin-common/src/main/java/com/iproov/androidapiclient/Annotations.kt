package com.iproov.androidapiclient

// Annotations ----

@Deprecated("The use of these apis in a client is not for production, only for demonstrations. Your ApiService Client Secret should never be embedded in client code.")
@Experimental(level = Experimental.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class DemonstrationPurposesOnly