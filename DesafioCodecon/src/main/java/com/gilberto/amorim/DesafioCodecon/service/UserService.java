package com.gilberto.amorim.DesafioCodecon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gilberto.amorim.DesafioCodecon.model.ResponseServer.ApiResponse;
import com.gilberto.amorim.DesafioCodecon.model.ResponseServer.UploadResponse;
import com.gilberto.amorim.DesafioCodecon.model.User.LogUser;
import com.gilberto.amorim.DesafioCodecon.model.User.User;
import com.gilberto.amorim.DesafioCodecon.model.dto.CountryTotal;
import com.gilberto.amorim.DesafioCodecon.model.dto.LoginCount;
import com.gilberto.amorim.DesafioCodecon.model.dto.TeamInsights;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();

    public UploadResponse uploadUsersForJson(MultipartFile arquivo) throws IOException {
        ObjectMapper objMap = new ObjectMapper();

        if (!users.isEmpty()){ users.clear();}

        users = objMap.readValue(arquivo.getBytes(),
                objMap.getTypeFactory().constructCollectionType(List.class, User.class));

        return new UploadResponse("Dados recebidos com sucesso", users.size());
    }

    public ApiResponse groupBySuperUsers() {;
        if (users.isEmpty()){ return null;}

        long startTime = System.currentTimeMillis();

        List<User> superUsers = users.stream()
                .filter(user -> user.getScore() >= 900 && user.getActive())
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        return new ApiResponse(LocalDateTime.now(), processingTime, superUsers);
    }

    public ApiResponse groupByTopCountries() {
        if (users.isEmpty()){ return null;}

        long startTime = System.currentTimeMillis();

        List<User>superUsers = users.stream()
                .filter(user -> user.getScore() >= 900 && user.getActive())
                .toList();

        Map<String, Long> countries = superUsers.stream()
                .map(User::getCountry)
                .collect(Collectors.groupingBy(country -> country, Collectors.counting()));


        List<CountryTotal> countryTotals = countries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new CountryTotal(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        return new ApiResponse(LocalDateTime.now(), processingTime, countryTotals);
    }

    public ApiResponse groupByTeamProjects() {
        if (users.isEmpty()){ return null;}

        long startTime = System.currentTimeMillis();

        Map<String, List<User>> usersByTeam = users.stream()
                .collect(Collectors.groupingBy(user -> user.getTeam().getName()));

        DecimalFormat df = new DecimalFormat("#.#",new DecimalFormatSymbols(Locale.ENGLISH));

        List<TeamInsights> teamInsights = usersByTeam.entrySet().stream()
                .map(entry -> {
                    String teamName = entry.getKey();
                    List<User> teamMembers = entry.getValue();
                    long totalMembersTeam = teamMembers.size();
                    long leaders = teamMembers.stream()
                            .filter(member -> Boolean.TRUE.equals(member.getTeam().getLeader()))
                            .count();
                    long completeProjects = teamMembers.stream()
                            .flatMap(member -> member.getTeam().getProjects().stream())
                            .filter(project -> Boolean.TRUE.equals(project.getCompleted()))
                            .count();
                    long activeMembers = teamMembers.stream()
                            .filter(member -> Boolean.TRUE.equals(member.getActive()))
                            .count();
                    double formattedPercentageDouble = Double.parseDouble(df.format((double)activeMembers / totalMembersTeam * 100));

                    return new TeamInsights(teamName,
                            totalMembersTeam,
                            leaders,
                            completeProjects,
                            formattedPercentageDouble
                    );
                })
                .toList();

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        return new ApiResponse(LocalDateTime.now(), processingTime, teamInsights);
    }

    public ApiResponse groupByLoginByDate(Integer minLogs){
        if (users.isEmpty()){ return null;}

        long startTime = System.currentTimeMillis();

        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;

        List<LogUser> loginLogs = users.stream()
                .flatMap(user -> user.getLogs().stream())
                .filter(log -> "login".equalsIgnoreCase(log.getAction()) && log.getDate() != null)
                .toList();

        Map<LocalDate, Long> loginCounts = loginLogs.stream()
                .collect(Collectors.groupingBy(
                        log-> LocalDate.parse(log.getDate(), dtf),
                        Collectors.counting()
                ));

        List<LoginCount> loginCountList = loginCounts.entrySet().stream()
                .filter(entry -> minLogs == null || entry.getValue() >= minLogs)
                .map(entry -> new LoginCount(entry.getKey(), entry.getValue()))
                .toList();

        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        return new ApiResponse(LocalDateTime.now(), processingTime, loginCountList);
    }
}
