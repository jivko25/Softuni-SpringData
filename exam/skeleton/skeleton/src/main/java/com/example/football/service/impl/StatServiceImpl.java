package com.example.football.service.impl;

import com.example.football.config.FilePaths;
import com.example.football.models.dto.StatsRootSeedDto;
import com.example.football.models.dto.StatsSeedDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {

    private StatRepository statRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private XmlParser xmlParser;

    public StatServiceImpl(StatRepository statRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return
                Files.readString(Path.of(FilePaths.STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        StatsRootSeedDto statsRootSeedDto = xmlParser
                .fromFile(FilePaths.STATS_FILE_PATH, StatsRootSeedDto.class);

        statsRootSeedDto.getStats().stream()
                .filter(statsSeedDto -> {
                    boolean isValid = validationUtil.isValid(statsSeedDto);

                    sb.append(isValid ?
                            String.format("Successfully imported Stat %f - %f - %f", statsSeedDto.getPassing(), statsSeedDto.getShooting(), statsSeedDto.getEndurance())
                            : "Invalid Stat"
                            );
                    sb.append(System.lineSeparator());

                    return isValid;
                })
                .map(statsSeedDto -> modelMapper.map(statsSeedDto, Stat.class))
                .forEach(statRepository::save);

        return sb.toString();
    }
}
