package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayersRootSeedDto {
    @XmlElement(name = "player")
    private List<PlayersSeedDto> players;
    public List<PlayersSeedDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayersSeedDto> players) {
        this.players = players;
    }

}
