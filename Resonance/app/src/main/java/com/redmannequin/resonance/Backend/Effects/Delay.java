package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */

public class Delay extends Effect {

  private double delay;
  private double factor;

  public Delay(int id, double delay, double factor) {
    super(id);
    this.delay = delay;
    this.factor = delay;
  }

  public void setDelay(double d) {delay = d;}
  public void setFactor(double f) {factor = f;}

  public double getDelay() {return delay;}
  public double getFactor() {return factor;}

}
