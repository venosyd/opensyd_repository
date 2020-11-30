package com.venosyd.open.repository.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.venosyd.open.commons.log.Debuggable;
import com.venosyd.open.commons.util.CommonPool;
import com.venosyd.open.commons.util.JSONUtil;
import com.venosyd.open.entities.infra.SerializableEntity;

import org.bson.Document;

/**
 * @author sergio lisan <sels@venosyd.com>
 * 
 *         Grande DTO basico que implementa funcoes gerais que todos os DTOs que
 *         extendem essa classe podem abstrair de implementar. Na pratica,
 *         gracas a esse BasicDTO, as subclasses so implementam aquilo q
 *         interessa a persistencia de seus proprios objetos
 */
public class BasicDTO<T extends SerializableEntity> extends SerializableEntity implements DTO<T>, Debuggable {

    /**  */
    private transient Class<T> entityClass;

    /**  */
    private transient String database;

    public BasicDTO(String database) {
        this.database = database;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        setCollection_key(entityClass.getSimpleName());
    }

    @Override
    public void save(T toSave) {
        var result = toSave.getId() != null ? REPOSITORY.update(database, toSave.toString())
                : REPOSITORY.save(database, toSave.toString());

        if (result.get("status").equals("ok")) {
            var rslt = JSONUtil.<String, String>fromJSONToMap(result.get("payload"));
            toSave.setId(rslt.get("id"));

            CommonPool.INSTANCE.push(entityClass, toSave);
        } else {
            out.tag("REPOSITORY").tag("BASICDTO").ln(result.get("status"));
        }
    }

    @Override
    public void save(T toSave, boolean withID) {
        var result = REPOSITORY.save(database, toSave.toString());

        if (result.get("status").equals("ok")) {
            var rslt = JSONUtil.<String, String>fromJSONToMap(result.get("payload"));
            toSave.setId(rslt.get("id"));

            CommonPool.INSTANCE.push(entityClass, toSave);
        } else {
            out.tag("REPOSITORY").tag("BASICDTO").ln(result.get("status"));
        }
    }

    @Override
    public T get(String id) {
        T obj = CommonPool.INSTANCE.pull(entityClass, id);
        if (obj == null) {
            var result = REPOSITORY.getByID(database, getCollection_key(), id);
            obj = result.get("status").equals("ok") ? buildEntity(result.get("payload")) : null;

            if (obj != null)
                CommonPool.INSTANCE.push(entityClass, obj);
        }

        return obj;
    }

    @Override
    public T get(String field, String data) {
        return get(new Document(field, data));
    }

    @Override
    public T get(String field, Integer data) {
        return get(new Document(field, data));
    }

    @Override
    public T get(String field, Double data) {
        return get(new Document(field, data));
    }

    @Override
    public T get(String field, Long data) {
        return get(new Document(field, data));
    }

    @Override
    public T get(Map<String, String> fieldData) {
        return get(Document.parse(JSONUtil.toJSON(fieldData)));
    }

    @Override
    public T get(Document query) {
        var queried = REPOSITORY.getIDByQuery(database, getCollection_key(), query).get("payload");
        var id = JSONUtil.<String, String>fromJSONToMap(queried).get("id");

        T obj = CommonPool.INSTANCE.pull(entityClass, id);
        if (obj == null) {
            var result = REPOSITORY.getByQuery(database, getCollection_key(), query);
            obj = result.get("status").equals("ok") ? buildEntity(result.get("payload")) : null;

            if (obj != null)
                CommonPool.INSTANCE.push(entityClass, obj);
        }

        return obj;
    }

    @Override
    public int count() {
        var result = REPOSITORY.count(database, getCollection_key());
        return result.get("status").equals("ok") ? Integer.parseInt(result.get("payload")) : 0;
    }

    @Override
    public void delete(String toDeleteID) {
        setId(toDeleteID);
        setCollection_key(getCollection_key());

        var payload = "{id:\"" + toDeleteID + "\", collection_key:\"" + getCollection_key() + "\"}";
        var result = REPOSITORY.erase(database, payload);

        if (!result.get("status").equals("ok"))
            err.exception("DELETE ERROR: ", new Exception(result.get("message")));
        else
            CommonPool.INSTANCE.pop(entityClass, toDeleteID);

    }

    @Override
    public List<T> list() {
        if (CommonPool.INSTANCE.list(entityClass).isEmpty()) {
            var result = REPOSITORY.list(database, getCollection_key(), null);
            var list = result.get("status").equals("ok") ? buildList(result.get("payload")) : new LinkedList<T>();

            for (T obj : list)
                CommonPool.INSTANCE.push(entityClass, obj);
        }

        return CommonPool.INSTANCE.list(entityClass);
    }

    @Override
    public List<T> list(final List<String> ids) {
        return list().stream().filter(e -> ids.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<T> list(String field, String data) {
        return list(new Document(field, data));
    }

    @Override
    public List<T> list(String field, Integer data) {
        return list(new Document(field, data));
    }

    @Override
    public List<T> list(String field, Double data) {
        return list(new Document(field, data));
    }

    @Override
    public List<T> list(String field, Long data) {
        return list(new Document(field, data));
    }

    @Override
    public List<T> list(Map<String, String> fieldData) {
        return list(Document.parse(JSONUtil.toJSON(fieldData)));
    }

    @Override
    public List<T> list(Document query) {
        var result = REPOSITORY.listAllIDs(database, getCollection_key(), query);
        var ids = result.get("status").equals("ok") ? JSONUtil.<String>fromJSONToList(result.get("payload"))
                : new LinkedList<String>();

        ids = ids.stream().map(id -> JSONUtil.<String, String>fromJSONToMap(id)).map(json -> json.get("id"))
                .collect(Collectors.toList());

        return list(ids);
    }

    @Override
    public List<T> listIDs() {
        var result = REPOSITORY.listAllIDs(database, getCollection_key(), null);
        if (result.get("status").equals("ok")) {
            var payload = result.get("payload");
            return payload == null || payload.isEmpty() ? new ArrayList<>() : buildList(payload);
        }

        return null;
    }

    @Override
    public List<T> skipList(int skip) {
        return skipList(skip, null);
    }

    @Override
    public List<T> skipList(int skip, Document query) {
        var result = REPOSITORY.skipList(database, getCollection_key(), skip, query);
        return result.get("status").equals("ok") ? buildList(result.get("payload")) : null;
    }

    /**
     * Converte o JSON em Lista
     */
    private List<T> buildList(String payload) {
        return payload.equals("[]") ? new LinkedList<>() : JSONUtil.<T>fromJSONToList(payload, entityClass);
    }

    /**
     * Converte o JSON em entidade
     */
    private T buildEntity(String payload) {
        return payload.equals("[]") ? null : JSONUtil.<T>fromJSON(payload, entityClass);
    }

}
