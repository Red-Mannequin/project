package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */
/*
 double maxLength
  -> double wetness
  -> double sampleRate
  -> double lowFilterFrequency*/


public class FlangerEffect extends Effect {

  private double wetness;
  private double maxLength;
  private double sampleRate;
  private double lowFilterFrequency;

  public FlangerEffect(double wetness, double maxLength, double sampleRate, double lowFilterFrequency) {
    super(1);
    this.wetness = wetness;
    this.maxLength = maxLength;
    this.sampleRate = sampleRate;
    this.lowFilterFrequency = lowFilterFrequency;
  }

  //for use by JSONCreator
  public FlangerEffect(boolean on, double wetness, double maxLength, double sampleRate, double lowFilterFrequency) {
    super(1, on);
    this.wetness = wetness;
    this.maxLength = maxLength;
    this.sampleRate = sampleRate;
    this.lowFilterFrequency = lowFilterFrequency;
  }

  public void setWetness(double d) {wetness = d;}
  public double getWetness() {return wetness;}

  public void setMaxLength(double d) {maxLength = d;}
  public double getMaxLength() {return maxLength;}

  public void setSampleRate(double d) {sampleRate = d;}
  public double getSampleRate() {return sampleRate;}

  public void setLowFilterFrequency(double d) {lowFilterFrequency = d;}
  public double getLowFilterFrequency() {return lowFilterFrequency;}
}
