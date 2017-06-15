package com.example.lucas.enums;

/**
 * Created by Lucas on 13/06/2017.
 */

public enum ContactInfoEnum {
    NOME("nome"),
    EMAIL("email"),
    CEP("cep"),
    LOGRADOURO("logradouro"),
    NUMERO("numero"),
    COMPLEMENTO("complemento"),
    BAIRRO("bairro"),
    CIDADE("cidade"),
    UF("uf"),
    FONE("fone");

    private String info;

    ContactInfoEnum(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }
}
