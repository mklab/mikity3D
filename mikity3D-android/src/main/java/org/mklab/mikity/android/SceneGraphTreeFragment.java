/**
 * Copyright (C) 2015 MKLab.org (Koga Laboratory)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.mikity.android;

import java.util.List;

import org.mklab.mikity.android.R;
import org.mklab.mikity.android.editor.ModelEditor;
import org.mklab.mikity.android.editor.ModelEditorFactory;
import org.mklab.mikity.android.model.GraphTree;
import org.mklab.mikity.android.model.TreeItem;
import org.mklab.mikity.model.xml.simplexml.SceneModel;
import org.mklab.mikity.model.xml.simplexml.model.BoxModel;
import org.mklab.mikity.model.xml.simplexml.model.CapsuleModel;
import org.mklab.mikity.model.xml.simplexml.model.ConeModel;
import org.mklab.mikity.model.xml.simplexml.model.CylinderModel;
import org.mklab.mikity.model.xml.simplexml.model.GroupModel;
import org.mklab.mikity.model.xml.simplexml.model.NullModel;
import org.mklab.mikity.model.xml.simplexml.model.ObjectModel;
import org.mklab.mikity.model.xml.simplexml.model.QuadrangleModel;
import org.mklab.mikity.model.xml.simplexml.model.SphereModel;
import org.mklab.mikity.model.xml.simplexml.model.TriangleModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;


/**
 * モデルツリーのフラグメントを表すクラスです。
 * 
 * @author koga
 * @version $Revision$, 2016/01/08
 */
public class SceneGraphTreeFragment extends Fragment {
  private ListView treeView;
  
  /** モデラー。 */
  OpenglesModeler modeler;
  
  /** グラフツリー。 */
  GraphTree tree;
    
  /** シーンモデル。 */
  SceneModel model;
  /** ルートグループ。 */
  GroupModel rootGroup;
  
  /** ルート要素。 */
  TreeItem rootItem = null;
  /** 選択されている要素。 */
  TreeItem selectedTreeItem = null;
  /** 選択されているオブジェクト。 */
  Object selectedObject = null;
  /** 選択されているグループ。 */
  GroupModel selectedGroup = null;
  
  /** 記憶されたオブジェクト。 */
  ObjectModel bufferedObject = null;
  /** 記憶されたグループ。 */
  GroupModel bufferedGroup = null;
  
  ModelEditor modelEditorFragment = null;
  
  private static final int CONTEXT_MENU_CUT = 1;
  private static final int CONTEXT_MENU_COPY = 2;
  private static final int CONTEXT_MENU_PASTE = 3;
  private static final int CONTEXT_MENU_DELETE = 4;
  private static final int CONTEXT_MENU_EDIT = 5;
  //private static final int CONTEXT_MENU_TRANSFORM = 6;
  
