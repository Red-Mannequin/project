package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */

/*
    Effect ID's based of type:
    0: DelayEffect
    1: FlangerEffect
    2: Pitch Shift
*/
public class Effect {

  private int id;
  private boolean on;

  public Effect() {
    id = -1;
    on = true;
  }

  /*
    When Effect is created it is automatically set to be on.
  */

  public Effect(int eyeDee) {
    id = eyeDee;
    on = true;
  }

  public Effect(int id, boolean on) {
      this.id = id;
      this.on = on;
  }

  public int getID() {return id;}
  public void setID(int i) {id = i;}

  public boolean toggleOn() {
    if (on) {
      return on = !on;
    }
    return on;
  }

  public boolean isOn() {
    return on;
  }
}
