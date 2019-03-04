package com.aksakalmustafa.genericdao.dao;

import java.io.Serializable;
import java.util.List;

import com.aksakalmustafa.genericdao.model.BaseEntity;

public interface BaseDao<T extends BaseEntity> {

	T save(T entity);

	void update(T entity);

	void saveOrUpdate(T entity);

	void delete(T persistentInstance);

	void deleteAll();

	T findById(Serializable id);

	List<T> findAll();

}