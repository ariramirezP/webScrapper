package com.example.proyectoWebScrapper.services;

import com.example.proyectoWebScrapper.entities.Webpage;
import com.example.proyectoWebScrapper.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    private SearchRepository repository;

    public List<Webpage> search(String textSearch){

        return repository.search(textSearch);
    }

    public void save(Webpage webpage){
        repository.save(webpage);
    }

    public boolean exist(String link) {
        return repository.exist(link);
    }

    public List<Webpage> getLinksToIndex(){
        return repository.getLinksToIndeex();
    }
}
