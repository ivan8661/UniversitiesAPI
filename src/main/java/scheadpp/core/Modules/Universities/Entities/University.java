package scheadpp.core.Modules.Universities.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class University {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("serviceName")
    private String service;


    @JsonProperty("referenceDate")
    private Integer referenceDate;


    @JsonProperty("referenceWeek")
    private String referenceWeek;




    public University() {

    }

    public University(String id, String name, String service, Integer referenceDate, String referenceWeek) {
        this.id = id;
        this.name = name;
        this.service = service;
        this.referenceDate = referenceDate;
        this.referenceWeek = referenceWeek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Integer referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getReferenceWeek() {
        return referenceWeek;
    }

    public void setReferenceWeek(String referenceWeek) {
        this.referenceWeek = referenceWeek;
    }
}
