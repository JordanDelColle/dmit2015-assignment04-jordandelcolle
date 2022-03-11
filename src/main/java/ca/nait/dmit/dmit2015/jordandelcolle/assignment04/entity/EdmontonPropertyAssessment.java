package ca.nait.dmit.dmit2015.jordandelcolle.assignment04.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Edmonton property assessment data retrieved from
 * https://data.edmonton.ca/City-Administration/Property-Assessment-Data-Current-Calendar-Year-/q7d6-ambg
 *
 * @author Jordan Del Colle
 * @version 2022.03.11
 *
 */
@Entity
@Table(name = "jordanDelColleEdmontonPropertyAssessment")
@Getter
@Setter
public class EdmontonPropertyAssessment implements Comparable<EdmontonPropertyAssessment>, Serializable {
    @Id
    private String accountNumber;
    private String suite;
    private String houseNumber;
    private String streetName;
    private Boolean garage;
    private Integer neighbourhoodId;
    private String neighbourhood;
    private String ward;
    private Long assessedValue;
    private double latitude;
    private double longitude;
    private String assessmentClass1;

    public EdmontonPropertyAssessment() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Boolean getGarage() {
        return garage;
    }

    public void setGarage(Boolean garage) {
        this.garage = garage;
    }

    public Integer getNeighbourhoodId() {
        return neighbourhoodId;
    }

    public void setNeighbourhoodId(Integer neighbourhoodId) {
        this.neighbourhoodId = neighbourhoodId;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public Long getAssessedValue() {
        return assessedValue;
    }

    public void setAssessedValue(Long assessedValue) {
        this.assessedValue = assessedValue;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAssessmentClass1() {
        return assessmentClass1;
    }

    public void setAssessmentClass1(String assessmentClass1) {
        this.assessmentClass1 = assessmentClass1;
    }

    @Override
    public int compareTo(EdmontonPropertyAssessment o) {
        return 0;
    }

    @Column(name = "point_location")
    @jakarta.json.bind.annotation.JsonbTransient
    private org.locationtech.jts.geom.Point pointLocation;

    private LocalDateTime createdDateTime;

    @PrePersist
    private void beforePersist() {
        createdDateTime = LocalDateTime.now();
    }
}