package com.vertx.hendra;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.rxjava.ext.web.handler.BodyHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vertx.hendra.model.*;

public class FirstVerticleApp extends AbstractVerticle  
{	
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_APPLICATION_JSON = "application/json; charset=UTF8";

	private Map<Integer, Users> usersMap = new LinkedHashMap();
	
	@Override
	  public void start(Future<Void> fut) {
		
		usersData();
		
		// Create a router object.
		Router router = Router.router(vertx);
		
		// Bind "/"
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON)
			.end("<h1> Hendra First Vert.x App </>");
		});
		
//		router.route().handler(StaticHandler.create("assets"));
		
		router.get("/api/users").handler(this::getAll);
//		router.route("/api/whiskies*").handler(BodyHandler.create());
		router.post("/api/users").handler(this::addOne);
		router.get("/api/users/:id").handler(this::getOne);
		router.put("/api/users/:id").handler(this::updateOne);
		router.delete("/api/users/:id").handler(this::deleteOne);
	  
		
		vertx
		.createHttpServer()
		.requestHandler(router::accept)
		.listen(
				config().getInteger("http.port", 9090), 
				result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
	}
	
	private void addOne(RoutingContext routingContext)
	{
		// Read the request's content and create an instance of Users
		Users users = getDataUsers(routingContext); 
		
		// Add it to the backend map
	    usersMap.put(users.getId(), users);

	    // Return the created Users as JSON
	    routingContext.response()
	    .setStatusCode(201)
	    .putHeader(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON)
	    .end(Json.encodePrettily(users));
	}
	
	private void getOne(RoutingContext routingContext)
	{
		final String id = routingContext.request().getParam("id");
		
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			final Integer idAsInt = Integer.valueOf(id);
			Users users = usersMap.get(id);
			
			if (users == null)
			{
				routingContext.response().setStatusCode(404).end();
			} else {
				routingContext.response()
				.setStatusCode(200)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON)
				.end(Json.encodePrettily(users));
			}
		}
	}
	
	private void updateOne(RoutingContext routingContext)
	{
		final String id = routingContext.request().getParam("id");
		
		JsonObject json = routingContext.getBodyAsJson();
		
		if (json == null || id == null)
		{
			routingContext.response().setStatusCode(400).end();
		} else {
			final Integer idAsInt = Integer.valueOf(id);
			Users users = usersMap.get(idAsInt);
			
			if (users == null) {
				routingContext.response().setStatusCode(404).end();
			} else {
				users.setFirstName(json.getString("firstName"));
				users.setLastName(json.getString("lastName"));
				users.setAge(Integer.valueOf(json.getString("age")));
				users.setEmail(json.getString("email"));
				
				routingContext.response()
				.setStatusCode(201)
				.putHeader(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON)
				.end(Json.encodePrettily(users));
			}
		}
	}
	
	private void deleteOne(RoutingContext routingContext)
	{
		final String id = routingContext.request().getParam("id");
		
		if (id == null)
		{
			routingContext.response().setStatusCode(400).end();
		} else {
			final Integer idAsInt = Integer.valueOf(id);
			usersMap.remove(idAsInt);
			
			routingContext.response().setStatusCode(204).end();
		}
	}
	
	private void getAll(RoutingContext routingContext)
	{
		// Write the HTTP response
	    // The response is in JSON using the utf-8 encoding
		// Return the list form values of our Users map.
		routingContext.response()
		.setStatusCode(200)
		.putHeader(HEADER_CONTENT_TYPE, HEADER_APPLICATION_JSON).end(Json.encodePrettily(usersMap.values()));
	}
	
	private Users getDataUsers(RoutingContext routingContext)
	{
		return Json.decodeValue(routingContext.getBodyAsString(), Users.class);
	}
	
	private void usersData()
	{
		Users users1 = new Users("Hendra", "Foldram", 18, "hendfold@gm.org");
		usersMap.put(users1.getId(), users1);
		Users users2 = new Users("Honda", "Supra", 24, "honda@cars.org");
		usersMap.put(users2.getId(), users2);
	}
}
