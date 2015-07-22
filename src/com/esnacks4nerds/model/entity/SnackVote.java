package com.esnacks4nerds.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: SnackVote
 *
 */
@Entity
@Table(name = "VOTE")
@NamedQuery(name = "findAllVotes", query = "SELECT v FROM SnackVote v ")
public class SnackVote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5190827484027379819L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;
	private Integer snack_id;
	private Integer num_votes;
	private Timestamp date_voted;

	public SnackVote() {
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

	public Integer getNum_votes() {
		return this.num_votes;
	}

	public void setNum_votes(Integer num_votes) {
		this.num_votes = num_votes;
	}

	public Timestamp getDate_voted() {
		return date_voted;
	}

	public void setDate_voted(Timestamp date_voted) {
		this.date_voted = date_voted;
	}

}
