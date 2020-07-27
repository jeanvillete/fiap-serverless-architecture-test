package fiap.aws.serverless.arch.trip.domain.usecase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;
import fiap.aws.serverless.arch.trip.domain.Trip;
import fiap.aws.serverless.arch.trip.domain.TripService;

import java.util.List;
import java.util.stream.Collectors;

public class TripUseCase {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TripPayload {

        @JsonProperty
        String uuid;

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

        public TripPayload(String uuid, String country, String city, String date, String reason) {
            this.uuid = uuid;
            this.country = country;
            this.city = city;
            this.date = date;
            this.reason = reason;
        }

        static TripPayload of(Trip trip) {
            return new TripPayload(
                    trip.getUuid(),
                    trip.getCountry(),
                    trip.getCity(),
                    dateFormattedToBePrinted(trip.getDate()),
                    trip.getReason()
            );
        }

        Trip toTrip() {
            return new Trip(
                    country,
                    city,
                    dateFormattedToBeStored(date),
                    reason
            );
        }

        @Override
        public String toString() {
            return "TripPayload{" +
                    "uuid='" + uuid + '\'' +
                    ", country='" + country + '\'' +
                    ", city='" + city + '\'' +
                    ", date='" + date + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }

        static String dateFormattedToBeStored(String date) {
            return date.replace("/", "-");
        }

        static String dateFormattedToBePrinted(String date) {
            return date.replace("-", "/");
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

    public List<TripPayload> listTripsByPeriod(String startDate, String endDate) throws InvalidSuppliedDataException {
        this.tripService.validateQueryStringStartDate(startDate);
        this.tripService.validateQueryStringEndDate(endDate);

        String startDateFormattedAsStored = TripPayload.dateFormattedToBeStored(startDate);
        String endDateFormattedAsStored = TripPayload.dateFormattedToBeStored(endDate);

        return this.tripService.listTripsByPeriod(startDateFormattedAsStored, endDateFormattedAsStored)
                .stream()
                .map(TripPayload::of)
                .collect(Collectors.toList());
    }

    public List<TripPayload> listTripsByCountryAndCityLikely(String country, String city) throws InvalidSuppliedDataException {
        this.tripService.validateCountry(country);

        return this.tripService.listTripsByCountryAndCityLikely(country, city)
                .stream()
                .map(TripPayload::of)
                .collect(Collectors.toList());
    }

}
