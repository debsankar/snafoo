package com.esnacks4nerds.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;

import com.esnacks4nerds.model.entity.SnackVote;
import com.esnacks4nerds.model.entity.Snacks;
import com.esnacks4nerds.services.SnacksServices;

@Named
@SessionScoped
public class ShoppingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8436049989332366833L;

	public ShoppingBean() {
		// TODO Auto-generated constructor stub
	}

	@EJB
	SnacksServices snacksVoteServices;

	public List<Snacks> getSnacksShoppingList() {
		List<SnackVote> votes = snacksVoteServices.getVotes();

		List<Snacks> allSnacks = null;
		try {
			allSnacks = snacksVoteServices.getAllSnacks();
		} catch (ClientProtocolException e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
		} catch (IOException ioe) {
			System.out.println(ExceptionUtils.getStackTrace(ioe));
		} catch (Exception ex) {
			System.out.println(ExceptionUtils.getStackTrace(ex));
		}

		System.out.println("Number of snacks: " + allSnacks.size());
		return snacksVoteServices.getShoppingList(allSnacks, votes);

	}
}
