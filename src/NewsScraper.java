
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;

public class NewsScraper {

    public static void main(String[] args) {

        String url = "https://timesofindia.indiatimes.com";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(15000)
                    .get();

            // Correct selector for TOI headlines
            Elements headlines = doc.select("a[href].w_tle");

            FileWriter writer = new FileWriter("news.html");

            writer.write("<html><head><title>Top News</title></head><body>");
            writer.write("<h2>Top News Headlines</h2><ul>");

            int count = 0;
            for (Element h : headlines) {
                String title = h.text();
                String link = h.absUrl("href");

                if (!title.isEmpty()) {
                    writer.write("<li><a href='" + link + "'>" + title + "</a></li>");
                    count++;
                }
                if (count == 10) break;
            }

            writer.write("</ul></body></html>");
            writer.close();

            System.out.println("News fetched successfully. Open news.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
