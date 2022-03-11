package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.batch;

import ca.nait.dmit.dmit2015.jordandelcolle.assignment04.entity.EdmontonPropertyAssessment;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.inject.Named;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */

@Named
public class EdmontonPropertyAssessmentItemProcessor implements ItemProcessor {
    @Override
    public Object processItem(Object item) throws Exception {
        String line = (String) item;
        final String delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] tokens = line.split(delimiter, -1);

        EdmontonPropertyAssessment currentEdmontonPropertyAssessment = new EdmontonPropertyAssessment();
        currentEdmontonPropertyAssessment.setAccountNumber(tokens[0]);
        currentEdmontonPropertyAssessment.setSuite(tokens[1]);
        currentEdmontonPropertyAssessment.setHouseNumber(tokens[2]);
        currentEdmontonPropertyAssessment.setStreetName(tokens[3]);
        currentEdmontonPropertyAssessment.setGarage(Boolean.parseBoolean(tokens[4]));
        if (tokens[5].length() == 0)
        {
            currentEdmontonPropertyAssessment.setNeighbourhoodId(null);
        } else
        {
            currentEdmontonPropertyAssessment.setNeighbourhoodId(Integer.parseInt(tokens[5]));
        }

        currentEdmontonPropertyAssessment.setNeighbourhood(tokens[6]);
        currentEdmontonPropertyAssessment.setWard(tokens[7]);
        currentEdmontonPropertyAssessment.setAssessedValue(Long.parseLong(tokens[8]));
        currentEdmontonPropertyAssessment.setLatitude(Double.valueOf(tokens[9]));
        currentEdmontonPropertyAssessment.setLongitude(Double.valueOf(tokens[10]));
        currentEdmontonPropertyAssessment.setAssessmentClass1(tokens[12]);

        Point pointLocation = new GeometryFactory().createPoint(
                new Coordinate(
                        currentEdmontonPropertyAssessment.getLongitude(), currentEdmontonPropertyAssessment.getLatitude()
                )
        );
        currentEdmontonPropertyAssessment.setPointLocation(pointLocation);
        return currentEdmontonPropertyAssessment;
    }
}
