package pcd.ass02;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LinkCounterWorker extends AbstractVerticle{
    String website;
    int depth;

    public LinkCounterWorker(String website, int depth){
        this.website = website;
        this.depth = depth;
    }

    public void start(){
        EventBus eventBus = this.getVertx().eventBus();
        Document d;
        try {
            d = Jsoup.connect(website).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> links = Arrays.stream(d.body().text().split(" ")).toList();
        links = links.stream()
                    .map((e) -> e = e.toLowerCase())
                    .filter((e) -> e.startsWith("https://") || e.startsWith("http://"))
                    .toList();
        for(String s: links){
            eventBus.publish("link", s);
        }
    }
}
