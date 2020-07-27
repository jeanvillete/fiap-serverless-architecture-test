package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import fiap.aws.serverless.arch.common.domain.RequestMapping;
import fiap.aws.serverless.arch.common.domain.ResponseMapping;
import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;
import fiap.aws.serverless.arch.common.infrastructure.ContextFactory;
import fiap.aws.serverless.arch.common.infrastructure.Controller;
import fiap.aws.serverless.arch.trip.domain.usecase.TripUseCase;
import fiap.aws.serverless.arch.trip.domain.usecase.TripUseCase.TripPayload;

import java.util.List;
import java.util.Map;

public abstract class TripController extends Controller {

    protected final TripUseCase tripUseCase;

    public TripController(TripUseCase tripUseCase) {
        this.tripUseCase = tripUseCase;
    }

    public static class CreateATripRecordFunction extends TripController implements RequestHandler<RequestMapping, ResponseMapping> {

        public CreateATripRecordFunction() {
            super(
                    ContextFactory.getContext()
                            .getInstance(TripUseCase.class)
            );
        }

        @Override
        public ResponseMapping handleRequest(RequestMapping requestMapping, Context context) {
            LambdaLogger LOGGER = context.getLogger();

            try {
                LOGGER.log(context.getAwsRequestId() + "; Request for inserting a new trip instance.");

                TripPayload tripRequestPayload = parseBody(requestMapping, TripPayload.class);
                LOGGER.log(context.getAwsRequestId() + "; Parsed Trip instance; " + tripRequestPayload);

                TripPayload tripResponsePayload = tripUseCase.createATripRecord(tripRequestPayload);

                LOGGER.log(context.getAwsRequestId() + "; Trip saved successfully.");

                return ResponseMapping.builder()
                        .setStatusCode(201)
                        .setObjectBody(tripResponsePayload)
                        .build();
            } catch (ImpossibleParseJsonException e) {
                LOGGER.log(context.getAwsRequestId() + "; Exception raised while unmarshalling Trip json content.");

                return ResponseMapping.builder()
                        .setStatusCode(400)
                        .setRawBody("It was not possible to parse request json body, maybe the content is malformed.")
                        .build();
            } catch (InvalidSuppliedDataException e) {
                LOGGER.log(context.getAwsRequestId() + "; " + e.getMessage());

                return ResponseMapping.builder()
                        .setStatusCode(400)
                        .setRawBody(e.getMessage())
                        .build();
            }
        }

    }

    public static class GetTripRecordsByPeriodFunction extends TripController implements RequestHandler<RequestMapping, ResponseMapping> {

        public GetTripRecordsByPeriodFunction() {
            super(
                    ContextFactory.getContext()
                            .getInstance(TripUseCase.class)
            );
        }

        @Override
        public ResponseMapping handleRequest(RequestMapping requestMapping, Context context) {
            LambdaLogger LOGGER = context.getLogger();

            try {
                LOGGER.log(context.getAwsRequestId() + "; Request for listing trips by period.");

                Map<String, String> queryStrings = requestMapping.getQueryStringParameters();
                if (queryStrings == null || queryStrings.isEmpty()) {
                    throw new InvalidSuppliedDataException(
                            "Query strings start date and end date are mandatory to be provided."
                    );
                }

                final String startDate = queryStrings.get("start");
                final String endDate = queryStrings.get("end");

                LOGGER.log(
                        context.getAwsRequestId() + "; Parameter for listing, start date [" + startDate + "] and" +
                                " end date [" + endDate + "]"
                );

                List<TripPayload> tripsPayloadResponse = tripUseCase.listTripsByPeriod(startDate, endDate);

                return ResponseMapping.builder()
                        .setStatusCode(200)
                        .setObjectBody(tripsPayloadResponse)
                        .build();
            } catch (InvalidSuppliedDataException e) {
                LOGGER.log(context.getAwsRequestId() + "; " + e.getMessage());

                return ResponseMapping.builder()
                        .setStatusCode(400)
                        .setRawBody(e.getMessage())
                        .build();
            }
        }

    }

    public static class GetTripRecordsByCountryAndCityLikelyFunction extends TripController implements RequestHandler<RequestMapping, ResponseMapping> {

        public GetTripRecordsByCountryAndCityLikelyFunction() {
            super(
                    ContextFactory.getContext()
                            .getInstance(TripUseCase.class)
            );
        }

        @Override
        public ResponseMapping handleRequest(RequestMapping requestMapping, Context context) {
            LambdaLogger LOGGER = context.getLogger();

            try {
                LOGGER.log(context.getAwsRequestId() + "; Request for listing trips by country and city likely.");

                Map<String, String> pathVariables = requestMapping.getPathParameters();
                if (pathVariables == null || pathVariables.isEmpty()) {
                    throw new InvalidSuppliedDataException("Path variable country is mandatory.");
                }
                final String country = pathVariables.get("country");

                Map<String, String> queryStrings = requestMapping.getQueryStringParameters();
                String cityLikely = null;
                if (queryStrings != null && !queryStrings.isEmpty()) {
                    cityLikely = queryStrings.get("city");
                }

                LOGGER.log(
                        context.getAwsRequestId() + "; Path variable country value [" + country + "] and" +
                                " query string city [" + cityLikely + "]"
                );

                List<TripPayload> tripsPayloadResponse = tripUseCase.listTripsByCountryAndCityLikely(country, cityLikely);

                return ResponseMapping.builder()
                        .setStatusCode(200)
                        .setObjectBody(tripsPayloadResponse)
                        .build();
            } catch (InvalidSuppliedDataException e) {
                LOGGER.log(context.getAwsRequestId() + "; " + e.getMessage());

                return ResponseMapping.builder()
                        .setStatusCode(400)
                        .setRawBody(e.getMessage())
                        .build();
            }
        }

    }
}
