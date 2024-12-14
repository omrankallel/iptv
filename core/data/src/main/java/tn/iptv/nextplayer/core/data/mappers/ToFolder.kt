package tn.iptv.nextplayer.core.data.mappers

import tn.iptv.nextplayer.core.common.Utils
import tn.iptv.nextplayer.core.database.relations.DirectoryWithMedia
import tn.iptv.nextplayer.core.database.relations.MediumWithInfo
import tn.iptv.nextplayer.core.model.Folder

fun DirectoryWithMedia.toFolder() = Folder(
    name = directory.name,
    path = directory.path,
    dateModified = directory.modified,
    parentPath = directory.parentPath,
    formattedMediaSize = Utils.formatFileSize(media.sumOf { it.mediumEntity.size }),
    mediaList = media.map(MediumWithInfo::toVideo),
)
