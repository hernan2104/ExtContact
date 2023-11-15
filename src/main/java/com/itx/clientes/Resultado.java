package com.itx.clientes;

import java.util.ArrayList;

public class Resultado {

    int totalContactos;
    int totalContactosOk;
    int totalContactosError;
    int totalContactosOK_API;
    int totalContactosError_API;
    ArrayList <String> contactError;
    
	public  Resultado (int val1, int val2, int val3, int val4, int val5) {
		totalContactos = val1;
		totalContactosOk = val2;
		totalContactosError = val3;
		totalContactosOK_API = val4;
		totalContactosError_API = val5;
	}
    public int getTotalContactos() {
        return totalContactos;
    }

    public void setTotalContactos(int totalContactos) {
        this.totalContactos = totalContactos;
    }

    public int getTotalContactosOk() {
        return totalContactosOk;
    }

    public void setTotalContactosOk(int totalContactosOk) {
        this.totalContactosOk = totalContactosOk;
    }

    public int getTotalContactosOk_API() {
        return totalContactosOK_API;
    }

    public void setTotalContactosOk_API(int totalContactosOkAPI) {
        this.totalContactosOK_API = totalContactosOkAPI;
    }
    
    public int getTotalContactosError() {
        return totalContactosError;
    }

    public void setTotalContactosError(int totalContactosError) {
        this.totalContactosError = totalContactosError;
    }

    public ArrayList<String> getContactError() {
        return contactError;
    }

    public void setContactError(ArrayList<String> contactError) {
        this.contactError = contactError;
    }
    
    public int getTotalContactosError_API() {
        return totalContactosError_API;
    }

    public void setTotalContactosError_API(int totalContactosErrorAPI) {
        this.totalContactosError_API = totalContactosErrorAPI;
    }    
  

}
