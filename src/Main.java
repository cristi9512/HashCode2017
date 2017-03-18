import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int nrVideos, nrEndpoints, nrRequest, nrCaches, dimCache;
    public static List<Video> videos;
    public static List<Cache> caches;
    public static List<Request> requests;
    public static List<VideoEndpoint> videoEndpoints;
    public static List<EndPoint> endpoints;

    public static void main(String[] args) throws IOException {
        String in1 = "kittens";
        String in2 = "me_at_the_zoo";
        String in3 = "trending_today";
        String in4 = "videos_worth_spreading";

        citireFisier(in3 + ".in");

        for(Cache cache : caches) {
            cache.createVideosList(videoEndpoints);
        }
        afisareFisier(in3 + ".out");
    }

    /* .................................... */
	/* .................................... */
    public static void citireFisier(String nume) {
        BufferedReader read = null;
        endpoints = new ArrayList<EndPoint>();
        try {

            read = new BufferedReader(new FileReader(nume));

            String line = read.readLine();
            String[] primaLinie = line.split(" ");

            nrVideos = Integer.parseInt(primaLinie[0]);
            nrEndpoints = Integer.parseInt(primaLinie[1]);
            nrRequest = Integer.parseInt(primaLinie[2]);
            nrCaches = Integer.parseInt(primaLinie[3]);
            dimCache = Integer.parseInt(primaLinie[4]);
			/* creare chaches */

            caches = new ArrayList<Cache>();
            Cache.size = dimCache;
            for (int i = 0; i < nrCaches; i++) {
                Cache cha = new Cache(i);
                caches.add(cha);
            }

			/* Refolosesc prima linie -> a doua linie */

            line = read.readLine();
            primaLinie = line.split(" ");
            videos = new ArrayList<Video>();
            for (int i = 0; i < nrVideos; i++) {
                Video vid = new Video(i, Integer.parseInt(primaLinie[i]));
                videos.add(vid);
            }
            for (int i = 0; i < nrEndpoints; i++) {

                line = read.readLine();
                primaLinie = line.split(" ");
                EndPoint ep = new EndPoint(i, Integer.parseInt(primaLinie[0]));
                endpoints.add(ep);
                //int stop = Integer.parseInt(primaLinie[1]);
                for (int j = 0; j < Integer.parseInt(primaLinie[1]); j++) {
                    line = read.readLine();
                    String [] secondLine = line.split(" ");
                    LatencyEndpoint lp = new LatencyEndpoint(
                            Integer.parseInt(secondLine[1]), ep);
                    caches.get(Integer.parseInt(secondLine[0])).addLatencyEndpoint(lp);
                }

            }

            videoEndpoints = new ArrayList<>();
            requests = new ArrayList<>();

            for (int i = 0; i < nrRequest; i++) {
                line = read.readLine();
                primaLinie = line.split(" ");

                VideoEndpoint vp = new VideoEndpoint(
                        videos.get(Integer.parseInt(primaLinie[0])),
                        endpoints.get(Integer.parseInt(primaLinie[1])));
                videoEndpoints.add(vp);

                Request rq = new Request(videos.get(Integer.parseInt(primaLinie[0])),
                        Integer.parseInt(primaLinie[2]));
                requests.add(rq);
            }

            for (Cache cache : caches) {
                for (VideoEndpoint videoEndpoint : videoEndpoints)
                    if (cache.found(videoEndpoint)) {
                        cache.addVideoEndpoint(videoEndpoint);
                    }
            }
        } catch (Exception e) {

        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (Exception e) {
                }
            }
        }
    }

	/* .................................... */
	/* .................................... */

    public static void afisareFisier(String name) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(name));

        int nrValid = 0;
        List<Cache> cachesValid = new ArrayList<>();

        for(Cache cache : caches) {
            if(!cache.listVideo.isEmpty()) {
                nrValid++;
                cachesValid.add(cache);
            }
        }

        bw.write(String.valueOf(nrValid) + "\n");

        for(Cache cache : cachesValid) {
            bw.write(String.valueOf(cache.id));
            for(Video video : cache.listVideo) {
                bw.write(" " + String.valueOf(video.id));
            }
            bw.write('\n');
        }

        bw.close();
    }

}
