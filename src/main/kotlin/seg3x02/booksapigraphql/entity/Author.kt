package seg3x02.booksapigraphql.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "author")
data class Author(
    val id: String,
    val name: String,
    val bookNumber: Int 
)

