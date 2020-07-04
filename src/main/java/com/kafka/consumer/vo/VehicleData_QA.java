package com.kafka.consumer.vo;

/**
 * This class represents Vehicle data received from 'microlise-raw-telemetry-v1' of QA box. 
 * Use this when deployed on QA
 * 
 * @author Pallavi Shetty
 * @since May 2020
 * 
 */

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


public class VehicleData_QA implements VehicleIdentifier {
    private String vehicleId;
    private Integer eventCode;
    private Integer intraSecondSequence;
    private Double gpsLatitude;
    private Double gpsLongitude;
    private Integer gpsCourseInDegrees;
    private BigDecimal hdop;
    private Integer odometerInMeters;
    private BigDecimal vehicleFuelLitres;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDateTime;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getEventCode() {
        return eventCode;
    }

    public void setEventCode(Integer eventCode) {
        this.eventCode = eventCode;
    }

    public Double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(Double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public Double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(Double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public Integer getGpsCourseInDegrees() {
        return gpsCourseInDegrees;
    }

    public void setGpsCourseInDegrees(Integer gpsCourseInDegrees) {
        this.gpsCourseInDegrees = gpsCourseInDegrees;
    }

    public Integer getOdometerInMeters() {
        return odometerInMeters;
    }

    public void setOdometerInMeters(Integer odometerInMeters) {
        this.odometerInMeters = odometerInMeters;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Integer getIntraSecondSequence() {
        return intraSecondSequence;
    }

    public void setIntraSecondSequence(Integer intraSecondSequence) {
        this.intraSecondSequence = intraSecondSequence;
    }

    public BigDecimal getHdop() {
        return hdop;
    }

    public void setHdop(BigDecimal hdop) {
        this.hdop = hdop;
    }

    public BigDecimal getVehicleFuelLitres() {
        return vehicleFuelLitres;
    }

    public void setVehicleFuelLitres(BigDecimal vehicleFuelLitres) {
        this.vehicleFuelLitres = vehicleFuelLitres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleData_QA that = (VehicleData_QA) o;
        return Objects.equals(vehicleId, that.vehicleId) &&
                Objects.equals(eventCode, that.eventCode) &&
                Objects.equals(intraSecondSequence, that.intraSecondSequence) &&
                Objects.equals(gpsLatitude, that.gpsLatitude) &&
                Objects.equals(gpsLongitude, that.gpsLongitude) &&
                Objects.equals(gpsCourseInDegrees, that.gpsCourseInDegrees) &&
                Objects.equals(hdop, that.hdop) &&
                Objects.equals(odometerInMeters, that.odometerInMeters) &&
                Objects.equals(vehicleFuelLitres, that.vehicleFuelLitres) &&
                Objects.equals(eventDateTime, that.eventDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, eventCode, intraSecondSequence, gpsLatitude, gpsLongitude, gpsCourseInDegrees, hdop, odometerInMeters, vehicleFuelLitres, eventDateTime);
    }

    @Override
    public String toString() {
        return "TelemetryData{" +
                "vehicleId='" + vehicleId + '\'' +
                ", eventCode=" + eventCode +
                ", intraSecondSequence=" + intraSecondSequence +
                ", gpsLatitude=" + gpsLatitude +
                ", gpsLongitude=" + gpsLongitude +
                ", gpsCourseInDegrees=" + gpsCourseInDegrees +
                ", hdop=" + hdop +
                ", odometerInMeters=" + odometerInMeters +
                ", vehicleFuelLitres=" + vehicleFuelLitres +
                ", eventDateTime=" + eventDateTime +
                '}';
    }
}