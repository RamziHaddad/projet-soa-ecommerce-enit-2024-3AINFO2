package org.ecommerce.api.ExceptionMappers;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.ecommerce.exceptions.EntityAlreadyExistsException;

@Provider
public class EntityAlreadyExistsExceptionMapper
     implements ExceptionMapper<EntityAlreadyExistsException> {

   public Response toResponse(EntityAlreadyExistsException e) {
      return Response.status(Response.Status.BAD_REQUEST).build();
   }
}