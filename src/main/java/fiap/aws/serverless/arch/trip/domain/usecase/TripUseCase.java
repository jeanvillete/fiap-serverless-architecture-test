package fiap.aws.serverless.arch.trip.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;
import fiap.aws.serverless.arch.trip.domain.Trip;
import fiap.aws.serverless.arch.trip.domain.TripService;

public class TripUseCase {

    public static class TripPayload {
        @JsonProperty
        String country;

        @JsonProperty
        String city;

        @JsonProperty
        String date;

        @JsonProperty
        String reason;

        public TripPayload() {
        }

        public TripPayload(String country, String city, String date, String reason) {
            this.country = country;
            this.city = city;
            this.date = date;
            this.reason = reason;
        }

        static TripPayload of(Trip trip) {
            return new TripPayload(
                    trip.getCountry(),
                    trip.getCity(),
                    trip.getDate(),
                    trip.getReason()
            );
        }

        Trip toTrip() {
            return new Trip(
                    country,
                    city,
                    date,
                    reason
            );
        }

        @Override
        public String toString() {
            return "TripPayload{" +
                    "country='" + country + '\'' +
                    ", city='" + city + '\'' +
                    ", date='" + date + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

    private final TripService tripService;

    public TripUseCase(TripService tripService) {
        this.tripService = tripService;
    }

    public TripPayload createATripRecord(TripPayload tripPayload) throws InvalidSuppliedDataException {
        this.tripService.validateCountry(tripPayload.country);
        this.tripService.validateCity(tripPayload.city);
        this.tripService.validateDate(tripPayload.date);
        this.tripService.validateReason(tripPayload.reason);

        Trip persistedTrip = this.tripService.save(tripPayload.toTrip());

        return TripPayload.of(persistedTrip);
    }

}
