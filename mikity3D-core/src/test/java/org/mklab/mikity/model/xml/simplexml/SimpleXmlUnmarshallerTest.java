/*
 * Created on 2015/05/06
 * Copyright (C) 2015 Koga Laboratory. All rights reserved.
 *
 */
package org.mklab.mikity.model.xml.simplexml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mklab.mikity.model.xml.Mikity3dSerializeDeserializeException;
import org.mklab.mikity.model.xml.simplexml.config.BackgroundModel;
import org.mklab.mikity.model.xml.simplexml.config.DataUnitModel;
import org.mklab.mikity.model.xml.simplexml.config.EyeModel;
import org.mklab.mikity.model.xml.simplexml.config.LightModel;
import org.mklab.mikity.model.xml.simplexml.config.ModelUnitModel;
import org.mklab.mikity.model.xml.simplexml.model.AnimationModel;
import org.mklab.mikity.model.xml.simplexml.model.BoxModel;
import org.mklab.mikity.model.xml.simplexml.model.ColorModel;
import org.mklab.mikity.model.xml.simplexml.model.CylinderModel;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.mikity.model.xml.simplexml.model.ObjectModel;
import org.mklab.mikity.model.xml.simplexml.model.RotationModel;
import org.mklab.mikity.model.xml.simplexml.model.SourceModel;
import org.mklab.mikity.model.xml.simplexml.model.TranslationModel;


/**
 * {@link Mikity3DUnmarshaller}のテストケースクラスです。
 * 
 * @author koga
 * @version $Revision$, 2015/05/06
 */
public class SimpleXmlUnmarshallerTest {

  private Mikity3DUnmarshaller unmarshaler = new Mikity3DUnmarshaller();

  /**
   * @throws IOException ファイルを参照できない場合
   * @throws Mikity3dSerializeDeserializeException unmarshallできない場合
   */
  @Before
  public void setUp() throws IOException, Mikity3dSerializeDeserializeException {
    try (final InputStream input = getClass().getClassLoader().getResourceAsStream("Pendulum.m3d")) { //$NON-NLS-1$
      this.unmarshaler.unmarshalFromMikity3DFile(input);
    }
  }

  //  /**
  //   * Test method for {@link org.mklab.mikity.model.xml.simplexml.SimpleXmlUnmarshaller#unmarshalFromMikity3DFile(java.io.File)}.
  //   */
  //  @Test
  //  public void testUnmarshalFromMikity3DFileFile() {
  //    fail("Not yet implemented"); //$NON-NLS-1$
  //  }

  /**
   * Test method for {@link Mikity3DUnmarshaller#unmarshalFromMikity3DFile(java.io.InputStream)}.
   * 
   * @throws IOException ファイルを参照できない場合
   * @throws Mikity3dSerializeDeserializeException unmarshallできない場合
   */
  @Test
  public void testUnmarshalFromMikity3DFileInputStream() throws IOException, Mikity3dSerializeDeserializeException {
    try (final InputStream input = getClass().getClassLoader().getResourceAsStream("Pendulum.m3d")) { //$NON-NLS-1$
      this.unmarshaler.unmarshalFromMikity3DFile(input);
    }
  }

  /**
   * Test method for {@link Mikity3DUnmarshaller#getRoot()}.
   */
  @Test
  public void testGetRoot() {
    final Mikity3DModel actual = this.unmarshaler.getRoot();
    final ConfigurationModel actualConfiguration = actual.getConfiguration(0);

    final BackgroundModel actualBackground = actualConfiguration.getBackground();
    final BackgroundModel expectedBackground = new BackgroundModel();
    final ColorModel expectedColor = new ColorModel("white"); //$NON-NLS-1$
    expectedBackground.setColor(expectedColor);
    assertThat(actualBackground, is(expectedBackground));

    final LightModel actualLight = actualConfiguration.getLight();
    final LightModel expectedLight = new LightModel();
    expectedLight.setX(10F);
    expectedLight.setY(10F);
    expectedLight.setZ(20F);
    assertThat(actualLight, is(expectedLight));

    final EyeModel actualEye = actualConfiguration.getEye();
    final EyeModel expectedEye = new EyeModel();
    expectedEye.setX(5.0F);
    expectedEye.setY(0.0F);
    expectedEye.setZ(0.0F);
//    expectedEye.setRotationX(0.0);
//    expectedEye.setRotationY(0.0);
//    expectedEye.setRotationZ(0.0);
    assertThat(actualEye, is(expectedEye));

    final ModelUnitModel actualModelUnit = actualConfiguration.getModelUnit();
    final ModelUnitModel expectedModelUnit = new ModelUnitModel();
    expectedModelUnit.setAngleUnit("radian"); //$NON-NLS-1$
    expectedModelUnit.setLengthUnit("m"); //$NON-NLS-1$
    assertThat(actualModelUnit, is(expectedModelUnit));
    
    final DataUnitModel actualDataUnit = actualConfiguration.getDataUnit();
    final DataUnitModel expectedDataUnit = new DataUnitModel();
    expectedDataUnit.setAngle("radian"); //$NON-NLS-1$
    expectedDataUnit.setLength("m"); //$NON-NLS-1$
    assertThat(actualDataUnit, is(expectedDataUnit));
    
    final SceneModel actualModel = actual.getScene(0);
    final GroupModel actualGroup = actualModel.getGroup(0);
    
    final TranslationModel actualTranslation = actualGroup.getTranslation();
    final TranslationModel expectedTranslation = new TranslationModel();
    expectedTranslation.setX(0.0F);
    expectedTranslation.setY(0.0F);
    expectedTranslation.setZ(0.0F);
    assertThat(actualTranslation, is(expectedTranslation));
    
    final RotationModel actualRotation = actualGroup.getRotation();
    final RotationModel expectedRotation = new RotationModel();
    expectedRotation.setX(0.0F);
    expectedRotation.setY(0.0F);
    expectedRotation.setZ(0.0F);
    assertThat(actualRotation, is(expectedRotation));
    
    final GroupModel actualDodaiGroup = actualGroup.getGroups().get(0);
    assertDodaiGroup(actualDodaiGroup);
  }

