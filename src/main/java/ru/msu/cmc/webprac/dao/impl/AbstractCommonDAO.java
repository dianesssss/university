package ru.msu.cmc.webprac.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.msu.cmc.webprac.dao.CommonDAO;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Transactional
public abstract class AbstractCommonDAO<T, ID> implements CommonDAO<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractCommonDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public List<T> getAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    @Override
    public T getById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(ID id) {
        T entity = getById(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
}