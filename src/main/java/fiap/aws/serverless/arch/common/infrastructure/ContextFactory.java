package fiap.aws.serverless.arch.common.infrastructure;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import fiap.aws.serverless.arch.trip.domain.TripRepository;
import fiap.aws.serverless.arch.trip.domain.TripService;
import fiap.aws.serverless.arch.trip.domain.TripServiceImpl;
import fiap.aws.serverless.arch.trip.domain.usecase.TripUseCase;

import java.util.HashMap;
import java.util.Map;

public class ContextFactory {

    private static final ContextFactory context = new ContextFactory();

    private final Map<Class, Object> instances;

    public static ContextFactory getContext() {
        return context;
    }

    public ContextFactory() {
        instances = instances();
    }

    public <T> T getInstance(Class<T> clazz) {
        return (T) instances.get(clazz);
    }

    private Map<Class, Object> instances() {
        return new HashMap<Class, Object>(){{
            DynamoDBMapper dynamoDBMapper = dynamoDBMapper();
            put(
                    DynamoDBMapper.class,
                    dynamoDBMapper
            );

            TripRepository tripRepository = new TripRepository(dynamoDBMapper);
            put(
                    TripRepository.class,
                    tripRepository
            );

            TripService tripService = new TripServiceImpl(tripRepository);
            put(
                    TripService.class,
                    tripService
            );
            put(
                    TripServiceImpl.class,
                    tripService
            );

            TripUseCase tripUseCase = new TripUseCase(tripService);
            put(
                    TripUseCase.class,
                    tripUseCase
            );
        }};
    }

    private DynamoDBMapper dynamoDBMapper() {
        AmazonDynamoDB ddb;
        final String endpoint = System.getenv("ENDPOINT_OVERRIDE");

        if (endpoint != null && !endpoint.isEmpty()) {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1");
            ddb = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
        } else {
            ddb = AmazonDynamoDBClientBuilder.defaultClient();
        }

        return new DynamoDBMapper(ddb);
    }

}
