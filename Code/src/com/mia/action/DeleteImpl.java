package com.mia.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mia.BbsInterface;
import com.mia.MvcProcessor;

public class DeleteImpl implements BbsInterface{

	@Override
	public String handling(HttpServletRequest request, HttpServletResponse response) {
		
		String num = request.getParameter("num");
		MvcProcessor mvcProcessor = MvcProcessor.getInstance();
		mvcProcessor.deleteArticle(Integer.parseInt(num));
		
		return "list.jsp";
	}
}
