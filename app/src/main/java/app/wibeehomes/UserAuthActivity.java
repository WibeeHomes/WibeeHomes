package app.wibeehomes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UserAuthActivity extends AppCompatActivity {

    private EditText et_name,et_birthfront,et_birthback,et_phonenum;
    private CheckBox check;
    private Button submit;
    int textlength=0;
    ArrayList<String> encoding_info=new ArrayList<String>();
    String encodedname;//암호화실명번호
    private Boolean available_worker_loan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        et_name=findViewById(R.id.auth_et_name);
        et_birthfront=findViewById(R.id.auth_et_birth_front);
        et_birthback=findViewById(R.id.auth_et_birth_back);
        et_phonenum=findViewById(R.id.auth_et_phone);
        check=findViewById(R.id.auth_checkbox_agree);
        submit=findViewById(R.id.auth_btn_submit);


        //고객한글명-한글만 입력하도록
        InputFilter[] filter_kor=new InputFilter[]{filterKor};
        et_name.setFilters(filter_kor);

        //password
        final PasswordTransformationMethod pass=new PasswordTransformationMethod();
        et_birthback.setTransformationMethod(pass);


        //사용자 인증하기 버튼 클릭
        final AlertDialog.Builder builder=new AlertDialog.Builder(UserAuthActivity.this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().length()==0){//이름을 입력 안한 경우
                    builder.setMessage("이름을 입력해야 합니다.");
                    builder.setPositiveButton("확인",null);
                    builder.create().show();
                }else{//이름을 입력한 경우
                    if(!et_name.getText().toString().equals(PreferenceManager.getString(getApplicationContext(),"name"))){
                        Log.d("pref name : ",PreferenceManager.getString(getApplicationContext(),"name"));
                        //대출정보조회에서 입력된 이름과 다른 경우
                        builder.setMessage("대출 정보에 입력된 이름과 일치하지 않습니다.");
                        builder.setPositiveButton("확인",null);
                        builder.create().show();
                    }
                    else{
                    if((et_birthfront.getText().toString().length()!=6)||(et_birthback.getText().toString().length()!=7)){//주민번호가 올바르게 입력되지 않은 경우
                        if(et_birthfront.getText().toString().length()==0||et_birthback.getText().toString().length()==0){
                            builder.setMessage("주민번호를 입력해야 합니다.");
                            builder.setPositiveButton("확인",null);
                            builder.create().show();
                        }else{
                            builder.setMessage("주민번호가 잘못되었습니다.");
                            builder.setPositiveButton("확인",null);
                            builder.create().show();
                        }
                    }else{//주민번호를 제대로 입력한 경우
                        if(et_phonenum.getText().toString().length()!=11){//핸드폰 번호가 잘못된 경우
                            if(et_phonenum.getText().toString().length()==0){
                                builder.setMessage("전화번호를 입력해야 합니다.");
                                builder.setPositiveButton("확인",null);
                                builder.create().show();
                            }
                            else{
                                builder.setMessage("전화번호가 잘못되었습니다.");
                                builder.setPositiveButton("확인",null);
                                builder.create().show();
                            }
                        }
                        else{//핸드폰 번호를 제대로 입력한 경우
                            if(!check.isChecked()){//동의를 안한 경우
                                builder.setMessage("개인정보사용에 동의해야 합니다.");
                                builder.setPositiveButton("확인",null);
                                builder.create().show();
                            }
                            else{//동의를 한 경우-모든게 완벽
                                encoding_info.add(et_name.getText().toString());//암호화 대상
                                encoding_info.add("98OLhiTmtTwA7OMoPy45U8Bujuzpr0yz");//시크릿 키
                                try {
                                    encodedname=getAES256EncStr(encoding_info);
                                    Log.d("암호화 테스트 ; ",encodedname);

                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (InvalidAlgorithmParameterException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                Intent homeIntent=new Intent(UserAuthActivity.this,HomeActivity.class);


                                available_worker_loan=getIntent().getBooleanExtra("available_worker_loan",true);

                                Log.d("직장인 대출 가능 여부-loan : ",Boolean.toString(available_worker_loan));

                                startActivity(homeIntent);
                            }
                        }
                    }
                    }
                }
            }
        });
    }
    //-------------------------------------------------------------------
    //인풋 필터-한글만 입력
    public InputFilter filterKor = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-흐]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    //----------------------------------------------------------------------
    //암호화
    public static String getAES256EncStr(List<String> params)throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        if(params==null||params.size()<2||params.get(0)==null||params.get(1)==null){
            return null;
        }
        String str=params.get(0).toString();//암호화 대상
        String key=params.get(1).toString();//시크릿 키
        String iv="0000000000000000";

        Key keySpec;
        byte[] keyBytes=new byte[key.length()];
        byte[] b=key.getBytes("UTF-8");

        int len=b.length;

        if(len>keyBytes.length){
            len=keyBytes.length;
        }
        System.arraycopy(b,0,keyBytes,0,len);

        keySpec=new SecretKeySpec(keyBytes,"AES");

        Cipher c=Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE,keySpec,new IvParameterSpec(iv.getBytes()));

        byte[] encrypted=c.doFinal(str.getBytes("UTF-8"));

        String encStr=new String(Base64.encodeToString(encrypted,0));
        return encStr;
    }



}