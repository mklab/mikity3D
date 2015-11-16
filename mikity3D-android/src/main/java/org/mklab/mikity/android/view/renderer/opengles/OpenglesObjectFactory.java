/*
 * Created on 2013/02/12
 * Copyright (C) 2013 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.android.view.renderer.opengles;

import org.mklab.mikity.model.Coordinate;
import org.mklab.mikity.model.Messages;
import org.mklab.mikity.model.GroupObjectManager;
import org.mklab.mikity.model.graphic.GraphicObjectFactory;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.mikity.model.xml.simplexml.model.NullModel;
import org.mklab.mikity.model.xml.simplexml.model.ObjectModel;
import org.mklab.mikity.model.xml.simplexml.model.RotationModel;
import org.mklab.mikity.model.xml.simplexml.model.TranslationModel;

/**
 * {@link OpenglesObject}のファクトリークラスです。
 * 
 * @author ohashi
 * @version $Revision$, 2013/02/12
 */
public class OpenglesObjectFactory {
  /** オブジェクトグループマネージャ。 */
  private GroupObjectManager manager;
  
  /**
   * 新しく生成された<code>OpenglesObjectGroupFactory</code>オブジェクトを初期化します。
   * @param manager オブジェクトグループマネージャ
   */
  public OpenglesObjectFactory(GroupObjectManager manager) {
    this.manager = manager;
  }
    
  /**
   * {@link OpenglesGroupObject}を生成します。
   * @param group オブジェクトのグループ
   * @return グループ
   */
  public OpenglesGroupObject create(final GroupModel group) {
    final OpenglesGroupObject groupObject = OpenglesGroupObject.create(group);

    for (final ObjectModel object : group.getObjects()) {
      if (object instanceof NullModel) {
        continue;
      }
      groupObject.addElement(create(object));
    }

    for (final GroupModel subGroup : group.getGroups()) {
      final OpenglesGroupObject subGroupObject = create(subGroup);
      groupObject.addElement(subGroupObject);
    }

    final Coordinate baseCoordinate = createCoordinateOf(group.getTranslation(), group.getRotation());
    groupObject.setBaseCoordinate(baseCoordinate);
    
    final String name = group.getName();
    if (name != null) {
      groupObject.setName(name);
    }
    
    this.manager.addGroupObject(groupObject);

    return groupObject;
  }
 
  /**
   * 与えられたプリミティブを含むグループを生成します。
   * 
   * @param model プリミティブ
   * @return 与えられたプリミティブを含むグループ
   */
  public OpenglesObject create(ObjectModel model) {
    final OpenglesSingleObject object = new OpenglesSingleObject(GraphicObjectFactory.create(model));
   
    final TranslationModel translation = model.getTranslation();
    final RotationModel rotation = model.getRotation();

    if (translation == null && rotation == null) {
      return object;
    }

    final OpenglesGroupObject group = OpenglesGroupObject.create();
    group.addElement(object);
    group.setBaseCoordinate(createCoordinateOf(translation, rotation));
    
    return group;
  }
  
  /**
   * 基準座標を生成します。
   * 
   * @param translation 並進変換
   * @param rotation 回転変換
   * @return 基準座標系
   */
  private Coordinate createCoordinateOf(final TranslationModel translation, final RotationModel rotation) {
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
