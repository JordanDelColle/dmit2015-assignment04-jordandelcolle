package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.resource;

import ca.nait.dmit.dmit2015.jordandelcolle.assignment04.entity.EdmontonPropertyAssessment;
import ca.nait.dmit.dmit2015.jordandelcolle.assignment04.repository.EdmontonPropertyAssessmentRepository;
import common.validation.BeanValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

/**
 *
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */

@ApplicationScoped
@Path("EdmontonPropertyAssessments")                    // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)    // All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)    // All methods returns data that has been converted to JSON format
public class EdmontonPropertyAssessmentResource {

    @Inject
    private EdmontonPropertyAssessmentRepository _edmontonPropertyAssessmentRepository;

    @GET
    @Path("count")
    public Response count() {
        return Response.ok(_edmontonPropertyAssessmentRepository.count()).build();
    }

    @GET    // This method only accepts HTTP GET requests.
    public Response listEdmontonPropertyAssessments() {
        return Response.ok(_edmontonPropertyAssessmentRepository.list()).build();
    }

    @Path("{id}")
    @GET    // This method only accepts HTTP GET requests.
    public Response findEdmontonPropertyAssessmentById(@PathParam("id") String edmontonPropertyAssessmentAccountNumber) {
        EdmontonPropertyAssessment existingEdmontonPropertyAssessment = _edmontonPropertyAssessmentRepository.findOptional(edmontonPropertyAssessmentAccountNumber).orElseThrow(NotFoundException::new);

        return Response.ok(existingEdmontonPropertyAssessment).build();
    }

    @POST    // This method only accepts HTTP POST requests.
    public Response addEdmontonPropertyAssessment(EdmontonPropertyAssessment newEdmontonPropertyAssessment, @Context UriInfo uriInfo) {

        String errorMessage = BeanValidator.validateBean(EdmontonPropertyAssessment.class, newEdmontonPropertyAssessment);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        try {
            // Persist the new EdmontonPropertyAssessment into the database
            _edmontonPropertyAssessmentRepository.create(newEdmontonPropertyAssessment);
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // userInfo is injected via @Context parameter to this method
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(newEdmontonPropertyAssessment.getAccountNumber() + "")
                .build();

        // Set the location path of the new entity with its identifier
        // Returns an HTTP status of "201 Created" if the EdmontonPropertyAssessment was successfully persisted
        return Response
                .created(location)
                .build();
    }

    @PUT            // This method only accepts HTTP PUT requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response updateEdmontonPropertyAssessment(@PathParam("id") String edmontonPropertyAssessmentAccountNumber, EdmontonPropertyAssessment updatedEdmontonPropertyAssessment) {
        if (!edmontonPropertyAssessmentAccountNumber.equals(updatedEdmontonPropertyAssessment.getAccountNumber())) {
            throw new BadRequestException();
        }

        _edmontonPropertyAssessmentRepository.findOptional(edmontonPropertyAssessmentAccountNumber).orElseThrow(NotFoundException::new);

        String errorMessage = BeanValidator.validateBean(EdmontonPropertyAssessment.class, updatedEdmontonPropertyAssessment);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        try {
            _edmontonPropertyAssessmentRepository.update(updatedEdmontonPropertyAssessment);
        } catch (Exception ex) {
            // Return an HTTP status of "500 Internal Server Error" containing the exception message
            return Response.
                    serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "204 No Content" if the EdmontonPropertyAssessment was successfully persisted
        return Response.noContent().build();
    }

    @DELETE            // This method only accepts HTTP DELETE requests.
    @Path("{id}")    // This method accepts a path parameter and gives it a name of id
    public Response delete(@PathParam("id") String edmontonPropertyAssessmentAccountNumber) {

        _edmontonPropertyAssessmentRepository.findOptional(edmontonPropertyAssessmentAccountNumber).orElseThrow(NotFoundException::new);

        try {
            _edmontonPropertyAssessmentRepository.delete(edmontonPropertyAssessmentAccountNumber);    // Removes the EdmontonPropertyAssessment from being persisted
        } catch (Exception ex) {
            // Return a HTTP status of "500 Internal Server Error" containing the exception message
            return Response
                    .serverError()
                    .encoding(ex.getMessage())
                    .build();
        }

        // Returns an HTTP status "204 No Content" if the EdmontonPropertyAssessment was successfully deleted
        return Response.noContent().build();
    }

    @GET
    @Path("/query/within")
    public Response within(
            @QueryParam("longitude") double longitude,
            @QueryParam("latitude") double latitude,
            @QueryParam("distance") double distanceMetre
    ) {
        List<EdmontonPropertyAssessment> queryResultList = _edmontonPropertyAssessmentRepository
                .findWithinDistance(longitude, latitude, distanceMetre);
        return Response.ok(queryResultList).build();
    }

    @GET
    @Path("/query/byHouseNumberAndStreetName")
    public Response findByHouseNumberAndStreetName(
            @QueryParam("houseNumber") String houseNumber,
            @QueryParam("streetName") String streetName) {
        EdmontonPropertyAssessment querySingleResult = _edmontonPropertyAssessmentRepository
                .findByHouseNumberAndStreetName(houseNumber, streetName)
                .orElseThrow(NotFoundException::new);
        return Response.ok(querySingleResult).build();
    }



    @GET
    @Path("/query/byNeighbourhood")
    public Response findbyNeighbourhood(
            @QueryParam("neighbourhood") String neighbourhood) {
        List<EdmontonPropertyAssessment> queryResultList = _edmontonPropertyAssessmentRepository
                .findbyNeighbourhood(neighbourhood);
        return Response.ok(queryResultList).build();
    }

    @GET
    @Path("/query/byNeighbourhoodAndAssessedValueRange")
    public Response findbyNeighbourhoodAndAssessedValueRange(
            @QueryParam("neighbourhood") String neighbourhood,
            @QueryParam("minValue") Long minValue,
            @QueryParam("maxValue") Long maxValue){
        List<EdmontonPropertyAssessment> queryResultList = _edmontonPropertyAssessmentRepository
                .findbyNeighbourhoodAndAssessedValueRange(neighbourhood, minValue, maxValue);
        return Response.ok(queryResultList).build();
    }
}

