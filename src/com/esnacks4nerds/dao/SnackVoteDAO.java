package com.esnacks4nerds.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.esnacks4nerds.model.entity.SnackVote;

/**
 * Session Bean implementation class SnackVoteDAO
 */
@Stateless
@LocalBean
public class SnackVoteDAO {

    /**
     * Default constructor. 
     */
	
	@PersistenceContext(unitName = "esnacks")
    private EntityManager em;
	
    public SnackVoteDAO() {
        // TODO Auto-generated constructor stub
    }


	
	public List<SnackVote> findAll() {
		Query query = em.createNamedQuery("findAllVotes",SnackVote.class);
		List<SnackVote> votes = query.getResultList();
		return votes;
	}
	

	
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}


	public void persist(SnackVote entity) {
		em.persist(entity);
		em.flush();
	}


	public void update(SnackVote entity) {
		// TODO Auto-generated method stub
		
	}


	public SnackVote findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	public void delete(SnackVote entity) {
		// TODO Auto-generated method stub
		
	}

    
    
}
