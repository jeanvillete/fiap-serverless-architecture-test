package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "trip_mgnt")
public class Trip {

    @DynamoDBHashKey(attributeName = "country")
    private String country;

    @DynamoDBRangeKey(attributeName = "date")
    private String date;

    @DynamoDBIndexRangeKey(attributeName = "city", localSecondaryIndexName = "cityLSI")
    private String city;

    @DynamoDBAttribute(attributeName = "reason")
    private String reason;

    public Trip() {
    }

    public Trip(String country, String city, String date, String reason) {
        this.country = country;
        this.city = city;
        this.date = date;
        this.reason = reason;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public String getReason() {
        return reason;
    }
}
