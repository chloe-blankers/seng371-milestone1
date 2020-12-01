package models;

public class Whale {

  public Long id;

  public String species;
  public int estimated_weight;
  //TODO convert to enum
  public String gender;

  public int getEstimated_weight() {
    return estimated_weight;
  }

  public void setEstimated_weight(int estimated_weight) {
    this.estimated_weight = estimated_weight;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }
}