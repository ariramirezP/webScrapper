package com.example.proyectoWebScrapper.repositories;

import com.example.proyectoWebScrapper.entities.Webpage;

import java.util.List;

public interface SearchRepository {

    Webpage getByUrl(String url);

    List<Webpage> getLinksToIndeex();

    List<Webpage> search(String textSearch);

    void save(Webpage webpage);

    boolean exist(String link);
}

