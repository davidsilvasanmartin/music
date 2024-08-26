package dev.davidsilva.music.utils;

public interface DtoMapper<DtoType, EntityType> {
    EntityType toEntity(DtoType dto);

    DtoType toDto(EntityType entity);
}
