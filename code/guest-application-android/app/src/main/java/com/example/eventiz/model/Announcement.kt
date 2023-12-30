package com.example.eventiz.model

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResponse(
    val data: List<AnnouncementData>,
    val meta: Meta?
)

@Serializable
data class AnnouncementData(
    val id: Int,
    val attributes: AnnouncementAttributes
)

@Serializable
data class AnnouncementAttributes(
    val createdAt: String,
    val updatedAt: String,
    val publishedAt: String,
    val message: String,
    val category: AnnouncementCategory
)

@Serializable
data class AnnouncementCategory(
    val data: AnnouncementCategoryData
)

@Serializable
data class AnnouncementCategoryData(
    val id: Int,
    val attributes: CategoryAttributes
)

@Serializable
data class CategoryAttributes(
    val type: String,
    val createdAt: String,
    val updatedAt: String,
    val publishedAt: String
)

@Serializable
data class Meta(
    val pagination: Pagination
)

@Serializable
data class Pagination(
    val page: Int,
    val pageSize: Int,
    val pageCount: Int,
    val total: Int
)
