/*
 * $Id: Visual_scene.java,v 1.4 2007/12/13 10:13:03 morimune Exp $
 * Copyright (C) 2004-2005 Koga Laboratoy. All rights reserved.
 *
 */
package org.mklab.mikity.xml.blender;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.xml.bind.annotation.XmlElement;

import org.mklab.mikity.xml.model.Group;


/**
 * Blenderから出力したCOLLADAデータを読み込むためのクラス(Visual_scene要素)です。
 * 
 * @author SHOGO
 * @version $Revision: 1.4 $. 2007/11/30
 */
public class Visual_scene {
  @XmlElement
  private List<Node> nodes;

  /** ノードの名前  */
  private List<String> nodeNames;
  /** 変換行列 */
  private List<Matrix4f> transformMatrices;

  private Group rootGroup;

  /**
   * 新しく生成された<code>Visual_scene</code>オブジェクトを初期化します。
   */
  public Visual_scene() {
    this.nodes = new ArrayList<Node>();
    this.nodeNames = new ArrayList<String>();
    this.transformMatrices = new ArrayList<Matrix4f>();
    this.rootGroup = new Group();
  }

  /**
   * 各ノードの名前が追加されているリストを返します。
   * 
   * @return　nameList 各ノードの名前が追加されているリスト
   */
  public List<String> getNodeNames() {
    for (final Node node : this.nodes) {
      this.nodeNames.add(node.getGeometryURL());
    }
    return this.nodeNames;
  }

  /**
   * 変換行列が追加されているリストを返します。
   * 
   * @return　matrixList　変換行列が追加されているリスト
   */
  public List<Matrix4f> getTransformMatrices() {
    for (final Node node :this.nodes) {
      this.transformMatrices.add(node.getTransformMatrix());
    }
    return this.transformMatrices;
  }

  /**
   * 各ノードごとに変換行列を生成します。
   */
  public void createTransformMatrix() {
    for (final Node node : this.nodes) {
      node.createTransformMatrix();
    }
  }

  private void createScene() {
    for (final Node node : this.nodes) {
      node.createGroup();
      node.createScene();
    }
    
    for (final Node node : this.nodes) {
      if (node != null && node.getGroup().loadName() != null) {
        this.rootGroup.addGroup(node.getGroup());
      }
    }
  }

  /**
   * @return scene
   */
  public Group getScene() {
    createScene();
    return this.rootGroup;
  }
}
