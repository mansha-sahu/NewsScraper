
package news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsFetcher {

    public static String getNewsHTML(String category) {

        String rssUrl = switch (category.toLowerCase()) {
            case "technology" -> "https://feeds.bbci.co.uk/news/technology/rss.xml";
            case "sports" -> "https://feeds.bbci.co.uk/sport/rss.xml";
            case "entertainment" -> "https://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml";
            default -> "https://feeds.bbci.co.uk/news/rss.xml"; // general
        };

        StringBuilder news = new StringBuilder("<ul>");

        try {
            Document doc = Jsoup.connect(rssUrl)
                    .timeout(15000)
                    .get();

            Elements items = doc.select("item");

            for (int i = 0; i < Math.min(10, items.size()); i++) {
                Element item = items.get(i);
                String title = item.select("title").text();
                String link = item.select("link").text();
                String pubDate = item.select("pubDate").text();

                news.append("<li>")
                        .append("<a href='").append(link).append("' target='_blank'>")
                        .append(title)
                        .append("</a>")
                        .append(" – <small>").append(pubDate).append("</small>")
                        .append("</li>");
            }

        } catch (Exception e) {
            news.append("<li>Error fetching news</li>");
        }

        news.append("</ul>");
        return news.toString();
    }
}
