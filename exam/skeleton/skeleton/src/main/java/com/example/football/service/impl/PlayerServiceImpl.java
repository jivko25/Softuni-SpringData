package com.example.football.service.impl;

import com.example.football.config.FilePaths;
import com.example.football.models.dto.PlayersRootSeedDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownRepository townRepository, TeamRepository teamRepository, StatRepository statRepository) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return
                Files.readString(Path.of(FilePaths.PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlayersRootSeedDto playersRootSeedDto =
                xmlParser.fromFile(FilePaths.PLAYERS_FILE_PATH, PlayersRootSeedDto.class);

        playersRootSeedDto.getPlayers().stream()
                .filter(playersSeedDto -> {
                    boolean isValid = validationUtil.isValid(playersSeedDto);

                    sb.append(isValid ?
                            String.format("Successfully imported Player %s %s - %s", playersSeedDto.getFirstName(), playersSeedDto.getLastName(), playersSeedDto.getPossition())
                            : "Invalid Player");

                    sb.append(System.lineSeparator());

                    return isValid;
                })
                .map(playersSeedDto -> {
                    Player player = modelMapper.map(playersSeedDto, Player.class);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate birthDay = LocalDate.parse(playersSeedDto.getBirthDate(), formatter);
                    player.setTown(townRepository.findTownByName(playersSeedDto.getTown().getName()));
                    player.setTeam(teamRepository.findTeamByName(playersSeedDto.getTeam().getName()));
                    player.setStat(statRepository.getById(playersSeedDto.getStat().getId()));
                    player.setBirthDay(birthDay);
                    return player;
                }
                )
                .forEach(playerRepository::save);

        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        List<Player> players = playerRepository.findAllByBirthDay(LocalDate.of(1995, 1, 1),
                LocalDate.of(2003, 1, 1));
        players.forEach(player -> {
            sb.append(String.format("Player - %s %s\n" +
                    "\tPosition - %s\n" +
                    "Team - %s\n", player.getFirstName(), player.getLastName(), player.getPossition(), player.getTeam().getName()));
            sb.append(System.lineSeparator());
        });
        return sb.toString();
    }
}
