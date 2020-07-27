package fiap.aws.serverless.arch.trip.domain;

import fiap.aws.serverless.arch.common.domain.exception.InvalidSuppliedDataException;

import java.util.List;

public interface TripService {

    void validateCountry(String country) throws InvalidSuppliedDataException;

    void validateCity(String city) throws InvalidSuppliedDataException;

    void validateDate(String date) throws InvalidSuppliedDataException;

    void validateReason(String reason) throws InvalidSuppliedDataException;

    Trip save(Trip trip);

    void validateQueryStringStartDate(String startDate) throws InvalidSuppliedDataException;

    void validateQueryStringEndDate(String endDate) throws InvalidSuppliedDataException;

    List<Trip> listTripsByPeriod(String startDate, String endDate);

    List<Trip> listTripsByCountry(String country);

    List<Trip> listTripsByCountryAndCityLikely(String country, String city);

}
