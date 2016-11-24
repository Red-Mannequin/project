package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */

public class PitchShiftEffect extends Effect {

  private double sampleRate;

  public PitchShiftEffect(double sampleRate) {
    super(2);
    this.sampleRate = sampleRate;
  }

  public void setSampleRate(double d) {sampleRate = d;}
  public double getSampleRate() {return sampleRate;}


}
