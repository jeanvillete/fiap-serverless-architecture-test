package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fiap.aws.serverless.arch.trip.domain.Trip.PARTITION_NAME;

public class TripRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public TripRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void save(Trip trip) {
        trip.setUuid(UUID.randomUUID().toString());

        dynamoDBMapper.save(trip);
    }

    public List<Trip> listTripsByPeriod(String startDate, String endDate) {
        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>(){{
            put(":partition", new AttributeValue().withS(PARTITION_NAME));
            put(":startDate", new AttributeValue().withS(startDate));
            put(":endDate", new AttributeValue().withS(endDate));
        }};

        final Map<String, String> expressionAttributeNames = new HashMap<String, String>(){{
            put("#partition", "partition");
            put("#date", "date");
        }};

        final DynamoDBQueryExpression<Trip> queryExpression = new DynamoDBQueryExpression<Trip>()
                .withIndexName("dateLSI")
                .withConsistentRead(false)
                .withKeyConditionExpression("#partition = :partition AND #date BETWEEN :startDate AND :endDate")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withExpressionAttributeNames(expressionAttributeNames);

        return dynamoDBMapper.query(Trip.class, queryExpression);
    }
}
