package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<GetStoryRequest, GetStoryResponse> {
    @Override
    public GetStoryResponse handleRequest(GetStoryRequest request, Context context) {
        StatusService service = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoAuthtokenDAO());
        return service.getStory(request);
    }
}