  private void assertDodaiGroup(GroupModel actualGroup) {
    final TranslationModel actualLocation = actualGroup.getTranslation();
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setX(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(0.0F);
    assertThat(actualLocation, is(expectedLocation));

    final RotationModel actualRotation = actualGroup.getRotation();
    final RotationModel expectedRotation = new RotationModel();
    expectedRotation.setX(0.0F);
    expectedRotation.setY(0.0F);
    expectedRotation.setZ(0.0F);
    assertThat(actualRotation, is(expectedRotation));
    
    final List<ObjectModel> actualBoxes = actualGroup.getObjects();
    
    final BoxModel actualBox1 = (BoxModel)actualBoxes.get(0);
    assertDodaiBox1(actualBox1);

    final BoxModel actualBox2 = (BoxModel)actualBoxes.get(1);
    assertDodaiBox2(actualBox2);

    final BoxModel actualBox3 = (BoxModel)actualBoxes.get(2);
    assertDodaiBox3(actualBox3);

    final BoxModel actualBox4 = (BoxModel)actualBoxes.get(3);
    assertDodaiBox4(actualBox4);

    final GroupModel actualDaishaGroup = actualGroup.getGroups().get(0);
    assertDaishaGroup(actualDaishaGroup);
  }

  private void assertDodaiBox1(BoxModel actualBox) {
    final BoxModel expectedBox = new BoxModel();
    final ColorModel color = new ColorModel("LightGray"); //$NON-NLS-1$
    expectedBox.setColor(color);
    expectedBox.setTransparent(false);
    expectedBox.setWidth(0.02F);
    expectedBox.setHeight(0.27F);
    expectedBox.setDepth(0.02F);
    
    final TranslationModel expectedTranslation = new TranslationModel();
    expectedTranslation.setX(0.0F);
    expectedTranslation.setY(-0.05F);
    expectedTranslation.setZ(-0.21F);

    expectedBox.setTranslation(expectedTranslation);

    final RotationModel expectedRotation = new RotationModel();
    expectedBox.setRotation(expectedRotation);
    
    assertThat(actualBox, is(expectedBox));
  }

  private void assertDodaiBox2(BoxModel actualBox) {
    final BoxModel expectedBox = new BoxModel();
    final ColorModel color = new ColorModel("LightGray"); //$NON-NLS-1$
    expectedBox.setColor(color);
    expectedBox.setTransparent(false);
    expectedBox.setWidth(0.3F);
    expectedBox.setHeight(0.005F);
    expectedBox.setDepth(0.1F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setX(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(-0.3475F);

    expectedBox.setTranslation(expectedLocation);

    final RotationModel expectedRotation = new RotationModel();
    expectedBox.setRotation(expectedRotation);

    assertThat(actualBox, is(expectedBox));
  }

  private void assertDodaiBox3(BoxModel actualBox) {
    final BoxModel expectedBox = new BoxModel();
    final ColorModel color = new ColorModel("LightGray"); //$NON-NLS-1$
    expectedBox.setColor(color);
    expectedBox.setTransparent(false);
    expectedBox.setWidth(0.02F);
    expectedBox.setHeight(0.27F);
    expectedBox.setDepth(0.02F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setX(0.0F);
    expectedLocation.setY(0.05F);
    expectedLocation.setZ(-0.21F);

    expectedBox.setTranslation(expectedLocation);
    
    final RotationModel expectedRotation = new RotationModel();
    expectedBox.setRotation(expectedRotation);
    
    assertThat(actualBox, is(expectedBox));
  }

  private void assertDodaiBox4(BoxModel actualBox) {
    final BoxModel expectedBox = new BoxModel();
    final ColorModel color = new ColorModel("LightGray"); //$NON-NLS-1$
    expectedBox.setColor(color);
    expectedBox.setTransparent(false);
    expectedBox.setWidth(0.54F);
    expectedBox.setHeight(0.05F);
    expectedBox.setDepth(0.07F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(-0.05F);
    expectedLocation.setX(0.0F);

    expectedBox.setTranslation(expectedLocation);

    final RotationModel expectedRotation = new RotationModel();
    expectedBox.setRotation(expectedRotation);
   
    assertThat(actualBox, is(expectedBox));
  }

  
  private void assertDaishaGroup(GroupModel actualGroup) {
    final TranslationModel actualLocation = actualGroup.getTranslation();
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setX(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(0.0F);
    assertThat(actualLocation, is(expectedLocation));

    final RotationModel actualRotation = actualGroup.getRotation();
    final RotationModel expectedRotation = new RotationModel();
    expectedRotation.setX(0.0F);
    expectedRotation.setY(0.0F);
    expectedRotation.setZ(0.0F);
    assertThat(actualRotation, is(expectedRotation));

    final AnimationModel actualAnimation = actualGroup.getAnimations()[0];
    final AnimationModel expectedAnimation = new AnimationModel();
    final SourceModel expectedSource = new SourceModel();
    expectedSource.setId("0"); //$NON-NLS-1$
    expectedSource.setNumber(2);
    expectedAnimation.setSource(expectedSource);
    expectedAnimation.setTarget("translationY"); //$NON-NLS-1$
    assertThat(actualAnimation, is(expectedAnimation));

    final BoxModel actualBox = (BoxModel)actualGroup.getObjects().get(0);
    assertDaishaBox(actualBox);
    
    final GroupModel actualSinsiGroup = actualGroup.getGroups().get(0);
    assertSinsiGroup(actualSinsiGroup);
  }
  
  private void assertDaishaBox(BoxModel actualBox) {
    final BoxModel expectedBox = new BoxModel();
    final ColorModel color = new ColorModel("yellow"); //$NON-NLS-1$
    expectedBox.setColor(color);
    expectedBox.setTransparent(false);
    expectedBox.setWidth(0.07F);
    expectedBox.setHeight(0.05F);
    expectedBox.setDepth(0.005F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setZ(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setX(0.02F);
   
    expectedBox.setTranslation(expectedLocation);
   
    final RotationModel expectedRotation = new RotationModel();
    expectedBox.setRotation(expectedRotation);
    
    assertThat(actualBox, is(expectedBox));
  }
  
  private void assertSinsiGroup(GroupModel actualGroup) {
    final TranslationModel actualLocation = actualGroup.getTranslation();
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setX(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(0.0F);
    assertThat(actualLocation, is(expectedLocation));

    final RotationModel actualRotation = actualGroup.getRotation();
    final RotationModel expectedRotation = new RotationModel();
    expectedRotation.setX(0.0F);
    expectedRotation.setY(0.0F);
    expectedRotation.setZ(0.0F);
    assertThat(actualRotation, is(expectedRotation));
    
    final AnimationModel actualAnimation = actualGroup.getAnimations()[0];
    final AnimationModel expectedAnimation = new AnimationModel();
    final SourceModel expectedSource = new SourceModel();
    expectedSource.setNumber(3);
    expectedAnimation.setSource(expectedSource);
    expectedAnimation.setTarget("rotationX"); //$NON-NLS-1$
    assertThat(actualAnimation, is(expectedAnimation));
    
    final List<ObjectModel> actualCylinders = actualGroup.getObjects();
    final CylinderModel actualCylinder1 = (CylinderModel)actualCylinders.get(0);
    assertCylinder1(actualCylinder1);
    
    final CylinderModel actualCylinder2 = (CylinderModel)actualCylinders.get(1);
    assertCylinder2(actualCylinder2);

  }

  private void assertCylinder1(CylinderModel actualCylinder) {
    final CylinderModel expectedCylinder = new CylinderModel();
    final ColorModel color = new ColorModel("cyan"); //$NON-NLS-1$
    expectedCylinder.setColor(color);
    expectedCylinder.setDivision(10);
    expectedCylinder.setHeight(0.018F);
    expectedCylinder.setRadius(0.01F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setZ(0.0F);
    expectedLocation.setY(0.0F);
    expectedLocation.setX(0.0325F);

    final RotationModel expectedRotation = new RotationModel();
    expectedRotation.setY(1.5708F);
    expectedRotation.setX(0.0F);
    expectedRotation.setZ(0.0F);
    
    expectedCylinder.setTranslation(expectedLocation);
    expectedCylinder.setRotation(expectedRotation);
    
    assertThat(actualCylinder, is(expectedCylinder));
  }

  private void assertCylinder2(CylinderModel actualCylinder) {
    final CylinderModel expectedCylinder = new CylinderModel();
    final ColorModel color = new ColorModel("orange"); //$NON-NLS-1$
    expectedCylinder.setColor(color);
    expectedCylinder.setDivision(10);
    expectedCylinder.setHeight(0.3F);
    expectedCylinder.setRadius(0.0025F);
    
    final TranslationModel expectedLocation = new TranslationModel();
    expectedLocation.setY(0.0F);
    expectedLocation.setZ(0.15F);
    expectedLocation.setX(0.037F);
    
    expectedCylinder.setTranslation(expectedLocation);

    final RotationModel expectedRotation = new RotationModel();
    expectedCylinder.setRotation(expectedRotation);
    
    assertThat(actualCylinder, is(expectedCylinder));
  }

}
