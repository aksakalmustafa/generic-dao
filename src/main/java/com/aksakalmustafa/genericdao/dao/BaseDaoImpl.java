package com.aksakalmustafa.genericdao.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.aksakalmustafa.genericdao.model.BaseEntity;

public class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	private final Class<T> entityClass;

	@SuppressWarnings("unchecked")
	protected BaseDaoImpl() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public BaseDaoImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T save(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public void update(T entity) {
		entityManager.merge(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		if (entity.getId() != null) {
			T findById = findById(entity.getId());
			if (findById != null) {
				update(entity);
				return;
			}
		}

		save(entity);
	}

	@Override
	public void delete(T persistentInstance) {
		entityManager.remove(persistentInstance);
	}

	@Override
	public void deleteAll() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);
		criteriaDelete.from(entityClass);
		entityManager.createQuery(criteriaDelete).executeUpdate();
	}

	@Override
	public T findById(Serializable id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root);

		return entityManager.createQuery(query).getResultList();
	}

}