package com.pennant.prodmtr.Dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.pennant.prodmtr.Dao.Interface.UserDao;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void UpdatePassword(Integer id, String password) {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
		query.setParameter("id", id);

		User user = query.getSingleResult();
		if (user != null) {
			user.setUserPassword(password);
		}
	}

	@Override
	public List<Task> getUserActivities(String taskStatus) {
		TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t WHERE t.taskStatus = :taskStatus",
				Task.class);
		query.setParameter("taskStatus", taskStatus);

		List<Task> tasks = query.getResultList();
		return tasks;
	}

	@Override
	public List<Subtask> getUserSubtaskActivities(String sbts_status) {
		System.out.println("here " + sbts_status);
		TypedQuery<Subtask> query = entityManager
				.createQuery("SELECT s FROM Subtask s WHERE s.sbts_status = :sbts_status", Subtask.class);

		query.setParameter("sbts_status", sbts_status);

		List<Subtask> subtasks = query.getResultList();

		return subtasks;
	}

	@Override
	public User verifyUser(String userDisplayName) {
		TypedQuery<User> query = null;

		try {
			query = entityManager.createQuery("SELECT u FROM User u WHERE u.userDisplayName = :userDisplayName",
					User.class);
			query.setParameter("userDisplayName", userDisplayName);
			query.setMaxResults(1);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getuserbyemailid(String email) {
		TypedQuery<User> query = entityManager.createQuery("select m from User m where m.email =:mail", User.class);
		query.setParameter("mail", email);
		return query.getResultList();
	}

	@Override
	public void UpdatePassword(String password, String finalemail) {
		String updateQuery = "MERGE INTO User e USING (SELECT :email AS email, :password AS password FROM DUAL) source " +
		        "ON (e.email = source.email) " +
		        "WHEN MATCHED THEN UPDATE SET e.userPassword = source.password " +
		        "WHEN NOT MATCHED THEN INSERT (e.email, e.userPassword) VALUES (source.email, source.password)";

		Query query = entityManager.createNativeQuery(updateQuery);
		query.setParameter("email", finalemail);
		query.setParameter("password", password);

		int updatedCount = query.executeUpdate();
		if (updatedCount > 0) {
		    System.out.println("Table updated successfully.");
		} else {
		    System.out.println("No rows were updated.");
		}

}