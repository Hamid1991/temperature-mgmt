package com.kata.temperature.mgmt.data.repository;

import com.kata.temperature.mgmt.data.entity.TemperatureData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("temperatureDataRepositoryImpl")
public class TemperatureDataRepositoryImpl implements TemperatureDataCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TemperatureData> findAllTemperaturesDataByCaptorId(Long captorId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TemperatureData> criteriaBuilderQuery = criteriaBuilder.createQuery(TemperatureData.class);
        Root<TemperatureData> root = criteriaBuilderQuery.from(TemperatureData.class);
        Path<String> path = root.get("captorId");

        criteriaBuilderQuery
                .select(root)
                .where(criteriaBuilder.equal(path, captorId))
                .orderBy(criteriaBuilder.desc(root.get("timestamp")));

        return entityManager.createQuery(criteriaBuilderQuery).setMaxResults(15).getResultList();
    }
}