package com.esnacks4nerds.model.entity;


public class VotedSnacks {
	private Snacks snacks;
	private boolean isVoted;
	private String cookieValue;
	private String cookieName = "vote4snacks";
	private int nVotes;
	
	public boolean isVoted() {
		return isVoted;
	}

	public void setVoted(boolean isVoted) {
		this.isVoted = isVoted;
	}

	public Snacks getSnacks() {
		return snacks;
	}

	public void setSnacks(Snacks snacks) {
		this.snacks = snacks;
	}

	public String getCookieValue() {
		return cookieValue;
	}

	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public int getnVotes() {
		return nVotes;
	}

	public void setnVotes(int nVotes) {
		this.nVotes = nVotes;
	}

	
}
