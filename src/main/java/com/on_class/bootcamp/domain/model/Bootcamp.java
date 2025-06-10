package com.on_class.bootcamp.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Bootcamp {
    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer duration;
    private List<Capability> capabilities;

    public Bootcamp(){

    }

    public Bootcamp(String name, String description, LocalDate launchDate, Integer duration, List<Capability> capabilities) {
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.duration = duration;
        this.capabilities = capabilities;
    }

    public Bootcamp(Long id, String name, String description, LocalDate launchDate, Integer duration, List<Capability> capabilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.duration = duration;
        this.capabilities = capabilities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }
}
