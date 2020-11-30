package com.venosyd.open.repository.dto;

import java.util.HashMap;
import java.util.Map;

import com.venosyd.open.commons.log.Debuggable;
import com.venosyd.open.entities.infra.SerializableEntity;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public abstract class DTOFactory implements Debuggable {

    private static Map<String, DTO<? extends SerializableEntity>> _cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends SerializableEntity> DTO<T> getDTO(String db, Class<T> clazz) {
        var key = db + clazz.getName();

        if (!_cache.containsKey(key)) {
            var dto = new BasicDTO<T>(db);
            dto.setEntityClass(clazz);

            _cache.put(key, dto);
        }

        return (DTO<T>) _cache.get(key);
    }

}
