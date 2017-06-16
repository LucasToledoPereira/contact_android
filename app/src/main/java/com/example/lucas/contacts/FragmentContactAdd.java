package com.example.lucas.contacts;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucas.enums.ContactInfoEnum;
import com.example.lucas.services.CepService;
import com.example.lucas.vo.CepResponseError;
import com.example.lucas.vo.EnderecoVO;

import org.androidannotations.annotations.ViewById;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class FragmentContactAdd extends Fragment {

    private static final String EMPTY_STRING = "";
    DBContactHelper mydb;
    View view;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       //Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_add, container, false);
       mydb = new DBContactHelper(view.getContext());

       Button save = (Button) view.findViewById(R.id.save);
       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               insertContact(v);
           }
       });
       addCepListener();
       showTextViesAsMandatory();

       return view;
   }

   private void addCepListener(){
       final TextView cep = (TextView) view.findViewById(R.id.addCep);

       cep.addTextChangedListener(new TextWatcher() {

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.length() == 8) {
                   buscaCep(s.toString());
               } else {
                   clearFields();
                   enableFields(true);
               }
           }

           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,
                                         int after) {
               // TODO Auto-generated method stub
           }

           @Override
           public void afterTextChanged(Editable s) {
               //cep.setError(null);
           }
       });

   }

   private void insertContact(View v){
       ContentValues contactInfo = getContactInfo();
       if(verificaCampos()){
           if(mydb.insertContact(contactInfo)){
               Toast.makeText(v.getContext(), R.string.contato_salvo,
                       Toast.LENGTH_SHORT).show();
               //redirectToList();
           } else{
               Toast.makeText(v.getContext(), R.string.error_salvar_contato,
                       Toast.LENGTH_SHORT).show();
           }
       }
   }

   private ContentValues getContactInfo(){
       ContentValues info = new ContentValues();

       TextView name = (TextView) view.findViewById(R.id.addName);
       TextView phone = (TextView) view.findViewById(R.id.addPhone);
       TextView email = (TextView) view.findViewById(R.id.addEmail);
       TextView cep = (TextView) view.findViewById(R.id.addCep);
       TextView numero = (TextView) view.findViewById(R.id.addNumero);
       TextView bairro = (TextView) view.findViewById(R.id.addBairro);
       TextView uf = (TextView) view.findViewById(R.id.addUf);
       TextView cidade = (TextView) view.findViewById(R.id.addCidade);
       TextView complemento = (TextView) view.findViewById(R.id.addComplemento);

       info.put(ContactInfoEnum.NOME.getInfo(), name.getText().toString());
       info.put(ContactInfoEnum.FONE.getInfo(), phone.getText().toString());
       info.put(ContactInfoEnum.EMAIL.getInfo(), email.getText().toString());
       info.put(ContactInfoEnum.CEP.getInfo(), cep.getText().toString());
       info.put(ContactInfoEnum.NUMERO.getInfo(), numero.getText().toString());
       info.put(ContactInfoEnum.BAIRRO.getInfo(), bairro.getText().toString());
       info.put(ContactInfoEnum.UF.getInfo(), uf.getText().toString());
       info.put(ContactInfoEnum.CIDADE.getInfo(), cidade.getText().toString());
       info.put(ContactInfoEnum.COMPLEMENTO.getInfo(), complemento.getText().toString());

       return info;
   }

   private void buscaCep(String cep) {

       final CepService service = new CepService(view.getContext());

       Call<EnderecoVO> call = service.getApiSearchService().getEndereco(cep);

       call.enqueue(new Callback<EnderecoVO>() {

           ProgressDialog dialog = ProgressDialog.show(view.getContext(), "",
                   "Localizando endereço", true);
           @Override
           public void onResponse(Call<EnderecoVO> call, Response<EnderecoVO> response) {
               dialog.hide();
               if (response.isSuccessful()) {
                   EnderecoVO endereco = response.body();
                   populaEndereco(endereco);
                   Toast.makeText(view.getContext(), R.string.endereco_encontrado, Toast.LENGTH_LONG).show();
               } else {
                   Converter<ResponseBody, CepResponseError> converter = service.getRetrofit().responseBodyConverter(CepResponseError.class, new Annotation[0]);

                   try {
                       CepResponseError errors = converter.convert(response.errorBody());
                       Toast.makeText(view.getContext(), errors.getMessage(), Toast.LENGTH_SHORT).show();
                   } catch (Exception e) {
                       Toast.makeText(view.getContext(), R.string.error_server_error, Toast.LENGTH_LONG).show();
                   }

               }
           }

           @Override
           public void onFailure(Call<EnderecoVO> call, Throwable t) {
               dialog.hide();
               Toast.makeText(view.getContext(), "Não foi possível realizar a requisição", Toast.LENGTH_SHORT).show();
           }
       });
   }

   private void populaEndereco (EnderecoVO endereco){
       TextView cidade = (TextView) view.findViewById(R.id.addCidade);
       TextView uf = (TextView) view.findViewById(R.id.addUf);
       TextView bairro = (TextView) view.findViewById(R.id.addBairro);

       cidade.setText(endereco.getCidade());
       uf.setText(endereco.getEstado());
       bairro.setText(endereco.getBairro());

       enableFields(false);

   }

    private void enableFields(Boolean enable){
        TextView cidade = (TextView) view.findViewById(R.id.addCidade);
        TextView uf = (TextView) view.findViewById(R.id.addUf);
        TextView bairro = (TextView) view.findViewById(R.id.addBairro);

        cidade.setEnabled(enable);
        uf.setEnabled(enable);
        bairro.setEnabled(enable);
    }

    private void clearFields(){
        TextView cidade = (TextView) view.findViewById(R.id.addCidade);
        TextView uf = (TextView) view.findViewById(R.id.addUf);
        TextView bairro = (TextView) view.findViewById(R.id.addBairro);

        cidade.setText("");
        uf.setText("");
        bairro.setText("");
    }

    private boolean verificaCampos(){
        return verificaNome() && verificaTelefone() && verificarEmail() &&
                verificaCep() && verificaCidade() && verificaUf() && verificarBairro() && verificaNumero();
    }

    private boolean verificaNome(){
        TextView name = (TextView) view.findViewById(R.id.addName);
        String nome = name.getText().toString();
        if(nome == null || nome.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.nome_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificaTelefone(){
        TextView phone = (TextView) view.findViewById(R.id.addPhone);
        String text = phone.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.telefone_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificarEmail(){
        TextView email = (TextView) view.findViewById(R.id.addEmail);
        String text = email.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.email_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        } else if(!isValidEmail(text)){
            Toast.makeText(view.getContext(), R.string.email_invalido, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificaCidade(){
        TextView cidade = (TextView) view.findViewById(R.id.addCidade);
        String text = cidade.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.cidade_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificaNumero(){
        TextView numero = (TextView) view.findViewById(R.id.addNumero);
        String text = numero.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.numero_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificarBairro(){
        TextView bairro = (TextView) view.findViewById(R.id.addBairro);
        String text = bairro.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.telefone_obrigatorio, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificaUf(){
        TextView uf = (TextView) view.findViewById(R.id.addUf);
        String text = uf.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.telefone_obrigatorio, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean verificaCep(){
        TextView cep = (TextView) view.findViewById(R.id.addCep);
        String text = cep.getText().toString();
        if(text == null || text.trim().equals(EMPTY_STRING)) {
            Toast.makeText(view.getContext(), R.string.cep_obrigatorio, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void redirectToList() {

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        FloatingActionButton list = (FloatingActionButton) view.findViewById(R.id.list);

        Fragment fr = new FragmentContactsList();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        add.show();
        list.hide();
    }

    public void showTextViesAsMandatory(){
        TextView name = (TextView) view.findViewById(R.id.textNome);
        TextView phone = (TextView) view.findViewById(R.id.textFone);
        TextView email = (TextView) view.findViewById(R.id.textEmail);
        TextView cep = (TextView) view.findViewById(R.id.textCep);
        TextView numero = (TextView) view.findViewById(R.id.textNumero);
        TextView bairro = (TextView) view.findViewById(R.id.textBairro);
        TextView uf = (TextView) view.findViewById(R.id.textUf);
        TextView cidade = (TextView) view.findViewById(R.id.textCidade);

        showTextViewsAsMandatory(name, phone, email, cep, numero, bairro, uf, cidade);
    }

    public void showTextViewsAsMandatory ( TextView... tvs )
    {
        for ( TextView tv : tvs )
        {
            String text = tv.getText ().toString ();

            tv.setText ( Html.fromHtml("<font color=\"#FF0000\">" + "* " + "</font>" + text));
        }
    }
}
