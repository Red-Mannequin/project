package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */

public class PitchShift extends Effect {

  private double sampleRate;

  public PitchShift(int id, double sampleRate) {
    super(id);
    this.sampleRate = sampleRate;
  }

  public void setSampleRate(double d) {sampleRate = d;}
  public double getSampleRate() {return sampleRate;}

  
}
