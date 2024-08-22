package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class UserAlreadyCreatedException extends RuntimeException {

    public UserAlreadyCreatedException(String userEmail, String userCpf) {
        super("Usuário com o email \"" + userEmail + "\" e CPF \"" + userCpf + "\" informados já existe.");
    }
}
