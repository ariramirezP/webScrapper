package com.example.proyectoWebScrapper.repositories;

import com.example.proyectoWebScrapper.entities.Webpage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class SearchRepositoryImp implements SearchRepository{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Webpage getByUrl(String url) {
        String query = "FROM Webpage WHERE url = :url";
        List<Webpage> list = entityManager.createQuery(query)
                .setParameter("url", url)
                .getResultList();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    @Transactional
    public List<Webpage> getLinksToIndeex() {
        int pagesToIndex = 100;
        String query = "FROM Webpage WHERE title is null AND description is null";
        return entityManager.createQuery(query)
                .setMaxResults(pagesToIndex)
                .getResultList();
    }

    @Override
    @Transactional
    public List<Webpage> search(String textSearch) {
        String query = "FROM Webpage WHERE description like :textSearch";
        return entityManager.createQuery(query)
                .setParameter("textSearch", "%"+textSearch+"%")
                .getResultList();
    }

    @Transactional
    @Override
    public void save(Webpage webpage) {
        entityManager.merge(webpage);
    }

    @Override
    @Transactional
    public boolean exist(String link) {
        return getByUrl(link)!= null;
    }


}
