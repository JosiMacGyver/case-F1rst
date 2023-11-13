package br.com.joseana;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//tutorial
// https://github.com/marcuspaulo/quarkus-panache-car
// https://dev.to/marcuspaulo/simplificando-o-hibernate-utilizando-panache-criando-uma-aplicacao-simples-utilizando-quarkus-java-rest-cdi-panache-14pj
// https://www.youtube.com/watch?v=kAui1-4KBrk&t=109s&ab_channel=GiuseppeScaramuzzino
//https://blog.codeleak.pl/2020/02/getting-started-with-quarkus.html 
// docker run --name product -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=product -p 5433:5432 postgres

import java.util.List;

import io.quarkus.panache.common.Page;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// docker run --name product -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=product -p 5433:5432 postgres

@Path("/product")
public class ProductResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        int defaultPage = (page != null && page >= 0) ? page : 0;
        int defaultPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        return Product.findAll().page(defaultPage, defaultPageSize).list();
    }

    @GET
    @Path("/valid")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> valid(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        int defaultPage = (page != null && page >= 0) ? page : 0;
        int defaultPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        LocalDate today = LocalDate.now();
        return Product.find("expiry_date > ?1", today).page(Page.of(defaultPage, defaultPageSize)).list();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Product product = Product.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(product).status(Response.Status.CREATED).build();
    }

    public boolean isRequiredFieldMissing(Product product) {
        return product.name == null || product.quantity == 0 || product.expiry_date == null
                || product.description == null;
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Product product) {
        if (isRequiredFieldMissing(product)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Todos os campos são obrigatórios")
                    .build();
        }
        try {
            LocalDate.parse(product.expiry_date.toString());
            Product.persist(product);
            return Response.created(URI.create("/product/" + product.id)).build();

        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data deve estar no formato yyyy-MM-dd.")
                    .build();
        }
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Product product) {
        if (isRequiredFieldMissing(product)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Todos os campos são obrigatórios")
                    .build();
        }
        try {
            LocalDate.parse(product.expiry_date.toString());

            Product productUpdate = Product.findById(id);

            if (productUpdate == null) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }

            productUpdate.name = product.name;
            productUpdate.quantity = product.quantity;
            productUpdate.expiry_date = product.expiry_date;
            productUpdate.description = product.description;

            return Response.ok(productUpdate).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data deve estar no formato yyyy-MM-dd")
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Product product = Product.findById(id);
        product.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
 

    
}