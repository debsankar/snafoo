package com.esnacks4nerds.services;

import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

	public void setCookie(String name, String value, long expiry) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		Cookie cookie = null;
		StringBuffer sb = new StringBuffer();
		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					sb.append(cookie.getValue());
					break;
				}
			}
		}

		if (cookie != null) {
			cookie.setValue(sb.toString() + "," + value);
			sb.delete(0, sb.length());
		} else {
			cookie = new Cookie(name, value);
			cookie.setPath(request.getContextPath());
		}

		cookie.setMaxAge((int)expiry);

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

	public Cookie getCookie(String name) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				System.out.println("Cookies: " + userCookies[i].getName());
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					return cookie;
				}
			}
		}
		return null;
	}


	public boolean cookieExists(String name, String value) {
		if (getCookie(name) != null)
			System.out.println("Inside cookieexists" + getCookie(name) + "  "
					+ getCookie(name).getValue());
		return (getCookie(name) != null && getCookie(name).getValue().contains(
				value));
	}

	public int getNumberOfVotesBasedOnCookies(String name) {
		if (getCookie(name) == null) {
			return 0;
		} else {
			List<String> items = Arrays.asList(getCookie(name).getValue()
					.split(","));
			return items.size();
		}
	}

	public static void main(String[] args) {
		CookieHelper ch = new CookieHelper();
		System.out.println(ch.getNumberOfVotesBasedOnCookies("abc,def,ijk"));
	}
}