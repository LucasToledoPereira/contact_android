package com.example.lucas.services;

import android.content.Context;

import com.example.lucas.vo.EnderecoVO;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lucas on 13/06/2017.
 */

public class CepService {

    private BuscaEndereco apiSearchService;
    private Retrofit retrofit;

    public CepService(Context context)
    {

         retrofit = new Retrofit.Builder()
                .baseUrl("http://correiosapi.apphb.com/cep/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiSearchService = retrofit.create(BuscaEndereco.class);
    }

    public BuscaEndereco getApiSearchService()
    {
        return apiSearchService;
    }

    public  Retrofit getRetrofit() {
        return retrofit;
    }
}
