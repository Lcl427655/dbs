package com.cn.dbs.entity;

import oracle.sql.DATE;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FbpProxySynLog {
    private DATE lastUpdateDate;
    private Long lastUpdatedBy;
    private DATE creationDate;
    private Long createdBy;
    private Long lastUpdateLogin;
    private String enabledFlag;
    private Long synId;
    private String priKey;
    private String input;
    private String output;
    private String description;
    private String synAddress;
    private Long timeCost;

    public DATE getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(DATE lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public DATE getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DATE creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getSynId() {
        return synId;
    }

    public void setSynId(Long synId) {
        this.synId = synId;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSynAddress() {
        return synAddress;
    }

    public void setSynAddress(String synAddress) {
        this.synAddress = synAddress;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    public FbpProxySynLog() {
    }

    @Override
    public String toString() {
        return "FbpProxySynLog{" +
                "lastUpdateDate=" + lastUpdateDate +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", creationDate=" + creationDate +
                ", createdBy=" + createdBy +
                ", lastUpdateLogin=" + lastUpdateLogin +
                ", enabledFlag='" + enabledFlag + '\'' +
                ", synId=" + synId +
                ", priKey='" + priKey + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", description='" + description + '\'' +
                ", synAddress='" + synAddress + '\'' +
                ", timeCost=" + timeCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FbpProxySynLog that = (FbpProxySynLog) o;
        return Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(lastUpdatedBy, that.lastUpdatedBy) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(lastUpdateLogin, that.lastUpdateLogin) &&
                Objects.equals(enabledFlag, that.enabledFlag) &&
                Objects.equals(synId, that.synId) &&
                Objects.equals(priKey, that.priKey) &&
                Objects.equals(input, that.input) &&
                Objects.equals(output, that.output) &&
                Objects.equals(description, that.description) &&
                Objects.equals(synAddress, that.synAddress) &&
                Objects.equals(timeCost, that.timeCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastUpdateDate, lastUpdatedBy, creationDate, createdBy, lastUpdateLogin, enabledFlag, synId, priKey, input, output, description, synAddress, timeCost);
    }

    public FbpProxySynLog(DATE lastUpdateDate, Long lastUpdatedBy, DATE creationDate, Long createdBy, Long lastUpdateLogin, String enabledFlag, Long synId, String priKey, String input, String output, String description, String synAddress, Long timeCost) {
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdateLogin = lastUpdateLogin;
        this.enabledFlag = enabledFlag;
        this.synId = synId;
        this.priKey = priKey;
        this.input = input;
        this.output = output;
        this.description = description;
        this.synAddress = synAddress;
        this.timeCost = timeCost;
    }
}
