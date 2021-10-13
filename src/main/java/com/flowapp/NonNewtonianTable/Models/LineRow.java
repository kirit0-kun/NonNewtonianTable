package com.flowapp.NonNewtonianTable.Models;

public class LineRow {

    private final float q;
    private final float v;
    private final float nre;
    private final float f;
    private final float hf;
    private final float ht;

    public LineRow(float q, float v, float nre, float f, float hf, float ht) {
        this.q = q;
        this.v = v;
        this.nre = nre;
        this.f = f;
        this.hf = hf;
        this.ht = ht;
    }

    public float getQ() {
        return q;
    }

    public float getV() {
        return v;
    }

    public float getNre() {
        return nre;
    }

    public float getF() {
        return f;
    }

    public float getHf() {
        return hf;
    }

    public float getHt() {
        return ht;
    }

    @Override
    public String toString() {
        return "LineRow{" +
                "q=" + q +
                ", v=" + v +
                ", nre=" + nre +
                ", f=" + f +
                ", hf=" + hf +
                ", ht=" + ht +
                '}';
    }
}
