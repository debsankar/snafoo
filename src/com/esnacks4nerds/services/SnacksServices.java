package com.esnacks4nerds.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.esnacks4nerds.dao.SnackVoteDAO;
import com.esnacks4nerds.dao.SuggestionDAO;
import com.esnacks4nerds.model.entity.PostedSnacksResponse;
import com.esnacks4nerds.model.entity.ServiceMessage;
import com.esnacks4nerds.model.entity.SnackSuggestion;
import com.esnacks4nerds.model.entity.SnackVote;
import com.esnacks4nerds.model.entity.Snacks;
import com.esnacks4nerds.model.entity.Suggestion;

@Named
@Stateless
public class SnacksServices {

	@EJB
	SnackVoteDAO dao;
	@EJB
	SuggestionDAO suggestionDAO;
	private String responseMessage;
	private ServiceMessage serviceMessage = new ServiceMessage();


	public List<Snacks> getAllSnacks() throws ClientProtocolException,
			IOException, Exception {
		List<Snacks> snackList = new ArrayList<Snacks>();
		ClientRequest request = new ClientRequest(
				"https://api-snacks.nerderylabs.com/v1/snacks/?ApiKey=746b7298-7608-4dc3-aebc-143c7bd3de4e");
		request.accept("application/json");
		ClientResponse<String> response = request.get(String.class);
		if (response.getStatus() == 401) {
			System.out.println("Unauthorized request");
			responseMessage = "Unauthorized GET request.";
			serviceMessage.setCode(401);
			serviceMessage.setMessage(responseMessage);

		} else if (response.getStatus() == 404) {
			responseMessage = "Webservice is unavailable. Please comeback later.";
			serviceMessage.setCode(404);
			serviceMessage.setMessage(responseMessage);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(response.getEntity().getBytes())));

		String output; // System.out.println("Output from Server .... \n");
		System.out.println("Output from Server ....");
		while ((output = br.readLine()) != null) {
			snackList
					.addAll(parseJsonToObject(output, List.class, Snacks.class));
			System.out.println(output);

		}
		br.close();
		return snackList;
	}

	public void saveVote(int snackid) {
		SnackVote snackVote = new SnackVote();
		snackVote.setSnack_id(snackid);
		snackVote.setNum_votes(1);
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(
				now.getTime());

		snackVote.setDate_voted(currentTimestamp);
		dao.persist(snackVote);
	}

	public PostedSnacksResponse postSnackSuggestion(
			SnackSuggestion snackSuggestion) throws Exception {
		ClientRequest request = new ClientRequest(
				"https://api-snacks.nerderylabs.com/v1/snacks/?ApiKey=746b7298-7608-4dc3-aebc-143c7bd3de4e");
		request.accept("application/json");

		ObjectMapper mapper = new ObjectMapper();
		String input = mapper.writeValueAsString(snackSuggestion);

		request.body("application/json", input);

		ClientResponse<String> response = request.post(String.class);

		if (response.getStatus() == 401) {
			responseMessage = "Unauthorized request.";
			serviceMessage.setCode(401);
			serviceMessage.setMessage(responseMessage);

		} else if (response.getStatus() == 400) {
			responseMessage = "Bad request.";
			serviceMessage.setCode(400);
			serviceMessage.setMessage(responseMessage);

		} else if (response.getStatus() == 409) {
			responseMessage = "This snack request has already been submitted.";
			serviceMessage.setCode(409);
			serviceMessage.setMessage(responseMessage);

		} else if (response.getStatus() == 200) {
			responseMessage = "Snack request submitted successfully.";
			serviceMessage.setCode(200);
			serviceMessage.setMessage(responseMessage);
		} else if (response.getStatus() == 404) {
			responseMessage = "Webservice is unavailable. Please comeback later.";
			serviceMessage.setCode(404);
			serviceMessage.setMessage(responseMessage);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(response.getEntity().getBytes())));

		String output;
		PostedSnacksResponse snack = null;
		System.out.println("Output from Server (response) .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			mapper = new ObjectMapper();
			snack = mapper.readValue(output, PostedSnacksResponse.class);
		}
		br.close();
		return snack;

	}

	public List<SnackVote> getVotes() {
		return dao.findAll();
	}

	@SuppressWarnings("rawtypes")
	public static <E, T extends Collection> T parseJsonToObject(String jsonStr,
			Class<T> collectionType, Class<E> elementType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// return mapper.readValue(jsonStr,
			// TypeFactory.collectionType(collectionType, elementType));
			return mapper.readValue(jsonStr, mapper.getTypeFactory()
					.constructCollectionType(collectionType, elementType));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Snacks> getShoppingList(List<Snacks> allSnacks,
			List<SnackVote> votes) {
		List<Snacks> shoppingList = new ArrayList<Snacks>();
		List<Snacks> sortedSnacks = SnackUtils
				.sortedSnacksOrderByNotOptional(allSnacks);
		System.out.println(sortedSnacks.size() + " this counts1");

		if ((votes == null || votes.isEmpty()) && allSnacks != null) {
			shoppingList = sortedSnacks.subList(0, 10);
		}

		else if (votes != null && !votes.isEmpty() && allSnacks != null
				&& !allSnacks.isEmpty()) {
			for (Snacks s : sortedSnacks) {
				if (!s.isOptional()) {
					shoppingList.add(s);
				}
			}
			int count = 10 - shoppingList.size();
			System.out.println(count + " this counts");
			if (count < 10) {
				List<Snacks> sortedListByVotes = SnackUtils.sortByCounts(
						allSnacks, votes).subList(0, count);

				System.out.println(sortedListByVotes.size());
				shoppingList.addAll(sortedListByVotes);
			}

		}
		System.out.println("shopping list: " + shoppingList.size());
		return shoppingList;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public ServiceMessage getServiceMessage() {
		return serviceMessage;
	}

	public void setServiceMessage(ServiceMessage serviceMessage) {
		this.serviceMessage = serviceMessage;
	}

	public List<Suggestion> getAllSuggestions() {
		return suggestionDAO.findAll();
	}

	public boolean saveSuggestion(int snack_id) {
		boolean saved = false;
		System.out.println("snackid:: " + snack_id);
		Suggestion suggestion = new Suggestion();
		suggestion.setSnack_id(snack_id);
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(
				now.getTime());
		suggestion.setSuggestion_date(currentTimestamp);
		boolean canPersist = true;
		for (Suggestion s : getAllSuggestions()) {
			if (DateTimeUtils.isValidDate(s.getSuggestion_date())
					&& s.getSnack_id() == snack_id) {
				canPersist = false;
				break;
			}
		}
		if (canPersist) {
			suggestionDAO.persist(suggestion);
			saved = true;
		} else {
			serviceMessage
					.setMessage("Snack already been suggested for this month");
			saved = false;
		}
		return saved;
	}
}
