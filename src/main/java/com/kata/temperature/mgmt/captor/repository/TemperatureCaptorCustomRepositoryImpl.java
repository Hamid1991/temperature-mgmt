package com.kata.temperature.mgmt.captor.repository;

import com.kata.temperature.mgmt.captor.entity.TemperatureCaptor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

public class TemperatureCaptorCustomRepositoryImpl implements TemperatureCaptorCustomRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public TemperatureCaptor findTemperatureCaptorBySensorId(Long sensorId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TemperatureCaptor> criteriaBuilderQuery = criteriaBuilder.createQuery(TemperatureCaptor.class);
        Root<TemperatureCaptor> root = criteriaBuilderQuery.from(TemperatureCaptor.class);
        Path<Long> captorHavingSensorId = root.get("sensorId");

        criteriaBuilderQuery
                .select(root)
                .where(criteriaBuilder.equal(captorHavingSensorId, sensorId));

        return entityManager.createQuery(criteriaBuilderQuery).getSingleResult();
    }
}