  private int positionCount = 0;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setRetainInstance(true);
      setHasOptionsMenu(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    
    final View view = inflater.inflate(R.layout.fragment_scene_graph_tree, container, false);
    this.treeView = (ListView)view.findViewById(R.id.scene_graph_tree_view);
    
    createTree();
    createPopupMenu();
    
    final Button backButton = (Button)view.findViewById(R.id.backButton);
    backButton.setOnClickListener(new OnClickListener() {

      /**
       * {@inheritDoc}
       */
      public void onClick(View v) {
        SceneGraphTreeFragment.this.selectedTreeItem = SceneGraphTreeFragment.this.rootItem;
        updatetSelectedObject();
        
        final FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
      }
    });
    
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    
    menu.clear();
    inflater.inflate(R.menu.menu_model, menu);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_add_group) {
      addGroup();
      return true;
    } else if (item.getItemId() == R.id.menu_add_box) {
      addBox();
      return true;
    } else if (item.getItemId() == R.id.menu_add_cylinder) {
      addCylinder();
      return true;
    } else if (item.getItemId() == R.id.menu_add_sphere) {
      addSphere();
      return true;
    } else if (item.getItemId() == R.id.menu_add_cone) {
      addCone();
      return true;
    } else if (item.getItemId() == R.id.menu_add_capsule) {
      addCapsule();
      return true;
    } else if (item.getItemId() == R.id.menu_add_triangle) {
      addTriangle();
      return true;
    } else if (item.getItemId() == R.id.menu_add_quadrangle) {
      addQuadrangle();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
  
  
  /**
   * ポップアップメニュを生成します。 
   */
  private void createPopupMenu() {
    registerForContextMenu(this.treeView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, view, menuInfo);
    
    if (view.getId() == R.id.scene_graph_tree_view) {
      menu.add(0, CONTEXT_MENU_CUT, 0, R.string.cut);
      menu.add(0, CONTEXT_MENU_COPY, 0, R.string.copy);
      menu.add(0, CONTEXT_MENU_PASTE, 0, R.string.paste);
      menu.add(0, CONTEXT_MENU_DELETE, 0, R.string.delete);
      menu.add(0, CONTEXT_MENU_EDIT, 0, R.string.edit);
      //menu.add(0, CONTEXT_MENU_TRANSFORM, 0, R.string.transform);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    
    int position = info.position;
    
    int itemId = item.getItemId();
    
    if (itemId == CONTEXT_MENU_CUT) {
      cutSelectedItem();
      return true;
    }
    if (itemId == CONTEXT_MENU_COPY) {
      copySelectedItem();
      return true;
    }
    if (itemId == CONTEXT_MENU_PASTE) {
      pasteBufferedObjectToSelectedItem();
      return true;
    }
    if (itemId == CONTEXT_MENU_DELETE) {
      deleteSelectedItem();
      return true;
    }
    if (itemId == CONTEXT_MENU_EDIT) {
      editSelectedItem();
      return true;
    }
//    if (itemId == CONTEXT_MENU_TRANSFORM) {
//      return true;
//    }
    
    return super.onContextItemSelected(item);
  }
  
  /**
   * モデルを設定します。
   * 
   * @param model モデル
   */
  public void setModel(SceneModel model) {
    this.model = model;
    this.rootGroup = new GroupModel("scene"); //$NON-NLS-1$
    this.rootGroup.setGroups(model.getGroups());
    
    this.selectedGroup = this.rootGroup;
    this.selectedObject = this.rootGroup;
  }
  
  /**
   * ツリーを生成します。
   */
  void createTree() {
    this.tree = new GraphTree(getActivity());
    this.treeView.setAdapter(this.tree);
       
    bindModelToTree();
    
    setSelectedItem(this.rootItem, 0);
    
    this.tree.notifyDataSetChanged();
    
    this.treeView.setOnItemClickListener(new OnItemClickListener() {

      /**
       * {@inheritDoc}
       */
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SceneGraphTreeFragment.this.selectedTreeItem = (TreeItem)parent.getItemAtPosition(position);
        
        if (SceneGraphTreeFragment.this.selectedTreeItem.isExpanded()) {
          SceneGraphTreeFragment.this.selectedTreeItem.collapse();
        } else {
          if (SceneGraphTreeFragment.this.selectedTreeItem.hasItems()) {
            SceneGraphTreeFragment.this.selectedTreeItem.expand();
          }
        }
        
        SceneGraphTreeFragment.this.tree.notifyDataSetChanged();
        
        updatetSelectedObject();
      }
    });
  }
  
  
  /**
   * モデルをシーングラフにバインドし(結びつけ)ます。
   */
  public void bindModelToTree() {
    clearTree();
    
    this.positionCount = 0;
    
    this.rootItem = new TreeItem(this.tree, this.rootGroup);
    this.rootItem.setText("scene"); //$NON-NLS-1$
    this.positionCount++;
    
    addItemToTree(this.rootItem, this.model.getGroups());
  }
  
  /**
   * 選択されている要素を設定します。
   * 
   * @param selectedItem 選択されている要素
   * @param position 要素の番号
   */
  private void setSelectedItem(TreeItem selectedItem, int position) {
    this.selectedTreeItem = selectedItem;
    this.treeView.setItemChecked(position, true);
  }
  
  /**
   * ツリーから全てのItemを消去します。
   */
  private void clearTree() {
    if (this.tree.getCount() == 0) {
      return;
    }
    
    for (final TreeItem item : this.tree.getItems()) {
      removeItemsFromTree(item);
      item.dispose();
    }
  }

  /**
   * ツリーからItemを削除します。
   * 
   * @param items Item郡
   */
  private void removeItemsFromTree(TreeItem items) {
    for (final TreeItem item : items.getItems()) {
      if (item.getItemsCount() != 0) {
        removeItemsFromTree(item);
      }
      item.dispose();
    }
  }

  
  /**
   * ツリーにオブジェクトを追加します。
   * 
   * @param parent 親
   * @param groups グループ
   */
  private void addItemToTree(TreeItem parent, List<GroupModel> groups) {
    for (final GroupModel group : groups) {
      final TreeItem groupItem = new TreeItem(parent, group);
      groupItem.setText(group.toShortString());
      
      if (group == this.selectedObject) {
        setSelectedItem(groupItem, this.positionCount);
      }
      this.positionCount++;

      final List<ObjectModel> objects = group.getObjects();
      final boolean groupHasAnyObject = objects.size() > 0 && (objects.get(0) instanceof NullModel) == false;
      
      if (groupHasAnyObject) {
        final TreeItem objectItems = new TreeItem(groupItem, group);
        objectItems.setText("object"); //$NON-NLS-1$
        this.positionCount++;

        for (final ObjectModel object :  objects) {
          if (object instanceof NullModel) {
            continue;
          }
          
          final TreeItem child = new TreeItem(objectItems, object);
          child.setText(object.toShortString());
          
          if (object == this.selectedObject) {
            objectItems.expand();
            setSelectedItem(child, this.positionCount);
          }
          this.positionCount++;
        }
      }
      
      addItemToTree(groupItem, group.getGroups());
    }
    
    if (parent != null) {
      parent.expand();
    }
  }

  /**
   * 選択されているオブジェクトを更新します。
   */
  void updatetSelectedObject() {
    this.selectedObject = this.selectedTreeItem.getData();
    
    if (this.selectedObject instanceof GroupModel) {
      this.selectedGroup = (GroupModel)this.selectedObject;
    } else {
      this.selectedGroup = (GroupModel)this.selectedTreeItem.getParentItem().getData();
    }
    
    makeObjectNontransparent(this.selectedObject);
  }
  
  /**
   * 指定されたオブジェクトのみを非透過に設定します。
   * 
   * @param object オブジェクト
   */
  private void makeObjectNontransparent(Object object) {
    if (object == this.rootGroup) {
      for (final GroupModel group : this.model.getGroups()) {
        setAllTransparent(group, false);
      }
      return;
    }

    for (final GroupModel group : this.model.getGroups()) {
      setAllTransparent(group, true);
    }
    
    if (object instanceof ObjectModel) {
      ((ObjectModel)object).setTransparent(false);
    } else if (object instanceof GroupModel) {
      final GroupModel group = (GroupModel)object;
      setTransparent(group, false);
    }
  }
  
  /**
   * 指定されたグループ以下に含まれる全てのプリミティブの透過性を設定する。
   * 
   * @param group グループ
   * @param transparent 透過ならばtrue
   */
  public void setAllTransparent(final GroupModel group, boolean transparent) {
    setTransparent(group, transparent);

    for (GroupModel childGroup : group.getGroups()) {
      setAllTransparent(childGroup, transparent);
    }
  }
  
  /**
   * グループに含まれる全てのプリミティブの透過性を設定する。
   * 
   * @param group グループ
   * @param transparent 透過ならばtrue
   */
  public void setTransparent(final GroupModel group, boolean transparent) {
    for (ObjectModel primitive : group.getObjects()) {
      if (primitive instanceof NullModel) {
        continue;
      }
      primitive.setTransparent(transparent);
    }
  }
  
  /**
   * グループを追加します。 
   */
  void addGroup() {
    final GroupModel group = new GroupModel(getActivity().getString(R.string.group));
    if (this.selectedGroup == this.rootGroup) {
      this.model.addGroup(group);
    } else {
      this.selectedGroup.add(group);
    }
    this.selectedGroup = group;
    this.selectedObject = group;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 直方体を追加します。 
   */
  void addBox() {
    final ObjectModel object = BoxModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 円柱を追加します。
   */
  void addCylinder() {
    final ObjectModel object = CylinderModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;

    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 球を追加します。 
   */
  void addSphere() {
    final ObjectModel object = SphereModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 円錐を追加します。 
   */
  void addCone() {
    final ObjectModel object = ConeModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * カプセルを追加します。
   */
  void addCapsule() {
    final ObjectModel object = CapsuleModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 三角形を追加します。 
   */
  void addTriangle() {
    final ObjectModel object = TriangleModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 四角形を追加します。 
   */
  void addQuadrangle() {
    final ObjectModel object = QuadrangleModel.createDefault();
    this.selectedGroup.add(object);
    this.selectedObject = object;
    
    this.modeler.setIsChanged(true);
    
    updateTree();
  }

  /**
   * 要素をカットします。 
   */
  void cutSelectedItem() {
    if (this.selectedObject instanceof GroupModel) {
      this.bufferedGroup = this.selectedGroup;
      this.bufferedObject = null;
    } else {
      this.bufferedGroup = null;
      this.bufferedObject = (ObjectModel)this.selectedObject;
    }
    
    deleteSelectedItem();
  }

  /**
   * 要素をコピーします。 
   */
  void copySelectedItem() {
    if (this.selectedObject instanceof GroupModel) {
      this.bufferedGroup = this.selectedGroup;
      this.bufferedObject = null;
    } else {
      this.bufferedGroup = null;
      this.bufferedObject = (ObjectModel)this.selectedObject;
    }
  }

  /**
   * 要素をペーストします。 
   */
  void pasteBufferedObjectToSelectedItem() {
    if ((this.selectedObject instanceof GroupModel) == false) {
      return;
    }
    
    if (this.bufferedGroup != null) {
      final GroupModel group = this.bufferedGroup.clone();
      if (this.selectedGroup == this.rootGroup) {
        this.model.addGroup(group);
      } else {
        this.selectedGroup.add(group);
      }
      this.selectedGroup = group;
      this.selectedObject = group;
    }
    
    if (this.bufferedObject != null) {
      final ObjectModel object = this.bufferedObject.createClone();
      this.selectedGroup.add(object);
      this.selectedObject = object;
    }

    this.modeler.setIsChanged(true);
    updateTree();
  }
  
  /**
   * 選択されたitemを削除します。 
   */
  void deleteSelectedItem() {
    if (this.selectedTreeItem.getText().equals("scene")) { //$NON-NLS-1$
      return;
    }

    final TreeItem parentItem = this.selectedTreeItem.getParentItem();

    if (parentItem.getText().equals("scene")) { //$NON-NLS-1$
      this.model.removeGroup((GroupModel)this.selectedObject);
    } else {
      final GroupModel group = (GroupModel)parentItem.getData();
      removeObjectFromGroup(group, this.selectedObject);
    }

    this.selectedTreeItem.dispose();
    parentItem.removeItem(this.selectedTreeItem);
    
    this.selectedGroup = (GroupModel)parentItem.getData();
    this.selectedObject = parentItem.getData();
    
    updateTree();
    
    this.modeler.setIsChanged(true);
    this.modeler.updateRenderer();
    //this.modeler.updatePropertyEditor();
  }
  
  /**
   * 選択されたitemを編集します。 
   */
  void editSelectedItem() {
    if (this.selectedTreeItem.getText().equals("scene")) { //$NON-NLS-1$
      return;
    }

    final FragmentManager manager = getActivity().getSupportFragmentManager();
    final FragmentTransaction transaction = manager.beginTransaction();
    transaction.addToBackStack(null);
    
    if (this.modelEditorFragment != null) {
      transaction.remove((Fragment)this.modelEditorFragment);
      this.modelEditorFragment = null;
    }

    this.modelEditorFragment = ModelEditorFactory.create(this.selectedObject, this.selectedGroup, this, this.modeler);
    
    transaction.add(R.id.fragment_navigation_drawer, (Fragment)this.modelEditorFragment);
    
    transaction.commit();
    
    updateTree();
    
    this.modeler.updateRenderer();
    //this.modeler.updatePropertyEditor();
  }
  
  /**
   * グループからモデルを削除します。
   * 
   * @param group グループ
   * @param object プリミティブ
   */
  void removeObjectFromGroup(GroupModel group, Object object) {
    if (object instanceof ObjectModel) {
      group.remove((ObjectModel)object);
    } 
    
    if (object instanceof GroupModel) {
      group.remove((GroupModel)object);
    }
  }
  
  /**
   * ツリーとビューアを更新します。
   */
  public void updateTree() {
    bindModelToTree();
    this.tree.notifyDataSetChanged();
    
    this.modeler.updateRenderer();
    //this.modeler.updatePropertyEditor();
  }
  
  /**
   * 選択されているグループを設定します。
   * 
   * @param selectedGroup 選択されているグループ
   */
  public void setSelectedGroup(GroupModel selectedGroup) {
    this.selectedGroup = selectedGroup;
  }
  
  /**
   * 選択されているグループを返します。
   * 
   * @return 選択されているグループ
   */
  public GroupModel getSelectedGroup() {
    return this.selectedGroup;
  }
  
  /**
   * 選択されているオブジェクトを設定します。
   * 
   * @param selectedObject 選択されているオブジェクト
   */
  public void setSelectedObject(Object selectedObject) {
    this.selectedObject = selectedObject;
  }
  
  /**
   * 選択されているオブジェクトを返します。
   * 
   * @return 選択されているオブジェクト
   */
  public Object getSelectedObject() {
    return this.selectedObject;
  }

  /**
   * モデルデータを返します。
   * 
   * @return モデルデータ
   */
  public SceneModel getModel() {
    return this.model;
  }
  
  /**
   * モデラーを設定します。
   * 
   * @param modeler モデラー
   */
  public void setModeler(OpenglesModeler modeler) {
    this.modeler = modeler;
  }
  
}
