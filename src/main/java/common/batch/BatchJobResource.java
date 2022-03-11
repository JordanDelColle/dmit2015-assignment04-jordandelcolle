package common.batch;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.operations.JobStartException;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Set;

/**
 * This resource contains methods for starting a batch job and to check the status of a batch job.
 *
 * 	URI					Http Method		Description
 * 	----------------	-----------		------------------------------------------
 *	/batch-jobs			POST			Start a new Batch Job
 *	/batch-jobs/1		GET				Find the status of the specified batch job
 *	/batch-jobs/names	GET				Get a set of batch job names
 *
 * @version 2022.03.11
 *
 */

@Path("batch-jobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BatchJobResource {

	@POST
	@Path("{filename}")
	public Response startBatchJob(@PathParam("filename") String jobXMLName, @Context UriInfo uriInfo) {
		JobOperator jobOperator = BatchRuntime.getJobOperator();

		try {
			long jobId = jobOperator.start(jobXMLName, null);

			URI location = URI.create(uriInfo.getBaseUri().toString() + "batch-jobs/" + jobId );

			return Response
				.created(location)
				.build();
		} catch (JobStartException ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.getMessage()).build();
		}
	}

	@GET
	@Path("{id}")
	public Response getBatchStatus(@PathParam("id") Long jobId) {
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		try {
			JobExecution jobExecution = jobOperator.getJobExecution(jobId);
			String jobStatus = jobExecution.getBatchStatus().toString();
			return Response.ok(jobStatus).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("jobnames")
	public Response getJobNames() {
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		try {
			Set<String> jobNames = jobOperator.getJobNames();
			return Response.ok(jobNames).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.getMessage()).build();
		}
	}
}