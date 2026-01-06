package com.example.mylist.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BookListResponse(
    val works: List<BookWorkDto>
)

@Serializable
data class BookWorkDto(
    val key: String,
    val title: String,
    val authors: List<AuthorDto>? = null,
    @SerialName("cover_id") val coverId: Long? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null
)

@Serializable
data class AuthorDto(
    val key: String,
    val name: String
)

@Serializable
data class BookDetailDto(
    val key: String,
    val title: String,
    val description: JsonElement? = null, // Can be string or object
    @SerialName("first_publish_date") val firstPublishDate: String? = null,
    val covers: List<Long>? = null,
    @SerialName("number_of_pages") val numberOfPages: Int? = null
)

@Serializable
data class SearchResponseDto(
    val docs: List<SearchResultDto>
)

@Serializable
data class SearchResultDto(
    val key: String,
    val title: String,
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("cover_i") val coverId: Long? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null
)

fun BookDetailDto.getDescriptionString(): String {
    return if (description is kotlinx.serialization.json.JsonPrimitive) {
        description.content
    } else if (description is kotlinx.serialization.json.JsonObject) {
        description["value"]?.toString()?.trim('"')?.replace("\\r\\n", "\n") ?: "Brak opisu"
    } else {
        "Brak opisu"
    }
}
