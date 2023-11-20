package entity_models

import java.time.LocalDateTime

interface BaseEntity {
    var Id: Long
    var LastEditedTime: LocalDateTime?
    var LastEditingUser: AppUser?
    var LastEditingUserId: Long?
}