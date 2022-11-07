package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class GetFollowersCountResponse extends Response {

    public GetFollowersCountResponse(int count) {
        super(true, null);
        this.count = count;
    }

    public GetFollowersCountResponse(String message) {
        super(false, message);
    }

    private int count;

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        GetFollowersCountResponse that = (GetFollowersCountResponse) param;

        return (Objects.equals(count, that.count) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
