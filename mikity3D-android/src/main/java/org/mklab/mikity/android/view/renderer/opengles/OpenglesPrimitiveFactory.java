/*
 * Created on 2013/02/12
 * Copyright (C) 2013 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.android.view.renderer.opengles;

import org.mklab.mikity.model.Coordinate;
import org.mklab.mikity.model.graphic.BoxPrimitive;
import org.mklab.mikity.model.graphic.ConePrimitive;
import org.mklab.mikity.model.graphic.CylinderPrimitive;
import org.mklab.mikity.model.graphic.QuadPolygonPrimitive;
import org.mklab.mikity.model.graphic.SpherePrimitive;
import org.mklab.mikity.model.graphic.TrianglePolygonPrimitive;
import org.mklab.mikity.model.xml.simplexml.model.BoxModel;
import org.mklab.mikity.model.xml.simplexml.model.ConeModel;
import org.mklab.mikity.model.xml.simplexml.model.CylinderModel;
import org.mklab.mikity.model.xml.simplexml.model.PrimitiveModel;
import org.mklab.mikity.model.xml.simplexml.model.QuadPolygonModel;
import org.mklab.mikity.model.xml.simplexml.model.RotationModel;
import org.mklab.mikity.model.xml.simplexml.model.SphereModel;
import org.mklab.mikity.model.xml.simplexml.model.TranslationModel;
import org.mklab.mikity.model.xml.simplexml.model.TrianglePolygonModel;


/**
 * OpenGLESのプリミティブを生成するファクトリークラスです。
 * 
 * @author ohashi
 * @version $Revision$, 2013/02/12
 */
public class OpenglesPrimitiveFactory {
  /**
   * 与えられたプリミティブを含むグループを生成します。
   * 
   * @param primitive プリミティブ
   * @return 与えられたプリミティブを含むグループ
   */
  public static OpenglesObject create(PrimitiveModel primitive) {
    final OpenglesPrimitive child;
    
    if (primitive instanceof BoxModel) {
      child = new OpenglesPrimitive(new BoxPrimitive((BoxModel)primitive));
    } else if (primitive instanceof CylinderModel) {
      child = new OpenglesPrimitive(new CylinderPrimitive((CylinderModel)primitive));
    } else if (primitive instanceof ConeModel) {
      child = new OpenglesPrimitive(new ConePrimitive((ConeModel)primitive));
    } else if (primitive instanceof SphereModel) {
      child = new OpenglesPrimitive(new SpherePrimitive((SphereModel)primitive));
    } else if (primitive instanceof TrianglePolygonModel) {
      child = new OpenglesPrimitive(new TrianglePolygonPrimitive((TrianglePolygonModel)primitive));
    } else if (primitive instanceof QuadPolygonModel) {
      child = new OpenglesPrimitive(new QuadPolygonPrimitive((QuadPolygonModel)primitive));
    } else {
      throw new IllegalArgumentException(primitive.getClass().toString());
    }
   
    final TranslationModel translation = primitive.getTranslation();
    final RotationModel rotation = primitive.getRotation();

    if (translation == null && rotation == null) {
      return child;
    }

    final OpenglesObjectGroup group = OpenglesObjectGroup.create();
    group.addChild(child);
    group.setBaseCoordinate(createCoordinate(translation, rotation));
    
    return group;
  }

  /**
   * 基準座標を生成します。
   * 
   * @param translation 並進変換
   * @param rotation 回転変換
   * @return 基準座標系
   */
  private static Coordinate createCoordinate(final TranslationModel translation, final RotationModel rotation) {
    if (translation != null && rotation != null) {
      return new Coordinate(translation, rotation);
    } 
    
    if (translation != null) {
      return new Coordinate(translation);
    } 
    
    if (rotation != null) {
      return new Coordinate(rotation);
    }
    
    throw new IllegalArgumentException(Messages.getString("JoglTransformGroupFactory.0")); //$NON-NLS-1$
  }

}
