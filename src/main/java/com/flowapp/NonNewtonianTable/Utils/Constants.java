package com.flowapp.NonNewtonianTable.Utils;

public class Constants {

    public static final int MmInMeter = 1000;
    public static final int MmInCMeter = 10;
    public static final float CmInInch = 2.54f;
    public static final float MmInInch = CmInInch * MmInCMeter;
    public static final float AllowedErrorInDeltaT = 0.5f;
    public static final float ZeroCInKelvin = 273f;
    public static final int LaminarFlowMaxNre = 2100;
    public static final int TurbulentFlowMaxNre = 4000;

    public static final float KmInMile = 1.609344f;
    public static final float HoursInDay = 24;
    public static final float C1MolecularWeight = 16.04f;
    public static final float C2MolecularWeight = 30.06f;
    public static final float C3MolecularWeight = 28.01f;
    public static final float C4MolecularWeight = 44.01f;
    public static final float NitrogenMolecularWeight = 28f;
    public static final float AirMolecularWeight = 29f;
    public static final float RankinZeroF = 460f;
    public static final float ZAllowedError = 0.002f;
    public static final float FahrenheitZero = 32f;
    public static final float WaterDensityLBCF = 62.4f;
    public static final float SCFInM3 = 35.31466667f;
    public static final float InchesInFoot = 12;
    public static final float InchesInMeter = MmInMeter / MmInInch;
    public static final float FeetInMeter = InchesInMeter / InchesInFoot;
}
