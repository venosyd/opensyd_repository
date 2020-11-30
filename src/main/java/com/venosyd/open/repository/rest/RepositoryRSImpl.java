package com.venosyd.open.repository.rest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.venosyd.open.commons.util.JSONUtil;
import com.venosyd.open.commons.util.RESTService;
import com.venosyd.open.repository.logic.RepositoryBS;

import org.bson.Document;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
@Path("/")
public class RepositoryRSImpl implements RepositoryRS, RESTService {

    @Context
    private HttpHeaders headers;

    @Override
    public Response echo() {
        String message = "REPOSITORY ECHO GRANTED" + Calendar.getInstance().get(Calendar.YEAR);

        var echoMessage = new HashMap<String, String>();
        echoMessage.put("status", "ok");
        echoMessage.put("message", message);

        return makeResponse(echoMessage);
    }

    @Override
    public Response getByID(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var id = request.get("id");

            var response = RepositoryBS.INSTANCE.getByID(database, collection, id);
            return makeResponse(response);
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection", "id");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response getIDByQuery(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var query = Document.parse(request.get("query"));

            return makeResponse(RepositoryBS.INSTANCE.getIDByQuery(database, collection, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection", "query");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response getByQuery(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var query = Document.parse(request.get("query"));

            return makeResponse(RepositoryBS.INSTANCE.getByQuery(database, collection, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection", "query");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response list(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var query = request.get("query") != null && !request.get("query").isEmpty()
                    ? Document.parse(request.get("query"))
                    : null;

            return makeResponse(RepositoryBS.INSTANCE.list(database, collection, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response listAllIDs(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var query = request.get("query") != null && !request.get("query").isEmpty()
                    ? Document.parse(request.get("query"))
                    : null;

            return makeResponse(RepositoryBS.INSTANCE.listAllIDs(database, collection, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response listDistinct(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            var field = request.get("field");
            var query = request.get("query") != null && !request.get("query").isEmpty()
                    ? Document.parse(request.get("query"))
                    : null;

            return makeResponse(RepositoryBS.INSTANCE.listDistinct(database, collection, field, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection", "field");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response skipList(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");
            int skip = Integer.parseInt(request.get("skip"));
            var query = request.get("query") != null && !request.get("query").isEmpty()
                    ? Document.parse(request.get("query"))
                    : null;

            return makeResponse(RepositoryBS.INSTANCE.skipList(database, collection, skip, query));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection", "skip");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response save(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var payload = request.get("payload");

            return makeResponse(RepositoryBS.INSTANCE.save(database, payload));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "payload");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response update(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var payload = request.get("payload");

            return makeResponse(RepositoryBS.INSTANCE.update(database, payload));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "payload");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response erase(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var payload = request.get("payload");

            return makeResponse(RepositoryBS.INSTANCE.erase(database, payload));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "payload");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    @Override
    public Response count(String body) {
        Function<Map<String, String>, Response> operation = (request) -> {
            var database = request.get("database");
            var collection = request.get("collection");

            return makeResponse(RepositoryBS.INSTANCE.count(database, collection));
        };

        var authorization = getauthcode(headers);
        var arguments = Arrays.<String>asList("database", "collection");

        return authorization != null ? process(_unwrap(body, true), authorization, arguments, operation) // com headers
                : process(_unwrap(body, false), operation); // com token via post
    }

    //
    // PRIVATE METHODS
    //

    private Map<String, String> _unwrap(String body, boolean withouttoken) {
        body = unzip(body);
        var request = JSONUtil.<String, String>fromJSONToMap(body);

        if (request.containsKey("hash")) {
            var hash = request.get("hash");

            String authkey;
            String token;
            String database;
            String logindb;

            if (withouttoken) {
                authkey = hash.substring(0, 64);
                database = hash.substring(64, 96);
                logindb = hash.substring(96);
            } else {
                authkey = hash.substring(0, 64);
                token = hash.substring(64, 128);
                database = hash.substring(128, 160);
                logindb = hash.substring(160);

                request.put("token", token);
            }

            request.put("authkey", authkey);

            database = (database == null || database.isEmpty()) ? "e5d67ad6f5af2e4bf29c5de07a24c61a" : database;
            request.put("database", database);

            request.put("logindb", logindb);
        }

        return request;
    }

}
