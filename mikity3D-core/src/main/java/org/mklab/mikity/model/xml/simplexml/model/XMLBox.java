package org.mklab.mikity.model.xml.simplexml.model;

import java.beans.PropertyChangeListener;
import java.util.Vector;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * Class XMLBox.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/11/20 02:51:56 $
 */
@Root(name="box")
public class XMLBox implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  /** _xsize */
  @Attribute(name="xsize")
  private float xSize;

  /** keeps track of state for field: _xsize */
  private boolean hasXsize;

  /** _ysize */
  @Attribute(name="ysize")
  private float ySize;

  /** keeps track of state for field: _ysize */
  private boolean hasYsize;

  /** _zsize */
  @Attribute(name="zsize")
  private float zSize;

  /** keeps track of state for field: _zsize */
  private boolean hasZsize;

  /** _color */
  @Attribute(name="color")
  private java.lang.String color;

  /** _transparent */
  @Attribute(name="transparent")
  private boolean transparent;

  /** keeps track of state for field: _transparent */
  private boolean hasTransparent;

  /** _rotation */
  @Element(name="rotation", required=false)
  private Rotation rotation;

  /** _location */
  @Element(name="location")
  private Location location;

  /** propertyChangeListeners */
  private java.util.Vector<PropertyChangeListener> propertyChangeListeners;

  /**
   * コンストラクター
   */
  public XMLBox() {
    super();
    this.propertyChangeListeners = new Vector<PropertyChangeListener>();
  } 

  /**
   * Method addPropertyChangeListenerRegisters a PropertyChangeListener with this class.
   * 
   * @param pcl The PropertyChangeListener to register.
   */
  public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
    this.propertyChangeListeners.addElement(pcl);
  } 

