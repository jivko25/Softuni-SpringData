package com.example.football.service.impl;

import com.example.football.config.FilePaths;
import com.example.football.models.dto.TownsSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


@Service
public class TownServiceImpl implements TownService {

    private TownRepository townRepository;
    private Gson gson;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        String path = Files.readString(Path.of(FilePaths.TOWNS_FILE_PATH));
        return path;
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();

        TownsSeedDto[] townsSeedDtos =
                gson.fromJson(readTownsFileContent(), TownsSeedDto[].class);

        Arrays.stream(townsSeedDtos)
                .filter(townsSeedDto -> {
                    boolean isValid = validationUtil.isValid(townsSeedDto);

                    sb.append(isValid ?
                            String.format("Successfully imported Town %s - %d", townsSeedDto.getName(), townsSeedDto.getPopulation())
                            : "Invalid Town");
                    sb.append(System.lineSeparator());

                    return isValid;
                })
                .map(townsSeedDto -> modelMapper.map(townsSeedDto, Town.class))
                .forEach(townRepository::save);

        return sb.toString();
    }
}
