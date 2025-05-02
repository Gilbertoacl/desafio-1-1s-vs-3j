package com.gilberto.amorim.DesafioCodecon.controller;

import com.gilberto.amorim.DesafioCodecon.model.ResponseServer.ApiResponse;
import com.gilberto.amorim.DesafioCodecon.model.ResponseServer.UploadResponse;
import com.gilberto.amorim.DesafioCodecon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UploadResponse> uploadJson (@RequestParam("arquivo")MultipartFile arquivo){
        if(arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body(new UploadResponse("Arquivo vazio", 0));
        }

        try {
            return ResponseEntity.ok(userService.uploadUsersForJson(arquivo));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new UploadResponse("Erro ao processar o arquivo", 0));
        }
    }

    @GetMapping("/superusers")
    public ResponseEntity<ApiResponse> superUsers () {
        ApiResponse response = userService.groupBySuperUsers();

        if (response == null){
            return  ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-countries")
    public ResponseEntity<ApiResponse> topCountries (){
        ApiResponse response = userService.groupByTopCountries();

        if (response == null){
            return  ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/team-insights")
    public ResponseEntity<ApiResponse> teamInsights () {
        ApiResponse response = userService.groupByTeamProjects();

        if (response == null){
            return  ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active-users-per-day")
    public ResponseEntity<ApiResponse> activeUserPerDate(@RequestParam(value="min", required = false) Integer minLogins){
        ApiResponse response = userService.groupByLoginByDate(minLogins);

        if (response == null){
            return  ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
