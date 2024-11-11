package seg3x02.booksapigraphql.resolvers

import org.springframework.stereotype.Controller
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.Argument
import seg3x02.booksapigraphql.entity.Book
import seg3x02.booksapigraphql.entity.Author
import seg3x02.booksapigraphql.repository.BookRepository
import seg3x02.booksapigraphql.repository.AuthorRepository
import java.util.*
import seg3x02.booksapigraphql.resolvers.types.CreateBookInput

@Controller
class BooksController(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) {

    @QueryMapping
    fun books(): List<Book> = bookRepository.findAll()

    @QueryMapping
    fun bookById(@Argument bookId: String): Book? = bookRepository.findById(bookId).orElse(null)

    @QueryMapping
    fun bookByNumber(@Argument bookNumber: Int): Book? =
        bookRepository.findAll().find { it.bookNumber == bookNumber }

    @MutationMapping
    fun newBook(@Argument createBookInput: CreateBookInput): Book {
        val book = Book(
            bookNumber = createBookInput.bookNumber ?: 0, // Valeur par défaut
            category = createBookInput.category ?: "Unknown",
            title = createBookInput.title ?: "Untitled",
            cost = createBookInput.cost ?: 0.0f,
            year = createBookInput.year ?: "Unknown",
            description = createBookInput.description ?: ""
        )

        book.bookId = UUID.randomUUID().toString()
        return bookRepository.save(book)
    }

    @MutationMapping
    fun deleteBook(@Argument bookId: String): Boolean {
        return if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId)
            true
        } else {
            false
        }
    }

    @MutationMapping
    fun updateBook(@Argument bookId: String, @Argument createBookInput: CreateBookInput): Book? {
        val existingBook = bookRepository.findById(bookId).orElse(null) ?: return null
        existingBook.apply {
            category = createBookInput.category ?: "Unknown"
            title = createBookInput.title ?: "Untitled"
            cost = createBookInput.cost ?: 0.0f
            year = createBookInput.year ?: "Unknown"
            description = createBookInput.description ?: ""
        }

        return bookRepository.save(existingBook)
    }

    @QueryMapping(name = "allAuthors")
    fun authors(@Argument limit: Int?): List<Author> {
        return authorRepository.findAll()
            .take(limit ?: Int.MAX_VALUE) // Limite optionnelle pour limiter les résultats
            .map { author ->
                Author(
                    id = author.id,
                    firstName = author.firstName,
                    lastName = author.lastName,
                    bookNumber = author.bookNumber
                )
            }
    }

    @MutationMapping
    fun newAuthor(@Argument bookNumber: Int, @Argument firstName: String, @Argument lastName: String): Author {
        val author = Author(
            id = UUID.randomUUID().toString(),
            firstName = firstName,
            lastName = lastName,
            bookNumber = bookNumber
        )
        return authorRepository.save(author)
    }
}
