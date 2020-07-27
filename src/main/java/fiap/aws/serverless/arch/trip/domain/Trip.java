package fiap.aws.serverless.arch.trip.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "trip_mgnt")
public class Trip {

    @DynamoDBHashKey(attributeName = "uuid")
    private String uuid;

    @DynamoDBAttribute(attributeName = "country")
    private String country;

    @DynamoDBRangeKey(attributeName = "date")
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
