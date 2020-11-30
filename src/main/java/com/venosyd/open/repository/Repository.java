package com.venosyd.open.repository;

import java.util.Map;

import com.venosyd.open.repository.logic.RepositoryBS;

import org.bson.Document;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public abstract class Repository {

    /**
     * gets a object by their objectid
     * 
     * { database: 'my-app' collection: 'contact' id: '7772DCA34BF3' }
     */
    public static Map<String, String> getByID(String database, String collection, String id) {
        return RepositoryBS.INSTANCE.getByID(database, collection, id);
    }

    /**
     * gets a object by a query
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    public static Map<String, String> getByQuery(String database, String collection, Document query) {
        return RepositoryBS.INSTANCE.getByQuery(database, collection, query);
    }

    /**
     * gets a object by a query
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    public static Map<String, String> getIDByQuery(String database, String collection, Document query) {
        return RepositoryBS.INSTANCE.getIDByQuery(database, collection, query);
    }

    /**
     * list a collection by a type
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    public static Map<String, String> list(String database, String collection, Document query) {
        return RepositoryBS.INSTANCE.list(database, collection, query);
    }

    /**
     * list a collection by a type
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    public static Map<String, String> listAllIDs(String database, String collection, Document query) {
        return RepositoryBS.INSTANCE.listAllIDs(database, collection, query);
    }

    /**
     * lists distinct objects by a field of a type
     * 
     * { database: 'my-app' collection: 'contact' field: 'phone' query: { ... } }
     */
    public static Map<String, String> listDistinct(String database, String collection, String field, Document query) {
        return RepositoryBS.INSTANCE.listDistinct(database, collection, field, query);
    }

    /**
     * lista elementos de uma lista, a partir de um indice
     * 
     * { database: 'my-app' collection: 'contact' skip: 217381 query: { ... } }
     */
    public static Map<String, String> skipList(String database, String collecction, int skip, Document query) {
        return RepositoryBS.INSTANCE.skipList(database, collecction, skip, query);
    }

    /**
     * saves new instances
     * 
     * { database: 'my-app' payload: { ... } }
     */
    public static Map<String, String> save(String database, String payload) {
        return RepositoryBS.INSTANCE.save(database, payload);
    }

    /**
     * updates new instances
     * 
     * { database: 'my-app' payload: { ... } }
     */
    public static Map<String, String> update(String database, String payload) {
        return RepositoryBS.INSTANCE.update(database, payload);
    }

    /**
     * erase instance
     * 
     * { database: 'my-app' payload: { ... } }
     */
    public static Map<String, String> erase(String database, String payload) {
        return RepositoryBS.INSTANCE.erase(database, payload);
    }

    /**
     * conta quantas instancias tem na colecao
     * 
     * { database: 'my-app' collection: Collection }
     */
    public static Map<String, String> count(String database, String collection) {
        return RepositoryBS.INSTANCE.count(database, collection);
    }
}
