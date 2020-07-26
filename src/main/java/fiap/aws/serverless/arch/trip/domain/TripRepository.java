package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class TripRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public TripRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void save(Trip trip) {
        dynamoDBMapper.save(trip);
    }

}
