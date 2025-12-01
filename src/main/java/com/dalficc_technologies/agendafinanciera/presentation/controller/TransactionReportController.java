package com.dalficc_technologies.agendafinanciera.presentation.controller;


import com.dalficc_technologies.agendafinanciera.application.service.GetUserTransactionsByPeriodService;
import com.dalficc_technologies.agendafinanciera.application.service.GetUserTransactionsByYearService;
import com.dalficc_technologies.agendafinanciera.infrastructure.excel.TransactionExcelGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionReportController {

    private final GetUserTransactionsByPeriodService getByPeriodService;
    private final GetUserTransactionsByYearService getByYearService;
    private final TransactionExcelGenerator excelGenerator = new TransactionExcelGenerator();

    public TransactionReportController(GetUserTransactionsByPeriodService getByPeriodService,
                                       GetUserTransactionsByYearService getByYearService) {
        this.getByPeriodService = getByPeriodService;
        this.getByYearService = getByYearService;
    }

    @GetMapping("/month/report")
    public ResponseEntity<byte[]> generateMonthlyReport(
            @RequestHeader("Authorization") String token,
            @RequestParam int year,
            @RequestParam int month) {

        try {
            FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decoded.getUid();

            var transactions = getByPeriodService.getTransactionsByPeriod(userId, year, month);
            var excel = excelGenerator.generate(transactions);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("reporte_mensual_" + year + "_" + month + ".xlsx")
                            .build()
            );

            return new ResponseEntity<>(excel, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/year/report")
    public ResponseEntity<byte[]> generateYearlyReport(
            @RequestHeader("Authorization") String token,
            @RequestParam int year) {

        try {
            FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decoded.getUid();

            var transactions = getByYearService.getTransactionsByYear(userId, year);
            var excel = excelGenerator.generate(transactions);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("reporte_anual_" + year + ".xlsx")
                            .build()
            );

            return new ResponseEntity<>(excel, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}