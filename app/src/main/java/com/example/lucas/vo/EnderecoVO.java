package com.example.lucas.vo;

/**
 * Created by Lucas on 13/06/2017.
 */

public class EnderecoVO {

    private String bairro;
    private String cidade;
    private String logradouro;
    private String estado;
    private  String tipoDeLogradouro;

    public EnderecoVO(String bairro, String cidade, String logradouro, String estado, String tipoDeLogradouro) {
        this.bairro = bairro;
        this.cidade = cidade;
        this.logradouro = logradouro;
        this.estado = estado;
        this.tipoDeLogradouro = tipoDeLogradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public  void setEstado(String estado){
        this.estado = estado;
    }

    public String getEstado(){
        return this.estado;
    }

    public void setTipoDeLogradouro(String tipoDeLogradouro){
        this.tipoDeLogradouro = tipoDeLogradouro;
    }

    public String getTipoDeLogradouro(){
        return this.tipoDeLogradouro;
    }
}
