package com.example.football.service.impl;

import com.example.football.config.FilePaths;
import com.example.football.models.dto.TeamsSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;
    private Gson gson;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private TownRepository townRepository;

    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }


    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return
                Files.readString(Path.of(FilePaths.TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        TeamsSeedDto[] teamsSeedDtos =
                gson.fromJson(readTeamsFileContent(), TeamsSeedDto[].class);

        Arrays.stream(teamsSeedDtos)
                .filter(teamsSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamsSeedDto);

                    sb.append(isValid ?
                            String.format("Successfully imported Team %s - %d",
                                    teamsSeedDto.getName(), teamsSeedDto.getFanBase())
                            : "Invalid Team"
                            );
                    sb.append(System.lineSeparator());
                    return isValid;
                })
                .map(teamsSeedDto -> {
                    Team team = modelMapper.map(teamsSeedDto, Team.class);
                    Town town = townRepository.findTownByName(teamsSeedDto.getTownName());
                    team.setTown(town);
                            return team;
                })
                .forEach(teamRepository::save);

        return sb.toString();
    }
}
