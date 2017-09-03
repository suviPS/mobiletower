package tk.ksfdev.mobiletower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tk.ksfdev.mobiletower.data.MobileTower;
import tk.ksfdev.mobiletower.data.TowerAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TowerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recuclerview01);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TowerAdapter(this);
        recyclerView.setAdapter(mAdapter);

        refreshData(recyclerView);

    }


    public void refreshData(View v){
        ArrayList<MobileTower> towerList = new ArrayList<>();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();

        for(CellInfo cell : cellInfos){
            Log.d("TAG+++", "Cell: " + cell.toString() + " | " + cell.describeContents());

            if (cell instanceof CellInfoWcdma){
                //3G GSM
                String sigLev = "" + ((CellInfoWcdma) cell).getCellSignalStrength().getDbm();

                String mcc = "" + ((CellInfoWcdma)cell).getCellIdentity().getMcc();
                String mnc = "" + ((CellInfoWcdma)cell).getCellIdentity().getMnc();
                String lac = "" + ((CellInfoWcdma)cell).getCellIdentity().getLac();
                String cid = "" + ((CellInfoWcdma)cell).getCellIdentity().getCid();

                towerList.add(new MobileTower("Wcdma (3G Gsm)", mcc, mnc, lac, cid, sigLev));

//                Log.d("TAG+++", "Info WCDMA:: mcc: " + mcc + " | mnc: " + mnc + " | lac: " +lac + " | cid: " + cid + " | sigLev: " + sigLev);

            } if(cell instanceof CellInfoGsm){
                //GMS (2G)
                String sigLev = "" + ((CellInfoGsm) cell).getCellSignalStrength().getDbm();

                String mcc = "" + ((CellInfoGsm)cell).getCellIdentity().getMcc();
                String mnc = "" + ((CellInfoGsm)cell).getCellIdentity().getMnc();
                String lac = "" + ((CellInfoGsm)cell).getCellIdentity().getLac();
                String cid = "" + ((CellInfoGsm)cell).getCellIdentity().getCid();

                towerList.add(new MobileTower("Gsm", mcc, mnc, lac, cid, sigLev));

//                Log.d("TAG+++", "Info GSM:: mcc: " + mcc + " | mnc: " + mnc + " | lac: " +lac + " | cid: " + cid + " | sigLev: " + sigLev);

            } else if(cell instanceof CellInfoLte){
                //4G LTE
                //no 4G in BiH :D
            }

        }

        mAdapter.swapData(towerList);

    }








}
