package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedResponse extends PagedResponse {

    public GetFeedResponse(boolean success, boolean hasMorePages) {
        super(success, hasMorePages);
    }

    public GetFeedResponse(List<Status> statuses, boolean hasMorePages) {
        super(true, false);
        this.statuses = statuses;
    }

    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        GetFeedResponse that = (GetFeedResponse) param;

        return (Objects.equals(statuses, that.statuses) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses);
    }

}
