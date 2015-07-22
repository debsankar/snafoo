package com.esnacks4nerds.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.esnacks4nerds.model.entity.Suggestion;

/**
 * Session Bean implementation class SnackVoteDAO
 */
@Stateless
@LocalBean
public class SuggestionDAO {

    /**
     * Default constructor. 
     */
	
	@PersistenceContext(unitName = "esnacks")
    private EntityManager em;
	
    public SuggestionDAO() {
        // TODO Auto-generated constructor stub
    }


	
	public List<Suggestion> findAll() {
		Query query = em.createNamedQuery("findAllSuggestions",Suggestion.class);
		List<Suggestion> suggestions = query.getResultList();
		return suggestions;
	}
	

	


	public void persist(Suggestion entity) {
		em.persist(entity);
		em.flush();
	}







    
}
