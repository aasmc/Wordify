package ru.aasmc.wordify.common.core.data.api.model.mappers

interface ApiMapper<DTO, Cache> {
    fun mapToCache(dto: DTO): Cache
}