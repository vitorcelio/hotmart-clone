package com.hotmart.auth.controller;

import com.hotmart.auth.services.dataAccess.DataAccessService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/dataAccess")
@RequiredArgsConstructor
public class DataAccessController {

    private final DataAccessService service;

    @GetMapping("/download")
    public void downloadDataAccess(HttpServletResponse response, @RequestParam("uuid") String uuid) throws IOException {
        service.downloadDataAccess(response, uuid);
    }

}
