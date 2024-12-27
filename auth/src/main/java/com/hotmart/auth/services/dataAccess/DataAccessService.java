package com.hotmart.auth.services.dataAccess;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface DataAccessService {

    void downloadDataAccess(HttpServletResponse response, String uuid) throws IOException;

}
