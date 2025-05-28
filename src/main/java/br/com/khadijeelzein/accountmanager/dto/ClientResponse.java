package br.com.khadijeelzein.accountmanager.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ClientResponse {
    private String id;
    private String cpf;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;
}
