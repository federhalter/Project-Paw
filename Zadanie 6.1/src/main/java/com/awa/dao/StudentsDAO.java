package com.awa.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.awa.entities.Students;

//DAO - Data Access Object for Students entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.

@Stateless
public class StudentsDAO {
    
        
	private final static String UNIT_NAME = "Projectbd_PU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Students students) {
		em.persist(students);
	}

	public Students merge(Students students) {
		return em.merge(students);
	}

	public void remove(Students students) {
		em.remove(em.merge(students));
	}

	public Students find(Object id) {
		return em.find(Students.class, id);
	}

	public List<Students> getFullList() {
		List<Students> list = null;

		Query query = em.createQuery("select s from Students s");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Students> getList(Map<String, Object> searchParams) {
		List<Students> list = null;

		// 1. Build query string with parameters
		String select = "select s ";
		String from = "from Students s ";
		String where = "";
		String orderby = "order by s.lastname asc, s.firstname";

		// search for LastName
		String lastname = (String) searchParams.get("lastname");
		if (lastname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "s.lastname like :lastname ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (lastname != null) {
			query.setParameter("lastname", lastname+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Students objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
