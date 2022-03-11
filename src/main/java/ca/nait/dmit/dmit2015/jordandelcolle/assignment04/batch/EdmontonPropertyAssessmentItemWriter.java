package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.batch;

import ca.nait.dmit.dmit2015.jordandelcolle.assignment04.entity.EdmontonPropertyAssessment;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

/**
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */

@Named
public class EdmontonPropertyAssessmentItemWriter extends AbstractItemWriter {
    @PersistenceContext(unitName = "mssql-jpa-pu")
    private EntityManager _entityManager;

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object currentItem : items)
        {
            EdmontonPropertyAssessment entity = (EdmontonPropertyAssessment) currentItem;
            _entityManager.persist(entity);
        }

    }
}
