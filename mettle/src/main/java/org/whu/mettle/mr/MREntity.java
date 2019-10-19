package org.whu.mettle.mr;

import java.util.List;

public class MREntity {

    private List<Integer> mrViolation;
    private List<Double> mrPreference;
    private List<Double> mrSeverity;

    public List<Integer> getMrViolation(){
        return mrViolation;
    }

    public void setMrViolation(List<Integer> mrViolation){
        this.mrViolation = mrViolation;
    }

    public List<Double> getMrPreference(){
        return mrPreference;
    }

    public void setMrPreference(List<Double> mrPreference) {
        this.mrPreference = mrPreference;
    }

    public List<Double> getMrSeverity() {
        return mrSeverity;
    }

    public void setMrSeverity(List<Double> mrSeverity) {
        this.mrSeverity = mrSeverity;
    }
}
