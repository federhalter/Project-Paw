package com.awa.Students;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import com.awa.dao.StudentsDAO;
import com.awa.entities.Students;

@Named
@ViewScoped
public class StudentsEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STUDENTS_LIST = "studentsList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Students student = new Students();
	private Students loaded = null;

	@EJB
	StudentsDAO studentsDAO;

	@Inject
	FacesContext context;

	@Inject
	Flash flash;

	public Students getStudent() {
		return student;
	}

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");

		// 2. load person passed through flash
		loaded = (Students) flash.get("students");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			student = loaded;
			// session.removeAttribute("person");
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}

	}

	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (student.getStudentid() == null) {
				// new record
				studentsDAO.create(student);
			} else {
				// existing record
				studentsDAO.merge(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_STUDENTS_LIST;
	}
}
