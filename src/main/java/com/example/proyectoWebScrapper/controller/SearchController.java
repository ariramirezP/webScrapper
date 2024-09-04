package com.example.proyectoWebScrapper.controller;

import com.example.proyectoWebScrapper.entities.Webpage;
import com.example.proyectoWebScrapper.services.SearchService;
import com.example.proyectoWebScrapper.services.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private SearchService service;
    @Autowired
    private SpiderService spiderService;
    @CrossOrigin("*")
    @RequestMapping(value = "api/search", method = RequestMethod.GET)
    public List<Webpage> search(@RequestParam Map<String, String> params){
        String query = params.get("query");
        return service.search(query);
    }

    @RequestMapping(value = "api/test", method = RequestMethod.GET)
    public void search(){
         spiderService.indexWebPages();
    }
}
