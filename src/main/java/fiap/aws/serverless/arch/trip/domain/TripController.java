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

public abstract class TripController extends Controller {

    private final TripUseCase tripUseCase;

    public TripController(TripUseCase tripUseCase) {
        this.tripUseCase = tripUseCase;
    }

    public static class CreateATripRecordFunction  extends TripController implements RequestHandler<RequestMapping, ResponseMapping> {

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

                TripPayload tripResponsePayload = getTripUseCase().createATripRecord(tripRequestPayload);

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

    protected TripUseCase getTripUseCase() {
        return tripUseCase;
    }
}