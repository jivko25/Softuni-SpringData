package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatsSeedDto {
    @XmlElement
    private BigDecimal passing;
    @XmlElement
    private BigDecimal shooting;
    @XmlElement
    private BigDecimal endurance;

    @Positive
    public BigDecimal getPassing() {
        return passing;
    }

    public void setPassing(BigDecimal passing) {
        this.passing = passing;
    }

    @Positive
    public BigDecimal getShooting() {
        return shooting;
    }

    public void setShooting(BigDecimal shooting) {
        this.shooting = shooting;
    }

    @Positive
    public BigDecimal getEndurance() {
        return endurance;
    }

    public void setEndurance(BigDecimal endurance) {
        this.endurance = endurance;
    }
}
