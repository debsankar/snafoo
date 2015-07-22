package com.esnacks4nerds.model.entity;

public class Snacks
{

  private int id;
  private String name;
  private boolean optional;
  private String purchaseLocations;
  private String purchaseCount;
  private String lastPurchaseDate;
  public int getId()
  {
    return id;
  }
  public void setId( int id )
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName( String name )
  {
    this.name = name;
  }
  public boolean isOptional()
  {
    return optional;
  }
  public void setOptional( boolean optional )
  {
    this.optional = optional;
  }
  public String getPurchaseLocations()
  {
    return purchaseLocations;
  }
  public void setPurchaseLocations( String purchaseLocations )
  {
    this.purchaseLocations = purchaseLocations;
  }
  public String getPurchaseCount()
  {
    return purchaseCount;
  }
  public void setPurchaseCount( String purchaseCount )
  {
    this.purchaseCount = purchaseCount;
  }
  public String getLastPurchaseDate()
  {
    return lastPurchaseDate;
  }
  public void setLastPurchasedDate( String lastPurchaseDate )
  {
    this.lastPurchaseDate = lastPurchaseDate;
  }
  
  
  
}
