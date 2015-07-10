package org.mklab.mikity.view.renderer.jogl.primitive;

import javax.media.opengl.GL2;

import org.mklab.mikity.util.Vector3;
import org.mklab.mikity.view.renderer.jogl.AbstractJoglObject;


/**
 * シリンダーをGLで表すクラスです。
 * 
 * @author iwamoto
 * @version $Revision$, 2012/01/31
 */
public class JoglCylinder extends AbstractJoglObject {
  /** 底面の半径 */
  private float radius = 0;

  /** 高さ */
  private float height = 0;

  /** 分割数 */
  private int division = 0;

  /** 上面ポリゴン。 */
  private JoglTrianglePolygon[] upperPolygons;

  /** 下面ポリゴン。 */
  private JoglTrianglePolygon[] lowerPolygons;

  /** 側面ポリゴン。 */
  private JoglQuadPolygon[] sidePolygons;
  
  /**
   * {@inheritDoc}
   */
  public void display(GL2 gl) {
    applyColor(gl);
    applyTransparency(gl);
    
    for (int i = 0; i < this.division; i++) {
      this.upperPolygons[i].display(gl);
      this.lowerPolygons[i].display(gl);
      this.sidePolygons[i].display(gl);
    }
  }

  /**
   * ポリゴンを更新します。
   */
  private void updatePolygons() {
    if (this.radius == 0 || this.height == 0 || this.division == 0) {
      return;
    }
    
    final float[] upperCenterPoint = new float[3];
    upperCenterPoint[0] = 0;
    upperCenterPoint[1] = 0;
    upperCenterPoint[2] = this.height / 2;

    final float[] lowerCenterPoint = new float[3];
    lowerCenterPoint[0] = 0;
    lowerCenterPoint[1] = 0;
    lowerCenterPoint[2] = -this.height / 2;

    final float[][] upperPoints = new float[this.division+1][3];
    for (int i = 0; i < this.division; i++) {
      final double theta = 2.0 * Math.PI / this.division * i;
      upperPoints[i][0] = (float)(this.radius * Math.cos(theta));
      upperPoints[i][1] = (float)(this.radius * Math.sin(theta));
      upperPoints[i][2] = this.height / 2.0f;
    }
    upperPoints[this.division][0] = (float)(this.radius * Math.cos(0));
    upperPoints[this.division][1] = (float)(this.radius * Math.sin(0));
    upperPoints[this.division][2] = this.height / 2.0f;

    final float[][] lowerPoints = new float[this.division+1][3];
    for (int i = 0; i < this.division; i++) {
      final double theta = 2.0 * Math.PI / this.division * i;
      lowerPoints[i][0] = (float)(this.radius * Math.cos(theta));
      lowerPoints[i][1] = (float)(this.radius * Math.sin(theta));
      lowerPoints[i][2] = -this.height / 2.0f;
    }
    lowerPoints[this.division][0] = (float)(this.radius * Math.cos(0));
    lowerPoints[this.division][1] = (float)(this.radius * Math.sin(0));
    lowerPoints[this.division][2] = -this.height / 2.0f;

    this.upperPolygons = new JoglTrianglePolygon[this.division];
    for (int i = 0; i < this.division; i++) {
      this.upperPolygons[i] = new JoglTrianglePolygon();
      final float[][] vertices = new float[3][3];
      vertices[0][0] = upperCenterPoint[0];
      vertices[0][1] = upperCenterPoint[1];
      vertices[0][2] = upperCenterPoint[2];
      
      vertices[1][0] = upperPoints[i][0];
      vertices[1][1] = upperPoints[i][1];
      vertices[1][2] = upperPoints[i][2];

      vertices[2][0] = upperPoints[i+1][0];
      vertices[2][1] = upperPoints[i+1][1];
      vertices[2][2] = upperPoints[i+1][2];

      final Vector3 v0 = new Vector3(vertices[1][0] - vertices[0][0], vertices[1][1] - vertices[0][1], vertices[1][2] - vertices[0][2]);
      final Vector3 v1 = new Vector3(vertices[2][0] - vertices[0][0], vertices[2][1] - vertices[0][1], vertices[2][2] - vertices[0][2]);
      final Vector3 normalVector = v0.cross(v1);

      this.upperPolygons[i].setVertices(vertices);
      this.upperPolygons[i].setNormalVector(new float[]{normalVector.getX(), normalVector.getY(), normalVector.getZ()});
    }
    
    this.lowerPolygons = new JoglTrianglePolygon[this.division];
    for (int i = 0; i < this.division; i++) {
      this.lowerPolygons[i] = new JoglTrianglePolygon();
      final float[][] vertices = new float[3][3];
      vertices[0][0] = lowerCenterPoint[0];
      vertices[0][1] = lowerCenterPoint[1];
      vertices[0][2] = lowerCenterPoint[2];
      
      vertices[1][0] = lowerPoints[i+1][0];
      vertices[1][1] = lowerPoints[i+1][1];
      vertices[1][2] = lowerPoints[i+1][2];

      vertices[2][0] = lowerPoints[i][0];
      vertices[2][1] = lowerPoints[i][1];
      vertices[2][2] = lowerPoints[i][2];

      final Vector3 v0 = new Vector3(vertices[1][0] - vertices[0][0], vertices[1][1] - vertices[0][1], vertices[1][2] - vertices[0][2]);
      final Vector3 v1 = new Vector3(vertices[2][0] - vertices[0][0], vertices[2][1] - vertices[0][1], vertices[2][2] - vertices[0][2]);
      final Vector3 normalVector = v0.cross(v1);

      this.lowerPolygons[i].setVertices(vertices);
      this.lowerPolygons[i].setNormalVector(new float[]{normalVector.getX(), normalVector.getY(), normalVector.getZ()});
    }
    
    this.sidePolygons = new JoglQuadPolygon[this.division];
    for (int i = 0; i < this.division; i++) {
      this.sidePolygons[i] = new JoglQuadPolygon();
      final float[][] vertices = new float[4][3];
      vertices[0][0] = upperPoints[i][0];
      vertices[0][1] = upperPoints[i][1];
      vertices[0][2] = upperPoints[i][2];
      
      vertices[1][0] = lowerPoints[i][0];
      vertices[1][1] = lowerPoints[i][1];
      vertices[1][2] = lowerPoints[i][2];

      vertices[2][0] = lowerPoints[i+1][0];
      vertices[2][1] = lowerPoints[i+1][1];
      vertices[2][2] = lowerPoints[i+1][2];

      vertices[3][0] = upperPoints[i+1][0];
      vertices[3][1] = upperPoints[i+1][1];
      vertices[3][2] = upperPoints[i+1][2];
      
      final Vector3 v0 = new Vector3(vertices[1][0] - vertices[0][0], vertices[1][1] - vertices[0][1], vertices[1][2] - vertices[0][2]);
      final Vector3 v1 = new Vector3(vertices[3][0] - vertices[0][0], vertices[3][1] - vertices[0][1], vertices[3][2] - vertices[0][2]);
      final Vector3 normalVector = v0.cross(v1);

      this.sidePolygons[i].setVertices(vertices);
      this.sidePolygons[i].setNormalVector(new float[]{normalVector.getX(), normalVector.getY(), normalVector.getZ()});
    }
  }

  /**
   * 大きさを設定します。
   * 
   * @param radius 底面の半径
   * @param height 高さ
   */
  public void setSize(float radius, float height) {
    this.radius = radius;
    this.height = height;
    updatePolygons();
  }

  /**
   * 分割数を設定します。
   * 
   * @param division 分割数
   */
  public void setDivision(int division) {
    this.division = division;
    updatePolygons();
  }

}
