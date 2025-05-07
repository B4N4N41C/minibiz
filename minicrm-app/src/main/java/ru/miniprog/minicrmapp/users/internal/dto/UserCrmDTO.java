package ru.miniprog.minicrmapp.users.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCrmDTO {
    private Long id;
    private String username;
    private String email;
}
