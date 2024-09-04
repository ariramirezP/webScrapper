package com.example.proyectoWebScrapper.services;

import com.example.proyectoWebScrapper.entities.Webpage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.internal.util.StringHelper.isBlank;

@Service
public class SpiderService {
    @Autowired
    private SearchService searchService;
    public void indexWebPages() {
        List<Webpage> linksToIndex = searchService.getLinksToIndex();
        linksToIndex.stream().parallel().forEach(webPage -> {
            try {
                System.out.println("Indexing");
                indexWebPage(webPage);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void indexWebPage(Webpage webPage) throws Exception {
        String url = webPage.getUrl();
        System.out.println(url);
        String content = getWebContent(url);
        if (isBlank(content)) {
            return;
        }

        saveWebPage(webPage, content);

        System.out.println("Domain: " + getDomain(url));
        saveLinks(getDomain(url), content);
    }


    public String getDomain(String url){
        String[] aux = url.split("/");
        return aux[0] + "//" + aux[2];
    }

    public void saveLinks(String domain, String content){
        System.out.println("save links");
        List<String> links = getlinks(domain, content);
        System.out.println("Links :"+ links);
        links.stream()
                .filter(link -> !searchService.exist(link))
                .map(link -> new Webpage(link))
                .forEach(webpage -> searchService.save(webpage));

    }

    public List<String> getlinks(String domain, String content){
        List<String> links = new ArrayList<>();

        String[] splitHref = content.split("href=\"");

        List<String> listHref = Arrays.asList(splitHref);

        listHref.forEach(strHref ->{
            String[] aux = strHref.split("\"");
            links.add(aux[0]);
        });

        return cleanLinks(domain, links);
    }

    private List<String> cleanLinks(String domain, List<String> links) {
        String[] excludedExtensions = new String[]{"css","js","json","jpg","png","woff2"};

        List<String> resultLinks = links.stream()
                .filter(link -> Arrays.stream(excludedExtensions).noneMatch(link::endsWith))
                .map(link -> link.startsWith("/") ? domain + link : link)
                .filter(link -> link.startsWith("http"))
                .collect(Collectors.toList());

        List<String> uniqueLinks = new ArrayList<>();
        uniqueLinks.addAll(new HashSet<>(resultLinks));

        return uniqueLinks;
    }

    private void saveWebPage(Webpage webpage, String content) {
        String title = getTitle(content);
        String description = getDescription(content);
        webpage.setTitle(title);
        webpage.setDescription(description);
        searchService.save(webpage);
    }

    public String getTitle(String content){
        String[] aux = content.split("<title>");
        String[] aux2 = aux[1].split("</title>");
        return aux2[0];
    }

    public String getDescription(String content){
        String[] aux = content.split("<meta name=\"description\" content=\"");
        String[] aux2 = aux[1].split("\"/>");
        return aux2[0];
    }
    private static String getWebContent(String link){
        try {
            URL url = new URL(link);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            String encoding = coon.getContentEncoding();

            InputStream input = coon.getInputStream();

            Stream<String> lines = new BufferedReader(new InputStreamReader(input))
                    .lines();

            return  lines.collect(Collectors.joining());
        }catch (IOException e){
            System.out.print(e.getMessage());
        }
        return "";
    }
}
