package composables

sealed class ViewLce<out T> {
    object Loading : ViewLce<Nothing>()
    data class Content<T>(val data: T) : ViewLce<T>()
    data class Error(val error: Throwable) : ViewLce<Nothing>()
}