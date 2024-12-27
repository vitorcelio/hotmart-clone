package com.hotmart.auth.services.dataAccess.impl;

import com.hotmart.auth.config.exception.ValidationException;
import com.hotmart.auth.models.DataAccessEntity;
import com.hotmart.auth.models.UserEntity;
import com.hotmart.auth.repositories.DataAccessRepository;
import com.hotmart.auth.services.dataAccess.DataAccessService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.hotmart.auth.utils.AuthUtils.hasXCharacters;
import static com.hotmart.auth.utils.AuthUtils.hidePartOfTheText;

@Service
@RequiredArgsConstructor
public class DataAccessServiceImpl implements DataAccessService {

    private final DataAccessRepository repository;

    @Override
    public void downloadDataAccess(HttpServletResponse response, String uuid) throws IOException {
        UserEntity user = validationAccessData(uuid);
        List<String> row = rowForDataAccess(user);
        assembleObjectToDownload(response, row);
        updateDataAccess(uuid);
    }

    private UserEntity validationAccessData(@Nonnull String uuid) {
        DataAccessEntity dataAccess =
                repository.findById(UUID.fromString(uuid)).orElseThrow(() -> new ValidationException("Solicitação de " +
                        "acesso de dados não encontrada"));

        if (dataAccess.isDownloaded()) {
            throw new ValidationException("Os dados já foram baixados.");
        }

        long hours = Duration.between(dataAccess.getCreatedAt(), LocalDateTime.now()).toHours();
        if (hours >= 2) {
            throw new ValidationException("A Solicitação expirou, o tempo é de 2 horas.");
        }

        return dataAccess.getUser();
    }

    private List<String> rowForDataAccess(UserEntity user) {
        String company = ObjectUtils.isEmpty(user.getCpfCnpj()) ?
                "Não" : hasXCharacters(user.getCpfCnpj(), 11) ?
                "Não" : "Sim";
        String typePerson = company.contains("Não") ? "Pessoa Física" : "Pessoa Jurídica";

        List<String> row = new ArrayList<>();
        row.add(typePerson);
        row.add(hidePartOfTheText(user.getName()));
        row.add(hidePartOfTheText(user.getEmail()));
        row.add(hidePartOfTheText(user.getName()));
        row.add(hidePartOfTheText(user.getPhone()));
        row.add(hidePartOfTheText(user.getPhone()));
        row.add(company);

        String birthDate = user.getBirthDate() != null ? hidePartOfTheText(user.getBirthDate().toString()) : "Não informado";
        row.add(birthDate);
        return row;
    }

    private void assembleObjectToDownload(HttpServletResponse response, List<String> row) {
        Workbook workbook;

        try (InputStream inputStream = new ClassPathResource("dataAccess.xlsx").getInputStream()) {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o arquivo XLSX", e);
        }

        Sheet sheet = workbook.getSheetAt(0);

        int lastRowNum = sheet.getLastRowNum();
        Row newRow = sheet.createRow(lastRowNum);
        newRow.createCell(0).setCellValue(row.get(0));
        newRow.createCell(1).setCellValue(row.get(1));
        newRow.createCell(2).setCellValue(row.get(2));
        newRow.createCell(3).setCellValue(row.get(3));
        newRow.createCell(4).setCellValue(row.get(4));
        newRow.createCell(5).setCellValue(row.get(5));
        newRow.createCell(6).setCellValue(row.get(6));
        newRow.createCell(7).setCellValue(row.get(7));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + UUID.randomUUID() + ".xlsx\"");

        try {
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao editar arquivo XLSX", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fechar arquivo XLSX", e);
            }
        }

    }

    private void updateDataAccess(String uuid) {
        DataAccessEntity dataAccess =
                repository.findById(UUID.fromString(uuid)).orElseThrow(() -> new ValidationException("Solicitação de " +
                        "acesso de dados não encontrada"));

        dataAccess.setDownloaded(true);
        repository.save(dataAccess);
    }

}
