package com.google.gwt.resources.client;

/**
 * The base interface all bundle resource types must extend.
 */
public interface ResourcePrototype {
  /**
   * Returns the name of the function within the ClientBundle used to create the
   * ResourcePrototype.
   * 
   * @return the name of the function within the ClientBundle used to create the
   *         ResourcePrototype
   */
  String getName();
}
