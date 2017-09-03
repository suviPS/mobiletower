package tk.ksfdev.mobiletower.data;

/**
 *  Tower model
 */

public class MobileTower {

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    private String connectionType;
    private String mcc;
    private String mnc;
    private String lac;
    private String cid;
    private String signalDbm;

    public MobileTower(){
    }

    public MobileTower(String connectionType, String mcc, String mnc, String lac, String cid, String signalDbm){
        this.connectionType = connectionType;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cid = cid;
        this.signalDbm = signalDbm;
    }



    //getters and setters

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSignalDbm() {
        return signalDbm;
    }

    public void setSignalDbm(String signalDbm) {
        this.signalDbm = signalDbm;
    }
}
