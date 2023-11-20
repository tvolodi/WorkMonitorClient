import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class AppChilds: Parcelable {

    @Parcelize
    object AppUserView : AppChilds()

    @Parcelize
    object ProjectView : AppChilds()
}