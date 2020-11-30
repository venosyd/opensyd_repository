package com.venosyd.open.repository.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public interface RepositoryRS {

    String REPOSITORY_BASE_URI = "/repository";

    String REPOSITORY_GET_ID = "/get/id";

    String REPOSITORY_GET_ID_QUERY = "/get/id/query";

    String REPOSITORY_GET_QUERY = "/get/query";

    String REPOSITORY_LIST = "/list";

    String REPOSITORY_LIST_ALL_IDS = "/list/ids";

    String REPOSITORY_LIST_DISTINCT = "/list/distinct";

    String REPOSITORY_LIST_SKIP = "/list/skip";

    String REPOSITORY_COUNT = "/count";

    String REPOSITORY_SAVE = "/save";

    String REPOSITORY_UPDATE = "/update";

    String REPOSITORY_ERASE = "/erase";

    /**
     * Hello from the server siiiiiiide!
     */
    @GET
    @Path("/echo")
    @Produces({ MediaType.APPLICATION_JSON })
    Response echo();

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' id: '7772DCA34BF3'
     * }
     */
    @POST
    @Path(REPOSITORY_GET_ID)
    @Produces({ MediaType.APPLICATION_JSON })
    Response getByID(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' query: { ... } }
     */
    @POST
    @Path(REPOSITORY_GET_QUERY)
    @Produces({ MediaType.APPLICATION_JSON })
    Response getByQuery(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' query: { ... } }
     */
    @POST
    @Path(REPOSITORY_GET_ID_QUERY)
    @Produces({ MediaType.APPLICATION_JSON })
    Response getIDByQuery(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' query: { ... } }
     */
    @POST
    @Path(REPOSITORY_LIST)
    @Produces({ MediaType.APPLICATION_JSON })
    Response list(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' query: { ... } }
     */
    @POST
    @Path(REPOSITORY_LIST_ALL_IDS)
    @Produces({ MediaType.APPLICATION_JSON })
    Response listAllIDs(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' field: 'phone'
     * query: { ... } }
     */
    @POST
    @Path(REPOSITORY_LIST_DISTINCT)
    @Produces({ MediaType.APPLICATION_JSON })
    Response listDistinct(String body);

    /**
     * { token: authkey: database: 'my-app' collection: 'contact' skip: 1278 }
     */
    @POST
    @Path(REPOSITORY_LIST_SKIP)
    @Produces({ MediaType.APPLICATION_JSON })
    Response skipList(String body);

    /**
     * { token: authkey: database: 'my-app' payload: { ... } }
     */
    @POST
    @Path(REPOSITORY_SAVE)
    @Produces({ MediaType.APPLICATION_JSON })
    Response save(String body);

    /**
     * { token: authkey: database: 'my-app' payload: { ... } }
     */
    @POST
    @Path(REPOSITORY_UPDATE)
    @Produces({ MediaType.APPLICATION_JSON })
    Response update(String body);

    /**
     * { token: authkey: database: 'my-app' payload: { ... } }
     */
    @POST
    @Path(REPOSITORY_ERASE)
    @Produces({ MediaType.APPLICATION_JSON })
    Response erase(String body);

    /**
     * { token: authkey: database: 'my-app' collection: Collection }
     */
    @POST
    @Path(REPOSITORY_COUNT)
    @Produces({ MediaType.APPLICATION_JSON })
    Response count(String body);

}