//  /**
//   * Method deleteTransparent
//   */
//  public void deleteTransparent() {
//    this.hasTransparent = false;
//  }
//
//  /**
//   * Method deleteXsize
//   */
//  public void deleteXsize() {
//    this.hasXsize = false;
//  }
//
//  /**
//   * Method deleteYsize
//   */
//  public void deleteYsize() {
//    this.hasYsize = false;
//  }
//
//  /**
//   * Method deleteZsize
//   */
//  public void deleteZsize() {
//    this.hasZsize = false;
//  }

  /**
   * Returns the value of field 'color'.
   * 
   * @return the value of field 'color'.
   */
  public java.lang.String getColor() {
    return this.color;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.color == null) ? 0 : this.color.hashCode());
    result = prime * result + (this.hasTransparent ? 1231 : 1237);
    result = prime * result + (this.hasXsize ? 1231 : 1237);
    result = prime * result + (this.hasYsize ? 1231 : 1237);
    result = prime * result + (this.hasZsize ? 1231 : 1237);
    result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
    result = prime * result + ((this.rotation == null) ? 0 : this.rotation.hashCode());
    result = prime * result + (this.transparent ? 1231 : 1237);
    result = prime * result + Float.floatToIntBits(this.xSize);
    result = prime * result + Float.floatToIntBits(this.ySize);
    result = prime * result + Float.floatToIntBits(this.zSize);
    result = prime * result + ((this.propertyChangeListeners == null) ? 0 : this.propertyChangeListeners.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    XMLBox other = (XMLBox)obj;
    if (this.color == null) {
      if (other.color != null) {
        return false;
      }
    } else if (!this.color.equals(other.color)) {
      return false;
    }
    if (this.hasTransparent != other.hasTransparent) {
      return false;
    }
    if (this.hasXsize != other.hasXsize) {
      return false;
    }
    if (this.hasYsize != other.hasYsize) {
      return false;
    }
    if (this.hasZsize != other.hasZsize) {
      return false;
    }
    if (this.location == null) {
      if (other.location != null) {
        return false;
      }
    } else if (!this.location.equals(other.location)) {
      return false;
    }
    if (this.rotation == null) {
      if (other.rotation != null) {
        return false;
      }
    } else if (!this.rotation.equals(other.rotation)) {
      return false;
    }
    if (this.transparent != other.transparent) {
      return false;
    }
    if (Float.floatToIntBits(this.xSize) != Float.floatToIntBits(other.xSize)) {
      return false;
    }
    if (Float.floatToIntBits(this.ySize) != Float.floatToIntBits(other.ySize)) {
      return false;
    }
    if (Float.floatToIntBits(this.zSize) != Float.floatToIntBits(other.zSize)) {
      return false;
    }
    if (this.propertyChangeListeners == null) {
      if (other.propertyChangeListeners != null) {
        return false;
      }
    } else if (!this.propertyChangeListeners.equals(other.propertyChangeListeners)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the value of field 'location'.
   * 
   * @return the value of field 'location'.
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Returns the value of field 'rotation'.
   * 
   * @return the value of field 'rotation'.
   */
  public Rotation getRotation() {
    return this.rotation;
  }

  /**
   * Returns the value of field 'transparent'.
   * 
   * @return the value of field 'transparent'.
   */
  public boolean getTransparent() {
    return this.transparent;
  }

  /**
   * Returns the value of field 'xsize'.
   * 
   * @return the value of field 'xsize'.
   */
  public float getXsize() {
    return this.xSize;
  }

  /**
   * Returns the value of field 'ysize'.
   * 
   * @return the value of field 'ysize'.
   */
  public float getYsize() {
    return this.ySize;
  }

  /**
   * Returns the value of field 'zsize'.
   * 
   * @return the value of field 'zsize'.
   */
  public float getZsize() {
    return this.zSize;
  }

  /**
   * Method hasTransparent
   * 
   * @return has_transparent
   */
  public boolean hasTransparent() {
    return this.hasTransparent;
  }

  /**
   * Method hasXsize
   * 
   * @return has_xsize
   */
  public boolean hasXsize() {
    return this.hasXsize;
  }

  /**
   * Method hasYsize
   * 
   * @return has_ysize
   */
  public boolean hasYsize() {
    return this.hasYsize;
  }

  /**
   * Method hasZsize
   * 
   * @return has_zsize
   */
  public boolean hasZsize() {
    return this.hasZsize;
  }

  /**
   * Method notifyPropertyChangeListenersNotifies all registered PropertyChangeListeners when a bound property's value changes.
   * 
   * @param fieldName the name of the property that has changed.
   * @param newValue the new value of the property.
   * @param oldValue the old value of the property.
   */
  protected void notifyPropertyChangeListeners(java.lang.String fieldName, java.lang.Object oldValue, java.lang.Object newValue) {
    if (this.propertyChangeListeners == null) return;
    java.beans.PropertyChangeEvent event = new java.beans.PropertyChangeEvent(this, fieldName, oldValue, newValue);

    for (int i = 0; i < this.propertyChangeListeners.size(); i++) {
      (this.propertyChangeListeners.elementAt(i)).propertyChange(event);
    }
  }

  /**
   * Method removePropertyChangeListenerRemoves the given PropertyChangeListener from this classes list of ProperyChangeListeners.
   * 
   * @param pcl The PropertyChangeListener to remove.
   * @return true if the given PropertyChangeListener was removed.
   */
  public boolean removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
    return this.propertyChangeListeners.removeElement(pcl);
  }

  /**
   * Sets the value of field 'color'.
   * 
   * @param color the value of field 'color'.
   */
  public void setColor(java.lang.String color) {
    this.color = color;
  }

  /**
   * Sets the value of field 'location'.
   * 
   * @param location the value of field 'location'.
   */
  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Sets the value of field 'rotation'.
   * 
   * @param rotation the value of field 'rotation'.
   */
  public void setRotation(Rotation rotation) {
    this.rotation = rotation;
  }

  /**
   * Sets the value of field 'transparent'.
   * 
   * @param transparent the value of field 'transparent'.
   */
  public void setTransparent(boolean transparent) {
    this.transparent = transparent;
    this.hasTransparent = true;
  }

  /**
   * Sets the value of field 'xsize'.
   * 
   * @param xsize the value of field 'xsize'.
   */
  public void setXsize(float xsize) {
    this.xSize = xsize;
    this.hasXsize = true;
  }

  /**
   * Sets the value of field 'ysize'.
   * 
   * @param ysize the value of field 'ysize'.
   */
  public void setYsize(float ysize) {
    this.ySize = ysize;
    this.hasYsize = true;
  }

  /**
   * Sets the value of field 'zsize'.
   * 
   * @param zsize the value of field 'zsize'.
   */
  public void setZsize(float zsize) {
    this.zSize = zsize;
    this.hasZsize = true;
  }

}
