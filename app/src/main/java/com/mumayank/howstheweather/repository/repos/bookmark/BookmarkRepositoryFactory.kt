package com.mumayank.howstheweather.repository.repos.bookmark

object BookmarkRepositoryFactory {
    fun get(): BookmarkRepository = BookmarkRepositoryImpl()
}