package com.venosyd.open.repository.logic;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.venosyd.open.commons.log.Debuggable;
import com.venosyd.open.commons.util.JSONUtil;
import com.venosyd.open.repository.lib.RepositoryConfig;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * @author sergio lisan <sels@venosyd.com>s
 */
class RepositoryBSImpl implements RepositoryBS, Debuggable {

    private MongoClient mongoClient;

    RepositoryBSImpl() {
        var config = RepositoryConfig.INSTANCE;

        var addr = (String) config.get("mongo-addr");
        var port = Integer.parseInt(config.get("mongo-port"));

        mongoClient = new MongoClient(addr, port);
    }

    @Override
    public Map<String, String> save(String database, String payload) {
        try {
            // converte o JSON para um mapa que vai ter os campos adaptados para os padroes
            // do mongo
            var toConvert = JSONUtil.<String, Object>fromJSONToMap(payload);
            if (!toConvert.containsKey("id") || toConvert.get("id") == null)
                toConvert.put("id", new ObjectId().toHexString());

            var toSave = _dtoToMongoMap(toConvert);

            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection((String) toSave.get("collection_key"));

            // salva o documento
            table.insertOne(toSave);

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.fromMapToJSON(_mapToDto(toSave)));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION SAVING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Problemas ao salvar");

            return result;
        }
    }

    @Override
    public Map<String, String> update(String database, String payload) {
        try {
            // converte o JSON para um mapa que vai ter os campos adaptados para os padroes
            // do mongo
            var toConvert = JSONUtil.<String, Object>fromJSONToMap(payload);
            var toUpdate = _dtoToMongoMap(toConvert);

            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection((String) toUpdate.get("collection_key"));

            // atualiza o documento
            table.updateOne(eq("_id", toUpdate.get("_id")), new Document("$set", toUpdate));

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.fromMapToJSON(_mapToDto(toUpdate)));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION UPDATING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel atualizar");

            return result;
        }
    }

    @Override
    public Map<String, String> erase(String database, String payload) {
        try {
            // converte o JSON para um mapa que vai ter os campos adaptados para os padroes
            // do mongo
            var toDelete = JSONUtil.<String, Object>fromJSONToMap(payload);

            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection((String) toDelete.get("collection_key"));

            // deleta o documento
            table.deleteOne(eq("_id", toDelete.get("id")));

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION ERASING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel remover o item");

            return result;
        }
    }

    @Override
    public Map<String, String> getByID(String database, String collection, String id) {
        var query = new Document();
        query.put("_id", id);

        var result = getByQuery(database, collection, query);
        if (!result.get("status").equals("ok")) {
            result.put("message", "Nao foi possivel resgatar pela ID");
        }

        // retorna o resultado
        return result;
    }

    @Override
    public Map<String, String> getByQuery(String database, String collection, Document query) {
        var result = list(database, collection, query);

        // se a operacao foi bem sucedida...
        if (result.get("status").equals("ok")) {
            var payload = JSONUtil.<String>fromJSONToList(result.get("payload"));

            // ... e retornou algo, separa o item e o converte para JSON
            if (!payload.isEmpty()) {
                result.put("payload", payload.get(0));
            }
        } else {
            result.put("message", "Nao foi possivel resgatar pela Query");
        }

        // retorna o resultado
        return result;
    }

    @Override
    public Map<String, String> getIDByQuery(String database, String collection, Document query) {
        var result = listAllIDs(database, collection, query);

        // se a operacao foi bem sucedida...
        if (result.get("status").equals("ok")) {
            var payload = JSONUtil.<String>fromJSONToList(result.get("payload"));

            // ... e retornou algo, separa o item e o converte para JSON
            if (!payload.isEmpty()) {
                result.put("payload", payload.get(0));
            }
        } else {
            result.put("message", "Nao foi possivel resgatar pela Query");
        }

        // retorna o resultado
        return result;
    }

    @Override
    public Map<String, String> list(String database, String collection, Document query) {
        try {
            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection(collection);

            var list = new ArrayList<String>();
            Iterable<Document> queried;

            // lista todos os documentos de uma colecao
            if (query == null || query.isEmpty()) {
                queried = table.find();
            }

            // lista todos os documentos de uma colecao que estao de acordo com a query
            else {
                queried = table.find(query);
            }

            // converte documento pra mapa whatever e depois para String body
            for (var doc : queried) {
                list.add(JSONUtil.fromMapToJSON(_mapToDto(doc)));
            }

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.toJSON(list));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION LISTING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel listar os itens");

            return result;
        }
    }

    @Override
    public Map<String, String> listAllIDs(String database, String collection, Document query) {
        try {
            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection(collection);

            var list = new ArrayList<String>();
            Iterable<Document> queried;

            // lista todos os documentos de uma colecao
            if (query == null || query.isEmpty()) {
                queried = table.find(query).projection(Document.parse("{_id:1}"));
            }

            // lista todos os documentos de uma colecao que estao de acordo com a query
            else {
                queried = table.find().projection(Document.parse("{_id:1}"));
            }

            // converte documento pra mapa whatever e depois para String body
            for (var doc : queried) {
                list.add(JSONUtil.fromMapToJSON(_mapToDto(doc)));
            }

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.toJSON(list));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION LISTING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel listar os itens");

            return result;
        }
    }

    @Override
    public Map<String, String> listDistinct(String database, String collection, String field, Document query) {
        try {
            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection(collection);

            Iterable<String> list;

            if (query != null && !query.isEmpty()) {
                list = table.distinct(field, query, String.class);
            } else {
                list = table.distinct(field, String.class);
            }

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.toJSON(Lists.newArrayList(list)));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION LIST DISTINCT").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel listar os itens distintos");

            return result;
        }
    }

    @Override
    public Map<String, String> skipList(String database, String collecction, int skip, Document query) {
        try {
            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection(collecction);

            var list = new ArrayList<String>();
            Iterable<Document> queried;

            // lista todos os documentos de uma colecao
            if (query == null || query.isEmpty()) {
                queried = table.find().skip(skip);
            }

            // lista todos os documentos de uma colecao que estao de acordo com a query
            else {
                queried = table.find(query).skip(skip);
            }

            // converte documento pra mapa whatever e depois para String body
            for (var doc : queried) {
                list.add(JSONUtil.fromMapToJSON(_mapToDto(doc)));
            }

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", JSONUtil.toJSON(list));

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION SKIP LISTING").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel listar os itens esquipados");

            return result;
        }

    }

    @Override
    public Map<String, String> count(String database, String collection) {
        try {
            // abre a conexao com o banco e com a "tabela"
            var db = _connection(database);
            var table = db.getCollection(collection);

            // converte para retornar o resultado
            var result = new HashMap<String, String>();
            result.put("status", "ok");
            result.put("payload", table.countDocuments() + "");

            return result;

        } catch (Exception e) {
            out.tag("MONGO EXCEPTION COUNT").ln(e);
            e.printStackTrace();

            var result = new HashMap<String, String>();
            result.put("status", "error");
            result.put("message", "Nao foi possivel fazer uma contagem");

            return result;
        }
    }

    ///
    /// PRIVATE UTILITY FUNCTIONS
    ///

    private MongoDatabase _connection(String database) {
        return mongoClient.getDatabase(database);
    }

    /**
     * converte os mapas de items whatever para documentos mongo
     */
    private Document _dtoToMongoMap(Map<String, Object> from) {
        var item = new Document(from);
        item.put("_id", item.get("id"));
        item.remove("id");

        return item;
    }

    /**
     * converte um documento em forma de mapa para o padrao whatever de items mongo
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<String, Object> _mapToDto(Document document) {
        var isObjectID = document.get("_id") instanceof ObjectId;

        document.put("id", isObjectID ? ((ObjectId) document.get("_id")).toHexString() : document.get("_id"));
        document.remove("_id");

        // remove qualquer objectid escondido e apenas seta as hashes deles
        document.keySet().forEach(k -> {
            // se for um campo ObjectID, torna String com apenas o hash
            if (document.get(k) instanceof ObjectId) {
                document.put(k, ((ObjectId) document.get(k)).toHexString());
            }

            // se for uma lista de objectIDs, converte todas elas
            else if (document.get(k) instanceof List && !((List) document.get(k)).isEmpty()
                    && ((List) document.get(k)).get(0) instanceof ObjectId) {

                // converte cada objectid pra string na funcao map()
                document.put(k, ((List) document.get(k)).stream().map(id -> ((ObjectId) id).toHexString())
                        .collect(Collectors.toList()));
            }
        });

        return document;
    }

}
