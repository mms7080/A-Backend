package com.example.Agreement;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class AgreementController {

    @GetMapping("/agreement2")
    //public List<String> getAgreement() throws IOException {
    public String getAgreement() throws IOException {
        // String[] agr = new String[5];
        // agr[0]=Files.readString(Path.of("./term1.txt"));
        // agr[1]=Files.readString(Path.of("./term2.txt"));
        // agr[2]=Files.readString(Path.of("./term3.txt"));
        // return List.of(agr[0],agr[1],agr[2]);
        return "a";
    }

}
