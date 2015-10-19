package com.prj.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

public class RequestHelper {
	public static ArrayList<SimpleExpression> parseParameters(
			HttpServletRequest request, String[] parameters) {
		ArrayList<SimpleExpression> list = new ArrayList<SimpleExpression>();
		// To parse the parameter and generate to be simeplexpression
		for (String key : parameters) {
			if (request.getParameterMap().containsKey(key)
					&& !request.getParameter(key).isEmpty()) {
				list.add(Restrictions.eq(key,
						Integer.parseInt(request.getParameter(key).toString())));
			}
		}
		return list;
	}

}
