package com.awa.Students;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.servlet.http.HttpSession;

import com.awa.dao.StudentsDAO;
import com.awa.entities.Students;

@Named
@RequestScoped
public class StudentsListBB {
	private static final String PAGE_STUDENTS_EDIT = "studentsEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String lastname;
		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	Flash flash;
	
	@EJB
	StudentsDAO studentsDAO;
		
      
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String LastName) {
		this.lastname = LastName;
	}

	public List<Students> getFullList(){
		return studentsDAO.getFullList();
	}

	public List<Students> getList(){
		List<Students> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (lastname != null && lastname.length() > 0){
			searchParams.put("lastname", lastname);
		}
		
		//2. Get list
		list = studentsDAO.getList(searchParams);
		
		return list;
	}

	public String newStudent(){
		Students student = new Students();
		
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("Students", Students);
		
		//2. Pass object through flash	
		flash.put("students", student);
		
		return PAGE_STUDENTS_EDIT;
	}

	public String editStudent(Students students){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("Students", Students);
		
		//2. Pass object through flash 
		flash.put("students", students);
		
		return PAGE_STUDENTS_EDIT;
	}

	public String deleteStudent(Students student){
		studentsDAO.remove(student);
		return PAGE_STAY_AT_THE_SAME;
	}
}
