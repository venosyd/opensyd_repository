package com.venosyd.open.repository.dto;

import java.util.List;
import java.util.Map;

import com.venosyd.open.entities.infra.SerializableEntity;
import com.venosyd.open.repository.logic.RepositoryBS;

import org.bson.Document;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public interface DTO<T extends SerializableEntity> {

    RepositoryBS REPOSITORY = RepositoryBS.INSTANCE;

    void save(T toSave);

    void save(T toSave, boolean withID);

    T get(String id);

    T get(String field, String data);

    T get(String field, Integer data);

    T get(String field, Long data);

    T get(String field, Double data);

    T get(Map<String, String> fieldData);

    T get(Document query);

    int count();

    void delete(String toDeleteID);

    List<T> list();

    List<T> listIDs();

    List<T> list(List<String> ids);

    List<T> list(String field, String data);

    List<T> list(String field, Integer data);

    List<T> list(String field, Long data);

    List<T> list(String field, Double data);

    List<T> list(Map<String, String> fieldData);

    List<T> list(Document query);

    List<T> skipList(int skip);

    List<T> skipList(int skip, Document query);

}