package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

import com.uniprojecao.fabrica.gprojuridico.services.UserService;

public class UserAlreadyCreatedException extends RuntimeException {

    public UserAlreadyCreatedException(String userEmail, String userCpf) {
        super("Usuário com o email \"" + userEmail + "\" e CPF \"" + userCpf + "\" informados já existe.");
    }

    public UserAlreadyCreatedException(UserService.UserUniqueField field, String fieldValue) {
        super("Usuário com o " + field.toString().toLowerCase() + " \"" + fieldValue + "\" já existe.");
    }
}