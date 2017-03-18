import java.util.*;

public class Cache {
    public static int size;
    public Set<Video> listVideo;
    public List<LatencyEndpoint> listLatencyEndpoint;
    public List<VideoEndpoint> listVideoEndpoint;
    public PriorityQueue<VideoEndpoint> priorityQueue;
    public int id;

    public Cache(int id) {
        this.id = id;
        listLatencyEndpoint = new ArrayList<>();
        listVideo = new HashSet<Video>();
        listVideoEndpoint = new ArrayList<>();
        PriorityComparator comp = new PriorityComparator();
        priorityQueue = new PriorityQueue<>(comp);
    }

    public void createVideosList(List<VideoEndpoint> listVideoEndpoint) {
        for(VideoEndpoint videoEndpoint : listVideoEndpoint) {
            priorityQueue.add(videoEndpoint);
        }

        int sum = 0;
        while(sum < size && !priorityQueue.isEmpty()) {
            if(priorityQueue.peek().video.size + sum < size) {
                sum += priorityQueue.peek().video.size;
                listVideo.add(priorityQueue.remove().video);
            }
            else
                priorityQueue.remove();
        }
    }

    public void addLatencyEndpoint(LatencyEndpoint latencyEndpoint) {
        listLatencyEndpoint.add(latencyEndpoint);
    }
    
    public void addVideoEndpoint(VideoEndpoint videoEndpoint) {
        listVideoEndpoint.add(videoEndpoint);
    }

    public void addVideo(Video video) {
        listVideo.add(video);
    }

    public EndPoint findEndPoint(EndPoint endPoint) {
        for(LatencyEndpoint latencyEndpoint : listLatencyEndpoint) {
            if(latencyEndpoint.endPoint.id == endPoint.id)
                return latencyEndpoint.endPoint;
        }
        return null;
    }

    public boolean found (VideoEndpoint videoEndpoint){
        for (LatencyEndpoint latencyEndpoint : listLatencyEndpoint){
            if (latencyEndpoint.endPoint.id == videoEndpoint.endPoint.id)
                return true;
        }
        return false;
    }

    public long priority(VideoEndpoint videoEndpoint) {
        if((findEndPoint(videoEndpoint.endPoint)) == null || videoEndpoint.endPoint.findRequest(videoEndpoint.video) == null)
            return 0;
        return (videoEndpoint.endPoint.latency -
                (findEndPoint(videoEndpoint.endPoint)).latency) *
                videoEndpoint.endPoint.findRequest(videoEndpoint.video).nrRequests /
                videoEndpoint.video.size;
    }

    class PriorityComparator implements Comparator<VideoEndpoint> {

        @Override
        public int compare(VideoEndpoint o1, VideoEndpoint o2) {
            return (int)(priority(o1) - priority(o2));
        }
    }
}
