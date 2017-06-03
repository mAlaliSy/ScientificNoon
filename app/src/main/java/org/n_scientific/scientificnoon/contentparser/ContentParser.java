package org.n_scientific.scientificnoon.contentparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.n_scientific.scientificnoon.utils.ApisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad on 31/05/17.
 */

public class ContentParser {

    private String html;
    private List<PostContent> content;

    public ContentParser(String html) {
        this.html = html;
    }

    public List<PostContent> parseContent() {
        content = new ArrayList<>();

        Elements elements = Jsoup.parse(html).body().children();

        for (Element e : elements) {
            parseNode(e);
        }

        return content;
    }

    private void parseNode(Element e) {
        if (!e.tag().getName().equals("iframe")
                && !e.tag().getName().equals("img")
                && e.getElementsByTag("img").size() == 0
                && e.getElementsByTag("iframe").size() == 0) {
            content.add(new Text(e.toString()));
            return;
        }

        if (e.children().size() != 0) {
            for (Element el : e.children()) {
                parseNode(el);
            }
        } else {
            parseSingleNode(e);
        }
    }

    private void parseSingleNode(Element e) {
        String tagName = e.tag().getName();

        PostContent content = null;
        if (tagName.equals("img")) {
            content = new Image(e.attr("src"));
        } else if (tagName.equals("iframe")) {
            String src = e.attr("src");
            if (src.contains("youtube")) {
                String key = ApisUtils.getYoutubeVideoKey(src);
                content = new YoutubeVideo(key);
            } else if (src.contains("soundcloud")) {
                content = new SoundCloudTrack(ApisUtils.getSoundCloudId(src));
            }
        } else {
            if (tagName.equals("p") || tagName.equals("strong") || tagName.equals("span")) {
                if (e.html().isEmpty() || e.html().equals("\n") || e.html().equals("&nbsp;"))
                    return;
            }
            content = new Text(e.toString());
        }

        if (content != null)
            this.content.add(content);
    }

}
