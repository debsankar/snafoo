package com.esnacks4nerds.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: SnackVote
 *
 */
@Entity
@Table(name = "SUGGESTION")
@NamedQueries({
	@NamedQuery(name = "findAllSuggestions", query = "SELECT v FROM Suggestion v "),
	@NamedQuery(name = "suggestionExists", query = "SELECT v FROM Suggestion v where v.snack_id=:snack_id"),
	})
public class Suggestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5190827484027379819L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;
	private Integer snack_id;
	private Timestamp suggestion_date;

	public Suggestion() {
		super();
	}

	public Integer getID() {
		return this.ID;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	public Integer getSnack_id() {
		return this.snack_id;
	}

	public void setSnack_id(Integer snack_id) {
		this.snack_id = snack_id;
	}

	public Timestamp getSuggestion_date() {
		return suggestion_date;
	}

	public void setSuggestion_date(Timestamp suggestion_date) {
		this.suggestion_date = suggestion_date;
	}


}
