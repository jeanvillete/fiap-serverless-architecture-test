package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "trip_mgnt")
public class Trip {

    public static final String PARTITION_NAME = "TRIP";

    @DynamoDBHashKey(attributeName = "partition")
    private String partition = PARTITION_NAME;

    @DynamoDBRangeKey(attributeName = "uuid")
    private String uuid;

    @DynamoDBAttribute(attributeName = "country")
    private String country;

    @DynamoDBIndexRangeKey(attributeName = "date", localSecondaryIndexName = "dateLSI")
    private String date;

    @DynamoDBAttribute(attributeName = "city")
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

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
