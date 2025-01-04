package com.kata.temperature.mgmt.sensor.repository;

import com.kata.temperature.mgmt.sensor.entity.StateLimits;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("stateLimitRepositoryImpl")
public class StateLimitsRepositoryImpl implements StateLimitsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public StateLimits findStateLimitsByTemperatureMeasure(BigDecimal temperatureMeasure){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<StateLimits> criteriaBuilderQuery = criteriaBuilder.createQuery(StateLimits.class);
        Root<StateLimits> root = criteriaBuilderQuery.from(StateLimits.class);

        Predicate temperatureIsWithinStateRange = temperatureIsWithinStateLimitsRange(criteriaBuilder, root, temperatureMeasure);

        criteriaBuilderQuery
                .select(root)
                .where(temperatureIsWithinStateRange);

        return entityManager.createQuery(criteriaBuilderQuery).getSingleResult();
    }

    private Predicate temperatureIsWithinStateLimitsRange(CriteriaBuilder criteriaBuilder, Root<StateLimits> root, BigDecimal temperatureMeasure){
        Path<BigDecimal> minLimitPath = root.get("min");
        Predicate temperatureIsGreaterOrEqualToMinLimit = criteriaBuilder.lessThanOrEqualTo(minLimitPath, temperatureMeasure);

        Path<BigDecimal> maxLimitPath = root.get("max");
        Predicate temperatureIsStrictlyLowerThanMaxLimit = criteriaBuilder.greaterThan(maxLimitPath, temperatureMeasure);

        return criteriaBuilder.and(temperatureIsGreaterOrEqualToMinLimit, temperatureIsStrictlyLowerThanMaxLimit);
    }
}