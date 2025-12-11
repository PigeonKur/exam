import kotlinx.serialization.Serializable

@Serializable
data class BooksDTO (
    val id: String,
    val title: String,
    val author: String,
    val price: String
)