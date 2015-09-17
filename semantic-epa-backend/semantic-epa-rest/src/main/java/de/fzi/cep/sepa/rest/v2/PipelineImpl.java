package de.fzi.cep.sepa.rest.v2;

import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonSyntaxException;

import de.fzi.cep.sepa.commons.exceptions.NoMatchingFormatException;
import de.fzi.cep.sepa.commons.exceptions.NoMatchingProtocolException;
import de.fzi.cep.sepa.commons.exceptions.NoMatchingSchemaException;
import de.fzi.cep.sepa.commons.exceptions.NoSuitableSepasAvailableException;
import de.fzi.cep.sepa.manager.operations.Operations;
import de.fzi.cep.sepa.messages.Notification;
import de.fzi.cep.sepa.messages.NotificationType;
import de.fzi.cep.sepa.messages.Notifications;
import de.fzi.cep.sepa.messages.PipelineOperationStatus;
import de.fzi.cep.sepa.model.client.Pipeline;
import de.fzi.cep.sepa.rest.api.AbstractRestInterface;
import de.fzi.cep.sepa.rest.api.v2.PipelineOperation;
import de.fzi.cep.sepa.storage.controller.StorageManager;
import de.fzi.sepa.model.client.util.Utils;

@Path("/v2/users/{username}/pipelines")
public class PipelineImpl extends AbstractRestInterface implements PipelineOperation {

	
	@Override
	public String getAvailable(String username) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFavorites(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/own")
	@Override
	public String getOwn(@PathParam("username") String username) {
		return Utils.getGson().toJson(userService.getOwnPipelines(username));
	}

	@Override
	public String addFavorite(String username, String elementUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeFavorite(String username, String elementUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@DELETE
	@Path("/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String removeOwn(@PathParam("username") String username, @PathParam("pipelineId") String elementUri) {
		StorageManager.INSTANCE.getPipelineStorageAPI().deletePipeline(elementUri);
		return toJson(Notifications.success("Pipeline deleted"));
	}

	@Override
	public String getAsJsonLd(String elementUri) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@GET
	@Path("/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String getElement(String username, @PathParam("pipelineId") String pipelineId) {
		return toJson(StorageManager.INSTANCE.getPipelineStorageAPI().getPipeline(pipelineId));
	}

	@Path("/{pipelineId}/start")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String start(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId)
	{
		try {
		de.fzi.cep.sepa.model.client.Pipeline pipeline = userService.getPipeline(username, pipelineId);
		PipelineOperationStatus status = Operations.startPipeline(pipeline);
		return toJson(status);
		} catch (Exception e)
		{
			e.printStackTrace();
			return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
		}
	}
	
	@Path("/{pipelineId}/stop")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String stop(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId)
	{
		try {
			de.fzi.cep.sepa.model.client.Pipeline pipeline = userService.getPipeline(username, pipelineId);
			PipelineOperationStatus status = Operations.stopPipeline(pipeline);
			return toJson(status);
			} catch (Exception e)
			{
				return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
			}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addPipeline(@PathParam("username") String username, String pipeline)
	{
		Pipeline serverPipeline = Utils.getGson().fromJson(pipeline, Pipeline.class);
		serverPipeline.setPipelineId(UUID.randomUUID().toString());
		serverPipeline.setRunning(false);
		serverPipeline.setCreatedByUser(username);
		userService.addOwnPipeline(username, serverPipeline);
		return toJson(Notifications.success(NotificationType.PIPELINE_STORAGE_SUCCESS));
	}

	@Path("/recommend")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String recommend(String pipeline)
	{
		try {
			return toJson(Operations.findRecommendedElements(Utils.getGson().fromJson(pipeline, de.fzi.cep.sepa.model.client.Pipeline.class)));
		} catch (JsonSyntaxException e) {
			return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
		} catch (NoSuitableSepasAvailableException e) {
			return constructErrorMessage(new Notification(NotificationType.NO_SEPA_FOUND.title(), NotificationType.NO_SEPA_FOUND.description(), e.getMessage()));
		}  
		catch (Exception e) {
			e.printStackTrace();
			return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
		}
	}

	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String update(String pipeline)
	{
		try {
			return toJson(Operations.validatePipeline(Utils.getGson().fromJson(pipeline, de.fzi.cep.sepa.model.client.Pipeline.class), true));
		} catch (JsonSyntaxException e) {
			return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
		} catch(NoMatchingSchemaException e) {
			return constructErrorMessage(new Notification(NotificationType.NO_VALID_CONNECTION.title(), NotificationType.NO_VALID_CONNECTION.description(), e.getMessage()));
		} catch(NoMatchingFormatException e) {
			return constructErrorMessage(new Notification(NotificationType.NO_MATCHING_FORMAT_CONNECTION.title(), NotificationType.NO_MATCHING_FORMAT_CONNECTION.description(), e.getMessage()));
		} catch(NoMatchingProtocolException e) {
			return constructErrorMessage(new Notification(NotificationType.NO_MATCHING_PROTOCOL_CONNECTION.title(), NotificationType.NO_MATCHING_PROTOCOL_CONNECTION.description(), e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
		}
	}

}
