package tn.iptv.nextplayer.core.common.extensions

import android.net.Uri

/**
 * Whether the Uri authority is ExternalStorageProvider.
 */
val Uri.isExternalStorageDocument: Boolean
    get() = "com.android.externalstorage.documents" == authority

/**
 * Whether the Uri authority is DownloadsProvider.
 */
val Uri.isDownloadsDocument: Boolean
    get() = "com.android.providers.downloads.documents" == authority

/**
 * Whether the Uri authority is MediaProvider.
 */
val Uri.isMediaDocument: Boolean
    get() = "com.android.providers.media.documents" == authority

/**
 * Whether the Uri authority is Google Photos.
 */
val Uri.isGooglePhotosUri: Boolean
    get() = "com.google.android.apps.photos.content" == authority
