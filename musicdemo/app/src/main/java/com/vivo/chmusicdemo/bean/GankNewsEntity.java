package com.vivo.chmusicdemo.bean;

import java.util.ArrayList;
import java.util.List;

public class GankNewsEntity {

    private boolean error;
    private List<GankNewsResultEntity> results = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankNewsResultEntity> getResults() {
        return results;
    }

    public void setResults(List<GankNewsResultEntity> results) {
        this.results = results;
    }
}
