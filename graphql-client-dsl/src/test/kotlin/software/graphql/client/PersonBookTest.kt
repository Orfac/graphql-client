package software.graphql.client

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

internal class PersonBookTest {
    @Test
    fun test() {
        val query = query("NameMayBeSkipped") {
            val authorInfo = createFragment("authorInfo", Book(), "Book") {
                author {
                    name()
                }
            }
            schemaVersion()
            person(
                name = "Boris",
                dateOfBirth = MyDateType(LocalDate.of(1970, 1, 1)),
                favouritePets = arrayListOf(Pet.CAT, Pet.DOG)
            ) {
                name(capitalize = true)
                age()
                book(title = "This tool spec") {
                    author {
                        // argument will not be present in the query, because 'false' is the default value
                        name(capitalize = false)
                    }
                    title()
                }
            }
            // defaults with authorName equal to "Boris"
            "ToolSpec" alias book(title = "This tool spec") {
                addInlineFragment(Book(), "Book") {
                    title()
                }
            }
            // null will be in the query, because it's not default
            "HelloWorld" alias book(authorName = null, title = "Hello world") {
                addFragment(authorInfo)
            }
            allBooks(titles = arrayListOf("Hello world", "This tool spec")) {
                addFragment(authorInfo)
            }
        }
        println(query.toString())
        // 'flattenQuery' leaves only words, numbers and double quotes to make testing easier
        assertEquals(
            flattenQuery(query.toString()),
            "query NameMayBeSkipped " +
                    "fragment authorInfo on Book author name " +
                    "schemaVersion " +
                    "person name \"Boris\" dateOfBirth \"1970 01 01\" favouritePet CAT DOG name capitalize true age " +
                    "book title \"This tool spec\" author name title " +
                    "ToolSpec book title \"This tool spec\" on Book title " +
                    "HelloWorld book authorName null title \"Hello world\" authorInfo " +
                    "allBooks titles \"Hello world\" \"This tool spec\" authorInfo"
        )
    }
}