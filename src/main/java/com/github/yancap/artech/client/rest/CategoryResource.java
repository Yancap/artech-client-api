package com.github.yancap.artech.client.rest;

import com.github.yancap.artech.client.services.CategoryService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("client/api/v1/category/")
public class CategoryResource {

  @Inject
  CategoryService categoryService;
  
  @GET
  public Response getAll() {
    var response = categoryService.getAll();
    return Response.status(200).entity(response).build();
  }

  
}
