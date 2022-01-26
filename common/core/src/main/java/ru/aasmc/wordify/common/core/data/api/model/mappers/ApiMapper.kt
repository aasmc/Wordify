package ru.aasmc.wordify.common.core.data.api.model.mappers

interface ApiMapper<DTO, Domain> {
    fun mapToDomain(dto: DTO): Domain
}