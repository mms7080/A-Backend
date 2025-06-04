package com.example.demo.Agreement;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class AgreementController {

    @GetMapping("/agreement")
    public List<String> getAgreement() throws IOException {
        String[] agr = new String[5];
        agr[0]=readResourceFile("static/texts/term1.txt");
        agr[1]=readResourceFile("static/texts/term2.txt");
        agr[2]=readResourceFile("static/texts/term3.txt");
        return List.of(agr[0],agr[1],agr[2]);
    }

    private String readResourceFile(String filename) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IOException("Resource file not found: " + filename);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
