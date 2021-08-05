package com.example.football.models.dto;

import com.example.football.models.entity.Possition;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayersSeedDto {
    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement(name = "town")
    private TownDto town;
    @XmlElement(name = "stat")
    private StatDto stat;
    @XmlElement(name = "position")
    private Possition possition;
    @XmlElement(name = "team")
    private TeamDto team;

    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public Possition getPossition() {
        return possition;
    }

    public void setPossition(Possition possition) {
        this.possition = possition;
    }

    @Size(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Size(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public TownDto getTown() {
        return town;
    }

    public void setTown(TownDto town) {
        this.town = town;
    }

    public StatDto getStat() {
        return stat;
    }

    public void setStat(StatDto stat) {
        this.stat = stat;
    }
}
