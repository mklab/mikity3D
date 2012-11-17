/*
 * Created on 2006/01/31
 * Copyright (C) 2006 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.gui.connector;

import org.mklab.mikity.gui.ModelingWindow;
import org.mklab.mikity.xml.Jamast;
import org.mklab.mikity.xml.model.Group;
import org.mklab.mikity.xml.model.Location;
import org.mklab.mikity.xml.model.Rotation;
import org.mklab.mikity.xml.model.XMLBox;
import org.mklab.mikity.xml.model.XMLCone;
import org.mklab.mikity.xml.model.XMLCylinder;
import org.mklab.mikity.xml.model.XMLSphere;


/**
 * プリミティブに適したコネクタを追加するクラスです。
 * 
 * @author SHOGO
 * @version $Revision: 1.3 $.2006/01/31
 */
public class PrimitiveConnector {
  /** プリミティブの座標 */
  private Location primLocation;
  /** プリミティブの回転 */
  private Rotation primRotation;

  /** 直方体のコネクタ */
  private BoxConnector boxConnector;
  /** 円錐のコネクタ */
  private ConeConnector coneConnector;
  /** 円柱のコネクタ */
  private CylinderConnector cylinderConnector;
  /** 球体のコネクタ */
  private SphereConnector sphereConnector;

  /**
   * コンストラクター
   */
  public PrimitiveConnector() {
    this.boxConnector = new BoxConnector();
    this.coneConnector = new ConeConnector();
    this.cylinderConnector = new CylinderConnector();
    this.sphereConnector = new SphereConnector();
  }

  /**
   * プリミティブごとのの各パラメータを取得し、キャンバス上のプリミティブごとに適したコネクタを追加する。 コネクタを追加されるプリミティブ
   * 
   * @param prim プリミティブ
   */
  public void createPrimitiveConnector(Object prim) {
    if (prim instanceof XMLBox) {
      XMLBox box = (XMLBox)prim;

      float param1 = box.loadXsize();
      float param2 = box.loadYsize();
      float param3 = box.loadZsize();

      this.primLocation = box.loadLocation();
      checkLocation(this.primLocation);

      this.primRotation = box.loadRotation();
      checkRotation(this.primRotation);

      this.boxConnector.createBoxConnector(param1, param2, param3, this.primLocation, this.primRotation);
    } else if (prim instanceof XMLCone) {
      XMLCone cone = (XMLCone)prim;

      float param1 = cone.loadR();
      float param2 = cone.loadHeight();

      this.primLocation = cone.loadLocation();
      checkLocation(this.primLocation);

      this.primRotation = cone.loadRotation();
      checkRotation(this.primRotation);

      this.coneConnector.createConeConnector(param1, param2, this.primLocation, this.primRotation);
    } else if (prim instanceof XMLCylinder) {
      XMLCylinder cylinder = (XMLCylinder)prim;

      float param1 = cylinder.loadR();
      float param2 = cylinder.loadHeight();

      this.primLocation = cylinder.loadLocation();
      checkLocation(this.primLocation);

      this.primRotation = cylinder.loadRotation();
      checkRotation(this.primRotation);

      this.cylinderConnector.createCylinderConnector(param1, param2, this.primLocation, this.primRotation);
    } else if (prim instanceof XMLSphere) {
      XMLSphere sphere = (XMLSphere)prim;

      float param1 = sphere.loadR();

      this.primLocation = sphere.loadLocation();
      checkLocation(this.primLocation);

      this.primRotation = sphere.loadRotation();
      checkRotation(this.primRotation);

      this.sphereConnector.createSphereConnector(param1, this.primLocation, this.primRotation);
    }
  }

  /**
   * プリミティブの位置座標におけるパラメータが変化していないとき、各座標に0.0の値を代入する。
   * 
   * @param location 　プリミティブの位置座標
   */
  private void checkLocation(Location location) {
    if (location == null) {
      this.primLocation = new Location();
      this.primLocation.setX(0.0f);
      this.primLocation.setY(0.0f);
      this.primLocation.setZ(0.0f);
    }
  }

  /**
   * プリミティブの各軸の回転角度におけるパラメータが変化していないとき、各軸の回転角度に0.0の値を代入する。
   * 
   * @param rotation 　プリミティブの回転角度
   */
  private void checkRotation(Rotation rotation) {
    if (rotation == null) {
      this.primRotation = new Rotation();
      this.primRotation.setXrotate(0.0f);
      this.primRotation.setYrotate(0.0f);
      this.primRotation.setZrotate(0.0f);
    }
  }

  /**
   * コネクタNを持つグループを生成します。
   * 
   * @return　newgroup　コネクタNを持つグループ
   */
  public Group createNorthConnectorGroup() {
    final Group northGroup = new Group();
    northGroup.setName("ConnectorN"); //$NON-NLS-1$

    final Jamast root = ModelingWindow.getRoot();
    final Group routGroup = root.loadModel(0).loadGroup(0);
    routGroup.addGroup(northGroup);
    return northGroup;
  }

  /**
   * コネクタSを持つグループを生成します。
   * 
   * @return　newgroup　コネクタSを持つグループ
   */
  public Group createSouthConnectorGroup() {   
    final Group southGroup = new Group();
    southGroup.setName("ConnectorS"); //$NON-NLS-1$
    
    final Jamast root = ModelingWindow.getRoot();
    final Group rootGroup = root.loadModel(0).loadGroup(0);
    rootGroup.addGroup(southGroup);
    return southGroup;
  }
}
