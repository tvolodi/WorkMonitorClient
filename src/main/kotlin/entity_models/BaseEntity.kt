package entity_models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface BaseEntity {
    var id: Long
    var lastEditedTime: Instant?
    var lastEditingUser: AppUser?
    var lastEditingUserId: Long?
}