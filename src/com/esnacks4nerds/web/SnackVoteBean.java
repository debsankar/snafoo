package com.esnacks4nerds.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esnacks4nerds.model.entity.SnackVote;
import com.esnacks4nerds.model.entity.Snacks;
import com.esnacks4nerds.model.entity.Suggestion;
import com.esnacks4nerds.model.entity.VotedSnacks;
import com.esnacks4nerds.services.CookieHelper;
import com.esnacks4nerds.services.DateTimeUtils;
import com.esnacks4nerds.services.SnackUtils;
import com.esnacks4nerds.services.SnacksServices;

@Named
@SessionScoped
public class SnackVoteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3437358178530890996L;

	public SnackVoteBean() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	SnacksServices snacksVoteServices;

	private List<Snacks> allSnacks;
	private List<VotedSnacks> allVotedSnacks;
	private boolean canVote;
	private List<Suggestion> suggestions;
	private List<SnackVote> votes;
	private int numberOfVotesRemaining;

	public synchronized List<VotedSnacks> getSnackList() {
		allSnacks = new ArrayList<Snacks>();
		allVotedSnacks = new ArrayList<VotedSnacks>();
		suggestions = snacksVoteServices.getAllSuggestions();
		votes = snacksVoteServices.getVotes();
		System.out.println("NVOTES : " + votes.size());
		try {
			allSnacks.addAll(snacksVoteServices.getAllSnacks());
			for (Snacks snacks : allSnacks) {
				VotedSnacks votedSnacks = new VotedSnacks();
				if (votes != null && !votes.isEmpty()) {
					votedSnacks.setnVotes(SnackUtils.count(votes,
							snacks.getId()));
					System.out.println("No. of votes: "
							+ SnackUtils.count(votes, snacks.getId())
							+ " snackid " + snacks.getId());
				}
				votedSnacks.setSnacks(snacks);
				StringBuffer sb = new StringBuffer();
				sb.append("votedsnack" + snacks.getId());
				CookieHelper cookieHelper = new CookieHelper();
				votedSnacks.setCookieValue(sb.toString());
				if (cookieHelper.cookieExists(votedSnacks.getCookieName(),
						sb.toString())) {
					votedSnacks.setVoted(true);
					sb.delete(0, sb.length());
				}
				System.out.println("Number of cookies by this user: "
						+ cookieHelper
								.getNumberOfVotesBasedOnCookies(votedSnacks
										.getCookieName()));
				if (cookieHelper.getNumberOfVotesBasedOnCookies(votedSnacks
						.getCookieName()) <= 3) {
					canVote = true;
				}
				numberOfVotesRemaining = 3 - cookieHelper
						.getNumberOfVotesBasedOnCookies("vote4snacks");
				if (suggestions != null && !suggestions.isEmpty()
						&& isSuggested(suggestions, snacks)
						&& snacks.isOptional()) {
					allVotedSnacks.add(votedSnacks);
				} else if (!snacks.isOptional()) {
					allVotedSnacks.add(votedSnacks);

				}
			}
 
			if (!canVote) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"You have already voted for 3 snacks. So, you cannot vote anymore until the next month.",
						null);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}

		} catch (Exception ex) {
			if (snacksVoteServices.getServiceMessage().getCode() != 200) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, snacksVoteServices
								.getServiceMessage().getMessage(), null);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			System.out.println(ExceptionUtils.getStackTrace(ex));
			// ex.printStackTrace();
		}
		return allVotedSnacks;
	}

	public void vote(VotedSnacks votedSnacks) {
		CookieHelper cookieHelper = new CookieHelper();
		cookieHelper.setCookie(votedSnacks.getCookieName(),
				votedSnacks.getCookieValue(),
				DateTimeUtils.getCoolieExpiryTime());
		System.out.println(votedSnacks.getCookieValue() + " <-- cookie value ");
		snacksVoteServices.saveVote(votedSnacks.getSnacks().getId());
	}

	public boolean isCanVote() {
		return canVote;
	}

	public void setCanVote(boolean canVote) {
		this.canVote = canVote;
	}

	private boolean isSuggested(List<Suggestion> list1, Snacks snacks) {
		for (Suggestion s : list1) {
			if (s.getSnack_id() == snacks.getId()) {
				System.out.println("snacks suggested: " + snacks.getName());
				return true;
			}
		}
		return false;
	}

	public int getNumberOfVotesRemaining() {
		CookieHelper cookieHelper = new CookieHelper();

		numberOfVotesRemaining = 3 - cookieHelper
				.getNumberOfVotesBasedOnCookies("vote4snacks");

		return numberOfVotesRemaining;
	}

	public void setNumberOfVotesRemaining(int numberOfVotesRemaining) {
		this.numberOfVotesRemaining = numberOfVotesRemaining;
	}

}
