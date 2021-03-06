package app.wibeehomes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import app.wibeehomes.WooriBankAPI.WarFeeLoan;
import app.wibeehomes.WooriBankAPI.bisangLoan;
import app.wibeehomes.WooriBankAPI.WorkerLoan;

enum RENTTYPE {
    JEONSE(0), WOLSE(1);
    private final int value;
    private RENTTYPE(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

public class HomeConditionActivity extends AppCompatActivity {

    private static OkHttpClient client=new OkHttpClient().newBuilder().build();
    private static String OkhttpUrl="http://3.34.216.87:8080/Wibee_Server/androidDB.jsp";
    //private static String OkhttpUrl="http://192.168.1.34:8080/Wibee_Server/androidDB.jsp";
    private static MediaType mediaType= MediaType.parse("text/plain");

    private RadioGroup rg_lease;
    private RadioButton rb_lease_year,rb_lease_month;
    private RENTTYPE rentType = RENTTYPE.JEONSE;

    private TextView tv_homecondition_budget,tv_homecondition_monthly, tv_condition_loan;
    RangeSeekBar rangeSeekBar_budget,rangeSeekBar_monthly;
    LinearLayout layout_monthly;

    // Spinner ?????? ????????????
    private Spinner bigLocSpinner, smallLocSpinner;
    private ArrayAdapter bigAdapter, smallAdapter;
    private int bigLocal, smallLocal;

    private static JSONArray jsonArray=null;

    private ArrayList<ResidentialFacilities> residentialFacilities = new ArrayList<ResidentialFacilities>();

    private TextView submitButton;
    private Button btn_loan_info;//?????? ?????? ?????? ??????

    //?????? ??????,?????????
    int min_value_jeonse;
    int max_value_jeonse;
    //?????? ??????,?????????
    int min_value_wolse;
    int max_value_wolse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_condition);

        rg_lease=(RadioGroup) findViewById(R.id.rg_lease); //??? ?????? ??????
        rb_lease_year=(RadioButton) findViewById(R.id.rb_lease_year); //?????? ??????
        rb_lease_month=(RadioButton) findViewById(R.id.rb_lease_month); //?????? ??????
        tv_homecondition_budget=(TextView) findViewById(R.id.tv_homecondition_budget); //?????????(?????????) ?????? ?????? ????????????
        tv_homecondition_monthly=(TextView) findViewById(R.id.tv_homecondition_monthly); //?????? ?????? ?????? ????????????
        rangeSeekBar_budget=(RangeSeekBar)findViewById(R.id.rangeSeekbar);//?????????(?????????) ?????? ?????? ???
        rangeSeekBar_monthly=(RangeSeekBar)findViewById(R.id.rangeSeekbar_monthly); //?????? ?????? ?????? ???
        layout_monthly=(LinearLayout) findViewById(R.id.linearLayout2); //??????????????? ?????? ?????? ???????????? ????????????


        if (PreferenceManager.getBoolean(this, "isSetting") == true) {
            bigLocal = PreferenceManager.getInt(this, "bigLocalNum");
            Log.d("????????? ???", Integer.toString(bigLocal));
            smallLocal = PreferenceManager.getInt(this, "smallLocalNum");
            int rentNum = PreferenceManager.getInt(this, "rentType");

            // ?????????????? ?????? ??????????????? ??????
            if (rentNum == 0) {
                rg_lease.check(R.id.rb_lease_year);
                for(int n=0;n<layout_monthly.getChildCount();n++){
                    View view=layout_monthly.getChildAt(n);
                    view.setVisibility(View.GONE);
                }
            } else {
                rg_lease.check(R.id.rb_lease_month);
            }
        } else {
            bigLocal = 0;
            smallLocal = 0;
        }

        //------------------------------------------------------------------------------------------
        //?????????(?????????)
        rangeSeekBar_budget.setSelectedMaxValue(50000);
        rangeSeekBar_budget.setSelectedMinValue(0);
        rangeSeekBar_budget.setRangeValues(0,50000);  //0??????~50,000??????(5??????)
        //??????
        rangeSeekBar_monthly.setSelectedMaxValue(500);
        rangeSeekBar_monthly.setSelectedMinValue(0);
        rangeSeekBar_monthly.setRangeValues(0,500);  //0??????~500??????

        //????????? ????????? ????????????
        if(PreferenceManager.getBoolean(this,"isSetting")){
            //??????, ????????? ???????????????->?????????(?????????) setting
            int temp_min=PreferenceManager.getInt(this,"minMoneyYear");
            int temp_max=PreferenceManager.getInt(this,"maxMoneyYear");
            rangeSeekBar_budget.setSelectedMinValue(temp_min);
            rangeSeekBar_budget.setSelectedMaxValue(temp_max);

            String temp_year=moneyToString(temp_min)+"~"+moneyToString(temp_max);
            tv_homecondition_budget.setText(temp_year);

            Log.d("??????????",Boolean.toString(PreferenceManager.getBoolean(getApplicationContext(),"isSetting")));
            Log.d("????????????",Integer.toString(temp_min));

            //????????? ???????????????->?????? setting
            if(PreferenceManager.getInt(this, "rentType")==1){
                int temp_min_month=PreferenceManager.getInt(this,"minMoneyMonth");
                int temp_max_month=PreferenceManager.getInt(this,"maxMoneyMonth");
                rangeSeekBar_monthly.setSelectedMinValue(temp_min_month);
                rangeSeekBar_monthly.setSelectedMaxValue(temp_max_month);

                String temp_month=moneyToString(temp_min_month)+"~"+moneyToString(temp_max_month);
                tv_homecondition_monthly.setText(temp_month);
                Log.d("????????????",temp_year);
            }
        }

        final DecimalFormat formatter=new DecimalFormat("###,###");

        rg_lease.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_lease_year){//????????? ????????? ??????->?????? ??????X
                        for(int n=0;n<layout_monthly.getChildCount();n++){
                            View view=layout_monthly.getChildAt(n);
                            view.setVisibility(View.GONE);
                        }
                        rentType = RENTTYPE.JEONSE;
                }
                else if(i==R.id.rb_lease_month){//????????? ????????? ??????->?????? ??????O
                    for(int n=0;n<layout_monthly.getChildCount();n++){
                        View view=layout_monthly.getChildAt(n);
                        view.setVisibility(View.VISIBLE);
                    }
                    rentType = RENTTYPE.WOLSE;
                }
            }
        });

        //?????????(?????????)
        rangeSeekBar_budget.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value_jeonse_bar=bar.getSelectedMinValue();
                Number max_value_jeonse_bar=bar.getSelectedMaxValue();

                min_value_jeonse=(int)min_value_jeonse_bar;
                max_value_jeonse=(int)max_value_jeonse_bar;
                String min_budget,max_budget;

                min_budget=moneyToString(min_value_jeonse);

                max_budget=moneyToString(max_value_jeonse);

                tv_homecondition_budget.setText(min_budget+"~"+max_budget);
            }

        });

        //??????
        rangeSeekBar_monthly.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value_wolse_bar=bar.getSelectedMinValue();
                Number max_value_wolse_bar=bar.getSelectedMaxValue();

                min_value_wolse=(int)min_value_wolse_bar;
                max_value_wolse=(int)max_value_wolse_bar;
                String min_budget=formatter.format(min_value_wolse)+"??????";
                String max_budget=formatter.format(max_value_wolse)+"??????";

                tv_homecondition_monthly.setText(min_budget+"~"+max_budget);
            }
        });

        //------------------------------------------------------------------------------------------
        // Spinner
        bigLocSpinner = findViewById(R.id.condition_spinner_big_local);
        smallLocSpinner = findViewById(R.id.condition_spinner_small_local);

        bigAdapter = ArrayAdapter.createFromResource(this, R.array.big_location_array, R.layout.item_spinner);
        bigAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bigLocSpinner.setAdapter(bigAdapter);
        bigLocSpinner.setSelection(bigLocal);

        int arrayResId = getResources().getIdentifier("array_"+Integer.toString(bigLocal), "array", getApplicationContext().getPackageName());
        smallAdapter = ArrayAdapter.createFromResource(this, R.array.array_0, R.layout.item_spinner);
        smallAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);
        smallLocSpinner.setSelection(PreferenceManager.getInt(this, "smallLocalNum"));
        int smallSelected = smallLocSpinner.getSelectedItemPosition();
        Log.d("????????? ?????? ??????", Integer.toString(smallSelected));

        BigSpinnerAction();
        SmallSpinnerAction();

        //------------------------------------------------------------------------------------------
        // ?????? ?????? ?????? ?????????
        tv_condition_loan = findViewById(R.id.condition_tv_loan);
        if (PreferenceManager.getBoolean(this, "isSetting_loan") != true) {
            // ?????? ?????? ????????? ?????? ??????
            tv_condition_loan.setText(R.string.condition_none_loan);
        } else {
            if (rentType == RENTTYPE.JEONSE) {
                // ?????? ??????
                if (PreferenceManager.getBoolean(this, "isSetting_jeonse") != true) {
                    // ?????????????????? ????????? ?????? ??????
                    tv_condition_loan.setText(R.string.condition_add_jeonse);
                }
            }
            // ?????? ????????? ?????? ?????? - ??????
            tv_condition_loan.setText(R.string.condition_edit);
        }

        // ?????? ??????-????????? ?????? ???????????? ?????? ??????, ???????????? ????????? ?????????! ????????? ????????? ??? ??????
        submitButton = findViewById(R.id.condition_btn_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rb_lease_year.isChecked()&&!rb_lease_month.isChecked()) { //???????????? ?????? ?????? ??????
                    AlertDialog.Builder builder=new AlertDialog.Builder(HomeConditionActivity.this);
                    builder.setMessage("??????, ????????? ???????????? ?????????.");
                    builder.setPositiveButton("??????",null);
                    builder.create().show();
                }
                else if(rb_lease_year.isChecked() || rb_lease_month.isChecked()) { //???????????? ????????? ??????

                    // sharedPreference-----------------------------------------------------------------
                    // ?????? Boolean
                    PreferenceManager.setBoolean(getApplicationContext(), "isSetting", true);

                    // ????????? ?????? ??????
                    PreferenceManager.setInt(getApplicationContext(), "rentType", rentType.getValue());

                    // preference ???/??? ??????, ????????? ??????
                        Log.d("????????? ??? ??? ??????", Integer.toString(bigLocal));
                    String[] bigArray = getResources().getStringArray(R.array.big_location_array);
                    PreferenceManager.setString(getApplicationContext(), "bigLocal", bigArray[bigLocal]);
                    PreferenceManager.setInt(getApplicationContext(), "bigLocalNum", bigLocal);

                    // preference ???/???/??? ??????, ????????? ??????
                    Log.d("????????? ??? ?????? ??????", Integer.toString(smallLocal));
                    int resId = getResources().getIdentifier("array_"+bigLocal, "array", getApplicationContext().getPackageName());
                    String[] smallArray = getResources().getStringArray(resId);
                    PreferenceManager.setString(getApplicationContext(), "smallLocal", smallArray[smallLocal]);
                    PreferenceManager.setInt(getApplicationContext(), "smallLocalNum", smallLocal);

                    // preference ?????? ??????, ?????? ??????
                    PreferenceManager.setInt(getApplicationContext(), "minMoneyYear", min_value_jeonse);
                    PreferenceManager.setInt(getApplicationContext(), "maxMoneyYear", max_value_jeonse);

                    //??????
                    if(rentType==RENTTYPE.WOLSE){
                        // preference ?????? ??????, ?????? ??????
                        PreferenceManager.setInt(getApplicationContext(), "minMoneyMonth", (int)min_value_wolse);
                        PreferenceManager.setInt(getApplicationContext(), "maxMoneyMonth", (int)max_value_wolse);
                    }

                    // ?????? ????????? ------------------------------------------------------------------
                    String localCodeName = bigArray[bigLocal]+" "+smallArray[smallLocal]; // DTO ?????? ??????

                    ArrayList<CityCode> cityCodes = DTO.getCityArr();
                    for(int i =0; i <cityCodes.size();i++) {
                        if (cityCodes.get(i).getName().equals(localCodeName)) {
                            RequestBody body = null;
                            if(rb_lease_year.isChecked()){
                                body = new FormBody.Builder().add("localCode","a"+cityCodes.get(i).getCode()).build();
                            }
                            else {
                                body = new FormBody.Builder().add("localCode","b"+cityCodes.get(i).getCode()).build();
                            }

                            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();
                            // ????????? ????????? ?????? ????????????.
                            final CountDownLatch countDownLatch = new CountDownLatch(1);
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("?????? ??????", "error Connect Server error is" + e.toString());
                                    e.printStackTrace();
                                    countDownLatch.countDown();
                                }

                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String responseData = response.body().string();
                                            jsonArray = new JSONArray(responseData);
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        countDownLatch.countDown();
                                    }
                                }
                            });
                            try {
                                countDownLatch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    bisangLoan bisangloan = null;
                    WorkerLoan workerLoan = null;
                    WarFeeLoan warFeeLoan = null;
                    JSONObject home = null;
                    Place temp = null;
                    String str = "[{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":5.02,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"14500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":2.08,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"49871262699\",\"APV_AM\":7.86E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.5\",\"warfee\":\"5000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.5588953607\",\"pointy\":\"127.0010669694\",\"hyear\":\"2001\",\"addjibun\":\"193-134\",\"hfloor\":\"1\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":0.61,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":0.87,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"95450909914\",\"APV_AM\":9.24E7},\"home\":{\"renfee\":\"0\",\"harea\":\"33.27\",\"warfee\":\"8500\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607616797\",\"pointy\":\"127.0080530162\",\"hyear\":\"1996\",\"addjibun\":\"48-11\",\"hfloor\":\"1\",\"hname\":\"(48-11)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":9.95,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"30500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":4.18,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"52021308486\",\"APV_AM\":2.98E7},\"home\":{\"renfee\":\"0\",\"harea\":\"29.22\",\"warfee\":\"8500\",\"adddong\":\"?????????\",\"pointx\":\"37.5508096308\",\"pointy\":\"127.0052459036\",\"hyear\":\"2000\",\"addjibun\":\"432-1974\",\"hfloor\":\"3\",\"hname\":\"????????????A???\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":6.02,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"06500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":5.45,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"09346432501\",\"APV_AM\":4.97E7},\"home\":{\"renfee\":\"0\",\"harea\":\"33.27\",\"warfee\":\"8500\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607616797\",\"pointy\":\"127.0080530162\",\"hyear\":\"1996\",\"addjibun\":\"48-11\",\"hfloor\":\"1\",\"hname\":\"(48-11)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.1,\"APL_ADN_IR\":8.88,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"90500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":2.27,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"13989772276\",\"APV_AM\":8.48E7},\"home\":{\"renfee\":\"0\",\"harea\":\"22.75\",\"warfee\":\"9450\",\"adddong\":\"?????????\",\"pointx\":\"37.5513559223\",\"pointy\":\"127.0077024894\",\"hyear\":\"2004\",\"addjibun\":\"353-22\",\"hfloor\":\"2\",\"hname\":\"JK????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.26,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"17500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":7.67,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"23960172428\",\"APV_AM\":2.08E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.05\",\"warfee\":\"10000\",\"adddong\":\"?????????\",\"pointx\":\"37.5673188853\",\"pointy\":\"127.0232988759\",\"hyear\":\"2013\",\"addjibun\":\"787\",\"hfloor\":\"10\",\"hname\":\"?????? I-Class\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":4.03,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2050000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"84500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":3.65,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"91489310427\",\"APV_AM\":4600000.0},\"home\":{\"renfee\":\"0\",\"harea\":\"13.14\",\"warfee\":\"10000\",\"adddong\":\"?????????\",\"pointx\":\"37.5637706357\",\"pointy\":\"127.0216505618\",\"hyear\":\"2013\",\"addjibun\":\"154-46\",\"hfloor\":\"4\",\"hname\":\"????????????2???\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":2.66,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"47500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":0.65,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"47644506665\",\"APV_AM\":3.63E7},\"home\":{\"renfee\":\"0\",\"harea\":\"38.53\",\"warfee\":\"12000\",\"adddong\":\"?????????\",\"pointx\":\"37.5505620944\",\"pointy\":\"127.0057340088\",\"hyear\":\"2001\",\"addjibun\":\"432-2000\",\"hfloor\":\"3\",\"hname\":\"???????????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":1.71,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"90500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":5.36,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"69667524854\",\"APV_AM\":2.79E7},\"home\":{\"renfee\":\"0\",\"harea\":\"21.78\",\"warfee\":\"12600\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607530235\",\"pointy\":\"127.0072273414\",\"hyear\":\"2009\",\"addjibun\":\"52-10\",\"hfloor\":\"5\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":0.16,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0750000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"98500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":0.83,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"31451403895\",\"APV_AM\":4.51E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.05\",\"warfee\":\"13000\",\"adddong\":\"?????????\",\"pointx\":\"37.5673188853\",\"pointy\":\"127.0232988759\",\"hyear\":\"2013\",\"addjibun\":\"787\",\"hfloor\":\"6\",\"hname\":\"?????? I-Class\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":4.06,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0750000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"69500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":2.72,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"27470585469\",\"APV_AM\":6.7E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.23\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"6\",\"hname\":\"?????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":9.54,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9150000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":8.34,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"32458337641\",\"APV_AM\":9.03E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.26\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"9\",\"hname\":\"?????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.07,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"36500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":6.23,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"77287543731\",\"APV_AM\":6.28E7},\"home\":{\"renfee\":\"0\",\"harea\":\"56.58\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5513644149\",\"pointy\":\"127.0047247441\",\"hyear\":\"1998\",\"addjibun\":\"432-1926\",\"hfloor\":\"3\",\"hname\":\"????????????(2???)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":8.18,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2050000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"27500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.99,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"74642404061\",\"APV_AM\":8.3E7},\"home\":{\"renfee\":\"0\",\"harea\":\"57.75\",\"warfee\":\"14700\",\"adddong\":\"?????????\",\"pointx\":\"37.550509448\",\"pointy\":\"127.0050508005\",\"hyear\":\"2000\",\"addjibun\":\"432-567\",\"hfloor\":\"2\",\"hname\":\"?????????????????????(432-567)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":3.65,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"96500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":6.28,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"73842330468\",\"APV_AM\":3.85E7},\"home\":{\"renfee\":\"0\",\"harea\":\"54.85\",\"warfee\":\"15000\",\"adddong\":\"?????????\",\"pointx\":\"37.5574729841\",\"pointy\":\"126.9636257681\",\"hyear\":\"1988\",\"addjibun\":\"392-4\",\"hfloor\":\"1\",\"hname\":\"(392-4)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":9.11,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"98500000\"},\"loan2\":{\"LON_DCS_IR\":0.1,\"APL_ADN_IR\":9.03,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"15649517965\",\"APV_AM\":7900000.0},\"home\":{\"renfee\":\"0\",\"harea\":\"59.57\",\"warfee\":\"17000\",\"adddong\":\"?????????\",\"pointx\":\"37.5512926775\",\"pointy\":\"127.0066109558\",\"hyear\":\"2000\",\"addjibun\":\"432-871\",\"hfloor\":\"1\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.66,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"7650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"02500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":8.22,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"89952440461\",\"APV_AM\":9.34E7},\"home\":{\"renfee\":\"0\",\"harea\":\"47.54\",\"warfee\":\"17850\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5611432802\",\"pointy\":\"126.9796217348\",\"hyear\":\"1975\",\"addjibun\":\"146-1\",\"hfloor\":\"7\",\"hname\":\"??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":9.06,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"44500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":9.36,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"49340547671\",\"APV_AM\":9.18E7},\"home\":{\"renfee\":\"0\",\"harea\":\"14.2664\",\"warfee\":\"18000\",\"adddong\":\"??????\",\"pointx\":\"37.682102206\",\"pointy\":\"129.0449826301\",\"hyear\":\"2020\",\"addjibun\":\"37\",\"hfloor\":\"7\",\"hname\":\"?????? ??????????????? ??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":7.84,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"7550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"24500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.12,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"60501622444\",\"APV_AM\":8.49E7},\"home\":{\"renfee\":\"0\",\"harea\":\"21.3\",\"warfee\":\"18000\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"9\",\"hname\":\"?????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.3,\"APL_ADN_IR\":3.07,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"04500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":5.79,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"87525423329\",\"APV_AM\":1.36E7},\"home\":{\"renfee\":\"0\",\"harea\":\"35.32\",\"warfee\":\"18900\",\"adddong\":\"?????????\",\"pointx\":\"37.5625446403\",\"pointy\":\"127.0125174485\",\"hyear\":\"2008\",\"addjibun\":\"236-44\",\"hfloor\":\"4\",\"hname\":\"(236-44)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":3.09,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6150000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"45500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":6.92,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"86754011678\",\"APV_AM\":3.83E7},\"home\":{\"renfee\":\"0\",\"harea\":\"35.32\",\"warfee\":\"18900\",\"adddong\":\"?????????\",\"pointx\":\"37.5625446403\",\"pointy\":\"127.0125174485\",\"hyear\":\"2008\",\"addjibun\":\"236-44\",\"hfloor\":\"4\",\"hname\":\"(236-44)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":5.49,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"25500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":4.99,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"76494012546\",\"APV_AM\":5.53E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.57\",\"warfee\":\"19000\",\"adddong\":\"?????????\",\"pointx\":\"37.5622810967\",\"pointy\":\"126.9995967863\",\"hyear\":\"2021\",\"addjibun\":\"32-5\",\"hfloor\":\"8\",\"hname\":\"???????????? ????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":1.54,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"18500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":1.78,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"50353637094\",\"APV_AM\":8.0E7},\"home\":{\"renfee\":\"0\",\"harea\":\"14.2664\",\"warfee\":\"19500\",\"adddong\":\"??????\",\"pointx\":\"37.682102206\",\"pointy\":\"129.0449826301\",\"hyear\":\"2020\",\"addjibun\":\"37\",\"hfloor\":\"5\",\"hname\":\"?????? ??????????????? ??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":5.44,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"1950000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"36500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":4.95,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"57871201735\",\"APV_AM\":7.28E7},\"home\":{\"renfee\":\"0\",\"harea\":\"25.4\",\"warfee\":\"19500\",\"adddong\":\"?????????\",\"pointx\":\"37.5563283349\",\"pointy\":\"127.0144273952\",\"hyear\":\"2015\",\"addjibun\":\"333-128\",\"hfloor\":\"4\",\"hname\":\"??????????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":3.09,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"65500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":9.04,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"17911203089\",\"APV_AM\":6.71E7},\"home\":{\"renfee\":\"0\",\"harea\":\"49.88\",\"warfee\":\"20000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.563792331\",\"pointy\":\"126.9958020009\",\"hyear\":\"1968\",\"addjibun\":\"192-30\",\"hfloor\":\"8\",\"hname\":\"????????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":7.33,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"12500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":5.42,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"61717574046\",\"APV_AM\":6.43E7},\"home\":{\"renfee\":\"0\",\"harea\":\"38.19\",\"warfee\":\"20000\",\"adddong\":\"?????????\",\"pointx\":\"37.560270817\",\"pointy\":\"126.9665347457\",\"hyear\":\"2013\",\"addjibun\":\"498\",\"hfloor\":\"2\",\"hname\":\"???????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":9.03,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"5950000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"96500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.58,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"27925309457\",\"APV_AM\":5.52E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.35\",\"warfee\":\"20000\",\"adddong\":\"??????2???\",\"pointx\":\"37.5650853367\",\"pointy\":\"126.9903463986\",\"hyear\":\"2018\",\"addjibun\":\"7-2\",\"hfloor\":\"11\",\"hname\":\"??????????????? ??????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":1.98,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":5.04,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"19644309797\",\"APV_AM\":3.69E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.57\",\"warfee\":\"20000\",\"adddong\":\"?????????\",\"pointx\":\"37.5622810967\",\"pointy\":\"126.9995967863\",\"hyear\":\"2021\",\"addjibun\":\"32-5\",\"hfloor\":\"9\",\"hname\":\"???????????? ????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":1.3,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"61500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":2.57,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"63715048854\",\"APV_AM\":8.41E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.06\",\"warfee\":\"21000\",\"adddong\":\"?????????\",\"pointx\":\"35.8515676998\",\"pointy\":\"128.4840036764\",\"hyear\":\"2021\",\"addjibun\":\"367-15\",\"hfloor\":\"3\",\"hname\":\"????????? ?????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":6.57,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"5350000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"22500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":3.33,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"12146632451\",\"APV_AM\":8.42E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.59\",\"warfee\":\"21000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.5630425387\",\"pointy\":\"126.997256044\",\"hyear\":\"2007\",\"addjibun\":\"48-2\",\"hfloor\":\"12\",\"hname\":\"?????? ????????????????????????\",\"hcate\":\"1\"}}]\n" +
                            "[{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":5.02,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"14500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":2.08,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"49871262699\",\"APV_AM\":7.86E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.5\",\"warfee\":\"5000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.5588953607\",\"pointy\":\"127.0010669694\",\"hyear\":\"2001\",\"addjibun\":\"193-134\",\"hfloor\":\"1\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":0.61,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":0.87,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"95450909914\",\"APV_AM\":9.24E7},\"home\":{\"renfee\":\"0\",\"harea\":\"33.27\",\"warfee\":\"8500\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607616797\",\"pointy\":\"127.0080530162\",\"hyear\":\"1996\",\"addjibun\":\"48-11\",\"hfloor\":\"1\",\"hname\":\"(48-11)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":9.95,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"30500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":4.18,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"52021308486\",\"APV_AM\":2.98E7},\"home\":{\"renfee\":\"0\",\"harea\":\"29.22\",\"warfee\":\"8500\",\"adddong\":\"?????????\",\"pointx\":\"37.5508096308\",\"pointy\":\"127.0052459036\",\"hyear\":\"2000\",\"addjibun\":\"432-1974\",\"hfloor\":\"3\",\"hname\":\"????????????A???\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":6.02,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"06500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":5.45,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"09346432501\",\"APV_AM\":4.97E7},\"home\":{\"renfee\":\"0\",\"harea\":\"33.27\",\"warfee\":\"8500\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607616797\",\"pointy\":\"127.0080530162\",\"hyear\":\"1996\",\"addjibun\":\"48-11\",\"hfloor\":\"1\",\"hname\":\"(48-11)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.1,\"APL_ADN_IR\":8.88,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"90500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":2.27,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"13989772276\",\"APV_AM\":8.48E7},\"home\":{\"renfee\":\"0\",\"harea\":\"22.75\",\"warfee\":\"9450\",\"adddong\":\"?????????\",\"pointx\":\"37.5513559223\",\"pointy\":\"127.0077024894\",\"hyear\":\"2004\",\"addjibun\":\"353-22\",\"hfloor\":\"2\",\"hname\":\"JK????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.26,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"17500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":7.67,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"23960172428\",\"APV_AM\":2.08E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.05\",\"warfee\":\"10000\",\"adddong\":\"?????????\",\"pointx\":\"37.5673188853\",\"pointy\":\"127.0232988759\",\"hyear\":\"2013\",\"addjibun\":\"787\",\"hfloor\":\"10\",\"hname\":\"?????? I-Class\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":4.03,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2050000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"84500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":3.65,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"91489310427\",\"APV_AM\":4600000.0},\"home\":{\"renfee\":\"0\",\"harea\":\"13.14\",\"warfee\":\"10000\",\"adddong\":\"?????????\",\"pointx\":\"37.5637706357\",\"pointy\":\"127.0216505618\",\"hyear\":\"2013\",\"addjibun\":\"154-46\",\"hfloor\":\"4\",\"hname\":\"????????????2???\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":2.66,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"47500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":0.65,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"47644506665\",\"APV_AM\":3.63E7},\"home\":{\"renfee\":\"0\",\"harea\":\"38.53\",\"warfee\":\"12000\",\"adddong\":\"?????????\",\"pointx\":\"37.5505620944\",\"pointy\":\"127.0057340088\",\"hyear\":\"2001\",\"addjibun\":\"432-2000\",\"hfloor\":\"3\",\"hname\":\"???????????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":1.71,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"90500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":5.36,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"69667524854\",\"APV_AM\":2.79E7},\"home\":{\"renfee\":\"0\",\"harea\":\"21.78\",\"warfee\":\"12600\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5607530235\",\"pointy\":\"127.0072273414\",\"hyear\":\"2009\",\"addjibun\":\"52-10\",\"hfloor\":\"5\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":0.16,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0750000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"98500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":0.83,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"31451403895\",\"APV_AM\":4.51E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.05\",\"warfee\":\"13000\",\"adddong\":\"?????????\",\"pointx\":\"37.5673188853\",\"pointy\":\"127.0232988759\",\"hyear\":\"2013\",\"addjibun\":\"787\",\"hfloor\":\"6\",\"hname\":\"?????? I-Class\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":4.06,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0750000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"69500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":2.72,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"27470585469\",\"APV_AM\":6.7E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.23\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"6\",\"hname\":\"?????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":9.54,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9150000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":8.34,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"32458337641\",\"APV_AM\":9.03E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.26\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"9\",\"hname\":\"?????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.07,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"36500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":6.23,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"77287543731\",\"APV_AM\":6.28E7},\"home\":{\"renfee\":\"0\",\"harea\":\"56.58\",\"warfee\":\"13500\",\"adddong\":\"?????????\",\"pointx\":\"37.5513644149\",\"pointy\":\"127.0047247441\",\"hyear\":\"1998\",\"addjibun\":\"432-1926\",\"hfloor\":\"3\",\"hname\":\"????????????(2???)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":8.18,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2050000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"27500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.99,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"74642404061\",\"APV_AM\":8.3E7},\"home\":{\"renfee\":\"0\",\"harea\":\"57.75\",\"warfee\":\"14700\",\"adddong\":\"?????????\",\"pointx\":\"37.550509448\",\"pointy\":\"127.0050508005\",\"hyear\":\"2000\",\"addjibun\":\"432-567\",\"hfloor\":\"2\",\"hname\":\"?????????????????????(432-567)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":3.65,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"96500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":6.28,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"73842330468\",\"APV_AM\":3.85E7},\"home\":{\"renfee\":\"0\",\"harea\":\"54.85\",\"warfee\":\"15000\",\"adddong\":\"?????????\",\"pointx\":\"37.5574729841\",\"pointy\":\"126.9636257681\",\"hyear\":\"1988\",\"addjibun\":\"392-4\",\"hfloor\":\"1\",\"hname\":\"(392-4)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":9.11,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"98500000\"},\"loan2\":{\"LON_DCS_IR\":0.1,\"APL_ADN_IR\":9.03,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"15649517965\",\"APV_AM\":7900000.0},\"home\":{\"renfee\":\"0\",\"harea\":\"59.57\",\"warfee\":\"17000\",\"adddong\":\"?????????\",\"pointx\":\"37.5512926775\",\"pointy\":\"127.0066109558\",\"hyear\":\"2000\",\"addjibun\":\"432-871\",\"hfloor\":\"1\",\"hname\":\"????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.66,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"7650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"02500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":8.22,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"89952440461\",\"APV_AM\":9.34E7},\"home\":{\"renfee\":\"0\",\"harea\":\"47.54\",\"warfee\":\"17850\",\"adddong\":\"?????????1???\",\"pointx\":\"37.5611432802\",\"pointy\":\"126.9796217348\",\"hyear\":\"1975\",\"addjibun\":\"146-1\",\"hfloor\":\"7\",\"hname\":\"??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":9.06,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9850000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"44500000\"},\"loan2\":{\"LON_DCS_IR\":0.0,\"APL_ADN_IR\":9.36,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"49340547671\",\"APV_AM\":9.18E7},\"home\":{\"renfee\":\"0\",\"harea\":\"14.2664\",\"warfee\":\"18000\",\"adddong\":\"??????\",\"pointx\":\"37.682102206\",\"pointy\":\"129.0449826301\",\"hyear\":\"2020\",\"addjibun\":\"37\",\"hfloor\":\"7\",\"hname\":\"?????? ??????????????? ??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":7.84,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"7550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"24500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":0.12,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"60501622444\",\"APV_AM\":8.49E7},\"home\":{\"renfee\":\"0\",\"harea\":\"21.3\",\"warfee\":\"18000\",\"adddong\":\"?????????\",\"pointx\":\"37.5662903106\",\"pointy\":\"127.0228506915\",\"hyear\":\"2014\",\"addjibun\":\"836\",\"hfloor\":\"9\",\"hname\":\"?????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.3,\"APL_ADN_IR\":3.07,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"04500000\"},\"loan2\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":5.79,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"87525423329\",\"APV_AM\":1.36E7},\"home\":{\"renfee\":\"0\",\"harea\":\"35.32\",\"warfee\":\"18900\",\"adddong\":\"?????????\",\"pointx\":\"37.5625446403\",\"pointy\":\"127.0125174485\",\"hyear\":\"2008\",\"addjibun\":\"236-44\",\"hfloor\":\"4\",\"hname\":\"(236-44)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":3.09,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"6150000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"45500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":6.92,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"86754011678\",\"APV_AM\":3.83E7},\"home\":{\"renfee\":\"0\",\"harea\":\"35.32\",\"warfee\":\"18900\",\"adddong\":\"?????????\",\"pointx\":\"37.5625446403\",\"pointy\":\"127.0125174485\",\"hyear\":\"2008\",\"addjibun\":\"236-44\",\"hfloor\":\"4\",\"hname\":\"(236-44)\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":5.49,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"3650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"25500000\"},\"loan2\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":4.99,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"76494012546\",\"APV_AM\":5.53E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.57\",\"warfee\":\"19000\",\"adddong\":\"?????????\",\"pointx\":\"37.5622810967\",\"pointy\":\"126.9995967863\",\"hyear\":\"2021\",\"addjibun\":\"32-5\",\"hfloor\":\"8\",\"hname\":\"???????????? ????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.7,\"APL_ADN_IR\":1.54,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"4450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"18500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":1.78,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"50353637094\",\"APV_AM\":8.0E7},\"home\":{\"renfee\":\"0\",\"harea\":\"14.2664\",\"warfee\":\"19500\",\"adddong\":\"??????\",\"pointx\":\"37.682102206\",\"pointy\":\"129.0449826301\",\"hyear\":\"2020\",\"addjibun\":\"37\",\"hfloor\":\"5\",\"hname\":\"?????? ??????????????? ??????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":5.44,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"1950000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"36500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":4.95,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"57871201735\",\"APV_AM\":7.28E7},\"home\":{\"renfee\":\"0\",\"harea\":\"25.4\",\"warfee\":\"19500\",\"adddong\":\"?????????\",\"pointx\":\"37.5563283349\",\"pointy\":\"127.0144273952\",\"hyear\":\"2015\",\"addjibun\":\"333-128\",\"hfloor\":\"4\",\"hname\":\"??????????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.8,\"APL_ADN_IR\":3.09,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"9650000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"65500000\"},\"loan2\":{\"LON_DCS_IR\":0.4,\"APL_ADN_IR\":9.04,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"17911203089\",\"APV_AM\":6.71E7},\"home\":{\"renfee\":\"0\",\"harea\":\"49.88\",\"warfee\":\"20000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.563792331\",\"pointy\":\"126.9958020009\",\"hyear\":\"1968\",\"addjibun\":\"192-30\",\"hfloor\":\"8\",\"hname\":\"????????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.2,\"APL_ADN_IR\":7.33,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2550000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"12500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":5.42,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"61717574046\",\"APV_AM\":6.43E7},\"home\":{\"renfee\":\"0\",\"harea\":\"38.19\",\"warfee\":\"20000\",\"adddong\":\"?????????\",\"pointx\":\"37.560270817\",\"pointy\":\"126.9665347457\",\"hyear\":\"2013\",\"addjibun\":\"498\",\"hfloor\":\"2\",\"hname\":\"???????????????\",\"hcate\":\"2\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":9.03,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"5950000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"96500000\"},\"loan2\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":4.58,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"27925309457\",\"APV_AM\":5.52E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.35\",\"warfee\":\"20000\",\"adddong\":\"??????2???\",\"pointx\":\"37.5650853367\",\"pointy\":\"126.9903463986\",\"hyear\":\"2018\",\"addjibun\":\"7-2\",\"hfloor\":\"11\",\"hname\":\"??????????????? ??????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":1.98,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"0450000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"88500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":5.04,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"19644309797\",\"APV_AM\":3.69E7},\"home\":{\"renfee\":\"0\",\"harea\":\"18.57\",\"warfee\":\"20000\",\"adddong\":\"?????????\",\"pointx\":\"37.5622810967\",\"pointy\":\"126.9995967863\",\"hyear\":\"2021\",\"addjibun\":\"32-5\",\"hfloor\":\"9\",\"hname\":\"???????????? ????????????\",\"hcate\":\"1\"}},{\"loan1\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":1.3,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"2250000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"61500000\"},\"loan2\":{\"LON_DCS_IR\":0.9,\"APL_ADN_IR\":2.57,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"63715048854\",\"APV_AM\":8.41E7},\"home\":{\"renfee\":\"0\",\"harea\":\"19.06\",\"warfee\":\"21000\",\"adddong\":\"?????????\",\"pointx\":\"35.8515676998\",\"pointy\":\"128.4840036764\",\"hyear\":\"2021\",\"addjibun\":\"367-15\",\"hfloor\":\"3\",\"hname\":\"????????? ?????????\",\"hcate\":\"0\"}},{\"loan1\":{\"LON_DCS_IR\":0.6,\"APL_ADN_IR\":6.57,\"PRC_STCD\":\"BA04\",\"TGT_CUS_PRME_IR\":\"0\",\"WOORI_PBOK_HLDG_PRME_IR\":\"0\",\"APV_AM\":3000000.0},\"loan3\":{\"FRCS_AVL_GRN_RT\":\"0.01\",\"FRCS_AVL_GRFE_RT\":\"0.01\",\"FRCS_AVL_GDOC_AM\":\"5350000\",\"RSP_ERR_TXT\":\"\",\"RSP_RTCD\":\"\",\"FRCS_AVL_GRFE\":\"0\",\"FRCS_AVL_LN_AM\":\"22500000\"},\"loan2\":{\"LON_DCS_IR\":0.5,\"APL_ADN_IR\":3.33,\"PRC_STCD\":\"BA04\",\"LNAPV_RQ_NO\":\"12146632451\",\"APV_AM\":8.42E7},\"home\":{\"renfee\":\"0\",\"harea\":\"17.59\",\"warfee\":\"21000\",\"adddong\":\"?????????2???\",\"pointx\":\"37.5630425387\",\"pointy\":\"126.997256044\",\"hyear\":\"2007\",\"addjibun\":\"48-2\",\"hfloor\":\"12\",\"hname\":\"?????? ????????????????????????\",\"hcate\":\"1\"}}]" ;
                    JSONObject objt =null;
                    try {
                        jsonArray = new JSONArray(str);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i =0; i < jsonArray.length();i++){
                        try {
                            objt = (JSONObject)jsonArray.get(i);
                        } catch (JSONException e) {
                        }
                        try {
                            JSONObject loan1 =  objt.getJSONObject("loan1");
                            bisangloan = new bisangLoan( Double.toString(loan1.getDouble("LON_DCS_IR")),
                                    Double.toString(loan1.getDouble("APL_ADN_IR")),
                                    String.valueOf(loan1.get("TGT_CUS_PRME_IR")),String.valueOf(loan1.get("WOORI_PBOK_HLDG_PRME_IR")),
                                    Double.toString(loan1.getDouble("APV_AM")),String.valueOf(loan1.get("PRC_STCD")));

                            JSONObject loan2 = objt.getJSONObject("loan2");
                            workerLoan = new WorkerLoan(Double.toString(loan2.getDouble("LON_DCS_IR")),
                                    Double.toString(loan2.getDouble("APL_ADN_IR")),
                                    String.valueOf(loan2.get("PRC_STCD")),
                                    String.valueOf(loan2.get("LNAPV_RQ_NO")),
                                    Double.toString(loan2.getDouble("APV_AM")));

                            home = objt.getJSONObject("home");
                            String address = String.valueOf(home.get("adddong"))+String.valueOf(home.get("addjibun"));
                            temp = new Place(String.valueOf(home.get("hname")),address,Double.parseDouble(String.valueOf(home.get("pointx"))),
                                    Double.parseDouble(String.valueOf(home.get("pointy"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(rb_lease_year.isChecked()){
                            try {
                                JSONObject loan3 = objt.getJSONObject("loan3");
                                warFeeLoan = new WarFeeLoan(String.valueOf(loan3.get("RSP_RTCD")),String.valueOf(loan3.get("RSP_ERR_TXT")),
                                                String.valueOf(loan3.get("FRCS_AVL_LN_AM"))
                                        ,String.valueOf(loan3.get("FRCS_AVL_GDOC_AM"))
                                ,String.valueOf(loan3.get("FRCS_AVL_GRN_RT"))
                                ,String.valueOf(loan3.get("FRCS_AVL_GRFE_RT")),
                                                String.valueOf(loan3.get("FRCS_AVL_GRFE")));

                                residentialFacilities.add(new ResidentialFacilities(temp,Integer.parseInt(String.valueOf(home.get("hyear"))),
                                        Integer.parseInt(String.valueOf(home.get("hfloor"))),
                                        Double.parseDouble(String.valueOf(home.get("harea"))),
                                        Integer.parseInt(String.valueOf(home.get("hcate"))),String.valueOf(home.get("addjibun")),
                                        String.valueOf(home.get("warfee")),String.valueOf(home.get("renfee")),warFeeLoan,bisangloan,workerLoan));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            try {
                                residentialFacilities.add(new ResidentialFacilities(temp,Integer.parseInt(String.valueOf(home.get("hyear"))),
                                        Integer.parseInt(String.valueOf(home.get("hfloor"))),
                                        Double.parseDouble(String.valueOf(home.get("harea"))),
                                        Integer.parseInt(String.valueOf(home.get("hcate"))),String.valueOf(home.get("addjibun")),
                                        String.valueOf(home.get("warfee")),String.valueOf(home.get("renfee")),warFeeLoan,bisangloan,workerLoan));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // ????????? ?????? ?????? ????????? ????????? HomeActivity??? ??????
                    Intent homeIntent = new Intent(HomeConditionActivity.this, HomeActivity.class);
                    homeIntent.putExtra("homeList", residentialFacilities);
                    setResult(RESULT_OK,homeIntent);
                    finish();
                    //startActivity(homeIntent);
                }
            }
        });

        //?????? ?????? ?????? ??????

        btn_loan_info=(Button) findViewById(R.id.btn_homecondition_search);
        btn_loan_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? ?????? ????????? ????????? ????????? LoanDetailActivity??? ??????
                if(!rb_lease_year.isChecked()&&!rb_lease_month.isChecked()){//???????????? ?????? ?????? ??????
                    AlertDialog.Builder builder=new AlertDialog.Builder(HomeConditionActivity.this);
                    builder.setMessage("??????, ????????? ???????????? ?????????.");
                    builder.setPositiveButton("??????",null);
                    builder.create().show();
                }
                else if(rb_lease_year.isChecked() || rb_lease_month.isChecked()) {//???????????? ????????? ??????
                    Intent loanIntent = new Intent(HomeConditionActivity.this, LoanDetailActivity.class);
                    loanIntent.putExtra("con_rent_type", rentType);

                    startActivity(loanIntent);
                }
            }
        });
    }

    private void BigSpinnerAction(){
        bigLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String index = Integer.toString(i);
                int resId = getResources().getIdentifier("array_"+index, "array", getApplicationContext().getPackageName());
                smallAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resId, R.layout.item_spinner);

                bigLocal = i;
                Log.d("??? ?????? ?????? : ", Integer.toString(i));

                smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                smallLocSpinner.setAdapter(smallAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bigLocal = 0;
            }
        });
    }

    private void SmallSpinnerAction(){
        smallLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                smallLocal = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                smallLocal = 0;
            }
        });
    }

    public String moneyToString(int money){
        String moneystring="";
        final DecimalFormat formatter=new DecimalFormat("###,###");
        if(money>=10000){//1??? ??????
            if(money%10000==0){//?????????
                moneystring=Integer.toString(money/10000)+"??????";
            }
            else{
                moneystring=Integer.toString(money/10000)+"???"+formatter.format(money%10000)+"??????";
            }
        }
        else{//1??? ??????
            moneystring=formatter.format(money)+"??????";
        }
        return moneystring;
    }
}