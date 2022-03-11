package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.repository;

import ca.nait.dmit.dmit2015.jordandelcolle.assignment04.entity.EdmontonPropertyAssessment;
import common.jpa.AbstractJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */

@ApplicationScoped
@Transactional
public class EdmontonPropertyAssessmentRepository extends AbstractJpaRepository<EdmontonPropertyAssessment, String> {

    public EdmontonPropertyAssessmentRepository() {
        super(EdmontonPropertyAssessment.class);
    }

    public List<EdmontonPropertyAssessment> findWithinDistance(
            double longitude,
            double latitude,
            double distanceMeters
    ) {
        List<EdmontonPropertyAssessment> propertyList = new ArrayList<>();

        var distanceKm = distanceMeters / 1000;
        final double EARTH_MEAN_RADIUS_KM = 6371.0087714;
        final double DEGREES_TO_RADIANS =  Math.PI / 180;
        final double RADIANS_TO_DEGREES =  1 / DEGREES_TO_RADIANS;
        var distanceCentralAngleDegrees = distanceKm * RADIANS_TO_DEGREES / EARTH_MEAN_RADIUS_KM;

        final String jpql = """
                        SELECT p
                        FROM EdmontonPropertyAssessment p
                        WHERE distance(p.pointLocation, :pointValue) <= :distanceCentralAngleDegreesValue
                        """;
        TypedQuery<EdmontonPropertyAssessment> query = getEntityManager().createQuery(jpql, EdmontonPropertyAssessment.class);

        Point geoLocation = new GeometryFactory()
                .createPoint(
                        new Coordinate( longitude, latitude  )
                );
        query.setParameter("pointValue", geoLocation);
        query.setParameter("distanceCentralAngleDegreesValue", distanceCentralAngleDegrees);
        propertyList = query
                .setMaxResults(25)
                .getResultList();

        return propertyList;
    }

    public Optional<EdmontonPropertyAssessment> findByHouseNumberAndStreetName(
            String houseNumber,
            String streetName
    ) {
        Optional<EdmontonPropertyAssessment> optionalSingleResult = Optional.empty();

        List<EdmontonPropertyAssessment> queryResultList = getEntityManager()
                .createQuery("""
                        SELECT p
                        FROM EdmontonPropertyAssessment p
                        WHERE p.houseNumber = :houseNumberValue AND p.streetName = :streetNameValue
                        """)
                .setParameter("houseNumberValue", houseNumber)
                .setParameter("streetNameValue", streetName)
                .getResultList();
        if (queryResultList.size() > 0) {
            optionalSingleResult = Optional.of(queryResultList.get(0));
        }

        return optionalSingleResult;
    }



    public List<EdmontonPropertyAssessment> findbyNeighbourhood(
            String neighbourhood
    ) {
        List<EdmontonPropertyAssessment> propertyList = new ArrayList<>();

        final String jpql = """
SELECT p
FROM EdmontonPropertyAssessment p
WHERE p.neighbourhood = :myNeighbourhood
ORDER BY p.streetName, p.houseNumber
""";
        TypedQuery<EdmontonPropertyAssessment> query = getEntityManager().createQuery(jpql, EdmontonPropertyAssessment.class);

        query.setParameter("myNeighbourhood", neighbourhood);
        propertyList = query
                .setMaxResults(999)
                .getResultList();

        return propertyList;
    }


    public List<EdmontonPropertyAssessment> findbyNeighbourhoodAndAssessedValueRange(
            String neighbourhood,
            Long minValue,
            Long maxValue
    ) {
        List<EdmontonPropertyAssessment> propertyList = new ArrayList<>();

        final String jpql = """
SELECT p
FROM EdmontonPropertyAssessment p
WHERE p.neighbourhood = :myNeighbourhood
AND p.assessedValue >= :myMin
AND p.assessedValue <= :myMax
ORDER BY p.streetName, p.houseNumber
""";
        TypedQuery<EdmontonPropertyAssessment> query = getEntityManager().createQuery(jpql, EdmontonPropertyAssessment.class);

        query.setParameter("myNeighbourhood", neighbourhood);
        query.setParameter("myMin", minValue);
        query.setParameter("myMax", maxValue);
        propertyList = query
                .setMaxResults(99)
                .getResultList();
        return propertyList;
    }

}