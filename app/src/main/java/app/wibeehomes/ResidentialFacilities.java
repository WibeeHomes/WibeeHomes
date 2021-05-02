package app.wibeehomes;

import java.io.Serializable;

import app.wibeehomes.WooriBankAPI.WarFeeLoan;
import app.wibeehomes.WooriBankAPI.bisangLoan;
import app.wibeehomes.WooriBankAPI.WorkerLoan;

public class ResidentialFacilities implements Serializable {

    private Place resident;

    private int hYear=0;// 집 건축년도
    private int hFloor=-2; //층수
    private double hArea=0; // 면적
    private int hCate=-1; //카테고리(아파트=0,오피스텔=1,연립주택 =2)
    private String addJibun;// 지번
    private String warFee;// 보증금
    private String renFee; // 월세

    private WarFeeLoan warFeeLoan;
    private bisangLoan bisangLoan;
    private WorkerLoan workerLoan;


    public ResidentialFacilities(Place resident, int hYear, int hFloor, double hArea, int hCate, String addJibun, String warFee, String renFee,
     WarFeeLoan warFeeLoan, bisangLoan bisangLoan,WorkerLoan workerLoan){
        this.resident = resident;
        this.hYear=hYear;
        this.hFloor=hFloor;
        this.hArea = hArea;
        this.hCate =hCate;
        this.addJibun =addJibun;
        this.warFee = warFee;
        this.renFee=renFee;
        this.warFeeLoan =warFeeLoan;
        this.bisangLoan = bisangLoan;
        this.workerLoan=workerLoan;
    }

    public double gethArea() {
        return hArea;
    }
    public int gethCate() {
        return hCate;
    }
    public int gethFloor() {
        return hFloor;
    }
    public int gethYear() {
        return hYear;
    }
    public Place getResident() {
        return resident;
    }
    public String getAddJibun() {
        return addJibun;
    }
    public String getRenFee() {
        return renFee;
    }
    public String getWarFee() {
        return warFee;
    }

    public void setAddJibun(String addJibun) {
        this.addJibun = addJibun;
    }
    public void sethArea(double hArea) {
        this.hArea = hArea;
    }
    public void sethCate(int hCate) {
        this.hCate = hCate;
    }
    public void sethFloor(int hFloor) {
        this.hFloor = hFloor;
    }
    public void sethYear(int hYear) {
        this.hYear = hYear;
    }
    public void setRenFee(String renFee) {
        this.renFee = renFee;
    }
    public void setResident(Place resident) {
        this.resident = resident;
    }
    public void setWarFee(String warFee) {
        this.warFee = warFee;
    }
}
