
package news;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MainServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // HOME PAGE
        server.createContext("/", exchange -> {

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>News Aggregator</title>
                    <style>
                        body { font-family: Arial, sans-serif; background: #f4f4f4; padding: 20px; }
                        h1 { color: #333; text-align: center; }
                        select, button { padding: 8px 12px; margin: 5px; }
                        ul { list-style: none; padding: 0; }
                        li { background: #fff; margin: 5px 0; padding: 10px; border-radius: 5px; 
                             box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
                        li a { text-decoration: none; color: #1a0dab; font-weight: bold; }
                        li a:hover { text-decoration: underline; }
                        small { color: #555; }
                    </style>
                </head>
                <body>
                    <h1> News Aggregator</h1>

                    <select id="category">
                        <option value="general">General</option>
                        <option value="technology">Technology</option>
                        <option value="sports">Sports</option>
                        <option value="entertainment">Entertainment</option>
                    </select>

                    <button onclick="loadNews()">Fetch News</button>

                    <div id="news"></div>

                    <script>
                        function loadNews() {
                            const cat = document.getElementById('category').value;
                            fetch('/news?category=' + cat)
                                .then(res => res.text())
                                .then(data => {
                                    document.getElementById('news').innerHTML = data;
                                });
                        }
                    </script>
                </body>
                </html>
                """;

            byte[] response = html.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        // NEWS API
        server.createContext("/news", exchange -> {
            String query = exchange.getRequestURI().getQuery(); // e.g., category=technology
            String category = "general";
            if (query != null && query.startsWith("category=")) {
                category = query.split("=")[1];
            }

            String newsHtml = NewsFetcher.getNewsHTML(category);
            byte[] response = newsHtml.getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        server.start();
        System.out.println(" Server started → http://localhost:8080/");
    }
}
