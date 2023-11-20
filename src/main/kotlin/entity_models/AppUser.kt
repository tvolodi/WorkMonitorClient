package entity_models

import java.time.LocalDateTime

data class AppUser
    (
    var FullName: String,
    var Email: String?,
    var Phone: String?,
    var Address: String?,
    var Photo: String?,
    var About: String?,
    var Birthday: LocalDateTime?,
    var Auth0UserId: String?,
    override var Id: Long,
    override var LastEditedTime: LocalDateTime?,
    override var LastEditingUser: AppUser?,
    override var LastEditingUserId: Long?,

    ): BaseEntity