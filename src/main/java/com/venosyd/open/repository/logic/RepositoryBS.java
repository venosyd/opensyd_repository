package com.venosyd.open.repository.logic;

import java.util.Map;

import org.bson.Document;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public interface RepositoryBS {

    /**
     * singleton
     */
    RepositoryBS INSTANCE = new RepositoryBSImpl();

    /**
     * gets a object by their objectid
     * 
     * { database: 'my-app' collection: 'contact' id: '7772DCA34BF3' }
     */
    Map<String, String> getByID(String database, String collection, String id);

    /**
     * gets a object by a query
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    Map<String, String> getByQuery(String database, String collection, Document query);

    /**
     * gets a object by a query
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    Map<String, String> getIDByQuery(String database, String collection, Document query);

    /**
     * list a collection by a type
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    Map<String, String> list(String database, String collection, Document query);

    /**
     * list a collection by a type
     * 
     * { database: 'my-app' collection: 'contact' query: { ... } }
     */
    Map<String, String> listAllIDs(String database, String collection, Document query);

    /**
     * lists distinct objects by a field of a type
     * 
     * { database: 'my-app' collection: 'contact' field: 'phone' query: { ... } }
     */
    Map<String, String> listDistinct(String database, String collection, String field, Document query);

    /**
     * lista elementos de uma lista, a partir de um indice
     * 
     * { database: 'my-app' collection: 'contact' skip: 217381 query: { ... } }
     */
    Map<String, String> skipList(String database, String collecction, int skip, Document query);

    /**
     * saves new instances
     * 
     * { database: 'my-app' payload: { ... } }
     */
    Map<String, String> save(String database, String payload);

    /**
     * updates new instances
     * 
     * { database: 'my-app' payload: { ... } }
     */
    Map<String, String> update(String database, String payload);

    /**
     * erase instance
     * 
     * { database: 'my-app' payload: { ... } }
     */
    Map<String, String> erase(String database, String payload);

    /**
     * conta quantas instancias tem na colecao
     * 
     * { database: 'my-app' collection: Collection }
     */
    Map<String, String> count(String database, String collection);
}
