package com.mentorship.untitled.api

import com.mentorship.untitled.SearchResult
import kotlinx.serialization.Serializable

@Serializable
data class DummySearch(
    val products: List<Product>
)

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
)

fun DummySearch.toSearchResultList() = products.map {
    SearchResult(title = it.title, description = it.description)
}
