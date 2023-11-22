package entity_models

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDateTime

@kotlinx.serialization.Serializable
data class AppUser
    (
    var fullName: String,
    var email: String?,
    var phone: String?,
    var address: String?,
    var photo: String?,
    var about: String?,

    @Contextual
    var birthday: Instant?,
    var auth0UserId: String?,
    override var id: Long,

    @Contextual
    override var lastEditedTime: Instant?,
    override var lastEditingUser: AppUser?,
    override var lastEditingUserId: Long?,

    ): BaseEntity