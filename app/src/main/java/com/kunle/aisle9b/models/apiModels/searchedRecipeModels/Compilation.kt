package com.kunle.aisle9b.models.apiModels.searchedRecipeModels

data class Compilation(
    val approved_at: Int,
    val aspect_ratio: String,
    val beauty_url: Any,
    val buzz_id: Any,
    val canonical_id: String,
    val country: String,
    val created_at: Int,
    val description: Any,
    val draft_status: String,
    val facebook_posts: List<Any>,
    val id: Int,
    val is_shoppable: Boolean,
    val keywords: Any,
    val language: String,
    val name: String,
    val promotion: String,
    val show: List<ShowX>,
    val slug: String,
    val thumbnail_alt_text: String,
    val thumbnail_url: String,
    val video_id: Int,
    val video_url: String
)