package com.redmannequin.resonance.Backend.Effects;

/**
 * Created by Matthew on 11/24/2016.
 */

public class Effect {

  private int id;

  public Effect() {
    id = -1;
  }

  public Effect(int eyeDee) {
    id = eyeDee;
  }

  public int getID() {return id;}
  public void setID(int i) {id = i;}
}
