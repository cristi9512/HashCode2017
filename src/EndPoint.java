import java.util.ArrayList;
import java.util.List;

public class EndPoint {
	public List<Request> listRequest;
	public int id;
	public int latency;

	public EndPoint(int id, int latency) {
		this.listRequest = new ArrayList<Request>();
		this.id = id;
		this.latency = latency;
	}

	public void addRequest(Request request) {
		this.listRequest.add(request);
	}

    public Request findRequest(Video v) {
        for(Request r : listRequest) {
            if(r.video.id == v.id)
                return r;
        }
        return null;
    }
}
