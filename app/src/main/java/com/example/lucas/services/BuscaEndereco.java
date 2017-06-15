package com.example.lucas.services;

import com.example.lucas.vo.EnderecoVO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Lucas on 13/06/2017.
 */

public interface BuscaEndereco {

    @GET("{cep}")
    Call<EnderecoVO> getEndereco(@Path("cep") String cep);
}
