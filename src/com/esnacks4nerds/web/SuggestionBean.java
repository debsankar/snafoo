package com.esnacks4nerds.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.primefaces.context.RequestContext;

import com.esnacks4nerds.model.entity.PostedSnacksResponse;
import com.esnacks4nerds.model.entity.SnackSuggestion;
import com.esnacks4nerds.model.entity.Snacks;
import com.esnacks4nerds.services.CookieHelper;
import com.esnacks4nerds.services.DateTimeUtils;
import com.esnacks4nerds.services.SnackUtils;
import com.esnacks4nerds.services.SnacksServices;

@Named
@SessionScoped
public class SuggestionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -548309232499984950L;

	@EJB
	SnacksServices svc;
	private boolean canSuggest;
	private boolean doNotProceed;
	List<Snacks> allSnacks;

	public SuggestionBean() {
		snackSuggestion = new SnackSuggestion();
	}

	private SnackSuggestion snackSuggestion;

	private String suggestedSnack;

	public SnackSuggestion getSnackSuggestion() {
		return snackSuggestion;
	}

	public void setSnackSuggestion(SnackSuggestion snackSuggestion) {
		this.snackSuggestion = snackSuggestion;
	}

	public List<Snacks> getOptionalSnacks() {
		allSnacks = new ArrayList<Snacks>();
		try {
			allSnacks = svc.getAllSnacks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Snacks> optionalSnacks = new ArrayList<Snacks>();
		for (Snacks s : allSnacks) {
			if (s.isOptional()) {
				System.out.println(s.getName() + "  " + s.getId());
				optionalSnacks.add(s);
			}
		}
		return optionalSnacks;
	}

	public void suggest() {
		System.out.println("Suggestion is being submitted for : "
				+ snackSuggestion.getName());
		try {
			if (SnackUtils.contains(snackSuggestion.getName(), allSnacks)) {
				doNotProceed = true;
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"This snack has already been suggested for this month.", null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
			if (!doNotProceed) {
				PostedSnacksResponse snackResponse = svc
						.postSnackSuggestion(snackSuggestion);
				System.out.println("Suggestion submitted successfully");
				if (svc.getServiceMessage().getCode() == 200) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							svc.getServiceMessage().getMessage(), null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					saveSuggestion(snackResponse.getId());
					RequestContext.getCurrentInstance().showMessageInDialog(message);


				} else {
					FacesMessage message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, svc.getServiceMessage()
									.getMessage(), null);
					FacesContext.getCurrentInstance().addMessage(null, message);
					RequestContext.getCurrentInstance().showMessageInDialog(message);

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(ExceptionUtils.getStackTrace(e));
		}
	}

	public String getSuggestedSnack() {
		return suggestedSnack;
	}

	public void setSuggestedSnack(String suggestedSnack) {
		this.suggestedSnack = suggestedSnack;
	}

	public void saveSuggestion() {
		System.out.println("Snackid: " + Integer.parseInt(suggestedSnack));
		if (!svc.saveSuggestion(Integer.parseInt(suggestedSnack))) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Snack suggestion not successful."
							, null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().showMessageInDialog(message);

		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Snack suggestion is successful.", null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().showMessageInDialog(message);


			cookieMatters();
		}
	}

	public void saveSuggestion(int snackid) {
		System.out.println("Snackid: " + snackid);
		if (!svc.saveSuggestion(snackid)) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, svc.getServiceMessage()
							.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().showMessageInDialog(message);

		} else {
			cookieMatters();
		}
	}

	private void cookieMatters() {
		CookieHelper cookieHelper = new CookieHelper();
		cookieHelper.setCookie("suggestioncookie", "suggestioncookie",
				DateTimeUtils.getCoolieExpiryTime());

	}

	public boolean isCanSuggest() {
		System.out.println(" canSuggest ");
		CookieHelper cookieHelper = new CookieHelper();

		if (cookieHelper.getCookie("suggestioncookie") != null) {
			canSuggest = true;
		} else {
			canSuggest = false;
		}
		System.out.println(canSuggest);
		return canSuggest;
	}

	public void setCanSuggest(boolean canSuggest) {
		this.canSuggest = canSuggest;
	}

	public void stateChange(AjaxBehaviorEvent event) {
		System.out.println("we are here"
				+ event.getComponent().getAttributes().get("value").toString());
		setSuggestedSnack(event.getComponent().getAttributes().get("value")
				.toString());
	}
}
